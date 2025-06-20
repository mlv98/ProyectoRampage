package com.eep.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ProfileForm {

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @Email @NotBlank
    private String email;

    private String currentPassword = "";

    private String newPassword = "";
    
 // Para subida de imagen
    private MultipartFile avatar;

    // getters & setters
    
    

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    public MultipartFile getAvatar() { return avatar; }
    public void setAvatar(MultipartFile avatar) { 
    	this.avatar = avatar; 
    }

    /** Mapea datos del Usuario a este formulario */
    public static ProfileForm fromUsuario(com.eep.entity.Usuario u) {
        ProfileForm f = new ProfileForm();
        f.setNombre(u.getNombre());
        f.setApellido(u.getApellido());
        f.setEmail(u.getEmail());
        return f;
    }
}
