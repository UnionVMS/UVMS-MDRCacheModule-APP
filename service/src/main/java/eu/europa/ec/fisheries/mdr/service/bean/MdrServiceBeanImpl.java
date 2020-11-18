/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.mdr.service.bean;

import eu.europa.ec.fisheries.mdr.entities.MdrCodeListStatus;
import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.exception.MdrCacheInitException;
import eu.europa.ec.fisheries.mdr.mapper.MasterDataRegistryEntityCacheFactory;
import eu.europa.ec.fisheries.mdr.repository.MdrLuceneSearchRepository;
import eu.europa.ec.fisheries.mdr.repository.MdrRepository;
import eu.europa.ec.fisheries.mdr.repository.MdrStatusRepository;
import eu.europa.ec.fisheries.mdr.service.MdrServiceBean;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.mdr.model.exception.MdrModelMarshallException;
import eu.europa.ec.fisheries.uvms.mdr.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.mdr.model.mapper.MdrModuleMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetAllCodeListsResponse;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListRequest;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetCodeListResponse;
import un.unece.uncefact.data.standard.mdr.communication.MdrGetLastRefreshDateResponse;
import un.unece.uncefact.data.standard.mdr.communication.SingleCodeListRappresentation;
import un.unece.uncefact.data.standard.mdr.communication.ValidationResultType;
import un.unece.uncefact.data.standard.mdr.response.FLUXMDRReturnMessage;
import un.unece.uncefact.data.standard.mdr.response.FLUXResponseDocumentType;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Stateless
@Slf4j
public class MdrServiceBeanImpl implements MdrServiceBean {

    @EJB
    private MdrRepository mdrRepository;

    @EJB
    private MdrStatusRepository statusBean;

    @EJB
    private MdrLuceneSearchRepository mdrSearchRepository;

    private static final String STAR = "*";
    private static final String ERROR_GET_LIST_FOR_THE_REQUESTED_CODE = "Error while trying to get list for the requested CodeList : ";
    private static final String ACRONYM_DOSENT_EXIST = "The acronym you are searching for does not exist! Acronym ::: ";
    private static final int MB = 1024 * 1024;


    /**
     * This method saves the received codeList (from FLUX or MANUAL upload).
     *
     * @param message
     */
    @Override
    public void receivedSyncMdrEntityMessage(FLUXMDRReturnMessage message) throws MdrModelMarshallException {
        log.info("[INFO] Received message from FLUX related to MDR Entity Synchronization.");
        if (isDataMessage(JAXBMarshaller.marshallJaxBObjectToString((message)), message)) {
            mdrRepository.updateMdrEntity(message);
        }
    }

    private boolean isDataMessage(String messageStr, FLUXMDRReturnMessage responseMessage) {
        boolean isDataMessage = true;
        if (responseMessage == null || responseMessage.getFLUXResponseDocument() == null || responseMessage.getMDRDataSet() == null) {
            log.error("[ERROR] The message received is not of type <<FLUXMDRReturnMessage>> so it won't be attempted to save it! Message content is as follows : " + messageStr);
            isDataMessage = false;
        } else if (isAcnowledgeMessage(messageStr)) {
            log.info("[ACKNOWLEDGE] : Received Acknowledge Message for a previous request sent to FLUX. No data so nothing is going to be persisted in MDR.");
            isDataMessage = false;
        } else if (isObjDescriptionMessage(responseMessage)) {
            mdrRepository.saveAcronymStructureMessage(messageStr, responseMessage.getMDRDataSet().getID().getValue());
            mdrRepository.updateMetaDataForAcronym(responseMessage.getMDRDataSet());
            isDataMessage = false;
        }
        return isDataMessage;
    }

    private boolean isObjDescriptionMessage(FLUXMDRReturnMessage fluxReturnMessage) {
        FLUXResponseDocumentType fluxResponseDocument = null;
        if (fluxReturnMessage != null) {
            fluxResponseDocument = fluxReturnMessage.getFLUXResponseDocument();
        }
        return fluxResponseDocument != null && fluxResponseDocument.getTypeCode() != null && "OBJ_DESC".equals(fluxResponseDocument.getTypeCode().getValue());
    }

    /**
     * This method serves the request of getting a CodeList and returning it as a jmsMessage.
     *
     * @param request
     */
    @Override
    public MdrGetCodeListResponse receivedGetSingleMdrCodeListMessage(MdrGetCodeListRequest request) {
        log.debug("[INFO] Received GetSingleMDRListMessageEvent.. Going to fetch Lucene Indexes..");
        try {

            log.debug("[INFO] Requested object is : [ " + request + " ].");
            // Request is Not OK
            if (!requestIsOk(request)) {
                return null;
            }
            // Check query, Run query, Create response
            List<String> columnFilters = request.getColumnsToFilters();
            String filter = request.getFilter();
            String[] columnFiltersArr;
            if (CollectionUtils.isNotEmpty(columnFilters)) {
                columnFiltersArr = columnFilters.toArray(new String[0]);
            } else {
                columnFiltersArr = new String[]{"code", "description"};
            }
            if (StringUtils.isNotEmpty(filter) && !filter.equals(STAR)) {
                filter = STAR + filter + STAR;
            } else {
                filter = STAR;
            }
            BigInteger wantedNumberOfResults = request.getWantedNumberOfResults();
            Integer nrOfResults = wantedNumberOfResults != null ? wantedNumberOfResults.intValue() : 9999999;

            List<? extends MasterDataRegistry> mdrList = mdrSearchRepository.findCodeListItemsByAcronymAndFilter(request.getAcronym(),
                    0, nrOfResults, null, false, filter, columnFiltersArr);
            String validationStr = "Validation is OK.";
            ValidationResultType validation = ValidationResultType.OK;
            if (CollectionUtils.isEmpty(mdrList)) {
                validationStr = "Codelist was found but, the search criteria returned 0 results. (Maybe the Table is empty!)";
                validation = ValidationResultType.WOK;
            }
            return MdrModuleMapper.createFluxMdrGetCodeListResponse(mdrList, request.getAcronym(), validation, validationStr);
        }catch (ServiceException e) {
            log.error(ERROR_GET_LIST_FOR_THE_REQUESTED_CODE,e);
        }
        return new MdrGetCodeListResponse();
    }

