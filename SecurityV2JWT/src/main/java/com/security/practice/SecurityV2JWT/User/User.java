package com.security.practice.SecurityV2JWT.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Collection;
import java.util.List;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
/* uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})}
sirve para indicar que un campo en especifico no se puede repetir el mismo valor
*/
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
/* implements UserDetails es necesario para poder trabajar con la autenticacion*/
public class User implements UserDetails {

    @Id
    @GeneratedValue
    Integer id;
    /*sirve para indicar que todos los insert que se hagan deben tener un valor
    como username no puede estar vacio de lo contrario dará error
    */
    @Column(nullable = false)
    String username;
    String lastname;
    String firstname;
    String country;
    String password;
    /* @Enumerated(EnumType.STRING) sirve para especificar que el valor con el cual
    se mapeará a la base de datos sera de tipo String en lugar de ordinal
    */
    @Enumerated(EnumType.STRING)
    Role role;
    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((role.name())));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
