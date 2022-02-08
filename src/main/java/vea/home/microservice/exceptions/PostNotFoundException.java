package vea.home.microservice.exceptions;

public class PostNotFoundException extends GenericException {

    public PostNotFoundException(Long userId, Long postId) {
        super(404, String.format("Post with id= %s for userID = %s not found!", postId, userId));
        putDetail("userId",userId);
        putDetail("postId", postId);
    }
}
