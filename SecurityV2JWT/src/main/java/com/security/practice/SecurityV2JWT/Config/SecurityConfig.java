package com.security.practice.SecurityV2JWT.Config;

import com.security.practice.SecurityV2JWT.Jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
//import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/* @Configuration sirve para definir la clase como una de configuracion por lo tanto
tendra metodos marcados con @Bean los cuales seran necesarios para crear
los objetos que vamos a requerir en nuestra app
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        return http
                /* deshabilitamos la proteccion csrf que habilita por 
                defecto springboot
                */
                /* csrf significa Cross-Site Request Forgery es una medida de seguridad
                que se utiliza para agregar a las solicitudes post una autenticacion basada
                en un token csrf valido pero unicamente es necesario dejarlo habilitado cuando
                se va a trabajar con el, de resto nos podria dar problemas al realizar las peticiones POST
                */
                .csrf(csrf ->
                csrf
                .disable())
                //expresion lambda
                .authorizeHttpRequests(authRequest
                        -> authRequest
                        /* indica si la ruta concuerda con la especificada les per
                        mitira el acceso
                         */
                        .requestMatchers("/auth/**").permitAll()
                        /*cualquier otro request diferente al de arriba
                                deberá autenticarse
                         */
                        .anyRequest().authenticated()
                //llamamos al formulario de login que nos provee spring security
                        /*formLogin(withDefaults() es una autenticacion propia de spring
                        security
                        */
                )/*formLogin(withDefaults())*/
                /* ahora trabajaremos con una autenticacion propia de JWT*/
                /*Primero vamos a inhabilitar las sessiones para ello utilizaremos
                expresiones lambda
                */
                .sessionManagement(sessionManager ->
                sessionManager
                        //especificamos la politica de creacion de sessiones
                        /*SessionCreationPolicy.STATELESS Hace referencia a que no
                        las utilizará
                        */
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //especificamos el authenticationProvider
                .authenticationProvider(authProvider)
                /*agregamos el filtro relacionado a jwtAuthenticationFilter*/
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
                
    }

}
