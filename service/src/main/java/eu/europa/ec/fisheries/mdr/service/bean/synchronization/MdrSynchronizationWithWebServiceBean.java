package eu.europa.ec.fisheries.mdr.service.bean.synchronization;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import eu.europa.ec.fisheries.mdr.client.MdrWebServiceClient;
import eu.europa.ec.fisheries.mdr.entities.MdrCodeListStatus;
import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.constants.AcronymListState;
import eu.europa.ec.fisheries.mdr.exception.MdrCacheInitException;
import eu.europa.ec.fisheries.mdr.exception.MdrMappingException;
import eu.europa.ec.fisheries.mdr.mapper.MasterDataRegistryEntityCacheFactory;
import eu.europa.ec.fisheries.mdr.mapper.MdrRequestMapper;
import eu.europa.ec.fisheries.mdr.mapper.webservice.MasterDataRegistryMapper;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRSync;
import eu.europa.ec.fisheries.mdr.repository.MdrRepository;
import eu.europa.ec.fisheries.mdr.repository.MdrStatusRepository;
import eu.europa.ec.fisheries.mdr.service.MdrSynchronizationService;
import eu.europa.ec.fisheries.mdr.service.bean.MdrConfigurationCache;
import eu.europa.ec.fisheries.mdr.util.GenericOperationOutcome;
import eu.europa.ec.fisheries.mdr.util.OperationOutcome;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.mdr.message.producer.MdrProducerBean;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

@Slf4j
@ApplicationScoped
@MDRSync(MDRSync.MDRSyncImpl.WEBSERVICE)
public class MdrSynchronizationWithWebServiceBean implements MdrSynchronizationService {

    private static final String ERROR_WHILE_TRYING_TO_MAP_MDRQUERY_TYPE_FOR_ACRONYM = "Error while trying to map MDRQueryType for acronym {}";
    private static final String OBJ_DESC = "OBJ_DESC";
    private static final String INDEX = "INDEX";
    private static final String MDR_EXCLUSION_LIST = "mdr.exclusion.list";

    private MdrStatusRepository statusRepository;
    
    private MdrWebServiceClient mdrWebServiceClient;
    
    private Instance<MasterDataRegistryMapper> mappers;
    
    private MdrRepository mdrRepository;
    
    private MdrProducerBean producer;
    
    private MdrConfigurationCache mdrConfigurationCache;

    private List<String> exclusionList;

    /**
     * Construction for injection
     *
     * @param mdrWebServiceClient
     * @param statusRepository
     * @param mappers
     */
    @Inject
    public MdrSynchronizationWithWebServiceBean(MdrStatusRepository statusRepository, MdrWebServiceClient mdrWebServiceClient, @Any Instance<MasterDataRegistryMapper> mappers,
                                                MdrRepository mdrRepository, MdrProducerBean producer, MdrConfigurationCache mdrConfigurationCache) {
        this.statusRepository = statusRepository;
        this.mdrWebServiceClient = mdrWebServiceClient;
        this.mappers = mappers;
        this.mdrRepository = mdrRepository;
        this.producer = producer;
        this.mdrConfigurationCache = mdrConfigurationCache;
    }

    /**
     * Constructor for frameworks.
     */
    @SuppressWarnings("unused")
    MdrSynchronizationWithWebServiceBean() {
        // NOOP
    }

