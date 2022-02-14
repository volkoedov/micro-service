package vea.home.microservice.exceptions;

import java.time.LocalDateTime;
import java.util.Map;


record ExceptionResponse(LocalDateTime timestamp, Map<String, Object> details,
                         String message, String errorCode, String requestInfo) {
}
