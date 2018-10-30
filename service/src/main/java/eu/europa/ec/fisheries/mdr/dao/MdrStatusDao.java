/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.mdr.dao;

import eu.europa.ec.fisheries.mdr.entities.AcronymVersion;
import eu.europa.ec.fisheries.mdr.entities.MdrCodeListStatus;
import eu.europa.ec.fisheries.mdr.entities.constants.AcronymListState;
import eu.europa.ec.fisheries.mdr.exception.AcronymNotFoundException;
import eu.europa.ec.fisheries.uvms.commons.domain.DateRange;
import eu.europa.ec.fisheries.uvms.commons.service.dao.AbstractDAO;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import un.unece.uncefact.data.standard.mdr.response.DataSetVersionType;
import un.unece.uncefact.data.standard.mdr.response.MDRDataSetType;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;

/**
 * Created by kovian on 29/07/2016.
 */
@Slf4j
public class MdrStatusDao extends AbstractDAO<MdrCodeListStatus> {

    private EntityManager em;

    private static final String SELECT_FROM_MDRSTATUS_WHERE_ACRONYM = "FROM MdrCodeListStatus WHERE objectAcronym=";
    private static final String SELECT_UPDATABLE_FROM_MDRSTATUS     = "FROM MdrCodeListStatus WHERE schedulable='Y'";
    private static final String SELECT_LAST_REFRESH_DATE_FROM_MDRSTATUS = "FROM MdrCodeListStatus WHERE lastSuccess IN (SELECT max(lastSuccess) FROM MdrCodeListStatus)";
    private static final String ERROR_WHILE_SAVING_STATUS           = "Error while trying to save/update new MDR Code List Status";


