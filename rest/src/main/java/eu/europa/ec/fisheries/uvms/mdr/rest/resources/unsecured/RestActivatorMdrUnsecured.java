package eu.europa.ec.fisheries.uvms.mdr.rest.resources.unsecured;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("mdrnonsecure")
public class RestActivatorMdrUnsecured extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(MdrRestResource.class);
        return resources;
    }
}
