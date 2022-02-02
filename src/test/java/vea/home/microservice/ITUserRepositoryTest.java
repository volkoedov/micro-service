package vea.home.microservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vea.home.microservice.entities.User;
import vea.home.microservice.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ITUserRepositoryTest {

    private static final String LAST_NAME = "Volkoedov";
    private static final String FIRST_NAME = "Eugen";

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    void persistTest() {

        User user = User.builder()
                .name(FIRST_NAME)
                .dateOfBirth(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(user);

        log.debug("Saved user : {}", savedUser);

        assertThat(savedUser, allOf(
                        hasProperty("id", equalTo(1L)),
                        hasProperty("firstName", equalTo(FIRST_NAME)),
                        hasProperty("lastName", equalTo(LAST_NAME))

                )
        );
    }

    @Test
    @Order(2)
    void findTest() {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new AssertionError("Такого не должно быть!"));

        assertThat(user, allOf(
                hasProperty("id", equalTo(1L)),
                hasProperty("firstName", equalTo(FIRST_NAME)),
                hasProperty("lastName", equalTo(LAST_NAME))

        ));
    }

    @Test
    @Order(3)
    void updateTest() {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new AssertionError("Такого не должно быть!"));

        String name = "Mikhail";
        user.setName(name);

        userRepository.save(user);

        user = userRepository.findById(1L)
                .orElseThrow(() -> new AssertionError("Такого не должно быть!"));

        log.debug("Updated user: {}", user);

        assertThat(user, allOf(
                hasProperty("id", equalTo(1L)),
                hasProperty("name", equalTo(name)),
                hasProperty("lastName", equalTo(LAST_NAME))

        ));
    }

    @Test
    @Order(4)
    void deleteTest() {

        userRepository.deleteById(1L);
        userRepository.findById(1L).ifPresent(r -> fail("Такого быть не должно!"));
    }
}
