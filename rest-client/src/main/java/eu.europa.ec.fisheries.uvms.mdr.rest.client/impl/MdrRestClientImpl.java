/*
        ﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
        © European Union, 2015-2016.
        This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
        redistribute it and/or modify it under the terms of the GNU General Public License as published by the
        Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
        the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
        FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
        copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
*/

package eu.europa.ec.fisheries.uvms.mdr.rest.client.impl;


import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import eu.europa.ec.fisheries.uvms.config.event.ConfigSettingEvent;
import eu.europa.ec.fisheries.uvms.config.event.ConfigSettingUpdatedEvent;
import eu.europa.ec.fisheries.uvms.mdr.model.exception.MdrRestClientException;
import eu.europa.ec.fisheries.uvms.mdr.rest.client.MdrClient;
import eu.europa.ec.fisheries.uvms.mdr.rest.client.config.MdrRestClientConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetAllCodeListsResponse;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListRequest;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListResponse;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetLastRefreshDateResponse;
import un.unece.uncefact.data.standard.mdr.response.FLUXMDRReturnMessage;

@ApplicationScoped
@Slf4j
public class MdrRestClientImpl implements MdrClient {

    private static final String SYNC_MDR_CODE_LIST = "/sync-mdr-code-list";
    private static final String GET_MDR_CODE_LIST = "/get-mdr-code-list";
    private static final String GET_MDR_STATUS = "/get-mdr-status";
    private static final String GET_ALL_MDR_CODE_LIST = "/get-all-mdr-code-list";
    private static final String GET_LAST_REFRESH_DATE = "/get-last-refresh-date";

    private MdrRestClientConfig config;

    private WebTarget webTarget;

    @Inject
    public MdrRestClientImpl(MdrRestClientConfig config) {
        this.config = config;
    }

    @PostConstruct
    public void init() {
        configureNewClient();
    }

