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
import vea.home.microservice.mappers.PostMapper;
import vea.home.microservice.services.UserDTO;
import vea.home.microservice.mappers.UserMapper;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class UserResource {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserMapper userMapper;
    private final PostMapper postMapper;

    public UserResource(UserRepository repository, PostRepository postRepository, UserMapper userMapper, PostMapper postMapper) {
        this.userRepository = repository;
        this.postRepository = postRepository;
        this.userMapper = userMapper;
        this.postMapper = postMapper;
    }

    @Operation(summary = "Retrieve user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/users/{id}")
    public UserDTO retrieveUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("/users")
    public List<UserDTO> retrieveAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @GetMapping("/users/{userId}/posts/{postId}")
    public PostDTO retrievePost(@PathVariable Long userId, @PathVariable Long postId) {
        return postRepository.findByIdAndUserId(postId, userId)
                .map(postMapper::toDTO)
                .orElseThrow(() -> new PostNotFoundException(userId, postId));
    }

    @GetMapping("/users/{userId}/posts")
    public List<PostDTO> retrieveAllPosts(@PathVariable Long userId) {
        return postRepository.findByUserId(userId).stream()
                .map(postMapper::toDTO)
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
        User user = userMapper.toEntity(newUser);
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        UserDTO userForResponse = userMapper.toDTO(savedUser);
        EntityModel<UserDTO> model = EntityModel.of(userForResponse);

        WebMvcLinkBuilder linkToUsers = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());
        model.add(linkToUsers.withRel("all-users"));

        return ResponseEntity.created(location).body(model);
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<PostDTO> newPost(@PathVariable(name = "id") Long userId, @RequestBody PostDTO newPost) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Post post = postMapper.toEntityWithUser(newPost,user);

        Post savedPost = postRepository.save(post);

        PostDTO result = postMapper.toDTO(savedPost);

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
