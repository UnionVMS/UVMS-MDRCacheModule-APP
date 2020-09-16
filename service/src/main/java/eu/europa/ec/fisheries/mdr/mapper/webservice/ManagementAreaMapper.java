package eu.europa.ec.fisheries.mdr.mapper.webservice;

import javax.enterprise.context.ApplicationScoped;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.ManagementArea;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

@ApplicationScoped
@MDRMapper("MANAGEMENT_AREA")
public class ManagementAreaMapper extends MasterDataRegistryMapper {

    static final String MA_CODE = "MA_CODE";
    static final String MA_DESCRIPTION = "MA_DESCRIPTION";
    static final String PLACES_CODE = "CODE";
    static final String PLACES_CODE2 = "PLACES_CODE2";
    
    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        ManagementArea entity = new ManagementArea();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setPlacesCode(getProperty(mdrDataNodeType, PLACES_CODE));
        entity.setPlacesCode2(getProperty(mdrDataNodeType, PLACES_CODE2));
        return entity;
    }

    @Override
    void populateCommonProperties(MDRDataNodeType mdrDataNodeType, MasterDataRegistry entity) {
        entity.setCode(getProperty(mdrDataNodeType, MA_CODE));
        entity.setDescription(getProperty(mdrDataNodeType, MA_DESCRIPTION));
        populateDateProperties(mdrDataNodeType.getEffectiveDelimitedPeriod(), entity);
    }
}
