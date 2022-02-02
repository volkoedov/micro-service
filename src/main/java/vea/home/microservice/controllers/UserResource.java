package vea.home.microservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vea.home.microservice.entities.User;
import vea.home.microservice.exceptions.UserNotFoundException;
import vea.home.microservice.repositories.UserRepository;
import vea.home.microservice.services.UserDTO;

import java.net.URI;
import java.util.List;

@RestController
public class UserResource {
    private final UserRepository repository;

    public UserResource(UserRepository repository) {
        this.repository = repository;
    }


    @GetMapping("/users/{id}")
    public UserDTO retrieveUser(@PathVariable Long id) {
        return repository.findById(id).map(e -> new UserDTO(e.getId(), e.getName(), e.getDateOfBirth())).orElseThrow(()->new UserNotFoundException(id));
    }

    @GetMapping("/users")
    public List<UserDTO> retrieveAllUsers() {
        return repository.findAll().stream()
                .map(e -> new UserDTO(e.getId(), e.getName(), e.getDateOfBirth()))
                .toList();
    }

    @PostMapping("/users")
    public ResponseEntity<Object> newUser(@RequestBody UserDTO newUser) {
        User user = User.builder()
                .name(newUser.name())
                .dateOfBirth(newUser.dateOfBirth())
                .build();

        User savedUser = repository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

}
