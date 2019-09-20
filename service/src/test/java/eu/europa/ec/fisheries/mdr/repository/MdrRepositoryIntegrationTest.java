/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.mdr.repository;

import eu.europa.ec.fisheries.mdr.TransactionalTests;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FaoSpecies;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class MdrRepositoryIntegrationTest extends TransactionalTests {

    @Inject
    private MdrRepository mdrRepository;

    @Before
    public void setUp() throws Exception {
        List<FaoSpecies> entities = new ArrayList<>();
        FaoSpecies faoSpecies = new FaoSpecies();
        faoSpecies.setCode("C");
        faoSpecies.setDescription("Creation");
        entities.add(faoSpecies);
        mdrRepository.insertNewData(entities);
    }

    @Test
    public void bulkDeleteAndInsert() throws ServiceException {
        // Given
        List<FaoSpecies> entities = mockSpecies();
        
        // When
        mdrRepository.deleteDataAndPurgeIndexes(entities);
        mdrRepository.insertNewDataWithoutPurging(entities);

        List<FaoSpecies> allForEntity = mdrRepository.findAllForEntity(FaoSpecies.class);

        // Then
        assertEquals(11, allForEntity.size());
    }

    private List<FaoSpecies> mockSpecies() {
        List<FaoSpecies> list = new ArrayList<>();
        for (int i = 0; i < 11; i++){
            FaoSpecies species = new FaoSpecies();
            species.setCode("areaCode" + i);
            species.setEnName("someDescription" + i);
            species.setCode("44" + i);
            species.setScientificName("StrangeName" + i);
            list.add(species);
        }
        return list;
    }
}
