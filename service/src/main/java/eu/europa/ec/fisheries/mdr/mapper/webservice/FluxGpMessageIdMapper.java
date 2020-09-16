package eu.europa.ec.fisheries.mdr.mapper.webservice;

import javax.enterprise.context.ApplicationScoped;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.sales.FluxGpMessageId;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

@ApplicationScoped
@MDRMapper("FLUX_GP_MSG_ID")
public class FluxGpMessageIdMapper extends MasterDataRegistryMapper {

    static final String FORMATEXPRESSION = "FORMATEXPRESSION";
    static final String FORMATDESC = "FORMATDESC";
    
    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FluxGpMessageId entity = new FluxGpMessageId();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setFormatExpression(getProperty(mdrDataNodeType, FORMATEXPRESSION));
        entity.setFormatExpressionDesc(getProperty(mdrDataNodeType, FORMATDESC));
        return entity;
    }
}
