package com.security.practice.SecurityV2JWT.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
/*sirve para inicializar todos los objetos de la clase dentro del constructor
asi podremos trabajar con las constantes de manera efectiva
*/
@RequiredArgsConstructor
/* OncePerRequestFilter esta clase abstracta se utiliza para crear
filtros personalizados la razon de extender de esta clase es para 
que el filtro se ejecute solo una vez por cada solicitud HTTP incluso si hay
multiples filtros dentro de la cadena de filtos
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    /*este metodo va a realizar todos los filtros relacionador al token*/
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String token = getTokenFromRequest(request);

        final String username;

        /* si el filterChain es igual a nulo le vamos a devolver a el filterChain el control
        y lo retornamos
         */
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        // obtenemos el nombre de usuario mediante el token
        username = this.jwtService.getUsernameFromToken(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //es para buscarlo en la base de datos
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            //validamos si el token es valido
            if (this.jwtService.isTokenValid(token, userDetails)) {
                //actualizamos el SecurityContextHolder
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // seteamos el details
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //Seteamos el contexto en el securityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        //Obtenemos el item de autenticacion del encabezado o header
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        /* verificamos si el authHeader contiene algo y que empiece con la palabra Bearer
        
         */
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            /* si se cumple retornaremos apartir del caracter 7 para asi evitar
            enviar la palabra 'Bearer'
             */
            return authHeader.substring(7);
        }
        // de lo contrario retornaremos null
        return null;
    }

}
