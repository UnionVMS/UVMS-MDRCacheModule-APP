package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.Rfmo;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MDRMapper("RFMO")
public class RfmoMapper extends MasterDataRegistryMapper {

    static final String RFMO_CODE = "RFMO_CODE";
    static final String PLACES_CODE = "CODE";
    static final String CODE2 = "CODE2";
    static final String NAME = "NAME";
    
    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        Rfmo entity = new Rfmo();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setPlacesCode(getProperty(mdrDataNodeType, PLACES_CODE));
        entity.setCode2(getProperty(mdrDataNodeType, CODE2));
        entity.setEnName(getProperty(mdrDataNodeType, NAME));
        return entity;
    }

    @Override
    void populateCommonProperties(MDRDataNodeType mdrDataNodeType, MasterDataRegistry entity) {
        entity.setCode(getProperty(mdrDataNodeType, RFMO_CODE));
        entity.setDescription(getProperty(mdrDataNodeType, DESCRIPTION));
        populateDateProperties(mdrDataNodeType.getEffectiveDelimitedPeriod(), entity);
    }
}
