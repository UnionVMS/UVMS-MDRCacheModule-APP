package eu.europa.ec.fisheries.mdr.mapper.webservice;

import javax.enterprise.context.ApplicationScoped;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.FaoAreaCodes;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

@ApplicationScoped
@MDRMapper("FAO_AREA")
public class FaoAreaCodesMapper extends MasterDataRegistryMapper {
    
    private static String LEVEL1 = "LEVEL1";
    private static String ENLEVELNAME = "ENLEVELNAME";
    private static String TERMINALIND = "TERMINALIND";

    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        FaoAreaCodes entity = new FaoAreaCodes();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setLevel(getProperty(mdrDataNodeType, LEVEL1));
        entity.setEnLevelName(getProperty(mdrDataNodeType, ENLEVELNAME));
        entity.setTerminalInd(getProperty(mdrDataNodeType, TERMINALIND));
        return entity;
    }
}
