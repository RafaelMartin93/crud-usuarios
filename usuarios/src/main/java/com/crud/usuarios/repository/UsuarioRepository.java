package com.crud.usuarios.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crud.usuarios.enity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository <Usuario, UUID> {

    public Usuario findByEmail(String email);

}
