package es.vira.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import es.vira.common.enums.UserRole;

@Schema(name = "User data transfer object")
@Data
@NoArgsConstructor
public class UserDataDTO {

    private String username;
    private String email;
    private String password;
    List<UserRole> roles;

}
