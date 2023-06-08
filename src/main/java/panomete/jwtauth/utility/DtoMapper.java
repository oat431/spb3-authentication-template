package panomete.jwtauth.utility;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import panomete.jwtauth.security.entity.Users;
import panomete.jwtauth.security.payload.response.AuthDto;

@Mapper
public interface DtoMapper {
    DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

    /**
     * code of conduct for this method
     * EntityDto toEntityDto(Entity);
     * when it is used it will be something like this
     * DtoMapper.INSTANCE.toEntityDto(entity);
     * example:
     * UserDto toUserDto(User user);
     * DtoMapper.INSTANCE.toUserDto(user);
     */

    @Mappings({
            @Mapping(target = "id", expression = "java(user.getId().toString())"),
            @Mapping(target = "age", expression = "java(user.getAge())"),
            @Mapping(target = "name", expression = "java(user.getFullName())"),
            @Mapping(target = "role", expression = "java(user.getSimpleAuthorities())")
    })
    AuthDto toAuthDto(Users user);
}
