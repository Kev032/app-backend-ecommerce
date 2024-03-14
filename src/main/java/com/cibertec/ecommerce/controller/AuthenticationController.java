package com.cibertec.ecommerce.controller;

import com.cibertec.ecommerce.model.Response;
import com.cibertec.ecommerce.model.User;
import com.cibertec.ecommerce.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/auth")
public class AuthenticationController {

    @Autowired
    private IUserService usuarioService;

    BCryptPasswordEncoder passEncode = new BCryptPasswordEncoder();

    private Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/login")
    public ResponseEntity<Response> login(@RequestBody User usuario, HttpSession session) {

        log.info("Accesos : {}", usuario);
        Optional<User> user = usuarioService.findByEmail(usuario.getEmail());

        if (user.isPresent()) {
            if (passEncode.matches(usuario.getPassword(), user.get().getPassword())) {
                log.info("Usuario existe: {}", user.get().getType());
                session.setAttribute("idusuario", user.get().getId());
                return ResponseEntity.status(HttpStatus.OK).body(new Response(user.get(), "Validación de Login exitoso."));
            } else {
                log.info("Usuario o Password  incorrecto.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Usuario o Password  incorrecto."));
            }
        } else {
            log.info("Usuario no existe");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("El usuario [" + usuario.getEmail() + "] no existe."));
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(path = "/register")
    public ResponseEntity<Response> register(@RequestBody User usuario) {
        log.info("Usuario registro: {}", usuario);


        Optional<User> user = usuarioService.findByEmail(usuario.getEmail());

        if (user.isPresent()) {
            log.info("Usuario existe: {}", user.get().getId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("El e-mail [" + usuario.getEmail() + "] ya está registrado."));
        } else {
            log.info("Usuario no existe");
            usuario.setType("Comprador");
            usuario.setPassword(passEncode.encode(usuario.getPassword()));
            usuario.setName(usuario.getName());
            usuario.setLastName(usuario.getLastName());
            usuario.setAddress(usuario.getAddress());
            usuario.setPhone(usuario.getPhone());
            usuario.setEmail(usuario.getEmail());
            Optional<User> newUser = Optional.ofNullable(usuarioService.save(usuario));
            if (newUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new Response(newUser, "Se registró correctamente al usuario."));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response("No se pudo registrar al usuario."));
            }
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping(path = "/update")
    public ResponseEntity<Response> updateUser(@RequestBody User usuario) {
        log.info("Usuario para actualizar: {}", usuario);
        Optional<User> user = usuarioService.findById(usuario.getId());
        if (user.isPresent()) {
            log.info("Usuario existe: {}", user.get().getId());
            usuario.setType(user.get().getType());
            usuario.setPassword(passEncode.encode(usuario.getPassword()));
            log.info("Usuario para actualizar: {}", usuario);
            Optional<User> userUpdate = Optional.ofNullable(usuarioService.save(usuario));
            log.info("Usuario actualizado: {}", userUpdate);
            userUpdate.get().setPassword("");
            return ResponseEntity.status(HttpStatus.OK).body(new Response(userUpdate, "Se actualizó correctamente al usuario."));
        } else {
            log.info("Usuario no existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("No se pudo actualizar al usuario."));
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path = "/logout")
    public ResponseEntity<Response> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).body(new Response("Se cerró sesión correctamente."));
    }

}
