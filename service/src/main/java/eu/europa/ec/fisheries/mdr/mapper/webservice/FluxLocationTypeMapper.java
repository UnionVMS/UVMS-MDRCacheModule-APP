package eu.europa.ec.fisheries.mdr.mapper.webservice;

import javax.enterprise.context.ApplicationScoped;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FluxLocationType;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

@ApplicationScoped
@MDRMapper("FLUX_LOCATION_TYPE")
public class FluxLocationTypeMapper extends MasterDataRegistryMapper {

    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FluxLocationType entity = new FluxLocationType();
        populateCommonProperties(mdrDataNodeType, entity);
        return entity;
    }
}
