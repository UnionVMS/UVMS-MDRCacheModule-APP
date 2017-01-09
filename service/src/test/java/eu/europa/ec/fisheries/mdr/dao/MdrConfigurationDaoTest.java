/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.mdr.dao;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by kovian on 06/01/2017.
 */
public class MdrConfigurationDaoTest extends BaseMdrDaoTest {

    private MdrConfigurationDao mdrConfigDao = new MdrConfigurationDao(em);

    @Before
    @SneakyThrows
    public void prepare() {
        Operation operation = sequenceOf(DELETE_ALL_CONFIGURATIONS, INSERT_ALL_CONFIGURATIONS);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    public void deleteAllData() {
        Operation operation = sequenceOf(DELETE_ALL_CONFIGURATIONS);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @SneakyThrows
    public void testGetEntityManager(){
        dbSetupTracker.skipNextLaunch();
        assertNotNull(mdrConfigDao.getEntityManager());
    }

    @Test
    @SneakyThrows
    public void testGetMdrSchedulerConfiguration(){
        dbSetupTracker.skipNextLaunch();
        assertNotNull(mdrConfigDao.getMdrSchedulerConfiguration());
    }

    @Test
    @SneakyThrows
    public void testFindAllConfigurations(){
        dbSetupTracker.skipNextLaunch();
        assertEquals(4,mdrConfigDao.findAllConfigurations().size());
    }

    @Test
    @SneakyThrows
    public void testChangeMdrSchedulerConfiguration(){
        dbSetupTracker.skipNextLaunch();
        try {
            mdrConfigDao.changeMdrSchedulerConfiguration("1 2 * * *");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        assertEquals("1 2 * * *", mdrConfigDao.getMdrSchedulerConfiguration().getConfigValue());
    }

    @Test
    @SneakyThrows
    public void saveBadSchedulerConfiguration(){
        dbSetupTracker.skipNextLaunch();
        try {
            mdrConfigDao.changeMdrSchedulerConfiguration(null);
            fail("It should have thrown @ this point..");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    @SneakyThrows
    public void saveInexistentSchedulerConfiguration(){
        dbSetupTracker.skipNextLaunch();
        deleteAllData();
        try {
            mdrConfigDao.changeMdrSchedulerConfiguration(null);
            fail("It should have thrown @ this point..");
        } catch (ServiceException e) {
            assertEquals("Cron expression cannot be empty!", e.getMessage());
        }


    }

}