    public MdrStatusDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }


    public Date findLastRefreshDateFromStatuses() {
        List<MdrCodeListStatus> statuses = new ArrayList<>();
        try {
            statuses = findEntityByHqlQuery(MdrCodeListStatus.class, SELECT_LAST_REFRESH_DATE_FROM_MDRSTATUS);
        } catch (ServiceException e) {
            log.error("Error while trying to get last refresh date from Acronyms list : ", e);
        }
        return CollectionUtils.isEmpty(statuses) ? null : statuses.get(0).getLastSuccess();
    }

    public MdrCodeListStatus findStatusByAcronym(String acronym) {
        MdrCodeListStatus entity = null;
        List<MdrCodeListStatus> stausList;
        try {
            stausList = findEntityByHqlQuery(MdrCodeListStatus.class, SELECT_FROM_MDRSTATUS_WHERE_ACRONYM + "'" + acronym + "'");
            if(CollectionUtils.isNotEmpty(stausList)){
                entity = stausList.get(0);
            } else {
                log.error("Couldn't find Status for acronym : {}",acronym);
            }
        } catch (ServiceException e) {
            log.error("Error while trying to get Status for acronym : ", acronym, e);
        }
        return entity;
    }

    public void saveAcronymsStatusList(List<MdrCodeListStatus> statusList) throws ServiceException {
        Session session = (getEntityManager().unwrap(Session.class)).getSessionFactory().openSession();
        try {
            log.info("Persisting entity entries for : MdrCodeListStatus");
            int counter = 0;
            for(MdrCodeListStatus actStatus : statusList){
                counter++;
                session.save(actStatus);
                if (counter % 20 == 0 ) {//Each 20 rows persist and release memory;
                    session.flush();
                    session.clear();
                }
            }
            log.debug("Committing transaction.");
        } catch (Exception e) {
            throw new ServiceException("Rollbacking transaction for reason : ", e);
        } finally {
            session.flush();
            log.debug("Closing session");
            session.close();
        }
    }

    public List<MdrCodeListStatus> findAllUpdatableStatuses() {
        List<MdrCodeListStatus> statuses = null;
        try {
            statuses = findEntityByHqlQuery(MdrCodeListStatus.class, SELECT_UPDATABLE_FROM_MDRSTATUS);
        } catch (ServiceException e) {
            log.error("Error while trying to get schedulable Acronyms list : ", e);
        }
        return statuses;
    }

    public void updateStatusSuccessForAcronym(String acronym, AcronymListState newStatus, Date lastSuccess) throws AcronymNotFoundException {
        MdrCodeListStatus mdrCodeListElement = findStatusByAcronym(acronym);
        if(mdrCodeListElement == null){
            throw new AcronymNotFoundException("The acronym status "+acronym+" you searched for is not present!");
        }
        mdrCodeListElement.setLastSuccess(lastSuccess);
        mdrCodeListElement.setLastStatus(newStatus);
        try {
            saveOrUpdateEntity(mdrCodeListElement);
        } catch (ServiceException e) {
            log.error(ERROR_WHILE_SAVING_STATUS,e);
        }
    }

    public void updateStatusSuccessForAcronym(MDRDataSetType dataSetType, AcronymListState newStatus, Date lastSuccess) {
        MdrCodeListStatus mdrCodeListElement = findStatusByAcronym(dataSetType.getID().getValue());
        try {
            mdrCodeListElement.setLastSuccess(lastSuccess);
            mdrCodeListElement.setLastStatus(newStatus);
            mdrCodeListElement.setObjectSource(dataSetType.getOrigin().getValue());
            mdrCodeListElement.setObjectDescription(dataSetType.getDescription().getValue());
            mdrCodeListElement.setObjectName(dataSetType.getName().getValue());
            saveOrUpdateEntity(mdrCodeListElement);
        } catch (ServiceException e) {
            log.error(ERROR_WHILE_SAVING_STATUS,e);
        } catch (NullPointerException nullEx){
            log.error("[ERROR] Couldn't find status for acronym : {}", dataSetType.getID().getValue());
        }
    }

    public void updateMetadataForAcronym(MDRDataSetType codeListType){
        MdrCodeListStatus mdrCodeListElement = findStatusByAcronym(codeListType.getID().getValue());
        mdrCodeListElement.setObjectSource(codeListType.getOrigin().getValue());
        mdrCodeListElement.setObjectDescription(codeListType.getDescription().getValue());
        mdrCodeListElement.setObjectName(codeListType.getName().getValue());
        List<DataSetVersionType> specifiedDataSetVersions = codeListType.getSpecifiedDataSetVersions();
        if(CollectionUtils.isNotEmpty(specifiedDataSetVersions)){
            Set<AcronymVersion> acrVersions = new HashSet<>();
            for(DataSetVersionType actVersion : codeListType.getSpecifiedDataSetVersions()){
                acrVersions.add(new AcronymVersion(
                        actVersion.getName().getValue(),
                        new DateRange(actVersion.getValidityStartDateTime().getDateTime().toGregorianCalendar().getTime(),
                                actVersion.getValidityEndDateTime().getDateTime().toGregorianCalendar().getTime())
                ));
            }
            mdrCodeListElement.setVersions(acrVersions);
        }
        try {
            saveOrUpdateEntity(mdrCodeListElement);
        } catch (ServiceException e) {
            log.error(ERROR_WHILE_SAVING_STATUS,e);
        }
    }

    public void updateStatusForAcronym(String acronym, AcronymListState acronymState) {
        MdrCodeListStatus mdrCodeListElement = findStatusByAcronym(acronym);
        mdrCodeListElement.setLastStatus(acronymState);
        try {
            saveOrUpdateEntity(mdrCodeListElement);
        } catch (ServiceException e) {
            log.error(ERROR_WHILE_SAVING_STATUS,e);
        }
    }


    public void updateSchedulableForAcronym(String acronym, boolean schedulable) {
        MdrCodeListStatus mdrCodeListElement = findStatusByAcronym(acronym);
        try {
            mdrCodeListElement.setSchedulable(schedulable);
            saveOrUpdateEntity(mdrCodeListElement);
        } catch (ServiceException | NullPointerException e) {
            log.error(ERROR_WHILE_SAVING_STATUS,e);
        }
    }

    public void updateStatusForAcronym(String acronym, AcronymListState newStatus, Date lastAttempt, String uuid) {
        MdrCodeListStatus mdrCodeListElement = findStatusByAcronym(acronym);
        try {
            mdrCodeListElement.setLastAttempt(lastAttempt);
            mdrCodeListElement.setLastStatus(newStatus);
            mdrCodeListElement.setReferenceUuid(uuid);
            saveOrUpdateEntity(mdrCodeListElement);
        } catch (NullPointerException | ServiceException e) {
            log.error(ERROR_WHILE_SAVING_STATUS, e);
        }
    }

    public List<MdrCodeListStatus> getAllUpdatableAcronymsStatuses() {
        return  findAllUpdatableStatuses();
    }

    public List<MdrCodeListStatus> getAllAcronymsStatuses() {
        List<MdrCodeListStatus> statussesList = null;
        try {
            statussesList =  findAllEntity(MdrCodeListStatus.class);
        } catch (ServiceException e) {
            log.error("Error while getting MDR Code List Statusses",e);
        }
        return statussesList;
    }

    public List<MdrCodeListStatus> findStatusAndVersionsForAcronym(String objAcronym){
        TypedQuery<MdrCodeListStatus> query = getEntityManager().createNamedQuery(MdrCodeListStatus.STATUS_AND_VERSIONS_QUERY, MdrCodeListStatus.class);
        query.setParameter("objectAcronym", objAcronym);
        return query.getResultList();
    }

    public MdrCodeListStatus getStatusForAcronym(String acronym) {
        return findStatusByAcronym(acronym);
    }

    public MdrCodeListStatus getStatusForUuid(String uuid) {
        TypedQuery<MdrCodeListStatus> query = getEntityManager().createNamedQuery(MdrCodeListStatus.STATUS_FOR_UUID, MdrCodeListStatus.class);
        query.setParameter("uuid", uuid);
        return query.getSingleResult();
    }
}