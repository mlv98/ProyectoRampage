// src/main/java/com/eep/controller/ProfileController.java
package com.eep.controller;

import com.eep.dto.ProfileForm;
import com.eep.entity.Usuario;
import com.eep.service.FileStorageService;
import com.eep.service.UsuarioService;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    public ProfileController(UsuarioService usuarioService,
                             PasswordEncoder passwordEncoder,
                             FileStorageService fileStorageService) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    }

    /**
     * Añade el usuario autenticado al modelo para Thymeleaf (acceso a avatarUrl, nombre, etc.)
     */
    @ModelAttribute("user")
    public Usuario addUserToModel(Principal principal) {
        return usuarioService.findByEmail(principal.getName()).orElse(null);
    }

    @GetMapping
    public String showForm(Model model, Principal principal) {
        Usuario usuario = usuarioService.findByEmail(principal.getName()).orElseThrow();
        ProfileForm form = ProfileForm.fromUsuario(usuario);
        model.addAttribute("profileForm", form);
        return "profile/form";
    }

    @PostMapping
    public String updateProfile(
            @Valid @ModelAttribute("profileForm") ProfileForm form,
            BindingResult errs,
            Principal principal,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatar,
            RedirectAttributes flash) {

        Usuario usuario = usuarioService.findByEmail(principal.getName()).orElseThrow();

        if (errs.hasErrors()) {
            return "profile/form";
        }

        // contraseña
        if (!form.getNewPassword().isBlank()) {
            if (!passwordEncoder.matches(form.getCurrentPassword(), usuario.getPassword())) {
                errs.rejectValue("currentPassword", "current.invalid", "La contraseña actual no coincide");
                return "profile/form";
            }
            usuario.setPassword(passwordEncoder.encode(form.getNewPassword()));
        }

        // campos básicos
        usuario.setNombre(form.getNombre());
        usuario.setApellido(form.getApellido());
        usuario.setEmail(form.getEmail());

        // avatar
        if (avatar != null && !avatar.isEmpty()) {
            String url = fileStorageService.storeFile(avatar);
            usuario.setAvatarUrl(url);
        }

        usuarioService.save(usuario);
        flash.addFlashAttribute("success", "Perfil actualizado correctamente");
        return "redirect:/profile";
    }
    
    
    
    
    
}
