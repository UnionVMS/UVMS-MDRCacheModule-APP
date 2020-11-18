package eu.europa.ec.fisheries.uvms.mdr.rest.client.config;


public interface MdrRestClientConfig {

    String getMdrGatewayEndpoint();

    boolean isEndpointUpdateEvent(String endpointSettingKey);

}