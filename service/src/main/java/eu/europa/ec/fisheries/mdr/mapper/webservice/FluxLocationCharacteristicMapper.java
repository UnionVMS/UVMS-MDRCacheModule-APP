package eu.europa.ec.fisheries.mdr.mapper.webservice;

import javax.enterprise.context.ApplicationScoped;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FluxLocationCharacteristic;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

@ApplicationScoped
@MDRMapper("FLUX_LOCATION_CHARACTERISTIC")
public class FluxLocationCharacteristicMapper extends MasterDataRegistryMapper {
    
    static final String FLC_CODE = "FLC_CODE";
    static final String FLC_ENDESCRIPTION = "FLC_ENDESCRIPTION";
    
    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FluxLocationCharacteristic entity = new FluxLocationCharacteristic();
        populateCommonProperties(mdrDataNodeType, entity);
        return entity;
    }

    @Override
    void populateCommonProperties(MDRDataNodeType mdrDataNodeType, MasterDataRegistry entity) {
        entity.setCode(getProperty(mdrDataNodeType, FLC_CODE));
        entity.setDescription(getProperty(mdrDataNodeType, FLC_ENDESCRIPTION));
        populateDateProperties(mdrDataNodeType.getEffectiveDelimitedPeriod(), entity);
    }
}
