package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FluxGpValidationType;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MDRMapper("FLUX_GP_VALIDATION_TYPE")
public class FluxGpValidationTypeMapper extends MasterDataRegistryMapper {

    static String FGVT_CODE = "FGVT_CODE";
    static String FGVT_ENDESCRIPTION = "FGVT_ENDESCRIPTION";

    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FluxGpValidationType entity = new FluxGpValidationType();
        populateCommonProperties(mdrDataNodeType, entity);
        return entity;
    }

    @Override
    void populateCommonProperties(MDRDataNodeType mdrDataNodeType, MasterDataRegistry entity) {
        entity.setCode(getProperty(mdrDataNodeType, FGVT_CODE));
        entity.setDescription(getProperty(mdrDataNodeType, FGVT_ENDESCRIPTION));
        populateDateProperties(mdrDataNodeType.getEffectiveDelimitedPeriod(), entity);
    }
}
