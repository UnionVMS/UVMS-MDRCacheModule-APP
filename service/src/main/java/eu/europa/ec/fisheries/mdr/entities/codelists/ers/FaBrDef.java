/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
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
 * Created by kovian on 31/08/2017.
 */
@Entity
@Table(name = "mdr_fa_br_def")
@Indexed
@Analyzer(impl = StandardAnalyzer.class)
public class FaBrDef extends MasterDataRegistry {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "FA_BR_DEF_SEQ_GEN", sequenceName = "mdr_fa_br_def_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FA_BR_DEF_SEQ_GEN")
    private long id;

    @Column(name = "field")
    @Field(name = "field")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField
    private String field;

    @Column(name = "message_if_failing")
    @Field(name = "message_if_failing")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "message_if_failing")
    private String messageIfFailing;

    @Column(name = "sequence_order")
    @Field(name = "sequence_order")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "sequence_order")
    private String sequenceOrder;

    @Column(name = "br_sublevel")
    @Field(name = "br_sublevel")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "br_sublevel")
    private String brSublevel;

    @Column(name = "flux_gp_validation_type_code")
    @Field(name = "flux_gp_validation_type_code")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "flux_gp_validation_type_code")
    private String fluxGpValidationTypeCode;

    @Column(name = "flux_gp_validation_en_descr")
    @Field(name = "flux_gp_validation_en_descr")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "flux_gp_validation_en_descr")
    private String fluxGpValidationEnDescr;

    @Column(name = "flux_gp_validation_level_code")
    @Field(name = "flux_gp_validation_level_code")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "flux_gp_validation_level_code")
    private String fluxGpValidationLevelCode;

    @Column(name = "flux_gp_validation_level_en_descr")
    @Field(name = "flux_gp_validation_level_en_descr")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "flux_gp_validation_level_en_descr")
    private String fluxGpValidationLevelEnDescr;

    @Override
    public String getAcronym() {
        return "FA_BR_DEF";
    }

    @Override
    public void populate(MDRDataNodeType mdrDataType) throws FieldNotMappedException {
        populateCommonFields(mdrDataType);
        for (MDRElementDataNodeType field : mdrDataType.getSubordinateMDRElementDataNodes()) {
            String fieldName = field.getName().getValue();
            String fieldValue = field.getValue().getValue();
            if (StringUtils.equalsIgnoreCase(fieldName, "FA_BR_DEF.FIELD")) {
                this.setField(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "FA_BR_DEF.ENMESSAGE")) {
                this.setMessageIfFailing(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "FA_BR_DEF.SEQORDER")) {
                this.setSequenceOrder(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "FA_BR_DEF.BRSUBLEVEL")) {
                this.setBrSublevel(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "FLUX_GP_VALIDATION_TYPE.ENDESCRIPTION")) {
                this.setFluxGpValidationEnDescr(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "FLUX_GP_VALIDATION_TYPE.CODE")) {
                this.setFluxGpValidationTypeCode(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "FLUX_GP_VALIDATION_LEVEL.ENDESCRIPTION")) {
                this.setFluxGpValidationLevelEnDescr(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "FLUX_GP_VALIDATION_LEVEL.CODE")) {
                this.setFluxGpValidationLevelCode(fieldValue);
            } else {
                logError(fieldName, this.getClass().getSimpleName());
            }
        }
    }


    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessageIfFailing() {
        return messageIfFailing;
    }

    public void setMessageIfFailing(String messageIfFailing) {
        this.messageIfFailing = messageIfFailing;
    }

    public String getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(String sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public String getBrSublevel() {
        return brSublevel;
    }

    public void setBrSublevel(String brSublevel) {
        this.brSublevel = brSublevel;
    }

    public String getFluxGpValidationTypeCode() {
        return fluxGpValidationTypeCode;
    }

    public void setFluxGpValidationTypeCode(String fluxGpValidationTypeCode) {
        this.fluxGpValidationTypeCode = fluxGpValidationTypeCode;
    }

    public String getFluxGpValidationEnDescr() {
        return fluxGpValidationEnDescr;
    }

    public void setFluxGpValidationEnDescr(String fluxGpValidationEnDescr) {
        this.fluxGpValidationEnDescr = fluxGpValidationEnDescr;
    }

    public String getFluxGpValidationLevelCode() {
        return fluxGpValidationLevelCode;
    }

    public void setFluxGpValidationLevelCode(String fluxGpValidationLevelCode) {
        this.fluxGpValidationLevelCode = fluxGpValidationLevelCode;
    }

    public String getFluxGpValidationLevelEnDescr() {
        return fluxGpValidationLevelEnDescr;
    }

    public void setFluxGpValidationLevelEnDescr(String fluxGpValidationLevelEnDescr) {
        this.fluxGpValidationLevelEnDescr = fluxGpValidationLevelEnDescr;
    }
}
