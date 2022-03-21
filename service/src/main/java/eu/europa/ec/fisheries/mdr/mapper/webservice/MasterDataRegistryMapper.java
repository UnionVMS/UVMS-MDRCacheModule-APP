package eu.europa.ec.fisheries.mdr.mapper.webservice;

import eu.europa.ec.fisheries.mdr.entities.codelists.baseentities.MasterDataRegistry;
import eu.europa.ec.mare.fisheries.model.mdr.v1.DelimitedPeriodType;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRDataNodeType;
import eu.europa.ec.mare.fisheries.model.mdr.v1.MDRElementDataNodeType;

import java.time.Instant;
import java.time.temporal.ChronoField;

public abstract class MasterDataRegistryMapper {

    static final String CODE = "CODE";
    static final String DESCRIPTION = "DESCRIPTION";

    public abstract MasterDataRegistry mapMDRDataNodeTypeToEntity(MDRDataNodeType mdrDataNodeType);

    void populateCommonProperties(MDRDataNodeType mdrDataNodeType, MasterDataRegistry entity) {
        entity.setCode(getProperty(mdrDataNodeType, CODE));
        entity.setDescription(getProperty(mdrDataNodeType, DESCRIPTION));
        populateDateProperties(mdrDataNodeType.getEffectiveDelimitedPeriod(), entity);
    }

    String getProperty(MDRDataNodeType mdrDataNodeType, String name) {
        return mdrDataNodeType.getSubordinateMDRElementDataNode().stream().filter(node -> name.equals(node.getName())).findFirst().map(MDRElementDataNodeType::getValue).orElse(null);
    }

    void populateDateProperties(DelimitedPeriodType delimitedPeriodType, MasterDataRegistry entity) {
        if (delimitedPeriodType != null) {
            if (delimitedPeriodType.getStartDateTime() != null) {
                Instant startInstant = Instant
                        .ofEpochMilli(delimitedPeriodType.getStartDateTime().getDateTime().toGregorianCalendar().getTimeInMillis())
                        .with(temporal -> temporal
                                .with(ChronoField.HOUR_OF_DAY, 0)
                                .with(ChronoField.MINUTE_OF_HOUR, 0)
                                .with(ChronoField.SECOND_OF_MINUTE, 0)
                                .with(ChronoField.NANO_OF_SECOND, 0));

                entity.setStartDate(java.util.Date.from(startInstant));
            }
            if (delimitedPeriodType.getEndDateTime() != null) {
                Instant endInstant = Instant
                        .ofEpochMilli(delimitedPeriodType.getEndDateTime().getDateTime().toGregorianCalendar().getTimeInMillis())
                        .with(temporal -> temporal
                                .with(ChronoField.HOUR_OF_DAY, 23)
                                .with(ChronoField.MINUTE_OF_HOUR, 59)
                                .with(ChronoField.SECOND_OF_MINUTE, 59)
                                .with(ChronoField.NANO_OF_SECOND, 999_999_999));
                entity.setEndDate(java.util.Date.from(endInstant));
            }
        }
    }

    Double parseStringToDouble(String value) {
        return value == null || value.isEmpty() ? null : Double.parseDouble(value);
    }
}
