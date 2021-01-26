package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FaBrDef;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MDRMapper("FA_BR_DEF")
public class FaBrDefMapper extends MasterDataRegistryMapper {

    static final String FIELD = "FIELD";
    static final String ENMESSAGE = "ENMESSAGE";
    static final String SEQORDER = "SEQORDER";
    static final String FGVL_CODE = "FGVL_CODE";
    static final String FGVL_DESCRIPTION = "FGVL_DESCRIPTION";
    static final String BRSUBLEVEL = "BRSUBLEVEL";

    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FaBrDef entity = new FaBrDef();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setField(getProperty(mdrDataNodeType, FIELD));
        entity.setMessageIfFailing(getProperty(mdrDataNodeType, ENMESSAGE));
        entity.setSequenceOrder(getProperty(mdrDataNodeType, SEQORDER));
        entity.setFluxGpValidationLevelCode(getProperty(mdrDataNodeType, FGVL_CODE));
        entity.setFluxGpValidationLevelEnDescr(getProperty(mdrDataNodeType, FGVL_DESCRIPTION));
        entity.setBrSublevel(getProperty(mdrDataNodeType, BRSUBLEVEL));
        return entity;
    }
}
