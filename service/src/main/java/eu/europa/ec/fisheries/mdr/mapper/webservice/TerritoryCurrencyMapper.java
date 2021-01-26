package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.sales.TerritoryCurrency;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MDRMapper("TERRITORY_CURR")
public class TerritoryCurrencyMapper extends MasterDataRegistryMapper {

    static final String CURRENCYCODE = "CURRENCYCODE";
    static final String CODE = "CODE";
    static final String CODE2 = "CODE2";
    static final String NAME = "NAME";
    static final String CURRENCYNAME = "CURRENCYNAME";
    
    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        TerritoryCurrency entity = new TerritoryCurrency();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setPlacesEnName(getProperty(mdrDataNodeType, NAME));
        entity.setPlacesCode(getProperty(mdrDataNodeType, CODE));
        entity.setIso2Code(getProperty(mdrDataNodeType, CODE2));
        entity.setEnName(getProperty(mdrDataNodeType, CURRENCYNAME));
        return entity;
    }

    @Override
    void populateCommonProperties(MDRDataNodeType mdrDataNodeType, MasterDataRegistry entity) {
        entity.setCode(getProperty(mdrDataNodeType, CURRENCYCODE));
        populateDateProperties(mdrDataNodeType.getEffectiveDelimitedPeriod(), entity);
    }
}
