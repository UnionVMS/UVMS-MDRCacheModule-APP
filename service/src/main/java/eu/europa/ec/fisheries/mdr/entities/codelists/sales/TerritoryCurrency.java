/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.mdr.entities.codelists.sales;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.exception.FieldNotMappedException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import un.unece.uncefact.data.standard.mdr.response.MDRDataNodeType;
import un.unece.uncefact.data.standard.mdr.response.MDRElementDataNodeType;

/**
 * Created by kovian on 01/09/2017.
 */
@Entity
@Table(name = "mdr_territory_curr")
@Indexed
public class TerritoryCurrency extends MasterDataRegistry {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "mdr_territory_curr_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    private long id;

    @Column(name = "iso_2_Code")
    @Field(name = "iso2Code")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    private String iso2Code;

    @Column(name = "en_name")
    @Field(name = "enName")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    private String enName;

    @Override
    public String getAcronym() {
        return "TERRITORY_CURR";
    }

    @Override
    public void populate(MDRDataNodeType mdrDataType) throws FieldNotMappedException {
        populateCommonFields(mdrDataType);
        for (MDRElementDataNodeType field : mdrDataType.getSubordinateMDRElementDataNodes()) {
            String fieldName = field.getName().getValue();
            String fieldValue = field.getName().getValue();
            if (StringUtils.equalsIgnoreCase(fieldName, "THEMATIC_PLACE.CODE2")) {
                this.setIso2Code(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "CURRENCY.CODE")) {
                this.setCode(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "CURRENCY.ENNAME")) {
                this.setEnName(fieldValue);
            } else {
                logError(fieldName, this.getClass().getSimpleName());
            }
        }
    }


    public String getIso2Code() {
        return iso2Code;
    }
    public void setIso2Code(String iso2Code) {
        this.iso2Code = iso2Code;
    }
    public String getEnName() {
        return enName;
    }
    public void setEnName(String enName) {
        this.enName = enName;
    }
}
