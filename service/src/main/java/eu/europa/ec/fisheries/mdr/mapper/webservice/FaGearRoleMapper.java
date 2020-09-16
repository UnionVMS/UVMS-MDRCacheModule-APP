package eu.europa.ec.fisheries.mdr.mapper.webservice;

import javax.enterprise.context.ApplicationScoped;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FaGearRole;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

@ApplicationScoped
@MDRMapper("FA_GEAR_ROLE")
public class FaGearRoleMapper extends MasterDataRegistryMapper {

    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FaGearRole entity = new FaGearRole();
        populateCommonProperties(mdrDataNodeType, entity);
        return entity;
    }
}
