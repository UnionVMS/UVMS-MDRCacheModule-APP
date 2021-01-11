package eu.europa.ec.fisheries.mdr.mapper.webservice;

import javax.enterprise.context.ApplicationScoped;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.Location;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

@ApplicationScoped
@MDRMapper("LOCATION")
public class LocationMapper extends MasterDataRegistryMapper {

    static final String AL_CODE = "AL_CODE";
    static final String CODE = "CODE";
    static final String CODE2 = "CODE2";
    static final String NAME = "NAME";
    static final String LATITUDE = "LATITUDE";
    static final String LONGITUDE = "LONGITUDE";
    static final String FISHINGPORTIND = "FISHINGPORTIND";
    static final String LANDPLACEIND = "LANDPLACEIND";
    static final String COMMERCIALPORTIND = "COMMERCIALPORTIND";
    static final String LOCODE = "LOCODE";
    static final String COORDINATES = "COORDINATES";
    static final String UNFCTCODE = "UNFCTCODE";
    
    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        Location entity = new Location();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setPlacesCode(getProperty(mdrDataNodeType, CODE));
        entity.setCode2(getProperty(mdrDataNodeType, CODE2));
        entity.setEnName(getProperty(mdrDataNodeType, NAME));
        entity.setLatitude(parseStringToDouble(getProperty(mdrDataNodeType, LATITUDE)));
        entity.setLongitude(parseStringToDouble(getProperty(mdrDataNodeType, LONGITUDE)));
        entity.setFishingPortInd(Boolean.valueOf(getProperty(mdrDataNodeType, FISHINGPORTIND)));
        entity.setLandingPlaceInd(Boolean.valueOf(getProperty(mdrDataNodeType, LANDPLACEIND)));
        entity.setCommercialPortInd(Boolean.valueOf(getProperty(mdrDataNodeType, COMMERCIALPORTIND)));
        entity.setUnloCode(getProperty(mdrDataNodeType, LOCODE));
        entity.setCoordinates(getProperty(mdrDataNodeType, COORDINATES));
        entity.setUnFunctionCode(getProperty(mdrDataNodeType, UNFCTCODE));
        return entity;
    }

    @Override
    void populateCommonProperties(MDRDataNodeType mdrDataNodeType, MasterDataRegistry entity) {
        entity.setCode(getProperty(mdrDataNodeType, AL_CODE));
        entity.setDescription(getProperty(mdrDataNodeType, DESCRIPTION));
        populateDateProperties(mdrDataNodeType.getEffectiveDelimitedPeriod(), entity);
    }
}
