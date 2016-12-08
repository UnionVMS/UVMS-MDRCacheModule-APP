/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.mdr.rest.resources;

import eu.europa.ec.fisheries.mdr.domain.codelists.base.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.repository.MdrRepository;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.mdr.rest.resources.util.IUserRoleInterceptor;
import eu.europa.ec.fisheries.uvms.mdr.rest.resources.util.MdrExceptionInterceptor;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import lombok.extern.slf4j.Slf4j;
import un.unece.uncefact.data.standard.communication.MdrFeaturesEnum;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by georgige on 8/22/2016.
 */
@Slf4j
@Path("/cl")
public class MDRCodeListResource extends UnionVMSResource {

    @EJB
    private MdrRepository mdrService;

    @GET
    @Path("/{acronym}/{offset}/{pageSize}")
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors(MdrExceptionInterceptor.class)
    @IUserRoleInterceptor(requiredUserRole = {MdrFeaturesEnum.MDR_SEARCH_CODE_LIST_ITEMS})
    public Response findCodeListByAcronymFilterredByFilter(@Context HttpServletRequest request,
                                                            @PathParam("acronym") String acronym,
                                                            @PathParam("offset") Integer offset,
                                                            @PathParam("pageSize") Integer pageSize,
                                                            @QueryParam("sortBy") String sortBy,
                                                            @QueryParam("sortReversed") Boolean isReversed,
                                                            @QueryParam("filter") String filter,
                                                            @QueryParam("searchAttribute") String searchAttribute) {
        log.debug("findCodeListByAcronymFilterredByFilter(acronym={}, offset={}, pageSize={}, sortBy={}, isReversed={}, filter={}, searchAttribute={})", acronym,offset,pageSize,sortBy,isReversed,filter,searchAttribute);
        try {
            List<? extends MasterDataRegistry> mdrList = mdrService.findCodeListItemsByAcronymAndFilter(acronym, offset, pageSize, sortBy, isReversed, filter, searchAttribute);
            int totalCodeItemsCount = mdrService.countCodeListItemsByAcronymAndFilter(acronym, filter, searchAttribute);
            return createSuccessPaginatedResponse(mdrList, totalCodeItemsCount);
        } catch (ServiceException e) {
            log.error("Internal Server Error.", e);
            return createErrorResponse("internal_server_error");
        }
    }
}
