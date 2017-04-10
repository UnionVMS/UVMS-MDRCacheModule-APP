package eu.europa.ec.fisheries.mdr.entities.constants;

/**
 * Created by kovian on 28/07/2016.
 */
public enum AcronymListState {

    RUNNING("RUNNING"),
    SUCCESS("SUCCESS"),
    FAILED("FAILED"),
    NEWENTRY("NEWENTRY"),
    EMPTY("EMPTY"),
    ACKNOWLEDGED("ACKNOWLEDGED");

    private String value;

    AcronymListState(final String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

}
