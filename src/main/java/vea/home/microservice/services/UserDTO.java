package vea.home.microservice.services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserDTO {
    Long id;
    Long version;

    @Size(min = 2)
    @NotBlank
    String name;

    @Past
    LocalDateTime dateOfBirth;

}
