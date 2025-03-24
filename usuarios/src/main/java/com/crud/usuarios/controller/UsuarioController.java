package com.crud.usuarios.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.usuarios.dto.UsuarioDTO;
import com.crud.usuarios.enity.Usuario;
import com.crud.usuarios.service.UsuarioService;
import com.crud.usuarios.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> getUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            usuarios = usuarioService.getUsuarios();
        } catch (ServiceException e) {
            log.error("Error al obtener los usuarios ", e.getMessage());
        }
        return usuarios;
    }

    @PostMapping
    public ResponseEntity<?> createUsuario(@RequestBody UsuarioDTO usuario) {
        try {
            if (!Utils.isValidEmail(usuario.getEmail()))
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("El email introducido no es correcto " + usuario.getEmail());

            Usuario existeUsuario = usuarioService.findByEmail(usuario.getEmail());
            if (existeUsuario == null) {
                Usuario nuevoUsuario = usuarioService.createUsuario(usuario);
                return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
            } else {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("El usuario con email " + usuario.getEmail() + " ya existe");
            }

        } catch (IllegalArgumentException e) {
            Map<String, String> error = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/byEmail")
    public Usuario getUsuarioByEmail(@RequestBody String email) {
        Usuario usuario = new Usuario();
        usuario = usuarioService.findByEmail(email);
        return usuario;
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUsuario(@RequestBody String jsonBody) {
        String email = Utils.extractEmailWithGson(jsonBody);
        Usuario existeUsuario = usuarioService.findByEmail(email);
        if (existeUsuario == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        usuarioService.deleteUsuarioByEmail(existeUsuario.getEmail());
        return ResponseEntity.ok("¡Usuario eliminado exitosamente!");
    }

}
