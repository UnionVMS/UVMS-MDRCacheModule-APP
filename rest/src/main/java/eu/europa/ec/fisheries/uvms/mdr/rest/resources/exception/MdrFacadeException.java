package eu.europa.ec.fisheries.uvms.mdr.rest.resources.exception;

public class MdrFacadeException extends Exception {

    private String message;

    public MdrFacadeException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}