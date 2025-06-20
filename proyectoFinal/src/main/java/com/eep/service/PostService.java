package com.eep.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.eep.dto.PostDto;
import com.eep.entity.Post;

public interface PostService {
    Page<Post> listAll(Pageable pageable);
    Post getById(Long id);
    Post create(PostDto dto, String username, List<MultipartFile> files);
    void delete(Long id, String username);
    int toggleLike(Long postId, String username);
    void update(Long id, PostDto dto, String authorEmail);
    void deleteComment(Long postId, Long commentId, String userEmail);
}
