package vea.home.microservice.exceptions;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

abstract class GenericException extends RuntimeException {
    private final Map<String, Object> details=new HashMap<>();
    private final Integer code;

    protected GenericException(Integer code,String message, Map<String, Object> details) {
        super(message);
        this.code = code;
        this.details.putAll(details);
    }

    public Map<String, Object> getDetails() {
        return Collections.unmodifiableMap(details);
    }

    public Integer getCode() {
        return code;
    }
}
