package com.cesde.educlic.service;

import com.cesde.educlic.dto.UsuarioDto;
import com.cesde.educlic.exception.BusinessException;
import com.cesde.educlic.model.entity.Usuario;
import com.cesde.educlic.model.enums.Rol;
import com.cesde.educlic.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Usuario registrar(UsuarioDto dto) {
        // El registro público siempre crea clientes: el rol nunca lo define quien se registra.
        dto.setRol(Rol.CLIENTE);
        return usuarioService.guardarUsuario(dto);
    }

    public Usuario login(String email, String password) {
        return usuarioRepository.findByEmailIgnoreCase(email == null ? "" : email.trim())
                .filter(usuario -> usuario.getPassword().equals(password))
                .orElseThrow(() -> new BusinessException("Credenciales inválidas"));
    }
}
