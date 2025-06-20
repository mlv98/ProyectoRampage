package com.eep.service;

import com.eep.dto.ProfileForm;
import com.eep.entity.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {
    private final UsuarioService usuarioService;
    private final FileStorageService storageService;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(UsuarioService usuarioService,
                          FileStorageService storageService,
                          PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.storageService = storageService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void updateProfile(String email, ProfileForm form) {
        Usuario u = usuarioService.findByEmail(email).orElseThrow();
        u.setNombre(form.getNombre());
        u.setApellido(form.getApellido());
        u.setEmail(form.getEmail());

        if(form.getNewPassword() != null && !form.getNewPassword().isBlank()) {
            if(!passwordEncoder.matches(form.getCurrentPassword(), u.getPassword())) {
                throw new RuntimeException("Contraseña actual incorrecta");
            }
            u.setPassword(passwordEncoder.encode(form.getNewPassword()));
        }

        if(form.getAvatar() != null && !form.getAvatar().isEmpty()) {
            String url = storageService.storeFile(form.getAvatar());
            u.setAvatarUrl(url);
        }
        usuarioService.save(u);
    }
}