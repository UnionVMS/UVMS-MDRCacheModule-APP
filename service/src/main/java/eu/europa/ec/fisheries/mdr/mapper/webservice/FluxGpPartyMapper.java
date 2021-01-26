package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FluxGpParty;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MDRMapper("FLUX_GP_PARTY")
public class FluxGpPartyMapper extends MasterDataRegistryMapper {
    
    static final String NAME = "NAME";
    static final String CODE2 = "CODE2";
    
    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FluxGpParty entity = new FluxGpParty();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setEnName(getProperty(mdrDataNodeType, NAME));
        entity.setCode2(getProperty(mdrDataNodeType, CODE2));
        return entity;
    }
}
