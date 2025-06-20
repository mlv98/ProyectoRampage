package com.eep.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsuarioRegistroDto {

	private Long id;

	@NotBlank(message = "El nombre es obligatorio.")
	private String nombre;

	@NotBlank(message = "El apellido es obligatorio.")
	private String apellido;

	@NotBlank(message = "El email es obligatorio.")
	@Email(message = "El correo electrónico debe ser válido.")
	private String email;

	@NotBlank(message = "La contraseña es obligatoria.")
	private String password;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UsuarioRegistroDto(Long id, String nombre, String apellido, String email, String password) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.password = password;
	}

	public UsuarioRegistroDto(String nombre, String apellido, String email, String password) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.password = password;
	}

	public UsuarioRegistroDto(String email) {
		super();
		this.email = email;
	}

	public UsuarioRegistroDto() {
		super();
	}

}