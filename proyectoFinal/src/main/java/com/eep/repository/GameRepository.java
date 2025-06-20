package com.eep.repository;

import com.eep.model.GameRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<GameRequirement, Long> {
    Optional<GameRequirement> findByGameNameIgnoreCase(String gameName);
}
