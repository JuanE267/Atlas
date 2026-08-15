package com.juanespinosa.atlas.auth;

import com.juanespinosa.atlas.auth.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {


    // Key for each token, makes a JWT verificable
    private final SecretKey key = Keys.hmacShaKeyFor(
            "esta-es-una-clave-secreta-de-desarrollo-cambiar-en-produccion-32chars".getBytes()
    );

    // expires in 24hs
    private final long expirationMs = 1000 * 60 * 60 * 24; // 24 horas

    // Generates a Token wiht email as the owner, role and id, set expiration and ends signed with the SecretKey
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name())
                .claim("userId", user.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }
}