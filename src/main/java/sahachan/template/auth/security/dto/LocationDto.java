package sahachan.template.auth.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "LocationDto", description = "Location Object Response")
public class LocationDto {
    @Schema(description = "SubDistrict", example = "สุเทพ", required = true)
    String subDistrict;

    @Schema(description = "District", example = "เมืองเชียงใหม่", required = true)
    String district;

    @Schema(description = "Province", example = "เชียงใหม่", required = true)
    String province;
}
