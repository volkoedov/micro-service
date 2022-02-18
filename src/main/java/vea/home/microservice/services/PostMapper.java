package vea.home.microservice.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import vea.home.microservice.entities.Post;
import vea.home.microservice.entities.User;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface PostMapper {
    @Mapping(target ="user", ignore = true )
    Post toEntity(PostDTO dto);

    @Mapping(source = "user",target ="user" )
    @Mapping(source = "dto.version",target ="version" )
    @Mapping(source = "dto.id",target ="id" )
    Post toEntityWithUser(PostDTO dto, User user);

    PostDTO toDTO(Post entity);
}
