package eu.europa.ec.fisheries.mdr.service.bean;

import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractConfigSettingsBean;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractConsumer;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractProducer;
import eu.europa.ec.fisheries.uvms.mdr.message.consumer.commonconsumers.MdrEventQueueConsumerBean;
import eu.europa.ec.fisheries.uvms.mdr.message.consumer.commonconsumers.MdrOutQueueConsumerBean;
import eu.europa.ec.fisheries.uvms.mdr.message.producer.MdrProducerBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * Created by kovian on 31/05/2017.
 */
@Singleton
@Slf4j
public class MdrConfigurationCache extends AbstractConfigSettingsBean {

    @EJB
    private MdrOutQueueConsumerBean consumer;

    @EJB
    private MdrProducerBean producer;

    @Override
    protected MdrOutQueueConsumerBean getConsumer() {
        return consumer;
    }

    @Override
    protected AbstractProducer getProducer() {
        return producer;
    }

    @Override
    protected String getModuleName() {
        return "mdr";
    }

    public String getNationCode() {
        String fluxNationCode = getSingleConfig("flux_local_nation_code");
        fluxNationCode = StringUtils.isNotEmpty(fluxNationCode) ? fluxNationCode : "please_set_flux_local_nation_code_in_config_settings_table";
        return fluxNationCode;
    }

}