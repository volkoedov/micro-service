package vea.home.microservice.exceptions;

public class UserNotFoundException extends GenericException {
    public UserNotFoundException(Long id) {
        super("1001");
        putDetail("id", id);
    }
}
