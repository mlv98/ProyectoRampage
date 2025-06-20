package com.eep.service;

import com.eep.dto.PostDto;
import com.eep.entity.Comment;
import com.eep.entity.Media;
import com.eep.entity.Post;
import com.eep.entity.Usuario;
import com.eep.exception.ResourceNotFoundException;
import com.eep.repository.CommentRepository;
import com.eep.repository.PostRepository;
import com.eep.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepo;
    private final UsuarioRepository userRepo;
    private final Path uploadDir;
    private final CommentRepository commentRepository; 

    public PostServiceImpl(PostRepository postRepo,
                           UsuarioRepository userRepo,
                           CommentRepository commentRepository,
                           @Value("${app.upload-dir}") String uploadPath) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.commentRepository = commentRepository;
        this.uploadDir = Paths.get(uploadPath);
    }

    @PostConstruct
    public void init() {
        try {
            if (Files.notExists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear carpeta de uploads: " + uploadDir, e);
        }
    }

    @Override
    public Page<Post> listAll(Pageable pageable) {
        return postRepo.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public Post getById(Long id) {
        return postRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id " + id));
    }

    @Override
    @Transactional
    public Post create(PostDto dto, String username, List<MultipartFile> files) {
        Usuario author = userRepo.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado " + username));

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setAuthor(author);

        
        post = postRepo.save(post);

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path dest = uploadDir.resolve(filename);
            try {
                file.transferTo(dest.toFile());
            } catch (IOException ex) {
                throw new RuntimeException("Error guardando archivo " + file.getOriginalFilename(), ex);
            }

            Media m = new Media();
            m.setType(
                file.getContentType().startsWith("video")
                    ? Media.Type.VIDEO
                    : Media.Type.IMAGE
            );
            // La URL pública bajo /uploads/**
            m.setPath("/uploads/" + filename);
            m.setPost(post);
            post.getMedia().add(m);
        }

        return postRepo.save(post);
    }

    @Override
    @Transactional
    public void delete(Long id, String username) {
        Post post = getById(id);
        if (!post.getAuthor().getEmail().equals(username)) {
            throw new SecurityException("No tienes permiso para eliminar este post");
        }
        postRepo.delete(post);
    }
    


    @Override
    @Transactional
    public int toggleLike(Long postId, String username) {
      Post post = postRepo.findById(postId)
          .orElseThrow(() -> new EntityNotFoundException("Post no encontrado " + postId));
      Usuario user = userRepo.findByEmail(username)
          .orElseThrow(() -> new UsernameNotFoundException(username));

      Set<Usuario> likes = post.getLikedBy();
      if (likes.contains(user)) likes.remove(user);
      else likes.add(user);
      
      postRepo.save(post);
      return likes.size();
    }
    
    @Override
    public void update(Long id, PostDto dto, String authorEmail) {
      Post post = postRepo.findById(id)
          .orElseThrow(() -> new EntityNotFoundException("Post no encontrado: " + id));
      
      if (!post.getAuthor().getEmail().equals(authorEmail)) {
        throw new AccessDeniedException("No puedes editar este post");
      }
      
      post.setTitle(dto.getTitle());
      post.setContent(dto.getContent());
      postRepo.save(post);
    }
    
    @Override
    public void deleteComment(Long postId, Long commentId, String userEmail) {
      Post post = postRepo.findById(postId)
          .orElseThrow(() -> new EntityNotFoundException("Post no encontrado: " + postId));
      Comment comment = commentRepository.findById(commentId)
          .orElseThrow(() -> new EntityNotFoundException("Comentario no encontrado: " + commentId));
      if (!comment.getAuthor().getEmail().equals(userEmail)) {
        throw new AccessDeniedException("No puedes eliminar este comentario");
      }
      
      post.getComments().remove(comment);
      commentRepository.delete(comment);
      postRepo.save(post);
    }


}
