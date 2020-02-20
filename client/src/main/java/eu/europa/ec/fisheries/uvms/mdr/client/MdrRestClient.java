package eu.europa.ec.fisheries.uvms.mdr.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListRequest;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListResponse;
import un.unece.uncefact.data.standard.mdr.communication.MdrModuleMethod;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Stateless
public class MdrRestClient {

    private WebTarget webTarget;

    @Resource(name = "java:global/mdr_endpoint")
    private String mdrEndpoint;

    @PostConstruct
    public void initClient() {
        String url = mdrEndpoint + "/mdrnonsecure/json/";

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        clientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        clientBuilder.readTimeout(30, TimeUnit.SECONDS);
        Client client = clientBuilder.build();
        client.register(new ContextResolver<ObjectMapper>() {
            @Override
            public ObjectMapper getContext(Class<?> type) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JaxbAnnotationModule());
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                return mapper;
            }
        });
        webTarget = client.target(url);
    }

    public MdrGetCodeListResponse getAcronym(String acronym, String filterOnValue, List<String> filterOnColumns, int numberOfResults) {
        MdrGetCodeListRequest mdrGetCodeListRequest = new MdrGetCodeListRequest();
        mdrGetCodeListRequest.setMethod(MdrModuleMethod.GET_MDR_CODE_LIST);
        mdrGetCodeListRequest.setAcronym(acronym);
        mdrGetCodeListRequest.setFilter(filterOnValue);
        mdrGetCodeListRequest.setColumnsToFilters(filterOnColumns);
        mdrGetCodeListRequest.setWantedNumberOfResults(BigInteger.valueOf(numberOfResults));

        Invocation.Builder builder = webTarget
                .path("getAcronym")
                .request(MediaType.APPLICATION_JSON);

        Response response = builder.post(Entity.entity(mdrGetCodeListRequest, MediaType.APPLICATION_JSON_TYPE));
        if (response.getStatus() != 200) {
            return null;
        }

        return response.readEntity(MdrGetCodeListResponse.class);
    }
}
