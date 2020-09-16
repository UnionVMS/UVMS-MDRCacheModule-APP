package eu.europa.ec.fisheries.mdr.mapper.webservice;

import javax.enterprise.context.ApplicationScoped;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FaBftSizeCategory;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

@ApplicationScoped
@MDRMapper("FA_BFT_SIZE_CATEGORY")
public class FaBftSizeCategoryMapper extends MasterDataRegistryMapper {

    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FaBftSizeCategory entity = new FaBftSizeCategory();
        populateCommonProperties(mdrDataNodeType, entity);
        return entity;
    }
}
