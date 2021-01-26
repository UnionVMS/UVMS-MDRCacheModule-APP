package eu.europa.ec.fisheries.mdr.service.bean.synchronization;

import eu.europa.ec.fisheries.mdr.client.MdrWebServiceClient;
import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.constants.AcronymListState;
import eu.europa.ec.fisheries.mdr.exception.AcronymNotFoundException;
import eu.europa.ec.fisheries.mdr.mapper.webservice.MasterDataRegistryMapper;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.fisheries.mdr.repository.MdrRepository;
import eu.europa.ec.fisheries.mdr.repository.MdrStatusRepository;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRServiceFault;
import eu.europa.ec.mare.fisheries.model.mdr.v1.ServiceError;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRServiceException;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class MdrSyncHelper {

    private static final long PAGE_SIZE = 1000;

    private MdrStatusRepository statusRepository;

    private MdrRepository mdrRepository;

    private MdrWebServiceClient mdrWebServiceClient;

    private Instance<MasterDataRegistryMapper> mappers;

    public MdrSyncHelper() {
    }

    @Inject
    public MdrSyncHelper(MdrStatusRepository statusRepository, MdrRepository mdrRepository, MdrWebServiceClient mdrWebServiceClient, @Any Instance<MasterDataRegistryMapper> mappers) {
        this.statusRepository = statusRepository;
        this.mdrRepository = mdrRepository;
        this.mdrWebServiceClient = mdrWebServiceClient;
        this.mappers = mappers;
    }

    public void updateMdrEntity(String acronym) {
        String uuid = java.util.UUID.randomUUID().toString();
        statusRepository.updateStatusAttemptForAcronym(acronym, AcronymListState.RUNNING, DateUtils.nowUTC().toDate(), uuid);
        log.info("Sync request sent for acronym: " + acronym);
        boolean success = false;
        try {
            mdrRepository.update(acronym, new WebServiceMdrDataProvider(acronym, 1, PAGE_SIZE));
            success = true;
        } catch (Exception e) {
            log.error("Sync failed for acronym: " + acronym, e);
        }
        try {
            if (success) {
                statusRepository.updateStatusSuccessForAcronym(acronym, AcronymListState.SUCCESS, DateUtils.nowUTC().toDate());
            } else {
                statusRepository.updateStatusFailedForAcronym(acronym);
            }
        } catch (AcronymNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private class WebServiceMdrDataProvider implements MdrRepository.MdrDataProvider {

        private final String acronym;
        private long offset;
        private final long limit;

        WebServiceMdrDataProvider(String acronym, long offset, long limit) {
            this.acronym = acronym;
            this.offset = offset;
            this.limit = limit;
        }

        @Override
        public List<? extends MasterDataRegistry> fetch() {
            try {
                List<MDRDataNodeType> result = mdrWebServiceClient.getMDRList(this.acronym, this.offset, this.limit);
                List<MasterDataRegistry> entities = result.stream().map(this::transform).collect(Collectors.toList());
                this.offset += this.limit;
                return entities;
            } catch (MDRServiceException e) {
                MDRServiceFault fault = e.getFaultInfo();
                if (fault != null) {
                    List<ServiceError> errors = fault.getErrors();
                    if (errors != null) {
                        for (ServiceError error : errors) {
                            if ("MDR_RECORD_NOT_FOUND".equalsIgnoreCase(error.getReason())) {
                                return Collections.emptyList();
                            }
                        }
                    }
                }
                throw new RuntimeException("Web service error for acronym: " + acronym, e);
            }
        }

        private MasterDataRegistry transform(MDRDataNodeType node) {
            return mappers.select(new MDRMapper.MDRMapperImpl(acronym)).get().mapMDRDataNodeTypeToEntity(node);
        }
    }
}
