/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.mdr.repository.bean;

import eu.europa.ec.fisheries.mdr.dao.MdrStatusDao;
import eu.europa.ec.fisheries.mdr.entities.MdrCodeListStatus;
import eu.europa.ec.fisheries.mdr.entities.constants.AcronymListState;
import eu.europa.ec.fisheries.mdr.exception.AcronymNotFoundException;
import eu.europa.ec.fisheries.mdr.repository.MdrStatusRepository;
import eu.europa.ec.fisheries.mdr.service.bean.BaseMdrBean;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kovian on 29/07/2016.
 */
@Stateless
@Slf4j
@Transactional
public class MdrStatusRepositoryBean extends BaseMdrBean implements MdrStatusRepository {

    private MdrStatusDao statusDao;

    @PostConstruct
    public void init() {
        initEntityManager();
        statusDao = new MdrStatusDao(getEntityManager());
    }

    @Override
    public List<MdrCodeListStatus> getAllAcronymsStatuses(){
        return statusDao.getAllAcronymsStatuses();
    }

    @Override
    public List<MdrCodeListStatus> getAllUpdatableAcronymsStatuses() {
        return statusDao.getAllUpdatableAcronymsStatuses();
    }

    @Override
    public void saveAcronymsStatusList(List<MdrCodeListStatus> diffList) throws ServiceException {
        statusDao.saveAcronymsStatusList(diffList);
    }

    @Override
    public MdrCodeListStatus getStatusForAcronym(String acronym){
        return statusDao.getStatusForAcronym(acronym);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void updateStatusAttemptForAcronym(String acronym, AcronymListState newStatus, Date lastAttempt, String uuid){
        statusDao.updateStatusForAcronym(acronym, newStatus, lastAttempt, uuid);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void updateStatusSuccessForAcronym(String acronym, AcronymListState newStatus, Date lastSuccess) throws AcronymNotFoundException {
            statusDao.updateStatusSuccessForAcronym(acronym, newStatus, lastSuccess);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void updateStatusFailedForAcronym(String acronym) {
        statusDao.updateStatusForAcronym(acronym, AcronymListState.FAILED);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void updateSchedulableForAcronym(String acronym, boolean schedulable){
        statusDao.updateSchedulableForAcronym(acronym, schedulable);
    }
}
