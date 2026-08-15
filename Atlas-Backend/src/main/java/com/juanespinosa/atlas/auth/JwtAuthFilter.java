package com.juanespinosa.atlas.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

@Component
// --- OncePerRequestFilter makes the filter work in every http request
public class JwtAuthFilter extends OncePerRequestFilter {

    private final SecretKey key = Keys.hmacShaKeyFor(
            "esta-es-una-clave-secreta-de-desarrollo-cambiar-en-produccion-32chars".getBytes()
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // if theres no header we let SecurityConfig handle it
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }


        // takes out "bearer " from the token
        String token = authHeader.substring(7);

        try {

            // verifies if the token matches the secret key
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // retrieves data from the token without consulting the database
            String email = claims.getSubject();
            String role = claims.get("role", String.class);


            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
            var authToken = new UsernamePasswordAuthenticationToken(email, null, authorities);

            // sets user as authenticated user for the rest of the request
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (Exception e) {
            System.out.println("Error validando JWT: " + e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}