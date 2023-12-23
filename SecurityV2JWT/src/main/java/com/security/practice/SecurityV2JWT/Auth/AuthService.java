package com.security.practice.SecurityV2JWT.Auth;

import com.security.practice.SecurityV2JWT.Jwt.JwtService;
import com.security.practice.SecurityV2JWT.User.Role;
import com.security.practice.SecurityV2JWT.User.User;
import com.security.practice.SecurityV2JWT.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        /*utilizamos el authenticationManager.authenticate para autenticar al usuario
        para ello le pasaremos por parametro el username y el password utilizando este
        metodo de spring UsernamePasswordAuthenticationToken
        */
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        /*luego generaremos el token, para ello necesitamos el UserDetails
          el cual utilizaremos para buscar el usuario intentando acceder via credenciales
        */
        UserDetails user = this.userRepository.findByUsername(request.getUsername()).orElseThrow();
        //generamos el token pasando el usuario con todos sus datos recuperados de la base de datos
        String token = this.jwtService.getToken(user);
        /*retornamos la respuesta y dentro de esta agregaremos el token*/
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        /* aqui utilizamos el patron de diseño builder*/
        User user = User.builder()
                .username(request.getUsername())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .country(request.getCountry())
                /*cuando se crea el usuario por primera vez siempre será
                de tipo USER0
                 */
                .role(Role.USER)
                .build();
        
        //lo persistimos a la base de datos
        this.userRepository.save(user);
        
        /*trabajaremos con el patron de diseño builder
        para la construccion del siguiente objeto
        */
        return AuthResponse.builder()
                .token(this.jwtService.getToken(user))
                .build();

    }

}
