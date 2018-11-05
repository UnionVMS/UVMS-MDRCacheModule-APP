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
@Table(name = "mdr_effort_zone")
@Indexed
public class EffortZone extends MasterDataRegistry {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "EFFORT_ZONE_SEQ_GEN", sequenceName = "mdr_effort_zone_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EFFORT_ZONE_SEQ_GEN")
    private long id;

    @Column(name = "legal_reference")
    @Field(name="legal_reference")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    @SortableField(forField = "legal_reference")
    private String legalReference;

    @Override
    public String getAcronym() {
        return "EFFORT_ZONE";
    }

    @Override
    public void populate(MDRDataNodeType mdrDataType) throws FieldNotMappedException {
        populateCommonFields(mdrDataType);
        for(MDRElementDataNodeType field : mdrDataType.getSubordinateMDRElementDataNodes()){
            String fieldName  = field.getName().getValue();
            String fieldValue  = field.getValue().getValue();
            if(StringUtils.equalsIgnoreCase(fieldName, "EFFORT_ZONE.LEGALREF")){
                this.setLegalReference(fieldValue);
            }  else {
                logError(fieldName, this.getClass().getSimpleName());
            }
        }
    }

    public String getLegalReference() {
        return legalReference;
    }
    public void setLegalReference(String legalReference) {
        this.legalReference = legalReference;
    }

}