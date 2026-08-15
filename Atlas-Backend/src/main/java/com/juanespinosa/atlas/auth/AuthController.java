package com.juanespinosa.atlas.auth;

import com.juanespinosa.atlas.auth.user.Role;
import com.juanespinosa.atlas.auth.user.User;
import com.juanespinosa.atlas.auth.user.UserRepository;
import com.juanespinosa.atlas.auth.user.UserResponse;
import com.juanespinosa.atlas.exception.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    // registers an user by using the data of the request, encoding the password and setting the role
    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.STUDENT);

        User userSaved = userRepository.save(user);

        return new UserResponse(userSaved.getId(), userSaved.getEmail(), userSaved.getRole());
    }

    // verificates that the email exists in the db, if the passwords matches, generates a token for authentication
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponse(token);
    }
}