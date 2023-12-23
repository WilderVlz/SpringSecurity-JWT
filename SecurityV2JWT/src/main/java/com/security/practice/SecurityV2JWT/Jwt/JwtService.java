package com.security.practice.SecurityV2JWT.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";

    //aqui estamos trabajando con el UserDetails de spring
    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user);
    }

    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        /*para generar nuestro token trabajaremos con la libreria Jwts*/
        return Jwts
                //construimos el objetos
                .builder()
                /* seteamos los claims que recibiremos
                por parametros
                 */
                .setClaims(extraClaims)
                //seteamos el username
                .setSubject(user.getUsername())
                //seteamos la fecha en que se crea 
                .setIssuedAt(new Date(System.currentTimeMillis()))
                /* seteamos la fecha de expiracion en un dia*/
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                //seteamos la firma
                /*SignatureAlgorithm.HS256 sirve para definir el algoritmo
                de encriptacion que vamos a utilizar para trabajar la clave 
                secreta 
                 */
                .signWith(getKey(), SignatureAlgorithm.HS256)
                /*llamamos al metodo compact() para que cree el objeto 
                y lo serialice
                 */
                .compact();

    }

    private Key getKey() {
        /*aqui pasaremos nuestra secret_key que esta en string a base64 para asi
        poder mandarla como key a la firma de nuestro token
         */
 /*para ello trabajaremos con un array de byte en el cual decodificaremos nuestra
        secret_key
         */
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        /*hmacShaKeyFor nos permitira crear una nueva instancia de nuestra secret_key*/
        return Keys.hmacShaKeyFor(keyBytes);

    }

    String getUsernameFromToken(String token) {
        /* aqui paremos por parametros el token y el claim en particular
        que en este caso seria el Subject Claims::getSubject ya que aqui es donde 
        tendremos alojado el username
        */
        return getClaim(token, Claims::getSubject);
    }

    /* primero verificaremos si el usuario concuerda con el de nuestro userDetails
    
    */
    boolean isTokenValid(String token, UserDetails userDetails) {
        //obtenemos el username del token
        final String username = getUsernameFromToken(token);
        //retornamos true si ambas validaciones se cumplen
        return(username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //este metodo va a obtener todos los claims del token
    private Claims getAllClaims(String token) {
        /* la libreria de Jwts nos permitira
        acceder a los claims
         */
        return Jwts
                //creamos un parser
                .parserBuilder()
                //especificamos la clave secreta para la firma
                .setSigningKey(getKey())
                //construimos el parser
                .build()
                /*seteamos el parseClaimsJws pasandole el token para
                que lo analice
                 */
                .parseClaimsJws(token)
                //obtenemos el cuerpo
                .getBody();
    }
    
    /* creamos un metodo publico genereico el cual nos permitira
    obtener un claim en particular
    */
    /* Le pasaremos por parametros el token y una funcion
    que especificara el claim y el tipo de datos
    */
    public <T> T getClaim(String token, Function<Claims,T> claimsResolver) {
        //obtenemos todos los claims
        final Claims claims = getAllClaims(token);
        //aplicamos la funcion y retornamos el resultado
        return claimsResolver.apply(claims);
        
    }
    //sirve para obtener la fecha de expiracion del token
    private Date getExpiration(String token) {
        //aqui accedemos a la expiracion
        return getClaim(token, Claims::getExpiration);
    }
    
    private boolean isTokenExpired(String token) {
        //el metodo before nos permitira saber si el token a expirado
        return getExpiration(token).before(new Date());
    }

}
