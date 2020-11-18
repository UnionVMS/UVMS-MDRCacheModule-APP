package eu.europa.ec.fisheries.uvms.mdr.rest.resources.config;

import eu.europa.ec.fisheries.uvms.commons.rest.filter.EncodingResponseFilter;
import eu.europa.ec.fisheries.uvms.mdr.rest.resources.MdrFacadeResource;
import eu.europa.ec.fisheries.uvms.mdr.rest.resources.exception.mapper.MdrFacadeExceptionMapper;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/internal")
public class MdrActivatorInternal extends Application {

    private final Set<Object> singletons = new HashSet<>();
    private final Set<Class<?>> set = new HashSet<>();

    public MdrActivatorInternal() {
        set.add(MdrFacadeResource.class);
        set.add(MdrFacadeExceptionMapper.class);
        set.add(EncodingResponseFilter.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return set;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

}