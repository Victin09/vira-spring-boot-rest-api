package es.vira.domain.model;

import es.vira.domain.enums.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Schema(name = "User Model",
        description = "Information about a user")
@Entity(name = "vira_user")
@Builder
@Getter @Setter
@ToString(exclude = {"password", "encryptedPassword"})
@EqualsAndHashCode()
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Column(unique = true)
    @Size(min = 3, max = 30)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String username;
    @NotBlank
    @Size(min = 3, max = 68)
    private String encryptedPassword;
    @Transient
    private String password;
    @NotBlank
    @Size(min = 3, max = 30)
    private String firstName;
    @NotBlank
    @Size(min = 3, max = 30)
    private String lastName;
    @NotNull
    private UserRole role;
    @Version
    private Integer version;
}
