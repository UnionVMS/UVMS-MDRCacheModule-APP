package eu.europa.ec.fisheries.mdr.client;

import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;
import eu.europa.ec.mare.fisheries.model.mdr.v1.PageRequestType;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRService;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRServiceException;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRService_Service;
import lombok.extern.slf4j.Slf4j;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.List;

@Slf4j
public class MdrWebServiceClient {

    private URL wsdlLocation;

    private QName serviceName = MDRService_Service.SERVICE;

    public void setWsdlLocation(URL wsdlLocation) {
        this.wsdlLocation = wsdlLocation;
    }

    public void setServiceName(QName serviceName) {
        this.serviceName = serviceName;
    }

    public List<MDRDataNodeType> getMDRList(String acronym, long offset, long limit) throws MDRServiceException {
        MDRService_Service ss = new MDRService_Service(this.wsdlLocation, this.serviceName);
        MDRService port = ss.getMDRServicePort();

        PageRequestType page = new PageRequestType();
        page.setOffset(offset);
        page.setLimit(limit);

        return port.getLatestVersionOfMDRList(acronym, page).getContainedMDRDataNode();
    }
}
