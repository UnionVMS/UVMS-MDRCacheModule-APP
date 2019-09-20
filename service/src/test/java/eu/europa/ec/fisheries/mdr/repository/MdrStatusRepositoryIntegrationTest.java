/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.mdr.repository;

import com.google.common.collect.Sets;
import eu.europa.ec.fisheries.mdr.TransactionalTests;
import eu.europa.ec.fisheries.mdr.entities.AcronymVersion;
import eu.europa.ec.fisheries.mdr.entities.MdrCodeListStatus;
import eu.europa.ec.fisheries.mdr.entities.constants.AcronymListState;
import eu.europa.ec.fisheries.mdr.exception.AcronymNotFoundException;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.commons.domain.DateRange;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(Arquillian.class)
public class MdrStatusRepositoryIntegrationTest extends TransactionalTests {

    private static final int CODE_LIST_STATUS_IN_DATABASE_FROM_MODULE_STARTUP = 75;

    @Inject
    private MdrStatusRepository mdrStatusRepository;

    @Before
    public void prepare() throws ServiceException, ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date startDate = simpleDateFormat.parse("2015-10-10 16:02:59.047");
        Date endDate = simpleDateFormat.parse("2016-12-10 16:02:59.047");

        DateRange dateRange = new DateRange(startDate, endDate);

        MdrCodeListStatus actionType = new MdrCodeListStatus("ACTION_TYPE", null, new Date(), new Date(), null, true);
        AcronymVersion actionTypeVersion1 = new AcronymVersion("VERS_1", dateRange);
        AcronymVersion actionTypeVersion2 = new AcronymVersion("VERS_2", dateRange);
        actionTypeVersion1.setMdrCodeListStatus(actionType);
        actionTypeVersion2.setMdrCodeListStatus(actionType);
        actionType.setVersions(Sets.newHashSet(actionTypeVersion1, actionTypeVersion2));

        MdrCodeListStatus conversionFactor = new MdrCodeListStatus("CONVERSION_FACTOR", null, new Date(), new Date(), null, true);

        MdrCodeListStatus gearType = new MdrCodeListStatus("GEAR_TYPE", null, new Date(), new Date(), null, false);
        AcronymVersion gearTypeVersion2 = new AcronymVersion("VERS_2", dateRange);
        gearTypeVersion2.setMdrCodeListStatus(gearType);
        gearType.setVersions(Sets.newHashSet(gearTypeVersion2));

        List<MdrCodeListStatus> mdrCodeListStatusList = new ArrayList<>();
        mdrCodeListStatusList.add(actionType);
        mdrCodeListStatusList.add(conversionFactor);
        mdrCodeListStatusList.add(gearType);

