package com.security.practice.SecurityV2JWT.User;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/*JpaRepository<User, Integer> se utiliza para persistir los datos a la base de datos
User hace referencia a la clase de nuestro modelo, Integer hace referencia al tipo de dato
que estamos utilizando como identificador
*/
public interface UserRepository extends JpaRepository<User, Integer> {
    /* se utiliza para poder arrojar excepciones en caso de que sea nulo*/
    Optional<User> findByUsername(String username);
    
}
