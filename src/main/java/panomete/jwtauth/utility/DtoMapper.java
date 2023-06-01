package panomete.jwtauth.utility;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DtoMapper {
    DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

    /**
     * code of conduct for this method
     * EntityDto toEntityDto(Entity entity);
     * when it is used it will be something like this
     * DtoMapper.INSTANCE.toEntityDto(entity);
     * example:
     * UserDto toUserDto(User user);
     * DtoMapper.INSTANCE.toUserDto(user);
     */
}
