package eu.europa.ec.fisheries.mdr.service.bean.synchronization;

import eu.europa.ec.fisheries.mdr.entities.MdrCodeListStatus;
import eu.europa.ec.fisheries.mdr.exception.MdrCacheInitException;
import eu.europa.ec.fisheries.mdr.exception.MdrMappingException;
import eu.europa.ec.fisheries.mdr.mapper.MasterDataRegistryEntityCacheFactory;
import eu.europa.ec.fisheries.mdr.mapper.MdrRequestMapper;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRSync;
import eu.europa.ec.fisheries.mdr.repository.MdrStatusRepository;
import eu.europa.ec.fisheries.mdr.service.MdrSynchronizationService;
import eu.europa.ec.fisheries.mdr.service.bean.MdrConfigurationCache;
import eu.europa.ec.fisheries.mdr.util.GenericOperationOutcome;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.mdr.message.producer.MdrProducerBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@MDRSync(MDRSync.MDRSyncImpl.WEBSERVICE)
public class MdrSynchronizationWithWebServiceBean implements MdrSynchronizationService {

    private static final String ERROR_WHILE_TRYING_TO_MAP_MDRQUERY_TYPE_FOR_ACRONYM = "Error while trying to map MDRQueryType for acronym {}";
    private static final String OBJ_DESC = "OBJ_DESC";
    private static final String INDEX = "INDEX";
    private static final String MDR_EXCLUSION_LIST = "mdr.exclusion.list";

    private MdrStatusRepository statusRepository;

    private MdrProducerBean producer;

    private MdrConfigurationCache mdrConfigurationCache;

    private MdrSyncHelper mdrSyncHelper;

    private List<String> exclusionList;

    private final ConcurrentMap<String, Lock> locks = new ConcurrentHashMap<>(128);

    @Resource
    private ManagedExecutorService managedExecutorService;

    /**
     * Construction for injection
     *
     * @param statusRepository
     * @param mdrSyncHelper
     */
    @Inject
    public MdrSynchronizationWithWebServiceBean(MdrStatusRepository statusRepository, MdrProducerBean producer, MdrConfigurationCache mdrConfigurationCache, MdrSyncHelper mdrSyncHelper) {
        this.statusRepository = statusRepository;
        this.producer = producer;
        this.mdrConfigurationCache = mdrConfigurationCache;
        this.mdrSyncHelper = mdrSyncHelper;
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
    public GenericOperationOutcome manualStartMdrSynchronization() {
        filterUpdatableAcronyms(getAvailableMdrAcronyms()).forEach(this::scheduleAcronymSync);
        return null;
    }

    private List<String> filterUpdatableAcronyms(List<String> acronyms) {
        List<String> knownAcronyms = statusRepository
                .getAllUpdatableAcronymsStatuses()
                .stream()
                .map(MdrCodeListStatus::getObjectAcronym)
                .collect(Collectors.toList());

        return acronyms
                .stream()
                .filter(acronym -> knownAcronyms.contains(acronym) && !exclusionList.contains(acronym))
                .collect(Collectors.toList());
    }

    @Override
    public GenericOperationOutcome extractAcronymsAndUpdateMdr() {
        filterUpdatableAcronyms(getAvailableMdrAcronyms()).forEach(this::scheduleAcronymSync);
        return null;
    }

    @Override
    public GenericOperationOutcome updateMdrEntities(List<String> acronyms) {
        filterUpdatableAcronyms(acronyms).forEach(this::scheduleAcronymSync);
        return null;
    }

    private void scheduleAcronymSync(String acronym) {
        try {
            managedExecutorService.execute(new SyncAcronymTask(acronym));
        } catch (RejectedExecutionException e) {
            log.error("Sync execution rejected for acronym: {}", acronym);
        }
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
            for (String actAcron : acronymsList) {
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

    private class SyncAcronymTask implements Runnable {

        private final String acronym;

        SyncAcronymTask(String acronym) {
            this.acronym = acronym;
        }

        @Override
        public void run() {
            Lock lock = locks.computeIfAbsent(this.acronym, key -> new ReentrantLock());
            if (lock.tryLock()) {
                try {
                    mdrSyncHelper.updateMdrEntity(this.acronym);
                } finally {
                    lock.unlock();
                }
            }
        }
    }

}
