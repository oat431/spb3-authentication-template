package sahachan.template.auth.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import sahachan.template.auth.security.dto.UserDto;
import sahachan.template.auth.security.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(imports = Collectors.class)
public interface BackendMapper {
    BackendMapper INSTANCE = Mappers.getMapper(BackendMapper.class);

    @Mappings({
            @Mapping(target = "authorities", expression = "java(user.getAuthorities().get(0).getName())"),
    })
    UserDto getUserDto(User user);
    List<UserDto> getUserDto(List<User> user);

}
