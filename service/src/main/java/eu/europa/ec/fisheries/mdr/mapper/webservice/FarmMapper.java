package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.Farm;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MDRMapper("FARM")
public class FarmMapper extends MasterDataRegistryMapper {

    static final String F_CODE = "F_CODE";
    static final String F_DESCRIPTION = "F_DESCRIPTION";
    static final String RFMO_CODE = "RFMO_CODE";
    static final String RFMOS_CODE = "RFMOS_CODE";
    static final String RFMOS_CODE2 = "RFMOS_CODE2";
    static final String RFMOS_NAME = "RFMOS_NAME";
    static final String DESCRIPTION = "DESCRIPTION";
    static final String CONTRACTINGPARTY = "CONTRACTINGPARTY";
    static final String CODE = "CODE";
    static final String CODE2 = "CODE2";
    static final String NAME = "NAME";
    
    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        Farm entity = new Farm();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setRfmoEnDescription(getProperty(mdrDataNodeType, DESCRIPTION));
        entity.setContractingParty(getProperty(mdrDataNodeType, CONTRACTINGPARTY));
        entity.setEnName(getProperty(mdrDataNodeType, NAME));
        entity.setPlacesCode(getProperty(mdrDataNodeType, CODE));
        entity.setPlacesCode2(getProperty(mdrDataNodeType, CODE2));
        entity.setRfmoCode(getProperty(mdrDataNodeType, RFMO_CODE));
        return entity;
    }

    @Override
    void populateCommonProperties(MDRDataNodeType mdrDataNodeType, MasterDataRegistry entity) {
        entity.setCode(getProperty(mdrDataNodeType, F_CODE));
        entity.setDescription(getProperty(mdrDataNodeType, F_DESCRIPTION));
        populateDateProperties(mdrDataNodeType.getEffectiveDelimitedPeriod(), entity);
    }
}
