package vea.home.microservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import vea.home.microservice.entities.Post;
import vea.home.microservice.entities.User;
import vea.home.microservice.repositories.PostRepository;
import vea.home.microservice.repositories.UserRepository;
import vea.home.microservice.services.PostDTO;
import vea.home.microservice.services.UserDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserResourceTest {
    private static final LocalDateTime DATE_OF_BIRTH = LocalDateTime.now();
    private static final Long POST_ID = 1L;
    private static final String NAME = "Eugen";
    private static final Long USER_ID = 1L;
    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    private UserRepository userRepository;

    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addUserTest() throws Exception {

        when(userRepository.save(any())).thenReturn(User.builder()
                .id(USER_ID)
                .name(NAME)
                .dateOfBirth(DATE_OF_BIRTH)
                .build());

        UserDTO user = new UserDTO(null, null, NAME, DATE_OF_BIRTH);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.dateOfBirth").value(containsString(DATE_OF_BIRTH.format(DateTimeFormatter.ISO_DATE_TIME))));
    }

    @Test
    void fetchAllUsers() throws Exception {
        List<User> users = new ArrayList<>();
        User firstUser = User.builder()
                .id(USER_ID)
                .name(NAME)
                .version(0L)
                .dateOfBirth(DATE_OF_BIRTH)
                .build();
        users.add(firstUser);

        LocalDateTime otherUserDateOfBirth = LocalDateTime.of(1983, 3, 29, 23, 59, 0);
        String otherUserName = "Other User";
        User secondUser = User.builder()
                .id(2L)
                .name(otherUserName)
                .version(0L)
                .dateOfBirth(otherUserDateOfBirth)
                .build();
        users.add(secondUser);

        when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[?(@['id']=='%s' && @['name']=='%s' && @['dateOfBirth']=='%s'&& @['version']=='0')]", USER_ID, NAME, DATE_OF_BIRTH.format(DateTimeFormatter.ISO_DATE_TIME)).exists())
                .andExpect(jsonPath("[?(@['id']=='%s' && @['name']=='%s' && @['dateOfBirth']=='%s'&& @['version']=='0')]", 2, otherUserName, otherUserDateOfBirth.format(DateTimeFormatter.ISO_DATE_TIME)).exists())
                .andExpect(jsonPath("$.length()").value(equalTo(2)));
    }

    @Test
    void fetchUserTest() throws Exception {

        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder()
                .id(USER_ID)
                .name(NAME)
                .dateOfBirth(DATE_OF_BIRTH)
                .build()));


        mockMvc.perform(get("/users/{id}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.dateOfBirth").value(containsString(DATE_OF_BIRTH.format(DateTimeFormatter.ISO_DATE_TIME))));

    }

    @Test
    void newPostTest() throws Exception {
        String message = "Hello world";

        PostDTO post = PostDTO.builder()
                .message(message)
                .build();

        User user = User.builder()
                .id(USER_ID)
                .name(NAME)
                .dateOfBirth(DATE_OF_BIRTH)
                .version(0L)
                .build();
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        Post savedPost = Post.builder()
                .id(POST_ID)
                .message(message)
                .user(user)
                .version(0L)
                .build();
        when(postRepository.save(any())).thenReturn(savedPost);

        mockMvc.perform(post("/users/{userId}/posts", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(POST_ID))
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.version").value(0));
    }

    @Test
    void deleteUserTest() throws Exception {
        mockMvc.perform(delete("/users/{userId}",USER_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void retrievePostTest() throws Exception {

        User user = User.builder()
                .id(USER_ID)
                .name(NAME)
                .dateOfBirth(DATE_OF_BIRTH)
                .version(0L)
                .build();

        String message = "Hello Eugen";
        Post post = Post.builder()
                .id(POST_ID)
                .message(message)
                .user(user)
                .version(0L)
                .build();

        when(postRepository.findByIdAndUserId(POST_ID,USER_ID)).thenReturn(Optional.of(post));
        mockMvc.perform(get("/users/{userId}/posts/{postId}",USER_ID,POST_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(POST_ID))
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.version").value(0))
        ;
    }

    @Test
    void retrieveAllPosts() throws Exception {
        Set<Post> posts=new HashSet<>();
        Post firstPost = Post.builder()
                .id(1L)
                .message("First Post")
                .version(0L)
                .build();
        posts.add(firstPost);

        Post secondPost= Post.builder()
                .id(2L)
                .message("Second post")
                .version(10L)
                .build();
        posts.add(secondPost);

        when(postRepository.findByUserId(USER_ID)).thenReturn(posts);
        mockMvc.perform(get("/users/{userId}/posts",USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@['id']=='%s' && @['message']=='%s' && @['version']=='%s')]", firstPost.getId(), firstPost.getMessage(), firstPost.getVersion()).exists())
                .andExpect(jsonPath("$.[?(@['id']=='%s' && @['message']=='%s' && @['version']=='%s')]", secondPost.getId(), secondPost.getMessage(), secondPost.getVersion()).exists())
                .andExpect(jsonPath("$.length()").value(equalTo(2)));
    }
}
