package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.GearType;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MDRMapper("GEAR_TYPE")
public class GearTypeMapper extends MasterDataRegistryMapper {
    
    static final String DESCRIPTION = "GEAR_DESCR";
    static final String CATEGORY = "CATEGORY";
    static final String SUBCATEGORY = "SUBCATEGORY";
    static final String GA_CODE = "GA_CODE";
    static final String GA_ENDESCRIPTION = "GA_ENDESCRIPTION";
    static final String TARGET = "TARGET";
    static final String ISSCFGCODE = "ISSCFGCODE";
    static final String ICCATCODE = "ICCATCODE";
    
    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        GearType entity = new GearType();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setCategory(getProperty(mdrDataNodeType, CATEGORY));
        entity.setSubCategory(getProperty(mdrDataNodeType, SUBCATEGORY));
        entity.setGearActivityCode(getProperty(mdrDataNodeType, GA_CODE));
        entity.setGearActivityDescription(getProperty(mdrDataNodeType, GA_ENDESCRIPTION));
        entity.setTarget(getProperty(mdrDataNodeType, TARGET));
        entity.setIssCfgCode(getProperty(mdrDataNodeType, ISSCFGCODE));
        entity.setIccatCode(getProperty(mdrDataNodeType, ICCATCODE));
        return entity;
    }

    @Override
    void populateCommonProperties(MDRDataNodeType mdrDataNodeType, MasterDataRegistry entity) {
        entity.setCode(getProperty(mdrDataNodeType, CODE));
        entity.setDescription(getProperty(mdrDataNodeType, DESCRIPTION));
        populateDateProperties(mdrDataNodeType.getEffectiveDelimitedPeriod(), entity);
    }
}
