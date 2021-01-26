package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.EffortZone;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MDRMapper("EFFORT_ZONE")
public class EffortZoneMapper extends MasterDataRegistryMapper {

    static final String LEGALREF = "LEGALREF";

    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        EffortZone entity = new EffortZone();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setLegalReference(getProperty(mdrDataNodeType, LEGALREF));
        return entity;
    }
}
