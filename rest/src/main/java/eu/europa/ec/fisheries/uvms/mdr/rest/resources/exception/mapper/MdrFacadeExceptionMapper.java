package eu.europa.ec.fisheries.uvms.mdr.rest.resources.exception.mapper;

import eu.europa.ec.fisheries.uvms.mdr.model.remote.dto.MdrErrorResponseDto;
import eu.europa.ec.fisheries.uvms.mdr.rest.resources.exception.MdrFacadeException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MdrFacadeExceptionMapper implements ExceptionMapper<MdrFacadeException> {

    @Override
    public Response toResponse(MdrFacadeException exception) {
        MdrErrorResponseDto errorDto = new MdrErrorResponseDto(exception.getMessage());
        return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(errorDto).build();
    }

}
