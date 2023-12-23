package com.security.practice.SecurityV2JWT.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
// @Builder sirve para construir despues los objetos
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    
    String username;
    String password;
    String firstname;
    String lastname;
    String country;
    
}
