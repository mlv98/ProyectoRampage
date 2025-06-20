package com.eep.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.eep.model.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUsername(String username);
    void deleteByUsernameAndGameId(String username, Long gameId);
}