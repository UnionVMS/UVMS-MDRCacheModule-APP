package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FaoSpecies;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MDRMapper("FAO_SPECIES")
public class FaoSpeciesMapper extends MasterDataRegistryMapper {

    static final String ISGROUP = "ISGROUP";
    static final String SCIENTNAME = "SCIENTNAME";
    static final String NAME = "NAME";
    static final String FRNAME = "FRNAME";
    static final String ESNAME = "ESNAME";
    static final String FAMILY = "FAMILY";
    static final String BIOORDER = "BIOORDER";
    static final String TAXOCODE = "TAXOCODE";

    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FaoSpecies entity = new FaoSpecies();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setIsGroup(getProperty(mdrDataNodeType, ISGROUP));
        entity.setScientificName(getProperty(mdrDataNodeType, SCIENTNAME));
        entity.setEnName(getProperty(mdrDataNodeType, NAME));
        entity.setFrName(getProperty(mdrDataNodeType, FRNAME));
        entity.setEsName(getProperty(mdrDataNodeType, ESNAME));
        entity.setFamily(getProperty(mdrDataNodeType, FAMILY));
        entity.setBioOrder(getProperty(mdrDataNodeType, BIOORDER));
        entity.setTaxoCode(getProperty(mdrDataNodeType, TAXOCODE));
        return entity;
    }
}
