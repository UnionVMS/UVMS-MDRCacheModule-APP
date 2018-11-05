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
 * Created by kovian on 11/23/2016.
 */
@Entity
@Table(name = "mdr_gear_type")
@Indexed
@Analyzer(impl = StandardAnalyzer.class)
public class GearType extends MasterDataRegistry {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "GEAR_TYPE_SEQ_GEN", sequenceName = "mdr_gear_type_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEAR_TYPE_SEQ_GEN")
    private long id;

    @Column(name = "group_name")
    @Field(name = "group_name")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "group_name")
    private String category;

    @Column(name = "sub_group_name")
    @Field(name = "sub_group_name")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "sub_group_name")
    private String subCategory;

    @Column(name = "iss_cfg_code")
    @Field(name = "iss_cfg_code")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "iss_cfg_code")
    private String issCfgCode;

    @Column(name = "iccat_code")
    @Field(name = "iccat_code")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "iccat_code")
    private String iccatCode;

    @Column(name = "target")
    @Field(name = "target")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField
    private String target;

    @Column(name = "gear_activity_code")
    @Field(name = "gear_activity_code")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "gear_activity_code")
    private String gearActivityCode;

    @Column(name = "gear_activity_description")
    @Field(name = "gear_activity_description")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "gear_activity_description")
    private String gearActivityDescription;

    @Override
    public String getAcronym() {
        return "GEAR_TYPE";
    }


    @Override
    public void populate(MDRDataNodeType mdrDataType) throws FieldNotMappedException {
        populateCommonFields(mdrDataType);
        for (MDRElementDataNodeType field : mdrDataType.getSubordinateMDRElementDataNodes()) {
            String fieldName = field.getName().getValue();
            String fieldValue = field.getValue().getValue();
            if (StringUtils.equalsIgnoreCase(fieldName, "GEAR_TYPE.CATEGORY")) {
                this.setCategory(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "GEAR_TYPE.SUBCATEGORY")) {
                this.setSubCategory(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "GEAR_TYPE.ICCATCODE")) {
                this.setIccatCode(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "GEAR_TYPE.ISSCFGCODE")) {
                this.setIssCfgCode(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "GEAR_TYPE.TARGET")) {
                this.setTarget(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "GEAR_ACTIVITY.CODE")) {
                this.setGearActivityCode(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "GEAR_ACTIVITY.ENDESCRIPTION")) {
                this.setGearActivityDescription(fieldValue);
            } else {
                logError(fieldName, this.getClass().getSimpleName());
            }
        }
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getIssCfgCode() {
        return issCfgCode;
    }

    public void setIssCfgCode(String issCfgCode) {
        this.issCfgCode = issCfgCode;
    }

    public String getIccatCode() {
        return iccatCode;
    }

    public void setIccatCode(String iccatCode) {
        this.iccatCode = iccatCode;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getGearActivityCode() {
        return gearActivityCode;
    }

    public void setGearActivityCode(String gearActivityCode) {
        this.gearActivityCode = gearActivityCode;
    }

    public String getGearActivityDescription() {
        return gearActivityDescription;
    }

    public void setGearActivityDescription(String gearActivityDescription) {
        this.gearActivityDescription = gearActivityDescription;
    }
}