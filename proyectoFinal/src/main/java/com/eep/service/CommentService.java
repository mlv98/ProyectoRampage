package com.eep.service;

import com.eep.dto.CommentDto;

public interface CommentService {
    void addComment(Long postId, String username, CommentDto dto);
}