    @SneakyThrows
    @Override
    public void syncMdrCodeList(FLUXMDRReturnMessage fluxmdrReturnMessage) {
        try {
            Response response = webTarget
                    .path(SYNC_MDR_CODE_LIST)
                    .request()
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.json(fluxmdrReturnMessage), Response.class);

            handleResponse(response);
        } catch (ResponseProcessingException e) {
            log.error("Error processing response from server");
            throw new MdrRestClientException("Error response processing from server", e);
        } catch (ProcessingException e) {
            log.error("I/O error processing response");
            throw new MdrRestClientException("I/O error processing response ", e);
        } catch (WebApplicationException e) {
            log.error("Error response from server");
            throw new MdrRestClientException("Error response from server", e);
        }
    }

    @SneakyThrows
    @Override
    public MdrGetCodeListResponse getSingleMdrCodeListMessage(MdrGetCodeListRequest request) {
        try {
            Response response = webTarget
                    .path(GET_MDR_CODE_LIST)
                    .request()
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.json(request), Response.class);

            return handleMdrGetCodeListResponse(response);
        } catch (ResponseProcessingException e) {
            log.error("Error processing response from server");
            throw new MdrRestClientException("Error response processing from server", e);
        } catch (ProcessingException e) {
            log.error("I/O error processing response");
            throw new MdrRestClientException("I/O error processing response ", e);
        } catch (WebApplicationException e) {
            log.error("Error response from server");
            throw new MdrRestClientException("Error response from server", e);
        }
    }

    @SneakyThrows
    @Override
    public MdrGetCodeListResponse getStatusRequest() {
        try {
            Response response = webTarget
                    .path(GET_MDR_STATUS)
                    .request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get(Response.class);

            return handleMdrGetCodeListResponse(response);
        } catch (ResponseProcessingException e) {
            log.error("Error processing response from server");
            throw new MdrRestClientException("Error response processing from server", e);
        } catch (ProcessingException e) {
            log.error("I/O error processing response");
            throw new MdrRestClientException("I/O error processing response ", e);
        } catch (WebApplicationException e) {
            log.error("Error response from server");
            throw new MdrRestClientException("Error response from server", e);
        }
    }


    @SneakyThrows
    @Override
    public MdrGetAllCodeListsResponse getAllMdrCodeList() {
        try {
            Response response = webTarget
                    .path(GET_ALL_MDR_CODE_LIST)
                    .request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get(Response.class);

            return handleAllMdrCodeListMessageResponse(response);
        } catch (ResponseProcessingException e) {
            log.error("Error processing response from server");
            throw new MdrRestClientException("Error response processing from server", e);
        } catch (ProcessingException e) {
            log.error("I/O error processing response");
            throw new MdrRestClientException("I/O error processing response ", e);
        } catch (WebApplicationException e) {
            log.error("Error response from server");
            throw new MdrRestClientException("Error response from server", e);
        }
    }

    @SneakyThrows
    @Override
    public MdrGetLastRefreshDateResponse getLastRefreshDate() {
        try {
            Response response = webTarget
                    .path(GET_LAST_REFRESH_DATE)
                    .request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get(Response.class);

            return handleMdrGetLastRefreshDateResponse(response);
        } catch (ResponseProcessingException e) {
            log.error("Error processing response from server");
            throw new MdrRestClientException("Error response processing from server", e);
        } catch (ProcessingException e) {
            log.error("I/O error processing response");
            throw new MdrRestClientException("I/O error processing response ", e);
        } catch (WebApplicationException e) {
            log.error("Error response from server");
            throw new MdrRestClientException("Error response from server", e);
        }
    }

    private void handleResponse(Response response) throws MdrRestClientException {
        handleNotOKStatusCode(response);
        response.close();
    }

    private MdrGetCodeListResponse handleMdrGetCodeListResponse(Response response) throws MdrRestClientException {
        handleNotOKStatusCode(response);
        MdrGetCodeListResponse mdrGetCodeListResponse = response.readEntity(new GenericType<MdrGetCodeListResponse>() {
        });
        response.close();
        return mdrGetCodeListResponse;
    }


    private MdrGetLastRefreshDateResponse handleMdrGetLastRefreshDateResponse(Response response) throws MdrRestClientException {
        handleNotOKStatusCode(response);
        MdrGetLastRefreshDateResponse mdrGetLastRefreshDateResponse = response.readEntity(new GenericType<MdrGetLastRefreshDateResponse>() {
        });
        response.close();
        return mdrGetLastRefreshDateResponse;
    }

    private MdrGetAllCodeListsResponse handleAllMdrCodeListMessageResponse(Response response) throws MdrRestClientException {
        handleNotOKStatusCode(response);
        MdrGetAllCodeListsResponse mdrGetAllCodeListsResponse = response.readEntity(new GenericType<MdrGetAllCodeListsResponse>() {
        });
        response.close();
        return mdrGetAllCodeListsResponse;
    }


    private void handleNotOKStatusCode(Response response) throws MdrRestClientException {
        if (response.getStatus() < 200 && response.getStatus() >= 300) {
            log.debug("Mdr Service responded with error code {} - {}", response.getStatus(), response.getEntity());
            throw new MdrRestClientException("Mdr service response: " + response.getStatusInfo().getReasonPhrase());
        }
    }

    public void setConfig(@Observes @ConfigSettingUpdatedEvent ConfigSettingEvent settingEvent) {
        if (config.isEndpointUpdateEvent(settingEvent.getKey())) {
            configureNewClient();
        }
    }

    private void configureNewClient() {
        webTarget = null;
        String url = config.getMdrGatewayEndpoint();
        Client client = ClientBuilder.newClient();
        ContextResolver<ObjectMapper> objectMapperContextResolver = new ContextResolver<ObjectMapper>() {
            @Override
            public ObjectMapper getContext(Class<?> type) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                mapper.registerModule(new JaxbAnnotationModule());
                return mapper;
            }
        };
        client.register(objectMapperContextResolver);
        webTarget = client.target(url);
    }
}
