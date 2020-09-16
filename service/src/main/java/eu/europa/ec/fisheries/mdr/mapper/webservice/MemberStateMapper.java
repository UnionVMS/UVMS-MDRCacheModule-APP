package eu.europa.ec.fisheries.mdr.mapper.webservice;

import javax.enterprise.context.ApplicationScoped;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.fisheries.mdr.entities.codelists.ers.MemberState;
import eu.europa.ec.fisheries.mdr.qualifiers.MDRMapper;
import eu.europa.ec.mare.fisheries.services.mdr.v1.MDRDataNodeType;

@ApplicationScoped
@MDRMapper("MEMBER_STATE")
public class MemberStateMapper extends MasterDataRegistryMapper {

    static final String NAME = "NAME";
    static final String CODE2 = "CODE2";
    
    @Override
    public MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType) {
        MemberState entity = new MemberState();
        populateCommonProperties(mdrDataNodeType, entity);
        entity.setEnName(getProperty(mdrDataNodeType, NAME));
        entity.setIso2Code(getProperty(mdrDataNodeType, CODE2));
        return entity;
    }
}
