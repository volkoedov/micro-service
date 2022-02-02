package vea.home.microservice.exceptions;


import java.util.Map;

abstract class GenericException extends RuntimeException {
    private final Map<String, Object> details;
    private final Integer code;


    protected GenericException(Integer code,String message, Map<String, Object> details) {
        super(message);
        this.code = code;
        this.details = details;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public Integer getCode() {
        return code;
    }
}
