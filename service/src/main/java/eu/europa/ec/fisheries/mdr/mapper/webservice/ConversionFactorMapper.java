package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.ConversionFactor;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@MDRMapper("CONVERSION_FACTOR")
public class ConversionFactorMapper extends MasterDataRegistryMapper {

    public static final String ISGROUP = "ISGROUP";
    public static final String SCIENTNAME = "SCIENTNAME";
    public static final String FRNAME = "FRNAME";
    public static final String ESNAME = "ESNAME";
    public static final String FAMILY = "FAMILY";
    public static final String BIOORDER = "BIOORDER";
    public static final String TAXOCODE = "TAXOCODE";
    public static final String STATE = "STATE";
    public static final String PRESENTATION = "PRESENTATION";
    public static final String FACTOR = "FACTOR";
    public static final String COMMENTS = "COMMENTS";
    public static final String NAME = "NAME";
    public static final String CODE_2 = "CODE2";
    public static final String COLLECTIVE = "COLLECTIVE";
    public static final String LEGALSOURCE = "LEGALSOURCE";

    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        ConversionFactor entity = new ConversionFactor();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setIsGroup(getProperty(mdrDataNodeType, ISGROUP));
        entity.setScientName(getProperty(mdrDataNodeType, SCIENTNAME));
        entity.setEnName(getProperty(mdrDataNodeType, NAME));
        entity.setFrName(getProperty(mdrDataNodeType, FRNAME));
        entity.setEsName(getProperty(mdrDataNodeType, ESNAME));
        entity.setFamily(getProperty(mdrDataNodeType, FAMILY));
        entity.setBioorder(getProperty(mdrDataNodeType, BIOORDER));
        entity.setTaxocode(getProperty(mdrDataNodeType, TAXOCODE));
        entity.setState(getProperty(mdrDataNodeType, STATE));
        entity.setPresentation(getProperty(mdrDataNodeType, PRESENTATION));
        entity.setFactor(getProperty(mdrDataNodeType, FACTOR));
        entity.setDescription(getProperty(mdrDataNodeType, COMMENTS));
        entity.setComment(getProperty(mdrDataNodeType, COMMENTS));
        entity.setPlacesCode2(getProperty(mdrDataNodeType, CODE_2));
        entity.setCollective(getProperty(mdrDataNodeType, COLLECTIVE));
        entity.setLegalSource(getProperty(mdrDataNodeType, LEGALSOURCE));
        return entity;
    }
}
