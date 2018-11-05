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
 * Created by kovian on 22/11/2016.
 */
@Entity
@Table(name = "mdr_territory")
@Indexed
@Analyzer(impl = StandardAnalyzer.class)
public class Territory extends MasterDataRegistry {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "TERRITORY_SEQ_GEN", sequenceName = "mdr_territory_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TERRITORY_SEQ_GEN")
    private long id;

    @Column(name = "code_2")
    @Field(name = "code_2")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "code_2")
    private String code2;

    @Column(name = "land_type_code")
    @Field(name = "land_type_code")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "land_type_code")
    private String landTypeCode;

    @Column(name = "en_name")
    @Field(name = "en_name")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "en_name")
    private String enName;

    @Column(name = "land_lock_ind")
    @Field(name = "land_lock_ind")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "land_lock_ind")
    private String landLockInd;

    @Override
    public void populate(MDRDataNodeType mdrDataType) throws FieldNotMappedException {
        populateCommonFields(mdrDataType);
        for (MDRElementDataNodeType field : mdrDataType.getSubordinateMDRElementDataNodes()) {
            String fieldName = field.getName().getValue();
            String fieldValue = field.getValue().getValue();
            if (StringUtils.equalsIgnoreCase(fieldName, "PLACES.CODE")) {
                this.setCode(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "PLACES.CODE2")) {
                this.setCode2(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "CR_LAND_TYPE.CODE")) {
                this.setLandTypeCode(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "PLACES.ENNAME")) {
                this.setEnName(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "TERRITORY.LANDLOCKIND")) {
                this.setLandLockInd(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "CR_LAND_TYPE.ENDESCRIPTION")) {
                this.setDescription(fieldValue);
            } else {
                logError(fieldName, this.getClass().getSimpleName());
            }
        }
    }

    @Override
    public String getAcronym() {
        return "TERRITORY";
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getLandLockInd() {
        return landLockInd;
    }

    public void setLandLockInd(String landLockInd) {
        this.landLockInd = landLockInd;
    }

    public String getLandTypeCode() {
        return landTypeCode;
    }

    public void setLandTypeCode(String landTypeCode) {
        this.landTypeCode = landTypeCode;
    }
}
