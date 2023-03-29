package sahachan.template.auth.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sahachan.template.auth.security.entity.AuthorityName;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "UserDto")
public class UserDto {
    @Schema(description = "User id",required = true)
    Long id;

    @Schema(description = "User email",required = true)
    String email;

    @Schema(description = "User firstname",required = true)
    String firstname;

    @Schema(description = "User lastname",required = true)
    String lastname;

    @Schema(description = "User birthday",required = true)
    Date birthday;

    @Schema(description = "User Location",required = true)
    LocationDto location;

    @Schema(description = "User Role",required = true)
    AuthorityName authorities;
}
