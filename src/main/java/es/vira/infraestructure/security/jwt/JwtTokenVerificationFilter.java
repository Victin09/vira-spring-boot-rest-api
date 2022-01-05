package es.vira.infraestructure.security.jwt;

import es.vira.infraestructure.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
public class JwtTokenVerificationFilter extends OncePerRequestFilter {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtTokenVerificationFilter(JwtProperties jwtProperties, SecretKey secretKey) {
        this.jwtProperties = jwtProperties;
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(jwtProperties.getAuthorizationHeaderName());
        String tokenPrefix = jwtProperties.getTokenPrefix();
        if (authorizationHeader == null || authorizationHeader.isBlank() || !authorizationHeader.startsWith(tokenPrefix)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorizationHeader.replace(tokenPrefix, "");

        try {
            Claims body = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    body.getSubject(),
                    null,
                    getAuthorities((List<Map<String, String>>) body.get(SecurityConstants.AUTHORITIES)));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            String errorMessage = String.format("Token %s cannot be trust", token);
            LOGGER.error(errorMessage, ex);
            throw new AccessDeniedException(errorMessage, ex);
        }
    }

    private Set<SimpleGrantedAuthority> getAuthorities(List<Map<String, String>> authorities) {
        return authorities.stream()
                .map(m -> m.get(SecurityConstants.AUTHORITY))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
