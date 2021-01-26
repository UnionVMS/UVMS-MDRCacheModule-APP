package eu.europa.ec.fisheries.mdr.service.bean.synchronization;

import eu.europa.ec.fisheries.mdr.client.MdrWebServiceClient;
import eu.europa.ec.fisheries.mdr.dao.MdrConfigurationDao;
import eu.europa.ec.fisheries.mdr.service.bean.BaseMdrBean;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;

@ApplicationScoped
public class MDRWebserviceClientProducer extends BaseMdrBean {

    private MdrWebServiceClient mdrWebServiceClient;

    @PostConstruct
    public void init() {
        MdrConfigurationDao mdrConfigurationDao = initMDRConfigurationDao();
        mdrWebServiceClient = new MdrWebServiceClient();
        String wsdlLocation = mdrConfigurationDao.getMDRConfigurationValue(MdrConfigurationDao.WEBSERVICE_WSDL_LOCATION);
        if (!wsdlLocation.isEmpty()) {
            try {
                mdrWebServiceClient.setWsdlLocation(new URL(wsdlLocation));
                String webServiceNameSpace = mdrConfigurationDao.getMDRConfigurationValue(MdrConfigurationDao.WEBSERVICE_NAMESPACE);
                String webServiceName = mdrConfigurationDao.getMDRConfigurationValue(MdrConfigurationDao.WEBSERVICE_NAME);
                mdrWebServiceClient.setServiceName(new QName(webServiceNameSpace, webServiceName));
            } catch (MalformedURLException e) {
                throw new RuntimeException("Tried to create URL for WSDL located at " + wsdlLocation + " but failed: ", e);
            }

        }
    }

    private MdrConfigurationDao initMDRConfigurationDao() {
        initEntityManager();
        return new MdrConfigurationDao(getEntityManager());
    }

    @Produces
    @ApplicationScoped
    public MdrWebServiceClient getMdrWebServiceClient() {
        return mdrWebServiceClient;
    }
}
