package eu.europa.ec.fisheries.mdr.service.bean.synchronization;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import eu.europa.ec.fisheries.mdr.client.MdrWebServiceClient;
import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.constants.AcronymListState;
import eu.europa.ec.fisheries.mdr.mapper.webservice.MasterDataRegistryMapper;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.fisheries.mdr.repository.MdrRepository;
import eu.europa.ec.fisheries.mdr.repository.MdrStatusRepository;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class MdrSyncHelper {

	private MdrStatusRepository statusRepository;

	private MdrRepository mdrRepository;

	private MdrWebServiceClient mdrWebServiceClient;

	private Instance<MasterDataRegistryMapper> mappers;

	@Inject
	public MdrSyncHelper(MdrStatusRepository statusRepository, MdrRepository mdrRepository, MdrWebServiceClient mdrWebServiceClient, @Any Instance<MasterDataRegistryMapper> mappers) {
		this.statusRepository = statusRepository;
		this.mdrRepository = mdrRepository;
		this.mdrWebServiceClient = mdrWebServiceClient;
		this.mappers = mappers;
	}

	public MdrSyncHelper() {
	}

	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public void updateMdrEntity(String actualAcronym) {
		statusRepository.updateStatusAttemptForAcronym(actualAcronym, AcronymListState.RUNNING, DateUtils.nowUTC().toDate(), java.util.UUID.randomUUID().toString());
		log.info("Synchronization Request Sent for Entity : " + actualAcronym);
		List<MasterDataRegistry> results = sendRequestForAcronym(actualAcronym);
		mdrRepository.updateMdrEntities(results, actualAcronym);
	}

	private List<MasterDataRegistry> sendRequestForAcronym(String acronym) {
		List<MDRDataNodeType> results = mdrWebServiceClient.getMDRList(acronym);
		try {
			return results.stream().map(node -> mapResultToEntity(node, acronym)).collect(Collectors.toList());
		} catch (UnsatisfiedResolutionException e) {
			log.error("Could not find mapper for acronym: " + acronym,  e);
			return Collections.emptyList();
		}
	}

	private MasterDataRegistry mapResultToEntity(MDRDataNodeType node, String acronym) {
		return mappers.select(new MDRMapper.MDRMapperImpl(acronym)).get().mapMDRDataNodeTypeToEntity(node);
	}
}
