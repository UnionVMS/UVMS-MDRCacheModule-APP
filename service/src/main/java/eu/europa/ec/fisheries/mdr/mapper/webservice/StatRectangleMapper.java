package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.RectangleCoordinates;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.StatRectangle;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MDRMapper("STAT_RECTANGLE")
public class StatRectangleMapper extends MasterDataRegistryMapper {

    static final String SOURCE = "SOURCE";
    static final String NORTH = "NORTH";
    static final String SOUTH = "SOUTH";
    static final String EAST = "EAST";
    static final String WEST = "WEST";

    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        StatRectangle entity = new StatRectangle();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setSource(getProperty(mdrDataNodeType, SOURCE));
        RectangleCoordinates rectangleCoordinates = new RectangleCoordinates();
        rectangleCoordinates.setNorth(getProperty(mdrDataNodeType, NORTH));
        rectangleCoordinates.setSouth(getProperty(mdrDataNodeType, SOUTH));
        rectangleCoordinates.setEast(getProperty(mdrDataNodeType, EAST));
        rectangleCoordinates.setWest(getProperty(mdrDataNodeType, WEST));
        entity.setRectangle(rectangleCoordinates);
        return entity;
    }
}
