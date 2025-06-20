package com.eep.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public class PostDto {
    private Long id;

    @NotBlank(message="El título es obligatorio")
    private String title;

    @NotBlank(message="El contenido no puede estar vacío")
    private String content;

    // para subida de imagen
    private MultipartFile image;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

    // getters y setters...
    
    
    
}
