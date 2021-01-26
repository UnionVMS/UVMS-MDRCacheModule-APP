package eu.europa.ec.fisheries.mdr.client;

import org.junit.Ignore;

@Ignore
public class TestTheClient {

    // 1. Set this to the URL of the WSDL
    private static final String WEBSERVICE_WSDL_LOCATION = "http://localhost:8080/mdr-service/v1/MDRService?wsdl";
    // 2. Set this to the target namespace from the WSDL
    private static final String WEBSERVICE_NAMESPACE = "http://services.fisheries.mare.ec.europa.eu/";
    // 3. Set this to the "service" name from your WSDL
    private static final String WEBSERVICE_NAME = "MDREndPointService";
    // The properties above match the configuration in MDR.mdr_configuration

    // 4. Activate any or both of the tests

    /**
     * Test the generated classes by using them directly.
     */
//	@Test
    public void testManual() throws Exception {
		/*
		MDREndPointService ss = new MDREndPointService(
				new URL(WEBSERVICE_WSDL_LOCATION),
				new QName(WEBSERVICE_NAMESPACE, WEBSERVICE_NAME)
		);
		final MDRService port = ss.getPort(new QName(WEBSERVICE_NAMESPACE, "MDREndPointPort"), MDRService.class);
		final MDRDataSetType mdrList = port.getLatestVersionOfMDRList("FAO_AREA");
		assertEquals("FAO_AREA", mdrList.getName());
		assertTrue(mdrList.getContainedMDRDataNode().size() > 1);
		*/
    }

    /**
     * Test the generated classes by using them through the client.
     */
//	@Test
    public void testWithClient() throws Exception {
		/*
		final MdrWebServiceClient mdrWebServiceClient = new MdrWebServiceClient();
		mdrWebServiceClient.setWsdlLocation(new URL(WEBSERVICE_WSDL_LOCATION));
		mdrWebServiceClient.setServiceName(new QName(WEBSERVICE_NAMESPACE, WEBSERVICE_NAME));
		final List<MDRDataNodeType> result = mdrWebServiceClient.getMDRList("FAO_AREA");
		assertTrue(result.size() > 1);
		 */
    }
}
