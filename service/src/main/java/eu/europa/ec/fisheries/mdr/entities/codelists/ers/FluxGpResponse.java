package eu.europa.ec.fisheries.mdr.entities.codelists.ers;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.exception.FieldNotMappedException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Indexed;
import un.unece.uncefact.data.standard.mdr.response.MDRDataNodeType;

import javax.persistence.*;

@Entity
@Table(name = "mdr_flux_gp_response")
@Indexed
@Analyzer(impl = StandardAnalyzer.class)
public class FluxGpResponse extends MasterDataRegistry {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@SequenceGenerator(name = "FLUX_GP_RESPONSE_SEQ_GEN", sequenceName = "mdr_flux_gp_response_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FLUX_GP_RESPONSE_SEQ_GEN")
	private long id;

	@Override
	public String getAcronym() {
		return "FLUX_GP_RESPONSE";
	}

	@Override
	public void populate(MDRDataNodeType mdrDataType) throws FieldNotMappedException {
		populateCommonFields(mdrDataType);
	}
}