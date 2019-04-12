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

@Entity
@Table(name = "mdr_management_area")
@Indexed
@Analyzer(impl = StandardAnalyzer.class)
public class ManagementArea extends MasterDataRegistry {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "MANAGEMENT_AREA_SEQ_GEN", sequenceName = "mdr_management_area_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MANAGEMENT_AREA_SEQ_GEN")
    private long id;

    @Column(name = "places_code")
    @Field(name = "places_code")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    //@SortableField(forField = "places_code")
    private String placesCode;

    @Column(name = "places_code2")
    @Field(name = "places_code2")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    //@SortableField(forField = "places_code2")
    private String placesCode2;

    @Override
    public String getAcronym() {
        return "MANAGEMENT_AREA";
    }

    @Override
    public void populate(MDRDataNodeType mdrDataType) throws FieldNotMappedException {
        populateCommonFields(mdrDataType);
        for (MDRElementDataNodeType field : mdrDataType.getSubordinateMDRElementDataNodes()) {
            String fieldName = field.getName().getValue();
            String fieldValue = field.getValue().getValue();
            if (StringUtils.equalsIgnoreCase(fieldName, "PLACES.CODE")) {
                this.setPlacesCode(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "PLACES.CODE2")) {
                this.setPlacesCode2(fieldValue);
            } else {
                logError(fieldName, this.getClass().getSimpleName());
            }
        }
    }

    public String getPlacesCode() {
        return placesCode;
    }

    public void setPlacesCode(String placesCode) {
        this.placesCode = placesCode;
    }

    public String getPlacesCode2() {
        return placesCode2;
    }

    public void setPlacesCode2(String placesCode2) {
        this.placesCode2 = placesCode2;
    }
}