package eu.europa.ec.fisheries.uvms.mdr.rest.resources;


import eu.europa.ec.fisheries.mdr.service.MdrServiceBean;
import eu.europa.ec.fisheries.uvms.mdr.rest.resources.exception.MdrFacadeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetAllCodeListsResponse;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListRequest;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListResponse;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetLastRefreshDateResponse;
import un.unece.uncefact.data.standard.mdr.response.FLUXMDRReturnMessage;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/mdr-gateway")
@Stateless
public class MdrFacadeResource {

    private static final String SYNC_MDR_CODE_LIST = "/sync-mdr-code-list" ;
    private static final String GET_MDR_CODE_LIST = "/get-mdr-code-list";
    private static final String GET_MDR_STATUS = "/get-mdr-status";
    private static final String GET_ALL_MDR_CODE_LIST = "/get-all-mdr-code-list";
    private static final String GET_LAST_REFRESH_DATE = "/get-last-refresh-date";

    final static Logger LOG = LoggerFactory.getLogger(MdrFacadeResource.class);


    @Inject
    private MdrServiceBean mdrServiceBeanImpl;

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(SYNC_MDR_CODE_LIST)
    public void syncMdrCodeList(FLUXMDRReturnMessage fluxmdrReturnMessage) throws MdrFacadeException {
        try {
            LOG.info("Synchronizing mdr code list :{}", fluxmdrReturnMessage);
            mdrServiceBeanImpl.receivedSyncMdrEntityMessage(fluxmdrReturnMessage);
        } catch (Exception e) {
            throw new MdrFacadeException("Exception while trying to synchronizing mdr code list [ " + e.getMessage() + "]");
        }
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(GET_MDR_CODE_LIST)
    public MdrGetCodeListResponse getMdrCodeList(MdrGetCodeListRequest mdrGetCodeListRequest) throws MdrFacadeException {
        try {
            LOG.info("Getting mdr code list :{}", mdrGetCodeListRequest);
            return mdrServiceBeanImpl.receivedGetSingleMdrCodeListMessage(mdrGetCodeListRequest);
        } catch (Exception e) {
            throw new MdrFacadeException("Exception while trying to get mdr code list [ " + e.getMessage() + "]");
        }
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(GET_MDR_STATUS)
    public MdrGetCodeListResponse getMdrStatus() throws MdrFacadeException {
        try {
            LOG.info("Getting mdr status");
            return mdrServiceBeanImpl.getMdrStatus();
        } catch (Exception e) {
            throw new MdrFacadeException("Exception while trying to get mdr status [ " + e.getMessage() + "]");
        }
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(GET_ALL_MDR_CODE_LIST)
    public MdrGetAllCodeListsResponse getAllMdrCodeList() throws MdrFacadeException {
        try {
            LOG.info("Getting mdr code list");
            return mdrServiceBeanImpl.receivedGetAllMdrCodeListMessage();
        } catch (Exception e) {
            throw new MdrFacadeException("Exception while trying to get mdr code list [ " + e.getMessage() + "]");
        }
    }


    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path(GET_LAST_REFRESH_DATE)
    public MdrGetLastRefreshDateResponse getLastRefreshDate() throws MdrFacadeException {
        try {
            LOG.info("Getting last refresh date");
            return mdrServiceBeanImpl.receivedGetLastRefreshDateFromStatuses();
        } catch (Exception e) {
            throw new MdrFacadeException("Exception while trying to get last refresh date [ " + e.getMessage() + "]");
        }
    }

}
