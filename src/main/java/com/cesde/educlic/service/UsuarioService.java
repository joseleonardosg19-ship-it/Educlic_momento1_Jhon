package com.cesde.educlic.service;

import com.cesde.educlic.dto.UsuarioDto;
import com.cesde.educlic.exception.BusinessException;
import com.cesde.educlic.model.entity.Usuario;
import com.cesde.educlic.model.enums.Rol;
import com.cesde.educlic.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    private static final int MAX_NOMBRE = 50;
    private static final int MIN_PASSWORD = 6;
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El usuario con ID " + id + " no existe."));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No se encontró ningún usuario registrado con el correo: " + email));
    }

    public Usuario guardarUsuario(UsuarioDto dto) {
        String nombre = dto.getNombre() == null ? "" : dto.getNombre().trim();
        String email = dto.getEmail() == null ? "" : dto.getEmail().trim().toLowerCase(Locale.ROOT);

        if (nombre.isEmpty()) {
            throw new BusinessException("El nombre es obligatorio.");
        }
        if (nombre.length() > MAX_NOMBRE) {
            throw new BusinessException("El nombre no puede superar los " + MAX_NOMBRE + " caracteres.");
        }
        if (!EMAIL.matcher(email).matches()) {
            throw new BusinessException("El correo electrónico no es válido.");
        }
        if (dto.getPassword() == null || dto.getPassword().length() < MIN_PASSWORD) {
            throw new BusinessException("La contraseña debe tener al menos " + MIN_PASSWORD + " caracteres.");
        }
        if (usuarioRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new BusinessException("Ya existe un usuario registrado con el correo: " + email);
        }

        Usuario usuario = Usuario.builder()
                .nombre(nombre)
                .email(email)
                .password(dto.getPassword())
                .rol(dto.getRol() != null ? dto.getRol() : Rol.CLIENTE)
                .build();

        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(Long id, UsuarioDto dto) {
        Usuario usuarioExistente = buscarPorId(id);

        usuarioExistente.setNombre(dto.getNombre());
        usuarioExistente.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuarioExistente.setPassword(dto.getPassword());
        }

        if (dto.getRol() != null) {
            usuarioExistente.setRol(dto.getRol());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public void eliminarUsuario(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }
}
