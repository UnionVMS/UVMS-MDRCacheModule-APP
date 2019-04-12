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
@Table(name = "mdr_flux_df")
@Indexed
@Analyzer(impl = StandardAnalyzer.class)
public class FluxDf extends MasterDataRegistry {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "FLUX_DF_SEQ_GEN", sequenceName = "mdr_flux_df_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FLUX_DF_SEQ_GEN")
    private long id;

    @Column(name = "context")
    @Field(name = "context")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    //@SortableField(forField = "context")
    private String context;

    @Column(name = "id_version")
    @Field(name = "id_version")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    //@SortableField(forField = "id_version")
    private String idVersion;

    @Column(name = "data_flow")
    @Field(name = "data_flow")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    //@SortableField(forField = "data_flow")
    private String dataFlow;


    @Override
    public void populate(MDRDataNodeType mdrDataType) throws FieldNotMappedException {
        populateCommonFields(mdrDataType);
        for (MDRElementDataNodeType field : mdrDataType.getSubordinateMDRElementDataNodes()) {
            String fieldName = field.getName().getValue();
            String fieldValue = field.getValue().getValue();
            if (StringUtils.equalsIgnoreCase(fieldName, "FLUX_DF.CONTEXT")) {
                this.setContext(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "FLUX_DF.IDVERSION")) {
                this.setIdVersion(fieldValue);
            } else if (StringUtils.equalsIgnoreCase(fieldName, "FLUX_DF.DATAFLOW")) {
                this.setDataFlow(fieldValue);
            } else {
                logError(fieldName, this.getClass().getSimpleName());
            }
        }
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getIdVersion() {
        return idVersion;
    }

    public void setIdVersion(String idVersion) {
        this.idVersion = idVersion;
    }

    public String getDataFlow() {
        return dataFlow;
    }

    public void setDataFlow(String dataFlow) {
        this.dataFlow = dataFlow;
    }

    @Override
    public String getAcronym() {
        return "FLUX_DF";
    }

}

