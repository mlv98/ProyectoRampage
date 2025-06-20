package com.eep.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.eep.dto.UsuarioRegistroDto;
import com.eep.entity.Usuario;

public interface UsuarioService extends UserDetailsService{

	public Usuario guardar(UsuarioRegistroDto registroDto);
	
	 // Para actualizar perfil existente
    Usuario save(Usuario usuario);
    Optional<Usuario> findByEmail(String email);

}
