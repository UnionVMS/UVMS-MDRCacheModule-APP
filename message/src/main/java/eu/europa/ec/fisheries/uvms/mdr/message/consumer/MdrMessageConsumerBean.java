/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.mdr.message.consumer;


import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.mdr.message.event.GetAllMdrCodeListsMessageEvent;
import eu.europa.ec.fisheries.uvms.mdr.message.event.GetLastRefreshDate;
import eu.europa.ec.fisheries.uvms.mdr.message.event.GetSingleMdrListMessageEvent;
import eu.europa.ec.fisheries.uvms.mdr.message.event.MdrSyncMessageEvent;
import eu.europa.ec.fisheries.uvms.mdr.message.event.carrier.EventMessage;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import un.unece.uncefact.data.standard.mdr.communication.MdrModuleMethod;
import un.unece.uncefact.data.standard.mdr.communication.MdrModuleRequest;


@MessageDriven(mappedName = MessageConstants.QUEUE_MDR_EVENT, activationConfig = {
    @ActivationConfigProperty(propertyName = MessageConstants.MESSAGING_TYPE_STR,   propertyValue = MessageConstants.CONNECTION_TYPE),
    @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_TYPE_STR, propertyValue = MessageConstants.DESTINATION_TYPE_QUEUE),
    @ActivationConfigProperty(propertyName = MessageConstants.DESTINATION_STR,      propertyValue = MessageConstants.MDR_MESSAGE_IN_QUEUE_NAME)
})
@Slf4j
public class MdrMessageConsumerBean implements MessageListener {

    @Inject
    @MdrSyncMessageEvent
    private Event<EventMessage> synMdrListEvent;

    @Inject
    @GetSingleMdrListMessageEvent
    private Event<EventMessage> getSingleMdrListEvent;

    @Inject
    @GetAllMdrCodeListsMessageEvent
    private Event<EventMessage> getAllMdrListEvent;

    @Inject
    @GetLastRefreshDate
    private Event<EventMessage> getLastRefreshDate;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void onMessage(Message message) {
        
        try {
            TextMessage textMessage = (TextMessage) message;
            MdrModuleRequest request = JAXBUtils.unMarshallMessage(textMessage.getText(), MdrModuleRequest.class);
            if(request==null){
                log.error("[ERROR] Request is null!!!");
                return;
            }
            MdrModuleMethod requestMethod = request.getMethod();
            if (requestMethod == null) {
                log.error("[ERROR] [ERROR] [ERROR] Request method is null! Request type : {}", request.getClass().getSimpleName());
                return;
            }
            EventMessage eventToBeFired = new EventMessage(textMessage);
            log.info("[INFO] Received following request method in MDR [{}]", requestMethod);
            switch (requestMethod) {
                case SYNC_MDR_CODE_LIST :
                	 synMdrListEvent.fire(eventToBeFired);
                     break;
                case GET_MDR_CODE_LIST :
                     getSingleMdrListEvent.fire(eventToBeFired);
                     break;
                case GET_ALL_MDR_CODE_LIST :
                    getAllMdrListEvent.fire(eventToBeFired);
                    break;
                case GET_LAST_REFRESH_DATE :
                    getLastRefreshDate.fire(eventToBeFired);
                    break;
                default:
                    log.error("[ERROR] Request method {} is not implemented!!", requestMethod.name());
                   // errorEvent.fire(new EventMessage(textMessage, "[ Request method " + request.getMethod().name() + "  is not implemented ]"));
            }
        } catch (JMSException | JAXBException | NullPointerException | ClassCastException e) {
            log.error("[ERROR] Error when receiving message in MDR module:  {}", e);
           // errorEvent.fire(new EventMessage(textMessage, "Error when receiving message in movement: " + e.getMessage()));
        }
    }

}
