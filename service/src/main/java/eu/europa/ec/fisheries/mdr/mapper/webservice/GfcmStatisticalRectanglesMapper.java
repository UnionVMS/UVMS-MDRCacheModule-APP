package eu.europa.ec.fisheries.mdr.mapper.webservice;

import javax.enterprise.context.ApplicationScoped;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.RectangleCoordinates;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.GfcmStatisticalRectangles;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.IcesStatisticalRectangles;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

@ApplicationScoped
@MDRMapper("GFCM_STAT_RECTANGLE")
public class GfcmStatisticalRectanglesMapper extends MasterDataRegistryMapper {

    static final String NORTH = "NORTH";
    static final String SOUTH = "SOUTH";
    static final String EAST = "EAST";
    static final String WEST = "WEST";
    
    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        GfcmStatisticalRectangles entity = new GfcmStatisticalRectangles();
        populateCommonProperties(mdrDataNodeType, entity);
        RectangleCoordinates rectangleCoordinates = new RectangleCoordinates();
        rectangleCoordinates.setNorth(getProperty(mdrDataNodeType, NORTH));
        rectangleCoordinates.setSouth(getProperty(mdrDataNodeType, SOUTH));
        rectangleCoordinates.setEast(getProperty(mdrDataNodeType, EAST));
        rectangleCoordinates.setWest(getProperty(mdrDataNodeType, WEST));
        entity.setRectangle(rectangleCoordinates);
        return entity;
    }
}
