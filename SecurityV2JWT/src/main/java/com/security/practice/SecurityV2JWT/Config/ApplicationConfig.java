package com.security.practice.SecurityV2JWT.Config;

import com.security.practice.SecurityV2JWT.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    /* crearemos metodos que le permitan al manager acceder a las instancias */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        /* aqui podremos acceder a la instancia y retornarla*/
        return config.getAuthenticationManager();
    }

    /*aqui crearemos un metodo que nos devuelva el provider*/
    @Bean
    public AuthenticationProvider authenticationProvider() {
        //el provider con el que trabajaremos sera DaoAuthenticationProvider
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        //seteamos el userDetailsService
        authenticationProvider.setUserDetailsService(userDetailsService());
        //seteamos el password encoder
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //retornamos una nueva instancia sin pasar ningun parametro
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        /* aqui trabajaremos con expresiones lambda para poder acceder al username
        asi que buscaremos el username y si no existe lanzaremos una excepcion
         */
        return username -> this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
