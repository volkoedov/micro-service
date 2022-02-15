package vea.home.microservice.exceptions;

import java.time.LocalDateTime;
import java.util.Map;


public record ExceptionResponse(LocalDateTime timestamp, Map<String, Object> details,
                         String message, String errorCode, String requestInfo) {
}
