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
import org.apache.commons.lang3.StringUtils;
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
@SuppressWarnings("serial")
@Entity
@Table(name = "mdr_fao_area_codes")
@Indexed
@Analyzer(impl = StandardAnalyzer.class)
public class FaoAreaCodes extends MasterDataRegistry {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "FAO_AREA_CODES_SEQ_GEN", sequenceName = "mdr_fao_area_codes_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAO_AREA_CODES_SEQ_GEN")
    private long id;

    @Column(name = "level")
    @Field(name = "level")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField
    private String level;

    @Column(name = "en_level_name")
    @Field(name = "en_level_name")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "en_level_name")
    private String enLevelName;

    @Column(name = "terminal_ind")
    @Field(name = "terminal_ind")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "terminal_ind")
    private String terminalInd;

    @Override
    public String getAcronym() {
        return "FAO_AREA";
    }


    @Override
    public void populate(MDRDataNodeType mdrDataType) throws FieldNotMappedException {
        populateCommonFields(mdrDataType);
        for (MDRElementDataNodeType field : mdrDataType.getSubordinateMDRElementDataNodes()) {
            String fieldName = field.getName().getValue();
            String fieldValue = field.getValue().getValue();
            if (StringUtils.equalsIgnoreCase(fieldName, "FAO_AREA.LEVEL")) {
                this.setLevel(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "FAO_AREA.ENLEVELNAME")) {
                this.setEnLevelName(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "FAO_AREA.TERMINALIND")) {
                this.setTerminalInd(fieldValue);
            } else {
                logError(fieldName, this.getClass().getSimpleName());
            }
        }
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getEnLevelName() {
        return enLevelName;
    }

    public void setEnLevelName(String enLevelName) {
        this.enLevelName = enLevelName;
    }

    public String getTerminalInd() {
        return terminalInd;
    }

    public void setTerminalInd(String terminalInd) {
        this.terminalInd = terminalInd;
    }
}