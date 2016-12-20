/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.mdr.repository;

import eu.europa.ec.fisheries.mdr.domain.MdrConfiguration;
import eu.europa.ec.fisheries.mdr.domain.codelists.base.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.domain.MdrCodeListStatus;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import un.unece.uncefact.data.standard.mdr.response.FLUXMDRReturnMessage;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

@Local
public interface MdrRepository {

	<T extends MasterDataRegistry> List<T> findAllForEntity(Class<T> mdr) throws ServiceException;
	
	<T extends MasterDataRegistry> List<T> findEntityByHqlQuery(Class<T> type, String hqlQuery, Map<Integer, String> parameters,
			int maxResultLimit) throws ServiceException;

    void updateMdrEntity(FLUXMDRReturnMessage response);

    List<MdrConfiguration> getAllConfigurations() throws ServiceException;

	MdrConfiguration getConfigurationByName(String vonfigName);

	List<MdrCodeListStatus> findAllStatuses() throws ServiceException;

	MdrCodeListStatus findStatusByAcronym(String acronym);

	void changeMdrSchedulerConfiguration(String newCronExpression) throws ServiceException;

	MdrConfiguration getMdrSchedulerConfiguration();

	// TODO : Delete me when done testing the functionality
	void insertTestData(List<? extends MasterDataRegistry> testList) throws ServiceException;
}