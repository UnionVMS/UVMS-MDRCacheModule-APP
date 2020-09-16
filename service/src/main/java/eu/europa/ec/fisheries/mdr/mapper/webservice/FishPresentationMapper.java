package eu.europa.ec.fisheries.mdr.mapper.webservice;

import javax.enterprise.context.ApplicationScoped;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FishPresentation;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

@ApplicationScoped
@MDRMapper("FISH_PRESENTATION")
public class FishPresentationMapper extends MasterDataRegistryMapper{

    static final String NAME = "NAME";

    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FishPresentation entity = new FishPresentation();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setEnName(getProperty(mdrDataNodeType, NAME));
        return entity;
    }
}
