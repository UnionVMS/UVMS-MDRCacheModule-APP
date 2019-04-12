
package eu.europa.ec.fisheries.mdr.entities.codelists.ers;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.exception.FieldNotMappedException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Indexed;
import un.unece.uncefact.data.standard.mdr.response.MDRDataNodeType;

import javax.persistence.*;

@Entity
@Table(name = "mdr_fa_device_gear_attachment")
@Indexed
@Analyzer(impl = StandardAnalyzer.class)
public class FaDeviceGearAttachment extends MasterDataRegistry {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "FA_DEVICE_GEAR_ATTACHMENT_SEQ_GEN", sequenceName = "mdr_fa_device_gear_attachment_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FA_DEVICE_GEAR_ATTACHMENT_SEQ_GEN")
    private long id;

    @Override
    public String getAcronym() {
        return "FA_DEVICE_GEAR_ATTACHMENT";
    }

    @Override
    public void populate(MDRDataNodeType mdrDataType) throws FieldNotMappedException {
        populateCommonFields(mdrDataType);
    }
}
