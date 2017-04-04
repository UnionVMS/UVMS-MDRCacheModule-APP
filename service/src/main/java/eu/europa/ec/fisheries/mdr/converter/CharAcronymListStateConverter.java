package eu.europa.ec.fisheries.mdr.converter;

import eu.europa.ec.fisheries.mdr.entities.constants.AcronymListState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by kovian on 29/07/2016.
 */
@Converter
public class CharAcronymListStateConverter implements AttributeConverter<AcronymListState, String> {

    public CharAcronymListStateConverter() {super();}

    @Override
    public String convertToDatabaseColumn(AcronymListState attribute) {
        if(attribute == null){
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public AcronymListState convertToEntityAttribute(String dbData) {
        if(dbData == null){
            return null;
        }
        return AcronymListState.valueOf(dbData);
    }
}