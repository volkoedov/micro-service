//package vea.home.microservice.controllers;
//
//import org.exparity.hamcrest.date.LocalDateTimeMatchers;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import vea.home.microservice.services.PostDTO;
//import vea.home.microservice.services.UserDTO;
//
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//import java.util.Optional;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//
//@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class ITUserResourceTest {
//    public static final LocalDateTime DATE_OF_BIRTH = LocalDateTime.now();
//    public static final String NAME = "Eugen";
//    @Autowired
//    private UserResource resource;
//
//    private Long savedUserId;
//    private Long savedPostId;
//
//    @Test
//    @Order(1)
//    void createUserTest() {
//        UserDTO user = new UserDTO(null,null, NAME, DATE_OF_BIRTH);
//        ResponseEntity<UserDTO> result = resource.newUser(user);
//
//        assertThat(result, allOf(
//                hasProperty("statusCode", equalTo(HttpStatus.CREATED)),
//                hasProperty("body", notNullValue()),
//                hasProperty("body", allOf(
//                                hasProperty("id", notNullValue()),
//                                hasProperty("name", is(NAME)),
//                                hasProperty("dateOfBirth", LocalDateTimeMatchers.within(1, ChronoUnit.MILLIS, DATE_OF_BIRTH))
//
//                        )
//
//                )));
//
//        savedUserId = Optional.ofNullable(result.getBody())
//                .map(UserDTO::getId)
//                .orElseThrow(() -> new AssertionError("Такого быть не должно!"));
//
//    }
//
//    @Test
//    @Order(2)
//    void retrieveAllUsersTest() {
//        List<UserDTO> users = resource.retrieveAllUsers();
//
//        assertThat(users, allOf(
//                notNullValue(),
//                hasItem(hasProperty("id", equalTo(savedUserId)))
//        ));
//
//    }
//
//
//    @Test
//    @Order(3)
//    void retrieveUserTest() {
//        UserDTO user = resource.retrieveUser(savedUserId);
//
//        assertThat(user, allOf(
//                notNullValue(),
//                hasProperty("id", equalTo(savedUserId)),
//                hasProperty("name", equalTo(NAME)),
//                hasProperty("dateOfBirth", LocalDateTimeMatchers.within(1, ChronoUnit.MILLIS, DATE_OF_BIRTH))
//        ));
//
//    }
//
//    @Test
//    @Order(4)
//    void createPostTest() {
//        String message = "Hello world!";
//        PostDTO post = PostDTO.builder()
//                .message(message)
//                .build();
//        ResponseEntity<PostDTO> result = resource.newPost(savedUserId, post);
//
//        assertThat(result, allOf(
//                hasProperty("statusCode", equalTo(HttpStatus.CREATED)),
//                hasProperty("body", notNullValue()),
//                hasProperty("body", allOf(
//                                hasProperty("id", notNullValue()),
//                                hasProperty("message", is(message))
//
//                        )
//                )
//        ));
//        savedPostId = Optional.ofNullable(result.getBody())
//                .map(PostDTO::getId)
//                .orElseThrow(() -> new AssertionError("Такого быть не должно"));
//    }
//
//    @Test
//    @Order(5)
//    void retrievePostTest() {
//        PostDTO post = resource.retrievePost(savedUserId, savedPostId);
//        assertThat(post, allOf(
//                        hasProperty("id", equalTo(savedPostId)),
//                        hasProperty("message", is(equalTo("Hello world!")))
//                )
//        );
//    }
//
//    @Test
//    @Order(6)
//    void retrieveAllPostsTest() {
//        List<PostDTO> posts = resource.retrieveAllPosts(savedUserId);
//        assertThat(posts, allOf(
//                iterableWithSize(1),
//                hasItem(allOf(
//                        hasProperty("id", equalTo(savedPostId)),
//                        hasProperty("message", is(equalTo("Hello world!")))
//                        )
//                ))
//        );
//    }
//
//    @Test
//    @Order(7)
//    void retrieveUserNotFoundTest() {
//        UserDTO user = resource.retrieveUser(-1L);
//
//        assertThat(user, allOf(
//                notNullValue(),
//                hasProperty("id", equalTo(savedUserId)),
//                hasProperty("name", equalTo(NAME)),
//                hasProperty("dateOfBirth", LocalDateTimeMatchers.within(1, ChronoUnit.MILLIS, DATE_OF_BIRTH))
//        ));
//
//    }
//}