        mdrStatusRepository.saveAcronymsStatusList(mdrCodeListStatusList);
    }

    @Test
    public void testGellAllAcronymStatuses() {
        List<MdrCodeListStatus> allAcronymsStatuses = mdrStatusRepository.getAllAcronymsStatuses();
        MdrCodeListStatus entity = allAcronymsStatuses.get(0);
        assertNotNull(entity);
    }

    @Test
    public void testgetStatusForAcronym() {
        MdrCodeListStatus status = mdrStatusRepository.getStatusForAcronym("ACTION_TYPE");
        assertNotNull(status);
    }

    @Test
    public void testgetStatusForAcronymNullReturn() {
        MdrCodeListStatus status = mdrStatusRepository.getStatusForAcronym(null);
        assertNull(status);
    }


    @Test
    public void testFindStatusAndRelatedVersionsByAcronym() {
        List<MdrCodeListStatus> actiontypeStatus = mdrStatusRepository.findStatusAndVersionsForAcronym("ACTION_TYPE");
        Set<AcronymVersion> actiontypeVersions = actiontypeStatus.get(0).getVersions();
        assertEquals(2, actiontypeVersions.size());
        assertNotNull(actiontypeStatus.get(0));
    }

    @Test
    public void testgetAllUpdatableAcronymsStatuses() {
        List<MdrCodeListStatus> status = mdrStatusRepository.getAllUpdatableAcronymsStatuses();
        assertEquals(CODE_LIST_STATUS_IN_DATABASE_FROM_MODULE_STARTUP + 2, status.size());
    }

    @Test
    public void testUpdateSchedulableForAcronym() {
        MdrCodeListStatus status = mdrStatusRepository.getStatusForAcronym("ACTION_TYPE");
        assertNotNull(status);
        mdrStatusRepository.updateSchedulableForAcronym("ACTION_TYPE", false);
        MdrCodeListStatus status_1 = mdrStatusRepository.getStatusForAcronym("ACTION_TYPE");
        assertEquals("false", status_1.getSchedulable().toString());
        mdrStatusRepository.updateSchedulableForAcronym("ACTION_TYPE", true);
        MdrCodeListStatus status_2 = mdrStatusRepository.getStatusForAcronym("ACTION_TYPE");
        assertEquals("true", status_2.getSchedulable().toString());
    }

    @Test
    public void testUpdateStatusAttemptForAcronym() {
        MdrCodeListStatus status = mdrStatusRepository.getStatusForAcronym("ACTION_TYPE");
        assertNotNull(status);
        mdrStatusRepository.updateStatusAttemptForAcronym("ACTION_TYPE", AcronymListState.NEWENTRY, new Date(), "uuid");
        MdrCodeListStatus status_1 = mdrStatusRepository.getStatusForAcronym("ACTION_TYPE");
        assertEquals(AcronymListState.NEWENTRY, status_1.getLastStatus());
        mdrStatusRepository.updateStatusAttemptForAcronym("ACTION_TYPE", AcronymListState.FAILED, new Date(), "uuid");
        MdrCodeListStatus status_2 = mdrStatusRepository.getStatusForAcronym("ACTION_TYPE");
        assertEquals(AcronymListState.FAILED, status_2.getLastStatus());
    }

    @Test
    public void testUpdateStatusFailedForAcronym() {
        MdrCodeListStatus status = mdrStatusRepository.getStatusForAcronym("ACTION_TYPE");
        assertNotNull(status);
        mdrStatusRepository.updateStatusFailedForAcronym("ACTION_TYPE");
        MdrCodeListStatus status_2 = mdrStatusRepository.getStatusForAcronym("ACTION_TYPE");
        assertEquals(AcronymListState.FAILED, status_2.getLastStatus());

    }

    @Test
    public void testUpdateStatusSuccessForAcronym() throws AcronymNotFoundException {
        MdrCodeListStatus status = mdrStatusRepository.getStatusForAcronym("ACTION_TYPE");
        assertNotNull(status);
        mdrStatusRepository.updateStatusSuccessForAcronym("ACTION_TYPE", AcronymListState.SUCCESS, Date.from(DateUtils.nowUTC()));
        MdrCodeListStatus status_2 = mdrStatusRepository.getStatusForAcronym("ACTION_TYPE");
        assertEquals(AcronymListState.SUCCESS, status_2.getLastStatus());

    }

    @Test
    public void testSaveAcronymsStatusList() throws ServiceException {
        // Given
        List<MdrCodeListStatus> allMdrStatuses = mdrStatusRepository.getAllAcronymsStatuses();
        int acronymStatusesBeforeSaving = allMdrStatuses.size();

        List<MdrCodeListStatus> diffList = mockMdrCodeListStatus();
        int diffListSize = diffList.size();

        // When
        mdrStatusRepository.saveAcronymsStatusList(diffList);

        // Then
        allMdrStatuses = mdrStatusRepository.getAllAcronymsStatuses();
        assertEquals(acronymStatusesBeforeSaving + diffListSize, allMdrStatuses.size());
        assertNotNull(mdrStatusRepository.findStatusAndVersionsForAcronym("SomeAcronym1"));
    }

    @Test
    public void testFlushSaveEach20AcronymsStatusList() throws ServiceException {
        // Given
        List<MdrCodeListStatus> allMdrStatuses = mdrStatusRepository.getAllAcronymsStatuses();
        int acronymStatusesBeforeSaving = allMdrStatuses.size();

        List<MdrCodeListStatus> diffList = mock42MdrCodeListStatus();
        int diffListSize = diffList.size();

        // When
        mdrStatusRepository.saveAcronymsStatusList(diffList);

        // Then
        allMdrStatuses = mdrStatusRepository.getAllAcronymsStatuses();
        assertEquals(acronymStatusesBeforeSaving + diffListSize, allMdrStatuses.size());
        assertNotNull(mdrStatusRepository.findStatusAndVersionsForAcronym("SomeAcronym1"));
    }

    @Test
    public void saveNotExistingAcronymValueFail() {
        try{
            mdrStatusRepository.updateStatusSuccessForAcronym("BAD_ACRONYM", AcronymListState.SUCCESS, Date.from(DateUtils.nowUTC()));
            fail("It should have failed since the BAD_ACRONYM doesn't exist");
        } catch (AcronymNotFoundException ex){
            assertEquals("The acronym status BAD_ACRONYM you searched for is not present!", ex.getMessage());
        }
    }

    private List<MdrCodeListStatus> mockMdrCodeListStatus() {
        List<MdrCodeListStatus> statusesList = new ArrayList<>();
        for(int i=0; i<5; i++){
            MdrCodeListStatus mdrStatAct = new MdrCodeListStatus();
            mdrStatAct.setObjectAcronym("SomeAcronym"+i);
            mdrStatAct.setSchedulable(true);
            mdrStatAct.setLastStatus(AcronymListState.SUCCESS);
            mdrStatAct.setLastAttempt(Date.from(DateUtils.nowUTC()));
            mdrStatAct.setLastSuccess(Date.from(DateUtils.nowUTC()));
            statusesList.add(mdrStatAct);
        }
        return statusesList;
    }

    private List<MdrCodeListStatus> mock42MdrCodeListStatus() {
        List<MdrCodeListStatus> statusesList = new ArrayList<>();
        for (int i = 0; i < 42; i++) {
            MdrCodeListStatus mdrStatAct = new MdrCodeListStatus();
            AcronymVersion version = new AcronymVersion();
            version.setMdrCodeListStatus(mdrStatAct);
            mdrStatAct.setObjectAcronym("SomeAcronym"+i);
            mdrStatAct.setSchedulable(true);
            mdrStatAct.setLastStatus(AcronymListState.SUCCESS);
            mdrStatAct.setLastAttempt(Date.from(DateUtils.nowUTC()));
            mdrStatAct.setLastSuccess(Date.from(DateUtils.nowUTC()));
            mdrStatAct.setVersions(new HashSet<>(Arrays.asList(version)));
            statusesList.add(mdrStatAct);
        }
        return statusesList;
    }


}
