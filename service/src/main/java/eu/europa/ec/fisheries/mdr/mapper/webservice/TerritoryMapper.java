package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.Territory;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MDRMapper("TERRITORY")
public class TerritoryMapper extends MasterDataRegistryMapper {

    static final String CODE2 = "CODE2";
    static final String NAME = "NAME";
    static final String LT_CODE = "LT_CODE";
    static final String LANDLOCKIND = "LANDLOCKIND";
    
    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        Territory entity = new Territory();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setCode2(getProperty(mdrDataNodeType, CODE2));
        entity.setEnName(getProperty(mdrDataNodeType, NAME));
        entity.setLandTypeCode(getProperty(mdrDataNodeType, LT_CODE));
        entity.setLandLockInd(getProperty(mdrDataNodeType, LANDLOCKIND));
        return entity;
    }
}
