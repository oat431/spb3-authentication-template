package panomete.jwtauth.utility;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DtoMapper {
    DtoMapper CONVERT = Mappers.getMapper(DtoMapper.class);

    /**
     * code of conduct for this method
     * EntityDto toEntityDto(Entity entity);
     * when it is used it will be something like this
     * DtoMapper.CONVERT.toEntityDto(entity);
     */
}
