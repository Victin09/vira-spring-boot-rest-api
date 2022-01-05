package es.vira.application.controller;

import es.vira.infraestructure.security.jwt.UsernameAndPasswordAuthenticationRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Authorization")
@RestController
@RequestMapping("/api")
public class AuthorizationController {

    @Operation(summary = "Authorization endpoint.",
            description = "Returns Authorization header with a JWT in case of successful authorization.")
    @PostMapping(value = "/getToken", consumes = APPLICATION_JSON_VALUE)
    public void getToken(@Parameter(description = "Username and password to authorize.", required = true)
                         @RequestBody UsernameAndPasswordAuthenticationRequest authenticationRequest) {
    }
}
