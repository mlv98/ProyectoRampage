package com.eep.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.eep.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
}
