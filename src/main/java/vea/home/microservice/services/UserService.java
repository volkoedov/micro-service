package vea.home.microservice.services;

import java.util.Optional;


public interface UserService {

    Optional<UserDTO> getUserById(Long id);

    UserDTO save(UserDTO user);
}
