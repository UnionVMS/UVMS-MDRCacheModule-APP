package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.movement.VPBrDef;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MDRMapper("VP_BR_DEF")
public class VPBrDefMapper extends MasterDataRegistryMapper {


        static final String CONTEXT = "CONTEXT";
        static final String FIELD = "FIELD";
        static final String ENMESSAGE = "ENMESSAGE";
        static final String SEQORDER = "SEQORDER";
        static final String BRSUBLEVEL = "BRSUBLEVEL";
        static final String FGVL_CODE = "FGVL_CODE";
        static final String FGVL_DESCRIPTION = "FGVL_DESCRIPTION";

        @Override
        public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
            VPBrDef entity = new VPBrDef();
            populateCommonProperties(mdrDataNodeType, entity);
            entity.setField(getProperty(mdrDataNodeType, FIELD));
            entity.setMessageIfFailing(getProperty(mdrDataNodeType, ENMESSAGE));
            entity.setSequenceOrder(getProperty(mdrDataNodeType, SEQORDER));
            entity.setBrSublevel(getProperty(mdrDataNodeType, BRSUBLEVEL));
            entity.setFluxGpValidationLevelCode(getProperty(mdrDataNodeType, FGVL_CODE));
            entity.setFluxGpValidationLevelEnDescr(getProperty(mdrDataNodeType, FGVL_DESCRIPTION));
            return entity;
    }
}