    @PostConstruct
    public void loadExclusionList() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("exclusionList.properties");
        Properties props = new Properties();
        try {
            props.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> propertyStr = Collections.singletonList((props.getProperty(MDR_EXCLUSION_LIST)));
        exclusionList = CollectionUtils.isNotEmpty(propertyStr) ? Arrays.asList(propertyStr.get(0).split(",")) : new ArrayList<>();
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public GenericOperationOutcome manualStartMdrSynchronization() {
        return extractAcronymsAndUpdateMdr();
    }

    @Override
    public GenericOperationOutcome extractAcronymsAndUpdateMdr() {
        log.info("\n\t\t[START] Starting sending code-lists synchronization requests.\n");
        List<String> updatableAcronyms = extractUpdatableAcronyms(getAvailableMdrAcronyms());
        GenericOperationOutcome errorContainer = updateMdrEntities(updatableAcronyms);
        log.info("\n\n\t\t[END] Sending of synchronization requests finished! (Sent : [ "+updatableAcronyms.size()+" ] code-lists synch requests in total!) \n\n");
        return errorContainer;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private List<String> extractUpdatableAcronyms(List<String> availableAcronyms) {
        List<String> statusListFromDb = extractAcronymsListFromAcronymStatusList(statusRepository.getAllUpdatableAcronymsStatuses());
        List<String> matchList = new ArrayList<>();
        for (String actualCacheAcronym : availableAcronyms) {
            if (statusListFromDb.contains(actualCacheAcronym)) {
                matchList.add(actualCacheAcronym);
            }
        }
        return matchList;
    }
    
    private List<String> extractAcronymsListFromAcronymStatusList(List<MdrCodeListStatus> allUpdatableAcronymsStatuses) {
        List<String> acronymsStrList = new ArrayList<>();
        for (MdrCodeListStatus actStatus : allUpdatableAcronymsStatuses) {
            acronymsStrList.add(actStatus.getObjectAcronym());
        }
        return acronymsStrList;
    }
    
    @Override
    public GenericOperationOutcome updateMdrEntities(List<String> acronymsList) {
        // For each Acronym send a request object towards Exchange module.
        GenericOperationOutcome errorContainer = new GenericOperationOutcome();
        List<String> existingAcronymsList;
        try {
            existingAcronymsList = MasterDataRegistryEntityCacheFactory.getAcronymsList();
        } catch (MdrCacheInitException e) {
            log.error("Error while trying to get acronymsList from cache", e);
            return new GenericOperationOutcome(OperationOutcome.NOK, "Error while trying to get acronymsList from cache");
        }
        for (String actualAcronym : acronymsList) {
            if (existingAcronymsList.contains(actualAcronym) && !acronymIsInExclusionList(actualAcronym)) {// Acronym exists
                statusRepository.updateStatusAttemptForAcronym(actualAcronym, AcronymListState.RUNNING, DateUtils.nowUTC().toDate(), java.util.UUID.randomUUID().toString());
                log.info("Synchronization Request Sent for Entity : " + actualAcronym);
                List<MasterDataRegistry> results = sendRequestForAcronym(actualAcronym);
                mdrRepository.updateMdrEntities(results, actualAcronym);
                errorContainer.setIncludedObject(statusRepository.getAllAcronymsStatuses());
            } else {// Acronym does not exist
                log.debug("Couldn't find the acronym \" " + actualAcronym + " \" (or the acronym is in Exclusion List) in the cachedAcronymsList! Request for said acronym won't be sent to flux!");
                errorContainer.addMessage("The following acronym doesn't exist (or is excluded) in the cacheFactory : " + actualAcronym);
            }
        }

        return errorContainer;
    }
    
    private List<MasterDataRegistry> sendRequestForAcronym(String acronym) {
        List<MDRDataNodeType> results = mdrWebServiceClient.getMDRList(acronym);
        try {
            return results.stream().map(node -> mapResultToEntity(node, acronym)).collect(Collectors.toList());
        } catch (UnsatisfiedResolutionException e) {
            throw new RuntimeException("Could not find mapper for acronym: " + acronym,  e);
        }
    }

    private MasterDataRegistry mapResultToEntity(MDRDataNodeType node, String acronym) {
        return mappers.select(new MDRMapper.MDRMapperImpl(acronym)).get().mapMDRDataNodeTypeToEntity(node);
    }

    private boolean acronymIsInExclusionList(String acronym) {
        return exclusionList.contains(acronym);
    }

    @Override
    public List<String> getAvailableMdrAcronyms() {
        List<String> acronymsList = new ArrayList<>();
        try {
            acronymsList = MasterDataRegistryEntityCacheFactory.getAcronymsList();
            if (!CollectionUtils.isEmpty(acronymsList)) {
                log.info("Acronyms exctracted. \nThere were found [ " + acronymsList.size() + " ] acronyms in the MDR entities package.");
            }
            log.info("\n---> Exctracted : " + acronymsList.size() + " acronyms!\n");
        } catch (MdrCacheInitException exC) {
            log.error("Couldn't extract Entity Acronyms. The following Exception was thrown : \n", exC);
        }
        return acronymsList;
    }

    @Override
    public void sendRequestForMdrCodelistsStructures(Collection<String> acronymsList) {
        try {
            for(String actAcron : acronymsList){
                sendRequestForSingleMdrCodelistsStructure(actAcron);
            }
        } catch (MdrMappingException e) {
            log.error(ERROR_WHILE_TRYING_TO_MAP_MDRQUERY_TYPE_FOR_ACRONYM, acronymsList, e);
        } catch (MessageException e) {
            log.error("Error while trying to send OBJ_DESC message from MDR module to Rules module.", e);
        }
    }

    @Override
    public void sendRequestForSingleMdrCodelistsStructure(String actAcron) throws MdrMappingException, MessageException {
        String strReqObj = MdrRequestMapper.mapMdrQueryTypeToString(actAcron, OBJ_DESC, java.util.UUID.randomUUID().toString(), mdrConfigurationCache.getNationCode());
        producer.sendRulesModuleMessage(strReqObj);
    }

    @Override
    public void sendRequestForMdrCodelistsIndex() {
        try {
            String strReqObj = MdrRequestMapper.mapMdrQueryTypeToStringForINDEXServiceType(INDEX, mdrConfigurationCache.getNationCode());
            producer.sendRulesModuleMessage(strReqObj);
            log.info("Synchronization Request Sent for INDEX ServiceType");
        } catch (MdrMappingException e) {
            log.error(ERROR_WHILE_TRYING_TO_MAP_MDRQUERY_TYPE_FOR_ACRONYM, e);
        } catch (MessageException e) {
            log.error("Error while trying to send message from MDR module to Rules module.", e);
        }
    }
}
