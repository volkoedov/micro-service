package vea.home.microservice.services;

import org.mapstruct.Mapper;
import vea.home.microservice.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDTO dto);

    UserDTO toDTO(User entity);

}
