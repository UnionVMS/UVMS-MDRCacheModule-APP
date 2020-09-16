package eu.europa.ec.fisheries.mdr.mapper.webservice;

import javax.enterprise.context.ApplicationScoped;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FluxGpValidationLevel;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

@ApplicationScoped
@MDRMapper("FLUX_GP_VALIDATION_LEVEL")
public class FluxGpValidationLevelMapper extends MasterDataRegistryMapper {

    static String FGVL_CODE = "FGVL_CODE";
    static String FGVL_DESCRIPTION = "FGVL_DESCRIPTION";

    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FluxGpValidationLevel entity = new FluxGpValidationLevel();
        populateCommonProperties(mdrDataNodeType, entity);
        return entity;
    }

    @Override
    void populateCommonProperties(MDRDataNodeType mdrDataNodeType, MasterDataRegistry entity) {
        entity.setCode(getProperty(mdrDataNodeType, FGVL_CODE));
        entity.setDescription(getProperty(mdrDataNodeType, FGVL_DESCRIPTION));
        populateDateProperties(mdrDataNodeType.getEffectiveDelimitedPeriod(), entity);
    }
}
