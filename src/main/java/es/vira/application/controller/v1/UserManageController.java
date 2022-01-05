package es.vira.application.controller.v1;

import es.vira.application.dto.UserDto;
import es.vira.domain.enums.UserRole;
import es.vira.domain.service.UserService;
import es.vira.application.exception.UserNotFoundException;
import es.vira.application.exception.UsernameAlreadyExistsException;
import es.vira.infraestructure.security.SecurityConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(tags = "User Management")
@RestController
@RequestMapping("/api/v1/user")
public class UserManageController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Creates a new user.",
            notes = "Creates a new user by provided information. Returns the ID of created user. ")
    @RolesAllowed(SecurityConstants.ADMIN_ROLE)
    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE)
    public long createUser(@ApiParam(value = "The information about a new user.", required = true)
                           @RequestBody UserDto user) throws UsernameAlreadyExistsException {
        return userService.create(user).getId();
    }

    @ApiOperation(value = "Returns list of users.",
            notes = "Returns a list that contains information about existing users. " +
                    "Supports pagination. Uses pageNumber and pageSize to get the list. " +
                    "If pageSize isn't provided, will be used a default value.")
    @RolesAllowed(SecurityConstants.ADMIN_ROLE)
    @GetMapping(value = "/list", produces = APPLICATION_JSON_VALUE)
    public List<UserDto> getUsersList(@ApiParam(value = "The page number.", required = true, allowableValues = "range[0, infinity]")
                                                 @RequestParam Integer pageNumber,
                                                 @ApiParam(value = "The page size.", allowableValues = "range[1, 500]")
                                                 @RequestParam(required = false) Integer pageSize) {
        if (pageSize == null) {
            return userService.getList(pageNumber);
        }
        return userService.getList(pageNumber, pageSize);
    }

    @ApiOperation(value = "Returns user information by ID or by username.",
            notes = "Returns user information by ID or by username. " +
                    "Both ID and username parameters are not required, but at least one of them must be. " +
                    "If both ID and username were specified, ID will be used to get a user.")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public UserDto getUser(@ApiParam(value = "The ID of the user.", allowableValues = "range[1, infinity]")
                                      @RequestParam(required = false) Long id,
                                      @ApiParam(value = "The username of the user.", allowableValues = "range[1, infinity]")
                                      @RequestParam(required = false) String username,
                                      Authentication authentication) throws UserNotFoundException {
        if (id == null && username == null) {
            throw new IllegalArgumentException("Can't get a user when both id and username null. Specify id or username parameter.");
        }
        assertAccessToGetUser(id, username, authentication);
        return id == null ? userService.get(username) : userService.get(id);
    }

    private void assertAccessToGetUser(Long id, String requestedUsername, Authentication authentication) throws UserNotFoundException {
        UserDto user = userService.get((String) authentication.getPrincipal());
        if (user.getRole() == UserRole.ADMIN) {
            return;
        }
        Long currentUserId = user.getId();
        String username = user.getUsername();
        if ((id != null && !currentUserId.equals(id)) ||
                (requestedUsername != null && !username.equals(requestedUsername))) {
            throw new AccessDeniedException(
                    format("User with ID = %s and username = %s doesn't have permission to view other users information.",
                            currentUserId, username)
            );
        }
    }

    @ApiOperation(value = "Updates user information",
            notes = "Updates user information by user ID. Fields that can be updated: firstName, lastName, birth.")
    @PutMapping(value = "/updateInfo", consumes = APPLICATION_JSON_VALUE)
    public void updateUserInfo(@ApiParam(value = "The user information to update.", required = true)
                               @RequestBody UserDto user,
                               Authentication authentication) throws UserNotFoundException {
        assertAccessToModify(user.getId(), authentication);
        userService.updateInfo(user);
    }

    @ApiOperation(value = "Updates a user password.",
            notes = "Updates a user password by user ID.")
    @PutMapping(value = "/updatePassword", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void updateUserPassword(@ApiParam(value = "The ID of the user to update the password.", required = true, allowableValues = "range[1, infinity]")
                                   @RequestParam Long id,
                                   @ApiParam(value = "A new password", required = true)
                                   @RequestBody String newPassword,
                                   Authentication authentication) throws UserNotFoundException {
        assertAccessToModify(id, authentication);
        userService.updatePassword(id, newPassword);
    }

    private void assertAccessToModify(Long id, Authentication authentication) throws UserNotFoundException {
        UserDto user = userService.get((String) authentication.getPrincipal());
        if (user.getRole() == UserRole.ADMIN) {
            return;
        }
        Long currentUserId = user.getId();
        if (!currentUserId.equals(id)) {
            throw new AccessDeniedException(
                    format("User with ID = %s and username = %s doesn't have permission to modify other users information.",
                            currentUserId, user.getUsername())
            );
        }
    }

    @ApiOperation(value = "Deletes a user by ID.")
    @RolesAllowed(SecurityConstants.ADMIN_ROLE)
    @DeleteMapping(value = "/delete")
    public void deleteUser(@ApiParam(value = "The ID of the user to delete", required = true, allowableValues = "range[1, infinity]")
                           @RequestParam Long id) throws UserNotFoundException {
        userService.delete(id);
    }

    @ApiOperation(value = "Returns count of existing users.")
    @RolesAllowed(SecurityConstants.ADMIN_ROLE)
    @GetMapping(value = "/count", produces = APPLICATION_JSON_VALUE)
    public long getUsersCount() {
        return userService.getCount();
    }
}
