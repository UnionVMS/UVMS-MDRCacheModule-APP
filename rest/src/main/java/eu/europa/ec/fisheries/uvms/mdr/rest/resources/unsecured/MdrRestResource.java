package eu.europa.ec.fisheries.uvms.mdr.rest.resources.unsecured;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.repository.MdrLuceneSearchRepository;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.mdr.model.mapper.MdrGenericObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import un.unece.uncefact.data.standard.mdr.communication.*;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;

@Path("json")
public class MdrRestResource {

    private static final Logger log = LoggerFactory.getLogger(MdrRestResource.class);

    private static final String STAR = "*";

    @EJB
    private MdrLuceneSearchRepository mdrLuceneSearchRepository;

    @POST
    @Path("getAcronym")
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getAcronym(MdrGetCodeListRequest request) {
        try {
            List<String> columnsToFilter = request.getColumnsToFilters();
            String[] columnsToFilterArray;
            if (CollectionUtils.isNotEmpty(columnsToFilter)) {
                columnsToFilterArray = columnsToFilter.toArray(new String[0]);
            } else {
                columnsToFilterArray = new String[]{"code", "description"};
            }

            String filter = request.getFilter();
            if (StringUtils.isNotEmpty(filter) && !filter.equals(STAR)) {
                filter = STAR + filter + STAR;
            } else {
                filter = STAR;
            }

            BigInteger wantedNumberOfResults = request.getWantedNumberOfResults();
            Integer numberOfResults = wantedNumberOfResults != null ? wantedNumberOfResults.intValue() : 9999999;

            List<? extends MasterDataRegistry> mdrList = mdrLuceneSearchRepository.findCodeListItemsByAcronymAndFilter(
                    request.getAcronym(),
                    0,
                    numberOfResults,
                    null,
                    false,
                    filter,
                    columnsToFilterArray);

            MdrGetCodeListResponse response = new MdrGetCodeListResponse();
            response.setAcronym(request.getAcronym());
            response.setValidation(new ValidationResult(ValidationResultType.OK, "Validation is OK."));
            List<ObjectRepresentation> objectRepresentations = null;
            if (CollectionUtils.isNotEmpty(mdrList)) {
                objectRepresentations = MdrGenericObjectMapper.mapToGenericObjectRepresentation(mdrList);
            }
            response.setDataSets(objectRepresentations);
            response.setMethod(MdrModuleMethod.MDR_CODE_LIST_RESP);

            return Response.ok(response).build();
        } catch (ServiceException e) {
            log.error(e.toString(),e);
            return Response.status(500).entity(ExceptionUtils.getRootCause(e)).build();
        }
    }
}
