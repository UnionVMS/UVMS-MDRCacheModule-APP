package eu.europa.ec.fisheries.uvms.mdr.rest.resources.unsecured;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.mdr.rest.resources.BuildMdrRestTestDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import un.unece.uncefact.data.standard.mdr.communication.ColumnDataType;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListRequest;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListResponse;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class MdrRestResourceTest extends BuildMdrRestTestDeployment {

    @Test
    public void getAcronym_locationVigo_expectCorrectLatitudeLongitude() {
        MdrGetCodeListRequest request = new MdrGetCodeListRequest();
        request.setWantedNumberOfResults(BigInteger.ONE);
        request.setColumnsToFilters(Lists.newArrayList("code"));
        request.setFilter("ESVGO");
        request.setAcronym("LOCATION");

        Invocation.Builder builder = getWebTarget()
                .path("json/getAcronym")
                .request(MediaType.APPLICATION_JSON);
        Response response =  builder.post(Entity.json(request), Response.class);

        assertEquals(200 ,response.getStatus());
        MdrGetCodeListResponse mdrGetCodeListResponse = response.readEntity(MdrGetCodeListResponse.class);

        Map<String, List<String>> columnNameValuesMap = new HashMap<>();
        for (ObjectRepresentation objectRepresentation : mdrGetCodeListResponse.getDataSets()) {
            for (ColumnDataType field : objectRepresentation.getFields()) {
                if (!columnNameValuesMap.containsKey(field.getColumnName())) {
                    columnNameValuesMap.put(field.getColumnName(), new ArrayList<>());
                }
                columnNameValuesMap.get(field.getColumnName()).add(field.getColumnValue());
            }
        }

        assertEquals("42.24239116", columnNameValuesMap.get("latitude").get(0));
        assertEquals("-8.71773643", columnNameValuesMap.get("longitude").get(0));
    }
}
