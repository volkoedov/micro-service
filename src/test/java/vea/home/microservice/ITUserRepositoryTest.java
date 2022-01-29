package vea.home.microservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vea.home.microservice.entities.User;
import vea.home.microservice.repositories.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Slf4j
class ITUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void persistTest() {

        String lastName = "Volkoedov";
        String firstName = "Eugen";

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
        User savedUser = userRepository.save(user);

        log.debug("Saved user : {}", savedUser);

        assertThat(savedUser, allOf(
                hasProperty("id", equalTo(1L)),
                hasProperty("firstName", equalTo(firstName)),
                hasProperty("lastName", equalTo(lastName))

                )
        );
    }

}
