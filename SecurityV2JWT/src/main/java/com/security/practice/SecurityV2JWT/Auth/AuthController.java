package com.security.practice.SecurityV2JWT.Auth;

import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
/*sirve para hacer obligatorio que se agregue el constructor
con todos los elementos
 */
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping(value = "login")
    /*ResponseEntity<?> representa todas las respuestas HTTP va a incluir los
    codigos de estado, encabezados y el cuerpo de respuesta 
    */
    /*aqui estariamos solicitando las credenciales @RequestBody LoginRequest request*/
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // new AuthResponse() retorna un token
        return ResponseEntity.ok(this.authService.login(request));
    }

    @PostMapping(value = "register")
    /*ResponseEntity<?> representa todas las respuestas HTTP va a incluir los
    codigos de estado, encabezados y el cuerpo de respuesta 
    */
    /*aqui estariamos solicitando las credenciales @RequestBody LoginRequest request*/
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        // new AuthResponse() retorna un token
        return ResponseEntity.ok(this.authService.register(request));
    }
}
