package es.vira.application.controller;

import es.vira.infraestructure.security.jwt.UsernameAndPasswordAuthenticationRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(tags = "Authorization")
@RestController
@RequestMapping("/api")
public class AuthorizationController {

    @ApiOperation(value = "Authorization endpoint.",
            notes = "Returns Authorization header with a JWT in case of successful authorization.")
    @PostMapping(value = "/getToken", consumes = APPLICATION_JSON_VALUE)
    public void getToken(@ApiParam(value = "Username and password to authorize.", required = true)
                         @RequestBody UsernameAndPasswordAuthenticationRequest authenticationRequest) {
    }
}
