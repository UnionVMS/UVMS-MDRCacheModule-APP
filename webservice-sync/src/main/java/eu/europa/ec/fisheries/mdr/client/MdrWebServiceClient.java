package eu.europa.ec.fisheries.mdr.client;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.mare.fisheries.services.MDREndPointService;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MdrWebServiceClient {

    private URL wsdlLocation;

    private QName serviceName = new QName("http://services.fisheries.mare.ec.europa.eu/", "MDREndPointService");

    public void setWsdlLocation(URL wsdlLocation) {
        this.wsdlLocation = wsdlLocation;
    }
    
    public void setServiceName(QName serviceName) {
        this.serviceName = serviceName;
    }

    public List<MDRDataNodeType> getMDRList(String acronym) {
        List<MDRDataNodeType> results = new ArrayList<>();
                
        URL wsdlURL = wsdlLocation;
        MDREndPointService ss = new MDREndPointService(wsdlURL, serviceName);
        MDRService mdrServicePort = ss.getMDREndPointPort();
        try {
            results = mdrServicePort.getLatestVersionOfMDRList(acronym).getContainedMDRDataNode();
        } catch (Exception e) {
            log.error("Failed to communicate with MDR webservice: " + e.getMessage());
        }
        return results;
    }
}
