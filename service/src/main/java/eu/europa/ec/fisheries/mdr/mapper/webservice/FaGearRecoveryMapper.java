package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FaGearRecovery;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MDRMapper("FA_GEAR_RECOVERY")
public class FaGearRecoveryMapper extends MasterDataRegistryMapper {

    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FaGearRecovery entity = new FaGearRecovery();
        populateCommonProperties(mdrDataNodeType, entity);
        return entity;
    }
}
