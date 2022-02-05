package vea.home.microservice.repositories;

import lombok.extern.slf4j.Slf4j;
import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vea.home.microservice.entities.User;
import vea.home.microservice.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ITUserRepositoryTest {

    private static final LocalDateTime DATE_OF_BIRTH = LocalDateTime.now();
    private static final String NAME = "Eugen";

    @Autowired
    private UserRepository userRepository;

    private Long savedUserId;

    @Test
    @Order(1)
    void persistTest() {

        User user = User.builder().name(NAME).dateOfBirth(DATE_OF_BIRTH).build();
        User savedUser = userRepository.save(user);

        log.debug("Saved user : {}", savedUser);

        assertThat(savedUser, allOf(hasProperty("id", notNullValue()), hasProperty("name", equalTo(NAME)), hasProperty("dateOfBirth", LocalDateTimeMatchers.within(1, ChronoUnit.MILLIS, DATE_OF_BIRTH))
        ));

        savedUserId = savedUser.getId();
    }

    @Test
    @Order(2)
    void findTest() {
        User user = userRepository.findById(savedUserId).orElseThrow(() -> new AssertionError("Такого не должно быть!"));

        assertThat(user, allOf(hasProperty("id", equalTo(savedUserId)),
                hasProperty("name", equalTo(NAME)),
                hasProperty("dateOfBirth", LocalDateTimeMatchers.within(1, ChronoUnit.MILLIS, DATE_OF_BIRTH))

        ));
    }

    @Test
    @Order(3)
    void updateTest() {
        User user = userRepository.findById(savedUserId).orElseThrow(() -> new AssertionError("Такого не должно быть!"));

        String name = "Mikhail";
        user.setName(name);

        userRepository.save(user);

        user = userRepository.findById(savedUserId).orElseThrow(() -> new AssertionError("Такого не должно быть!"));

        log.debug("Updated user: {}", user);

        assertThat(user, allOf(hasProperty("id", equalTo(savedUserId)), hasProperty("name", equalTo(name)), hasProperty("dateOfBirth", LocalDateTimeMatchers.within(1, ChronoUnit.MILLIS, DATE_OF_BIRTH))

        ));
    }

    @Test
    @Order(4)
    void deleteTest() {

        userRepository.deleteById(savedUserId);
        userRepository.findById(savedUserId).ifPresent(r -> fail("Такого быть не должно!"));
    }
}
