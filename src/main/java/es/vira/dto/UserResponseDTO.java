package es.vira.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import es.vira.common.enums.UserRole;

@Schema(name = "User response data transfer object")
@Data
public class UserResponseDTO {

    private String id;
    private String username;
    private String email;
    List<UserRole> roles;

}
