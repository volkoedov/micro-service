package vea.home.microservice.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;


@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserDTO {
    Long id;
    Long version;
    String name;
    LocalDateTime dateOfBirth;

}
