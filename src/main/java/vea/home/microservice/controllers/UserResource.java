package vea.home.microservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vea.home.microservice.entities.Post;
import vea.home.microservice.entities.User;
import vea.home.microservice.exceptions.ExceptionResponse;
import vea.home.microservice.exceptions.PostNotFoundException;
import vea.home.microservice.exceptions.UserNotFoundException;
import vea.home.microservice.repositories.PostRepository;
import vea.home.microservice.repositories.UserRepository;
import vea.home.microservice.services.PostDTO;
import vea.home.microservice.services.UserDTO;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class UserResource {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserResource(UserRepository repository, PostRepository postRepository) {
        this.userRepository = repository;
        this.postRepository = postRepository;
    }

    @Operation(summary = "Retrieve user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/users/{id}")
    public UserDTO retrieveUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(e -> new UserDTO(e.getId(), e.getVersion(), e.getName(), e.getDateOfBirth()))
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("/users")
    public List<UserDTO> retrieveAllUsers() {
        return userRepository.findAll().stream()
                .map(e -> new UserDTO(e.getId(), e.getVersion(), e.getName(), e.getDateOfBirth()))
                .toList();
    }

    @GetMapping("/users/{userId}/posts/{postId}")
    public PostDTO retrievePost(@PathVariable Long userId, @PathVariable Long postId) {
        return postRepository.findByIdAndUserId(postId, userId)
                .map(e -> new PostDTO(e.getId(), e.getVersion(), e.getMessage()))
                .orElseThrow(() -> new PostNotFoundException(userId, postId));
    }

    @GetMapping("/users/{userId}/posts")
    public List<PostDTO> retrieveAllPosts(@PathVariable Long userId) {
        return postRepository.findByUserId(userId).stream()
                .map(e -> new PostDTO(e.getId(), e.getVersion(), e.getMessage()))
                .toList();
    }

    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)),
                    @Content(mediaType = "application/xml", schema = @Schema(implementation = UserDTO.class))
            }),

            @ApiResponse(responseCode = "400", description = "Validation errors", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)),
                    @Content(mediaType = "application/xml", schema = @Schema(implementation = ExceptionResponse.class))

            }),
            @ApiResponse(responseCode = "500", description = "Internal server errors", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)),
                    @Content(mediaType = "application/xml", schema = @Schema(implementation = ExceptionResponse.class))})
    })
    @PostMapping(value = "/users", consumes = {"application/json"})
    public ResponseEntity<EntityModel<UserDTO>> createUser(@Valid @RequestBody UserDTO newUser) {
        User user = User.builder()
                .name(newUser.getName())
                .dateOfBirth(newUser.getDateOfBirth())
                .build();

        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        UserDTO userForResponse = new UserDTO(savedUser.getId(), savedUser.getVersion(), savedUser.getName(), savedUser.getDateOfBirth());
        EntityModel<UserDTO> model = EntityModel.of(userForResponse);

        WebMvcLinkBuilder linkToUsers = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());
        model.add(linkToUsers.withRel("all-users"));

        return ResponseEntity.created(location).body(model);
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<PostDTO> newPost(@PathVariable(name = "id") Long userId, @RequestBody PostDTO newPost) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Post post = Post.builder()
                .message(newPost.getMessage())
                .user(user)
                .build();

        Post savedPost = postRepository.save(post);

        PostDTO result = PostDTO.builder()
                .id(savedPost.getId())
                .message(savedPost.getMessage())
                .version(savedPost.getVersion())
                .build();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).body(result);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        userRepository.deleteById(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Класс-workaround для генерации документации OpenAPI
     */
    private static class UserResponse extends EntityModel<UserDTO> {
    }

}
