package eu.europa.ec.fisheries.mdr.mapper.webservice;

import javax.enterprise.context.ApplicationScoped;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FaGearCharacteristic;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

@ApplicationScoped
@MDRMapper("FA_GEAR_CHARACTERISTIC")
public class FaGearCharacteristicMapper extends MasterDataRegistryMapper {
    
    static final String FGC_CODE = "FGC_CODE";
    static final String FGC_ENDESCRIPTION = "FGC_ENDESCRIPTION";
    static final String DATA_TYPE = "CODE";
    static final String DATA_TYPE_DESC = "DESCRIPTION";
    
    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FaGearCharacteristic entity = new FaGearCharacteristic();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setDataType(getProperty(mdrDataNodeType, DATA_TYPE));
        entity.setDataTypeDesc(getProperty(mdrDataNodeType, DATA_TYPE_DESC));
        return entity;
    }

    @Override
    void populateCommonProperties(MDRDataNodeType mdrDataNodeType, MasterDataRegistry entity) {
        entity.setCode(getProperty(mdrDataNodeType, FGC_CODE));
        entity.setDescription(getProperty(mdrDataNodeType, FGC_ENDESCRIPTION));
        populateDateProperties(mdrDataNodeType.getEffectiveDelimitedPeriod(), entity);
    }
}
