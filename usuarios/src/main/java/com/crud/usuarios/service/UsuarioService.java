package com.crud.usuarios.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import com.crud.usuarios.dto.UsuarioDTO;
import com.crud.usuarios.enity.Usuario;
import com.crud.usuarios.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> getUsuarios() throws ServiceException {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario createUsuario(UsuarioDTO usuario) throws ServiceException{
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(usuario.getNombre());
        nuevoUsuario.setApellido(usuario.getApellido());
        nuevoUsuario.setEmail(usuario.getEmail());
        usuarioRepository.save(nuevoUsuario);
        return nuevoUsuario;
    }

    public Usuario findByEmail(String email) throws ServiceException{
        return usuarioRepository.findByEmail(email);
    }
    
    @Transactional
    public void deleteUsuarioByEmail(String email) throws ServiceException{
        usuarioRepository.deleteByEmail(email);
    }

    public Optional<Usuario> findById(UUID id) throws ServiceException{
        return usuarioRepository.findById(id);
    }


}
