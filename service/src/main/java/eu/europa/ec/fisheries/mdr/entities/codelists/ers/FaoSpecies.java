package eu.europa.ec.fisheries.mdr.entities.codelists.ers;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.exception.FieldNotMappedException;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.SortableField;
import un.unece.uncefact.data.standard.mdr.response.MDRDataNodeType;
import un.unece.uncefact.data.standard.mdr.response.MDRElementDataNodeType;

import javax.persistence.*;

/**
 * Created by kovian on 11/23/2016.
 */
@Entity
@Table(name = "mdr_fao_species")
@Indexed
@Analyzer(impl = StandardAnalyzer.class)
public class FaoSpecies extends MasterDataRegistry {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "FAO_SPECIES_SEQ_GEN", sequenceName = "mdr_fao_species_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAO_SPECIES_SEQ_GEN")
    private long id;

    @Column(name = "is_group")
    @Field(name = "is_group")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    //@SortableField(forField = "is_group")
    private String isGroup;

    @Column(name = "scientific_name")
    @Field(name = "scientific_name")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    //@SortableField(forField = "scientific_name")
    private String scientificName;

    @Column(name = "en_name")
    @Field(name = "en_name")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    //@SortableField(forField = "en_name")
    private String enName;

    @Column(name = "fr_name")
    @Field(name = "fr_name")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    //@SortableField(forField = "fr_name")
    private String frName;

    @Column(name = "es_name")
    @Field(name = "es_name")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    //@SortableField(forField = "es_name")
    private String esName;

    @Column(name = "family")
    @Field(name = "family")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    //@SortableField
    private String family;

    @Column(name = "bio_order")
    @Field(name = "bio_order")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    //@SortableField(forField = "bio_order")
    private String bioOrder;

    @Column(name = "taxo_code")
    @Field(name = "taxo_code")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    //@SortableField(forField = "taxo_code")
    private String taxoCode;


    @Override
    public void populate(MDRDataNodeType mdrDataType) throws FieldNotMappedException {
        populateCommonFields(mdrDataType);
        for (MDRElementDataNodeType field : mdrDataType.getSubordinateMDRElementDataNodes()) {
            String fieldName = field.getName().getValue();
            String fieldValue = field.getValue().getValue();
            if (StringUtils.equalsIgnoreCase(fieldName, "ALL_SPECIES.CODE")) {
                this.setCode(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "ALL_SPECIES.ISGROUP")) {
                this.setIsGroup(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "ALL_SPECIES.SCIENTNAME")) {
                this.setScientificName(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "ALL_SPECIES.ENNAME")) {
                this.setEnName(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "ALL_SPECIES.FRNAME")) {
                this.setFrName(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "ALL_SPECIES.ESNAME")) {
                this.setEsName(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "ALL_SPECIES.FAMILY")) {
                this.setFamily(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "ALL_SPECIES.BIOORDER")) {
                this.setBioOrder(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "ALL_SPECIES.TAXOCODE")) {
                this.setTaxoCode(fieldValue);
            } else {
                logError(fieldName, this.getClass().getSimpleName());
            }
        }
    }

    @Override
    public String getAcronym() {
        return "FAO_SPECIES";
    }

    public String getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getFrName() {
        return frName;
    }

    public void setFrName(String frName) {
        this.frName = frName;
    }

    public String getEsName() {
        return esName;
    }

    public void setEsName(String esName) {
        this.esName = esName;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getBioOrder() {
        return bioOrder;
    }

    public void setBioOrder(String bioOrder) {
        this.bioOrder = bioOrder;
    }

    public String getTaxoCode() {
        return taxoCode;
    }

    public void setTaxoCode(String taxoCode) {
        this.taxoCode = taxoCode;
    }
}
