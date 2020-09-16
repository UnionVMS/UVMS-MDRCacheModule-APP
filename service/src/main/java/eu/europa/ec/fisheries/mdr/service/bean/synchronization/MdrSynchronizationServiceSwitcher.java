package eu.europa.ec.fisheries.mdr.service.bean.synchronization;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

import eu.europa.ec.fisheries.mdr.dao.MdrConfigurationDao;
import eu.europa.ec.fisheries.mdr.exception.MdrMappingException;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRSync;
import eu.europa.ec.fisheries.mdr.service.MdrSynchronizationService;
import eu.europa.ec.fisheries.mdr.service.bean.BaseMdrBean;
import eu.europa.ec.fisheries.mdr.util.GenericOperationOutcome;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;

@ApplicationScoped
public class  MdrSynchronizationServiceSwitcher extends BaseMdrBean implements MdrSynchronizationService {
    
    @Inject @Any
    Instance<MdrSynchronizationService> mdrSynchronizationServices;

    MdrSynchronizationService mdrSynchronizationService;
    
    
    
    @PostConstruct
    public void init() {
        try {
            String mdrSync = MDRSync.MDRSyncImpl.ORACLE_DB;
            MdrConfigurationDao mdrConfigurationDao = initMDRConfigurationDao();
            if(!mdrConfigurationDao.getMDRConfigurationValue(MdrConfigurationDao.WEBSERVICE_WSDL_LOCATION).isEmpty()) {
                mdrSync = MDRSync.MDRSyncImpl.WEBSERVICE;
            }
            mdrSynchronizationService = mdrSynchronizationServices.select(new MDRSync.MDRSyncImpl(mdrSync)).get();
        } catch (Exception e) {
            throw new RuntimeException("Mdr synchronisation property is not set");
        }
    }

    private MdrConfigurationDao initMDRConfigurationDao() {
        initEntityManager();
        return new MdrConfigurationDao(getEntityManager());
    }
    
    @Override
    public GenericOperationOutcome manualStartMdrSynchronization() {
        return mdrSynchronizationService.manualStartMdrSynchronization();
    }

    @Override
    public List<String> getAvailableMdrAcronyms() {
        return mdrSynchronizationService.getAvailableMdrAcronyms();
    }

    @Override
    public GenericOperationOutcome extractAcronymsAndUpdateMdr() {
        return mdrSynchronizationService.extractAcronymsAndUpdateMdr();
    }

    @Override
    public GenericOperationOutcome updateMdrEntities(List<String> acronymsList) {
        return mdrSynchronizationService.updateMdrEntities(acronymsList);
    }

    @Override
    public void sendRequestForMdrCodelistsStructures(Collection<String> acronym) {
        mdrSynchronizationService.sendRequestForMdrCodelistsStructures(acronym);
    }

    @Override
    public void sendRequestForSingleMdrCodelistsStructure(String actAcron) throws MdrMappingException, MessageException {
        mdrSynchronizationService.sendRequestForSingleMdrCodelistsStructure(actAcron);
    }

    @Override
    public void sendRequestForMdrCodelistsIndex() {
        mdrSynchronizationService.sendRequestForMdrCodelistsIndex();
    }
}
