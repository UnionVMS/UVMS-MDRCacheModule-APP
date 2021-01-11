package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.mare.fisheries.services.mdr.v1.DelimitedPeriodType;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRElementDataNodeType;

public abstract class MasterDataRegistryMapper { 
   
    static final String CODE = "CODE";
    static final String DESCRIPTION = "DESCRIPTION";

    public abstract MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType);
    
    void populateCommonProperties(MDRDataNodeType mdrDataNodeType, MasterDataRegistry entity) {
        entity.setCode(getProperty(mdrDataNodeType, CODE));
        entity.setDescription(getProperty(mdrDataNodeType, DESCRIPTION));
        populateDateProperties(mdrDataNodeType.getEffectiveDelimitedPeriod(), entity);
    }
    
    String getProperty(MDRDataNodeType mdrDataNodeType, String name) {
        return mdrDataNodeType.getSubordinateMDRElementDataNode().stream().filter(node -> name.equals(node.getName())).findFirst().map(MDRElementDataNodeType::getValue).orElse(null);
    }
    
    void populateDateProperties(DelimitedPeriodType delimitedPeriodType, MasterDataRegistry entity) {
        if(delimitedPeriodType != null) {
            if(delimitedPeriodType.getStartDateTime() != null) {
                entity.setStartDate(delimitedPeriodType.getStartDateTime().getDateTime().toGregorianCalendar().getTime());
            }
            if(delimitedPeriodType.getEndDateTime() != null) {
                entity.setEndDate(delimitedPeriodType.getEndDateTime().getDateTime().toGregorianCalendar().getTime());
            }
        }
    }

    Double parseStringToDouble(String value) {
        return value == null || value.isEmpty() ? null : Double.parseDouble(value);
    }
}
