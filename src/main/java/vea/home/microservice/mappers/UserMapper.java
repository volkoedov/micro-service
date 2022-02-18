package vea.home.microservice.mappers;

import org.mapstruct.Mapper;
import vea.home.microservice.entities.User;
import vea.home.microservice.services.UserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDTO dto);

    UserDTO toDTO(User entity);

}
