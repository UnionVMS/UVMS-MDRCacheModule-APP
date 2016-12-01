package eu.europa.ec.fisheries.mdr.domain.codelists;

import eu.europa.ec.fisheries.mdr.domain.codelists.base.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.exception.FieldNotMappedException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import un.unece.uncefact.data.standard.response.MDRDataNodeType;
import un.unece.uncefact.data.standard.response.MDRElementDataNodeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by kovian on 11/23/2016.
 */
@Entity
@Table(name = "mdr_fao_species")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Indexed
public class FaoSpecies extends MasterDataRegistry {

    @Column(name = "is_group")
    @Field(name="is_group", analyze= Analyze.NO, store = Store.YES)
    private String isGroup;

    @Column(name = "scientific_name")
    @Field(name="scientific_name", analyze= Analyze.NO, store = Store.YES)
    private String scientificName;

    @Column(name = "en_name")
    @Field(name="en_name", analyze= Analyze.NO, store = Store.YES)
    private String enName;

    @Column(name = "fr_name")
    @Field(name="fr_name", analyze= Analyze.NO, store = Store.YES)
    private String frName;

    @Column(name = "es_name")
    @Field(name="es_name", analyze= Analyze.NO, store = Store.YES)
    private String esName;

    @Column(name = "family")
    @Field(name="family", analyze= Analyze.NO, store = Store.YES)
    private String family;

    @Column(name = "bio_order")
    @Field(name="bio_order", analyze= Analyze.NO, store = Store.YES)
    private String bioOrder;

    @Column(name = "taxo_code")
    @Field(name="taxo_code", analyze= Analyze.NO, store = Store.YES)
    private String taxoCode;

    @Override
    public void populate(MDRDataNodeType mdrDataType) throws FieldNotMappedException {
        populateCommonFields(mdrDataType);
        for(MDRElementDataNodeType field : mdrDataType.getSubordinateMDRElementDataNodes()){
            String fieldName  = field.getName().getValue();
            String fieldValue  = field.getName().getValue();
            if(StringUtils.equalsIgnoreCase("ISGROUP", fieldName)){
                this.setIsGroup(fieldValue);
            } else if(StringUtils.equalsIgnoreCase("SCIENTNAME", fieldName)){
                this.setScientificName(fieldValue);
            } else if(StringUtils.equalsIgnoreCase("ENNAME", fieldName)){
                this.setEnName(fieldValue);
            } else if(StringUtils.equalsIgnoreCase("FRNAME", fieldName)){
                this.setFrName(fieldValue);
            } else if(StringUtils.equalsIgnoreCase("ESNAME", fieldName)){
                this.setEsName(fieldValue);
            } else if(StringUtils.equalsIgnoreCase("FAMILY", fieldName)){
                this.setFamily(fieldValue);
            } else if(StringUtils.equalsIgnoreCase("BIOORDER", fieldName)){
                this.setBioOrder(fieldValue);
            } else if(StringUtils.equalsIgnoreCase("TAXOCODE", fieldName)){
                this.setEnName(fieldValue);
            } else {
                throw new FieldNotMappedException(this.getClass().getSimpleName(), fieldName);
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
