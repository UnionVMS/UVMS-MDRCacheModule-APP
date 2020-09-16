package eu.europa.ec.fisheries.mdr.mapper.webservice;

import javax.enterprise.context.ApplicationScoped;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.sales.SaleBr;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

@ApplicationScoped
@MDRMapper("SALE_BR")
public class SaleBrMapper extends MasterDataRegistryMapper {

    static final String CONTEXT = "CONTEXT";
    static final String FIELD = "FIELD";
    static final String ENMESSAGE = "ENMESSAGE";
    static final String SEQORDER = "SEQORDER";
    static final String BRSUBLEVEL = "BRSUBLEVEL";
    static final String FGVL_CODE = "FGVL_CODE";
    static final String FGVL_DESCRIPTION = "FGVL_DESCRIPTION";
    static final String FGVT_CODE = "FGVT_CODE";
    static final String FGVT_ENDESCRIPTION = "FGVT_ENDESCRIPTION";
    static final String ACTIVEIND = "ACTIVEIND";

    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        SaleBr entity = new SaleBr();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setContext(getProperty(mdrDataNodeType, CONTEXT));
        entity.setField(getProperty(mdrDataNodeType, FIELD));
        entity.setMessageIfFailing(getProperty(mdrDataNodeType, ENMESSAGE));
        entity.setSequenceOrder(getProperty(mdrDataNodeType, SEQORDER));
        entity.setBrSublevel(getProperty(mdrDataNodeType, BRSUBLEVEL));
        entity.setFluxGpValidationLevelCode(getProperty(mdrDataNodeType, FGVL_CODE));
        entity.setFluxGpValidationLevelEnDescr(getProperty(mdrDataNodeType, FGVL_DESCRIPTION));
        entity.setFluxGpValidationTypeCode(getProperty(mdrDataNodeType, FGVT_CODE));
        entity.setFluxGpValidationEnDescr(getProperty(mdrDataNodeType, FGVT_ENDESCRIPTION));
        entity.setActivationIndicator(getProperty(mdrDataNodeType, ACTIVEIND));
        return entity;
    }
}
