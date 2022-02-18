package vea.home.microservice.exceptions;

public class PostNotFoundException extends GenericException {

    public PostNotFoundException(Long userId, Long postId) {
        super("1002");
        putDetail("userId", userId);
        putDetail("postId", postId);
    }
}
