package csd230.s26.lab1.repositories;

import csd230.s26.lab1.entities.ActionGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActionGameRepository extends JpaRepository<ActionGameEntity, Long> {

    List<ActionGameEntity> findByIsMultiplayerTrue();

    List<ActionGameEntity> findByGenreClassificationIgnoreCase(String genre);

    Optional<Object> findById(String gameId);
}