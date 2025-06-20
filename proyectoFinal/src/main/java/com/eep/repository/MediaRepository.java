package com.eep.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eep.entity.Media;


public interface MediaRepository extends JpaRepository<Media, Long> { }
