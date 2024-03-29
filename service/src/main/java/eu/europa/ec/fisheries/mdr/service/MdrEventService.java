/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.mdr.service;

import eu.europa.ec.fisheries.uvms.mdr.message.event.GetAllMdrCodeListsMessageEvent;
import eu.europa.ec.fisheries.uvms.mdr.message.event.GetLastRefreshDate;
import eu.europa.ec.fisheries.uvms.mdr.message.event.GetSingleMdrListMessageEvent;
import eu.europa.ec.fisheries.uvms.mdr.message.event.MdrSyncMessageEvent;
import eu.europa.ec.fisheries.uvms.mdr.message.event.carrier.EventMessage;
import javax.ejb.Local;
import javax.enterprise.event.Observes;

@Local
public interface MdrEventService {

	void recievedSyncMdrEntityMessage(@Observes @MdrSyncMessageEvent EventMessage message);

    void recievedGetSingleMdrCodeListMessage(@Observes @GetSingleMdrListMessageEvent EventMessage message);

    void recievedGetAllMdrCodeListMessage(@Observes @GetAllMdrCodeListsMessageEvent EventMessage message);

    void receivedGetLastRefreshDateFromStatuses(@Observes @GetLastRefreshDate EventMessage message);
}