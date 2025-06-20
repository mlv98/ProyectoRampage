package com.eep.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentDto {
    @NotBlank
    private String text;
    // getter/setter

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
    
    
    
}
