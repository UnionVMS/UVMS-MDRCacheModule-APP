package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FaCharacteristic;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MDRMapper("FA_CHARACTERISTIC")
public class FaCharacteristicMapper extends MasterDataRegistryMapper {

    static final String FC_CODE = "FC_CODE";
    static final String FC_ENDESCRIPTION = "FC_ENDESCRIPTION";
    static final String CODE = "CODE";
    static final String DESCRIPTION = "DESCRIPTION";


    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FaCharacteristic entity = new FaCharacteristic();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setDataType(getProperty(mdrDataNodeType, CODE));
        entity.setDataTypeDesc(getProperty(mdrDataNodeType, DESCRIPTION));
        return entity;
    }

    @Override
    void populateCommonProperties(MDRDataNodeType mdrDataNodeType, MasterDataRegistry entity) {
        entity.setCode(getProperty(mdrDataNodeType, FC_CODE));
        entity.setDescription(getProperty(mdrDataNodeType, FC_ENDESCRIPTION));
        populateDateProperties(mdrDataNodeType.getEffectiveDelimitedPeriod(), entity);
    }
}
