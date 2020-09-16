package eu.europa.ec.fisheries.mdr.mapper.webservice;

import javax.enterprise.context.ApplicationScoped;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FluxVesselIdType;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

@ApplicationScoped
@MDRMapper("FLUX_VESSEL_ID_TYPE")
public class FluxVesselIdTypeMapper extends MasterDataRegistryMapper {

    static final String FORMATDESC = "FORMATDESC";  
    static final String FORMATEXPRESSION = "FORMATEXPRESSION";  
    
    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FluxVesselIdType entity = new FluxVesselIdType();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setFormatExpression(getProperty(mdrDataNodeType, FORMATEXPRESSION));
        entity.setFormatExpressionDesc(getProperty(mdrDataNodeType, FORMATDESC));
        return entity;
    }
}
