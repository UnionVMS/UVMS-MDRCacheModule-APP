/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.mdr.domain.codelists.base;

import eu.europa.ec.fisheries.mdr.exception.FieldNotMappedException;
import eu.europa.ec.fisheries.uvms.domain.BaseEntity;
import eu.europa.ec.fisheries.uvms.domain.DateRange;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.commongrams.CommonGramsFilterFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.*;
import un.unece.uncefact.data.standard.mdr.response.DelimitedPeriodType;
import un.unece.uncefact.data.standard.mdr.response.MDRDataNodeType;
import un.unece.uncefact.data.standard.mdr.response.MDRElementDataNodeType;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.List;

import static eu.europa.ec.fisheries.mdr.domain.codelists.base.MasterDataRegistry.LOW_CASE_ANALYSER;

@SuppressWarnings("serial")
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@ToString
@AnalyzerDef(name = LOW_CASE_ANALYSER,
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = StopFilterFactory.class, params = {
                        @Parameter(name = "ignoreCase", value = "true")
                }),
                @TokenFilterDef(factory = CommonGramsFilterFactory.class),
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
        })
public abstract class MasterDataRegistry extends BaseEntity {

    public static final String LOW_CASE_ANALYSER = "lowCaseAnalyser";

    @Embedded
    @IndexedEmbedded
    private DateRange validity;

    @Column(name = "version")
    @Field(name = "version")
    private String version;

    @Column(name = "code")
    @Field
    @Analyzer(definition = LOW_CASE_ANALYSER)
    private String code;

    @Column(name = "description")
    @Field
    @Analyzer(definition = LOW_CASE_ANALYSER)
    private String description;

    protected static final String CODE_STR = "CODE";
    protected static final String DESCRIPTION_STR = "DESCRIPTION";
    protected static final String EN_DESCRIPTION_STR = "ENDESCRIPTION";
    protected static final String VERSION_STR = "VERSION_STR";

    protected void populateCommonFields(MDRDataNodeType mdrDataType) throws FieldNotMappedException {

        // Start date end date
        final DelimitedPeriodType validityPeriod = mdrDataType.getEffectiveDelimitedPeriod();
        if (validityPeriod != null) {
            this.setValidity(new DateRange(validityPeriod.getStartDateTime().getDateTime().toGregorianCalendar().getTime(),
                                           validityPeriod.getEndDateTime().getDateTime().toGregorianCalendar().getTime()));
        }

        // Code, Description, Version
        List<MDRElementDataNodeType> fieldsToRemove = new ArrayList<>();
        final List<MDRElementDataNodeType> subordinateMDRElementDataNodes = mdrDataType.getSubordinateMDRElementDataNodes();
        for (MDRElementDataNodeType field : subordinateMDRElementDataNodes) {
            String fieldName = field.getName().getValue();
            String fieldValue = field.getValue().getValue();
            if (StringUtils.equalsIgnoreCase(CODE_STR, fieldName)) {
                setCode(fieldValue);
                fieldsToRemove.add(field);
            } else if (StringUtils.equalsIgnoreCase(DESCRIPTION_STR, fieldName)
                    || StringUtils.equalsIgnoreCase(EN_DESCRIPTION_STR, fieldName)) {
                setDescription(fieldValue);
                fieldsToRemove.add(field);
            } else if (StringUtils.equalsIgnoreCase(VERSION_STR, fieldName)) {
                setVersion(fieldValue);
                fieldsToRemove.add(field);
            }
        }
        // If we are inside here it means that code and description have to be both set, otherwise we have attributes missing.
        if (StringUtils.isEmpty(getCode()) || StringUtils.isEmpty(getDescription())) {
            throw new FieldNotMappedException(this.getClass().getSimpleName(), "Code or Description missing");
        }
        subordinateMDRElementDataNodes.removeAll(fieldsToRemove);
    }

    public abstract void populate(MDRDataNodeType mdrDataType) throws FieldNotMappedException;

    public abstract String getAcronym();
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public DateRange getValidity() {
        return validity;
    }
    public void setValidity(DateRange validity) {
        this.validity = validity;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}