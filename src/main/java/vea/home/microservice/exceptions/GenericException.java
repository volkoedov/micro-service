package vea.home.microservice.exceptions;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

abstract class GenericException extends RuntimeException {
    private final Map<String, Object> details=new HashMap<>();
    private final Integer code;



    protected GenericException(int code, String message) {
        super(message);
        this.code = code;
    }

    public void putDetail(String key,Object value){
        details.put(key, value);
    }
    public Map<String, Object> getDetails() {
        return Collections.unmodifiableMap(details);
    }

    public Integer getCode() {
        return code;
    }
}
