package vea.home.microservice.controllers;

import org.apache.tomcat.util.codec.binary.Base64;
import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import vea.home.microservice.entities.User;
import vea.home.microservice.services.PostDTO;
import vea.home.microservice.services.UserDTO;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayName("IT User Resource")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ITUserResourceTest {

    public static final String MESSAGE = "Hello world";
    private static final String NAME = "Eugen";
    private static final LocalDateTime DATE_OF_BIRTH = LocalDateTime.now();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    private Long userId;
    private Long postId;

    @DisplayName("Create new user")
    @Test
    @Order(1)
    void createUserTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.setBasicAuth("testUser", "testUserPassword");

        UserDTO user = UserDTO.builder()
                .dateOfBirth(DATE_OF_BIRTH)
                .name(NAME)
                .build();
        HttpEntity<UserDTO> request = new HttpEntity<>(user, headers);

        ResponseEntity<UserDTO> response = restTemplate.postForEntity(URI.create("http://localhost:" + port + "/users"), request, UserDTO.class);

        assertThat(response, allOf(
                hasProperty("statusCode", is(HttpStatus.CREATED)),
                hasProperty("body", allOf(
                        hasProperty("id", notNullValue()),
                        hasProperty("name", equalTo(NAME)),
                        hasProperty("dateOfBirth", LocalDateTimeMatchers.within(1, ChronoUnit.MILLIS, DATE_OF_BIRTH))
                ))
        ));

        userId = Optional.ofNullable(response.getBody())
                .map(UserDTO::getId).orElseThrow();
    }

    @DisplayName("Fetch user by userId")
    @Test
    @Order(2)
    void fetchUserTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("testUser", "testUserPassword");
        ResponseEntity<UserDTO> response = restTemplate.exchange(URI.create("http://localhost:" + port + "/users/" + userId), HttpMethod.GET, new HttpEntity<>(headers), UserDTO.class);


        assertThat(response, allOf(
                hasProperty("statusCode", is(HttpStatus.OK)),
                hasProperty("body", allOf(
                        hasProperty("id", notNullValue()),
                        hasProperty("name", equalTo(NAME)),
                        hasProperty("dateOfBirth", LocalDateTimeMatchers.within(1, ChronoUnit.MILLIS, DATE_OF_BIRTH))
                ))
        ));
    }

    @DisplayName("Fetch all users")
    @Test
    @Order(3)
    void fetchAllUsersTest() {

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("testUser", "testUserPassword");
        ResponseEntity<UserDTO[]> response = restTemplate.exchange(URI.create("http://localhost:" + port + "/users/"), HttpMethod.GET, new HttpEntity<>(headers), UserDTO[].class);


        assertThat(response, allOf(
                hasProperty("statusCode", equalTo(HttpStatus.OK)),
                hasProperty("body",
                        allOf(
                                arrayWithSize(equalTo(1)),
                                hasItemInArray(allOf(
                                        hasProperty("id", notNullValue()),
                                        hasProperty("name", equalTo(NAME)),
                                        hasProperty("dateOfBirth", LocalDateTimeMatchers.within(1, ChronoUnit.MILLIS, DATE_OF_BIRTH))
                                ))
                        ))
        ));
    }

    @DisplayName("Create new post")
    @Test
    @Order(4)
    void addPostTest() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.setBasicAuth("testUser", "testUserPassword");
        PostDTO post = PostDTO.builder()
                .message(MESSAGE)
                .build();
        HttpEntity<PostDTO> request = new HttpEntity<>(post, headers);
        ResponseEntity<PostDTO> response = restTemplate.postForEntity(URI.create("http://localhost:" + port + "/users/" + userId + "/posts"), request, PostDTO.class);

        assertThat(response, allOf(
                hasProperty("statusCode", equalTo(HttpStatus.CREATED)),
                hasProperty("body",
                        allOf(
                                hasProperty("id", notNullValue()),
                                hasProperty("message", equalTo(MESSAGE)),
                                hasProperty("version", equalTo(0L))
                        ))
        ));

        postId = Optional.ofNullable(response.getBody())
                .map(PostDTO::getId)
                .orElseThrow();
    }

    @DisplayName("Fetch post by Id")
    @Test
    @Order(5)
    void fetchPostTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.setBasicAuth("testUser", "testUserPassword");

        ResponseEntity<PostDTO> response = restTemplate.exchange(URI.create("http://localhost:" + port + "/users/" + userId + "/posts/" + postId), HttpMethod.GET, new HttpEntity<>(headers), PostDTO.class);

        assertThat(response, allOf(
                hasProperty("statusCode", is(HttpStatus.OK)),
                hasProperty("body", allOf(
                        hasProperty("id", notNullValue()),
                        hasProperty("version", equalTo(0L)),
                        hasProperty("message", equalTo(MESSAGE))
                ))
        ));
    }

    @DisplayName("Fetch all posts")
    @Test
    @Order(6)
    void fetchAllPostTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.setBasicAuth("testUser", "testUserPassword");

        ResponseEntity<PostDTO[]> response = restTemplate.exchange(URI.create("http://localhost:" + port + "/users/" + userId + "/posts/"), HttpMethod.GET, new HttpEntity<>(headers), PostDTO[].class);

        assertThat(response, allOf(
                hasProperty("statusCode", is(HttpStatus.OK)),
                hasProperty("body", allOf(
                        arrayWithSize(equalTo(1)),
                        hasItemInArray(allOf(
                                hasProperty("id", notNullValue()),
                                hasProperty("version", equalTo(0L)),
                                hasProperty("message", equalTo(MESSAGE))
                        ))
                ))
        ));
    }

    @DisplayName("Delete all users and posts")
    @Test
    @Order(7)
    void deleteUserTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.setBasicAuth("testUser", "testUserPassword");

        HttpEntity<Object> request = new HttpEntity<>(null, headers);
        ResponseEntity<Object> response = restTemplate.exchange(URI.create("http://localhost:" + port + "/users/" + userId), HttpMethod.DELETE, request, Object.class);

        assertThat(response, allOf(
                        hasProperty("statusCode", is(HttpStatus.NO_CONTENT)),
                        hasProperty("body", nullValue())
                )
        );


    }


}