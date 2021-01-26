package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.WeightMeans;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MDRMapper("WEIGHT_MEANS")
public class WeightMeansMapper extends MasterDataRegistryMapper {

    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        WeightMeans entity = new WeightMeans();
        populateCommonProperties(mdrDataNodeType, entity);
        return entity;
    }
}
