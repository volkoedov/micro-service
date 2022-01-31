package vea.home.microservice.controllers;

import org.springframework.web.bind.annotation.*;
import vea.home.microservice.services.UserDTO;
import vea.home.microservice.services.UserService;

import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        Optional<UserDTO> userById = userService.getUserById(id);
        return userById.orElseThrow(() -> new IllegalStateException(String.format("Не удалось найти польователя с ID = %s", id)));
    }

    @PostMapping
    public UserDTO newUser(@RequestBody UserDTO newUser){
        return userService.save(newUser);
    }

}
