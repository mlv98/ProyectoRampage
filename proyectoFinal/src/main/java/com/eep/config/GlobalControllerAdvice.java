// src/main/java/com/eep/config/GlobalControllerAdvice.java
package com.eep.config;

import com.eep.entity.Usuario;
import com.eep.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UsuarioService usuarioService;

    public GlobalControllerAdvice(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @ModelAttribute("user")
    public Usuario addUserToModel(Principal principal) {
        if (principal == null) return null;
        return usuarioService.findByEmail(principal.getName()).orElse(null);
    }
}
