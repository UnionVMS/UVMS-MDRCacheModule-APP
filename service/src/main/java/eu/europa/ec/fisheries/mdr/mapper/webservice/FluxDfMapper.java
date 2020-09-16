package eu.europa.ec.fisheries.mdr.mapper.webservice;

import javax.enterprise.context.ApplicationScoped;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FluxDf;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

@ApplicationScoped
@MDRMapper("FLUX_DF")
public class FluxDfMapper extends MasterDataRegistryMapper {

    static final String CONTEXT = "CONTEXT";
    static final String IDVERSION = "IDVERSION";
    static final String DATAFLOW = "DATAFLOW";
    
    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FluxDf entity = new FluxDf();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setContext(getProperty(mdrDataNodeType, CONTEXT));
        entity.setIdVersion(getProperty(mdrDataNodeType, IDVERSION));
        entity.setDataFlow(getProperty(mdrDataNodeType, DATAFLOW));
        return entity;        
    }
}
