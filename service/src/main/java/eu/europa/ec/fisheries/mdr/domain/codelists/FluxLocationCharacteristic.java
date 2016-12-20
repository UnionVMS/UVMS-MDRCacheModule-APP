/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.mdr.domain.codelists;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Indexed;
import eu.europa.ec.fisheries.mdr.domain.codelists.base.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.exception.FieldNotMappedException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import un.unece.uncefact.data.standard.mdr.response.MDRDataNodeType;

import javax.persistence.Entity;
import javax.persistence.Table;;

/**
 * Created by georgige on 11/23/2016.
 */
@Entity
@Table(name = "mdr_flux_location_characteristic")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Indexed
@Analyzer(impl = StandardAnalyzer.class)
public class FluxLocationCharacteristic extends MasterDataRegistry {

    @Override
    public void populate(MDRDataNodeType mdrDataType) throws FieldNotMappedException {
        super.populateCommonFields(mdrDataType);
    }

    @Override
    public String getAcronym() {
        return "FLUX_LOCATION_CHARACTERISTIC";
    }
}