    @Override
    public MdrGetCodeListResponse getMdrStatus() {

            List<MdrCodeListStatus> codelistStatus = statusBean.findAllMdrStatuses();
            String validationStr = "Validation is OK.";
            ValidationResultType validation = ValidationResultType.OK;
            if (CollectionUtils.isEmpty(codelistStatus)) {
                validationStr = "Codelist was found but, the search criteria returned 0 results. (Maybe the Table is empty!)";
                validation = ValidationResultType.WOK;
            }
            return MdrModuleMapper.createFluxMdrGetCodeListResponse(codelistStatus, validation, validationStr);
    }

    @Override
    public MdrGetAllCodeListsResponse receivedGetAllMdrCodeListMessage() {

        try {

            log.debug("[INFO] Got GetAllMdrCodeListsMessageEvent..");
            List<String> acronymsList = null;
            StopWatch watch = StopWatch.createStarted();
            try {
                acronymsList = MasterDataRegistryEntityCacheFactory.getAcronymsList();
            } catch (MdrCacheInitException e) {
                log.error("[ERROR] While trying to get the acronym list (MasterDataRegistryEntityCacheFactory.getAcronymsList())!!",e);
            }
            List<SingleCodeListRappresentation> allCoceLists = new ArrayList<>();
            for (String actAcronym : acronymsList) {
                List<? extends MasterDataRegistry> mdrList = mdrSearchRepository.findCodeListItemsByAcronymAndFilter(actAcronym,
                        0, 99999999, "code", false, "*", "code");
                allCoceLists.add(MdrModuleMapper.mapToSingleCodeListRappresentation(mdrList, actAcronym, null, "OK"));
            }
            watch.stop();
            printOutJavHepSize();
            MdrGetAllCodeListsResponse resp = new MdrGetAllCodeListsResponse();
            resp.setCodeLists(allCoceLists);
            log.info("Retrieved and marshaled [{}] codelists in [{}] seconds. Size of message to be sent is [{}] Mb. Now sending..", allCoceLists.size(), watch.getTime(TimeUnit.SECONDS), getSizeInMb(allCoceLists));
            return  resp;
        } catch (ServiceException | IllegalStateException  e) {
            log.error(ERROR_GET_LIST_FOR_THE_REQUESTED_CODE,e);
        }
        return new MdrGetAllCodeListsResponse();
    }

    private Object getSizeInMb(List<SingleCodeListRappresentation> response) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(response);
            byte[] bytes = bos.toByteArray();
            return  bytes.length / MB;
        } catch (IOException e) {
            log.warn("Couldn't get the size of the response!", e);
        }
        return 0;
    }

    private void printOutJavHepSize() {
        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();
        log.info("##### Heap utilization statistics [MB] #####");
        //Print used memory
        log.info("Used Memory : [{}] MB.", (runtime.totalMemory() - runtime.freeMemory()) / MB);
        //Print free memory
        log.info("Free Memory : [{}] MB.", runtime.freeMemory() / MB);
        //Print total available memory
        log.info("Total Memory : [{}] MB.", runtime.totalMemory() / MB);
        //Print Maximum available memory
        log.info("Max Memory : [{}] MB.", runtime.maxMemory() / MB);
    }

    @Override
    public MdrGetLastRefreshDateResponse receivedGetLastRefreshDateFromStatuses() {
        try {
            MdrGetLastRefreshDateResponse resp = new MdrGetLastRefreshDateResponse();
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(statusBean.getLastRefreshDate());
            resp.setLastRefreshDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
            return resp;
        } catch (DatatypeConfigurationException e) {
            log.warn("Error when trying to create new instance for DatatypeFactory");
            return new MdrGetLastRefreshDateResponse();
        }
    }

    /**
     * Checks if the request received for the code list has the minimum requirements.
     *
     * @param requestObj
     * @return
     */
    private boolean requestIsOk( MdrGetCodeListRequest requestObj) {
        if (requestObj == null || StringUtils.isEmpty(requestObj.getAcronym())) {
            log.error("[ERROR] The message received is not of type MdrGetCodeListRequest so it won't be attempted to unmarshall it! " +
                    "Message content is as follows : ",requestObj);
            return false;
        }
        // Acronym doesn't exist
        if (!MasterDataRegistryEntityCacheFactory.getInstance().existsAcronym(requestObj.getAcronym())) {
            log.error(ACRONYM_DOSENT_EXIST);
            return false;
        }
        return true;
    }

    private boolean isAcnowledgeMessage(String jmsMessage) {
        return !StringUtils.isBlank(jmsMessage) && jmsMessage.contains("ACK") && jmsMessage.contains("Acknowledge Of Receipt");
    }

}
