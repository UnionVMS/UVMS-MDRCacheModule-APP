package eu.europa.ec.fisheries.mdr.mapper.webservice;

import javax.enterprise.context.ApplicationScoped;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FaTripIdType;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

@ApplicationScoped
@MDRMapper("FA_TRIP_ID_TYPE")
public class FaTripIdTypeMapper extends MasterDataRegistryMapper {

    static final String FORMATDESC = "FORMATDESC";
    static final String FORMATEXPRESSION = "FORMATEXPRESSION";

    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FaTripIdType entity = new FaTripIdType();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setFormatExpressionDesc(getProperty(mdrDataNodeType, FORMATDESC));
        entity.setFormatExpression(getProperty(mdrDataNodeType, FORMATEXPRESSION));
        return entity;
    }
}
