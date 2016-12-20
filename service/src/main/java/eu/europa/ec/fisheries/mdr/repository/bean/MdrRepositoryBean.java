/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.mdr.repository.bean;

import eu.europa.ec.fisheries.mdr.dao.MasterDataRegistryDao;
import eu.europa.ec.fisheries.mdr.dao.MdrBulkOperationsDao;
import eu.europa.ec.fisheries.mdr.dao.MdrConfigurationDao;
import eu.europa.ec.fisheries.mdr.dao.MdrStatusDao;
import eu.europa.ec.fisheries.mdr.domain.MdrCodeListStatus;
import eu.europa.ec.fisheries.mdr.domain.MdrConfiguration;
import eu.europa.ec.fisheries.mdr.domain.codelists.base.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.domain.constants.AcronymListState;
import eu.europa.ec.fisheries.mdr.mapper.MdrEntityMapper;
import eu.europa.ec.fisheries.mdr.repository.MdrRepository;
import eu.europa.ec.fisheries.uvms.common.DateUtils;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.mdr.response.FLUXMDRReturnMessage;
import un.unece.uncefact.data.standard.mdr.response.FLUXResponseDocumentType;
import un.unece.uncefact.data.standard.mdr.response.IDType;
import un.unece.uncefact.data.standard.mdr.response.MDRDataSetType;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Stateless
@Slf4j
public class MdrRepositoryBean implements MdrRepository {
	
	@PersistenceContext(unitName = "mdrPU")
    private EntityManager em;

	private MdrBulkOperationsDao bulkOperationsDao;
	
	private MdrConfigurationDao mdrConfigDao;

	private MdrStatusDao statusDao;

	private MasterDataRegistryDao mdrDao;

    @PostConstruct
    public void init() {
    	bulkOperationsDao = new MdrBulkOperationsDao(em);
    	mdrDao 			  = new MasterDataRegistryDao<>(em);
    	mdrConfigDao      = new MdrConfigurationDao(em);
		statusDao         = new MdrStatusDao(em);
    }

	@SuppressWarnings("unchecked")
	public <T extends MasterDataRegistry> List<T> findAllForEntity(Class<T> mdr) throws ServiceException {
		return mdrDao.findAllEntity(mdr);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends MasterDataRegistry> List<T> findEntityByHqlQuery(Class<T> type, String hqlQuery, Map<Integer, String> parameters,
			int maxResultLimit) throws ServiceException {
		return mdrDao.findEntityByHqlQuery(type, hqlQuery, parameters, maxResultLimit);
	}
	
	@Override
	public void updateMdrEntity(FLUXMDRReturnMessage response){
		// Response is OK
		final FLUXResponseDocumentType fluxResponseDocument = response.getFLUXResponseDocument();
		if(fluxResponseDocument.getResponseCode().toString().toUpperCase() != "NOK") {
			List<MasterDataRegistry> mdrEntityRows = MdrEntityMapper.mapJAXBObjectToMasterDataType(response);
			final MDRDataSetType mdrDataSet = response.getMDRDataSet();
			if (CollectionUtils.isNotEmpty(mdrEntityRows)) {
				try {
					bulkOperationsDao.singleEntityBulkDeleteAndInsert(mdrEntityRows);
					statusDao.updateStatusSuccessForAcronym(mdrDataSet, AcronymListState.SUCCESS, DateUtils.nowUTC().toDate());
				} catch (ServiceException e) {
					statusDao.updateStatusFailedForAcronym(mdrEntityRows.get(0).getAcronym());
					log.error("Transaction rolled back! Couldn't persist mdr Entity : ", e);
				}
			} else {
				log.error("Got Message from Flux related to MDR but, the list is empty! So, nothing is going to be persisted!");
			}
		// Response is NOT OK
		} else {
			final IDType referencedID = fluxResponseDocument.getReferencedID();
			if(referencedID != null && StringUtils.isNotEmpty(referencedID.getValue())){//, but has referenceID
				statusDao.updateStatusFailedForAcronym(extractAcronymFromReferenceId(referencedID.getValue()));
			} else {//, and doesn't have referenceID
                log.error("[[ERROR]] The MDR response received in activity was NOK and has no referenceId!!");
            }
		}
	}

	private String extractAcronymFromReferenceId(String responseReferenceID) {
		return responseReferenceID.split("--")[0];
	}

	/*
	 * MDR Configurations.
	 */
	@Override
	public List<MdrConfiguration> getAllConfigurations() throws ServiceException{
		return mdrConfigDao.findAllConfigurations();
	}
	
	@Override
	public MdrConfiguration getConfigurationByName(String configName) {
		return mdrConfigDao.findConfiguration(configName);
	}
	
	@Override
    public void changeMdrSchedulerConfiguration(String newCronExpression) throws ServiceException{
    	mdrConfigDao.changeMdrSchedulerConfiguration(newCronExpression);
    }
	
	@Override
	public MdrConfiguration getMdrSchedulerConfiguration(){
		return mdrConfigDao.getMdrSchedulerConfiguration();
	}
	
	/*
	 * MDR Acronym's statuses.
	 */
	@Override
    public List<MdrCodeListStatus> findAllStatuses() throws ServiceException {
        return statusDao.getAllAcronymsStatuses();
    }

	@Override
    public MdrCodeListStatus findStatusByAcronym(String acronym){
    	return statusDao.getStatusForAcronym(acronym);
    }

	// TODO : Delete me when done testing the functionality
	@Override
	public void insertTestData(List<? extends MasterDataRegistry> testList) throws ServiceException {
		bulkOperationsDao.singleEntityBulkDeleteAndInsert(testList);
	}

}