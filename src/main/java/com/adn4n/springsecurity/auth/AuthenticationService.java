package com.adn4n.springsecurity.auth;


import com.adn4n.springsecurity.config.JwtService;
import com.adn4n.springsecurity.token.Token;
import com.adn4n.springsecurity.token.TokenRepo;
import com.adn4n.springsecurity.token.TokenType;
import com.adn4n.springsecurity.user.Role;
import com.adn4n.springsecurity.user.User;
import com.adn4n.springsecurity.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepository;
    private final TokenRepo tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResposne register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResposne.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
    }

    public AuthenticationResposne authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(user,jwtToken);
        return AuthenticationResposne.builder()
                .token(jwtToken)
                .build();
    }
}
