package vea.home.microservice.services;

import org.springframework.stereotype.Service;
import vea.home.microservice.entities.User;
import vea.home.microservice.repositories.UserRepository;

import java.util.Optional;

@Service
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(r -> new UserDTO(r.getId(), r.getFirstName(), r.getLastName()));

    }

    @Override
    public UserDTO save(UserDTO user) {
        User userForSave = User.builder()
                .firstName(user.firstName())
                .lastName(user.lastName())
                .build();

        User savedUser = userRepository.save(userForSave);

        return new UserDTO(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName());

    }
}
