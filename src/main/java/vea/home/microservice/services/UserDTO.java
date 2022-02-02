package vea.home.microservice.services;

import java.time.LocalDateTime;

public record UserDTO(Long id, String name, LocalDateTime dateOfBirth) {
}
