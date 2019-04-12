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
import un.unece.uncefact.data.standard.mdr.response.MDRDataNodeType;
import un.unece.uncefact.data.standard.mdr.response.MDRElementDataNodeType;

import javax.persistence.*;

/**
 * Created by kovian on 13/04/2017.
 */
@Entity
@Table(name = "mdr_fa_trip_id_type")
@Indexed
@Analyzer(impl = StandardAnalyzer.class)
public class FaTripIdType extends MasterDataRegistry {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "FA_TRIP_ID_TYPE_SEQ_GEN", sequenceName = "mdr_fa_trip_id_type_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FA_TRIP_ID_TYPE_SEQ_GEN")
    private long id;

    @Column(name = "format_expression")
    @Field(name = "format_expression")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    //@SortableField(forField = "format_expression")
    private String formatExpression;

    @Column(name = "format_expression_desc")
    @Field(name = "format_expression_desc")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    //@SortableField(forField = "format_expression_desc")
    private String formatExpressionDesc;

    @Override
    public void populate(MDRDataNodeType mdrDataType) throws FieldNotMappedException {
        populateCommonFields(mdrDataType);
        for (MDRElementDataNodeType field : mdrDataType.getSubordinateMDRElementDataNodes()) {
            String fieldName = field.getName().getValue();
            String fieldValue = field.getValue().getValue();
            if (StringUtils.equalsIgnoreCase(fieldName, "FA_TRIP_ID_TYPE.FORMATDESC")) {
                this.setFormatExpressionDesc(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "FA_TRIP_ID_TYPE.FORMATEXPRESSION")) {
                this.setFormatExpression(fieldValue);
            } else {
                logError(fieldName, this.getClass().getSimpleName());
            }
        }
    }

    public String getFormatExpression() {
        return formatExpression;
    }

    public void setFormatExpression(String formatExpression) {
        this.formatExpression = formatExpression;
    }

    public String getFormatExpressionDesc() {
        return formatExpressionDesc;
    }

    public void setFormatExpressionDesc(String formatExpressionDesc) {
        this.formatExpressionDesc = formatExpressionDesc;
    }

    @Override
    public String getAcronym() {
        return "FA_TRIP_ID_TYPE";
    }

}
