package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FaNeafcStock;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MDRMapper("FA_NEAFC_STOCK")
public class FaNeafcStockMapper extends MasterDataRegistryMapper {

    static final String REFERENCE = "REFERENCE";

    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FaNeafcStock entity = new FaNeafcStock();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setReference(getProperty(mdrDataNodeType, REFERENCE));
        return entity;
    }
}
