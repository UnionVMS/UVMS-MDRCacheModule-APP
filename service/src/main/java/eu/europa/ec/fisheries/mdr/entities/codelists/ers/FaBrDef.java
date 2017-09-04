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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import un.unece.uncefact.data.standard.mdr.response.MDRDataNodeType;
import un.unece.uncefact.data.standard.mdr.response.MDRElementDataNodeType;

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
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "mdr_fa_br_def_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    private long id;

    @Column(name = "field")
    @Field(name = "field")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    private String field;

    @Column(name = "message_if_failing")
    @Field(name = "messageIfFailing")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    private String messageIfFailing;

    @Column(name = "sequence_order")
    @Field(name = "sequenceOrder")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    private String sequenceOrder;

    @Column(name = "br_sublevel")
    @Field(name = "brSublevel")
    @Analyzer(definition = LOW_CASE_ANALYSER)
    private String brSublevel;

    @Override
    public String getAcronym() {
        return "FA_BR_DEF";
    }

    @Override
    public void populate(MDRDataNodeType mdrDataType) throws FieldNotMappedException {
        populateCommonFields(mdrDataType);
        for (MDRElementDataNodeType field : mdrDataType.getSubordinateMDRElementDataNodes()) {
            String fieldName = field.getName().getValue();
            String fieldValue = field.getName().getValue();
            if (StringUtils.endsWith(fieldName, "FA_BR_DEF.FIELD")) {
                this.setField(fieldValue);
            } else if (StringUtils.endsWith(fieldName, "FA_BR_DEF.ENMESSAGE")) {
                this.setMessageIfFailing(fieldValue);
            } else if (StringUtils.endsWith(fieldName, "FA_BR_DEF.SEQORDER")) {
                this.setSequenceOrder(fieldValue);
            } else if (StringUtils.endsWith(fieldName, "FA_BR_DEF.BRSUBLEVEL")) {
                this.setBrSublevel(fieldValue);
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

}
