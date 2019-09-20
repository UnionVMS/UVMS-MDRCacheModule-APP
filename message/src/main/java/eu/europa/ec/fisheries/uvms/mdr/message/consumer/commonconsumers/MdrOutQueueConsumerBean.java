package eu.europa.ec.fisheries.uvms.mdr.message.consumer.commonconsumers;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractConsumer;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.Destination;
import javax.jms.Queue;

@Stateless
@Local
public class MdrOutQueueConsumerBean extends AbstractConsumer {

    @Resource(mappedName =  "java:/" + MessageConstants.QUEUE_MDR)
    private Queue destination;

    @Override
    public Destination getDestination() {
        return destination;
    }
}