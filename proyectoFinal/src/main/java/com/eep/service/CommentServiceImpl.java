package com.eep.service;

import com.eep.dto.CommentDto;
import com.eep.entity.Comment;
import com.eep.entity.Post;
import com.eep.entity.Usuario;
import com.eep.exception.ResourceNotFoundException;
import com.eep.repository.CommentRepository;
import com.eep.repository.PostRepository;
import com.eep.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepo;
    private final UsuarioRepository userRepo;
    private final CommentRepository commentRepo;

    public CommentServiceImpl(PostRepository postRepo,
                              UsuarioRepository userRepo,
                              CommentRepository commentRepo) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.commentRepo = commentRepo;
    }

    @Override
    @Transactional
    public void addComment(Long postId, String username, CommentDto dto) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado"));
        Usuario user = userRepo.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Comment c = new Comment();
        c.setText(dto.getText());
        c.setAuthor(user);
        c.setPost(post);
        commentRepo.save(c);
        // opcionalmente añadir a post.getComments().add(c);
    }
}
