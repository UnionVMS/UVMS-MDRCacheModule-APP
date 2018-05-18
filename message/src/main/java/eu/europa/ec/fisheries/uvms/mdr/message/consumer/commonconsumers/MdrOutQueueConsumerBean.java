package eu.europa.ec.fisheries.uvms.mdr.message.consumer.commonconsumers;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractConsumer;

import javax.ejb.Local;
import javax.ejb.Stateless;

@Stateless
@Local
public class MdrOutQueueConsumerBean extends AbstractConsumer {

    @Override
    public String getDestinationName() {
        return MessageConstants.QUEUE_MDR;
    }
}