package vea.home.microservice.exceptions;

public class UserNotFoundException extends GenericException {
    public UserNotFoundException(Long id) {
        super(404, String.format("User with id = %s not found!", id));
        putDetail("id", id);
    }
}
