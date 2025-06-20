package com.eep.controller;

import com.eep.dto.CommentDto;
import com.eep.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Método para procesar el envío de un nuevo comentario en un post.
     * 
     * @param postId       ID del post al que se añade el comentario
     * @param commentDto   DTO con el contenido del comentario
     * @param bindingResult Resultado de la validación del DTO
     * @param principal    Usuario autenticado
     * @return Redirección a la lista de posts (o detalle de post)
     */
    @PostMapping
    public String addComment(
            @PathVariable Long postId,
            @Valid @ModelAttribute("commentDto") CommentDto commentDto,
            BindingResult bindingResult,
            Principal principal
    ) {
        if (bindingResult.hasErrors()) {
            
            return "redirect:/posts?page=0";
        }
        
        commentService.addComment(postId, principal.getName(), commentDto);
       
        return "redirect:/posts";
    }
}
