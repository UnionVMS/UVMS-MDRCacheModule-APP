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

    private QName portName = new QName("http://services.fisheries.mare.ec.europa.eu/", "MDREndPointPort");

    public void setWsdlLocation(URL wsdlLocation) {
        this.wsdlLocation = wsdlLocation;
    }
    
    public void setServiceName(QName serviceName) {
        this.serviceName = serviceName;
    }

    public void setPortName(QName portName) {
        this.portName = portName;
    }

    public List<MDRDataNodeType> getMDRList(String acronym) {
        List<MDRDataNodeType> results = new ArrayList<>();
                
        URL wsdlURL = wsdlLocation;
        MDREndPointService ss = new MDREndPointService(wsdlURL, serviceName);
        MDRService mdrServicePort = ss.getPort(portName, MDRService.class);
        try {
            results = mdrServicePort.getLatestVersionOfMDRList(acronym).getContainedMDRDataNode();
        } catch (Exception e) {
            throw new RuntimeException("Failed to communicate with MDR webservice", e);
        }
        return results;
    }
}
