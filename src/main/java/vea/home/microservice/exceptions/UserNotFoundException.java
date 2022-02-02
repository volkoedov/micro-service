package vea.home.microservice.exceptions;

import java.util.Collections;


public class UserNotFoundException extends GenericException {
    public UserNotFoundException(Long id) {
        super(404, String.format("User with id = %s not found!", id), Collections.singletonMap("id", id));
    }
}
