package csd230.s26.lab1.repositories;

import csd230.s26.lab1.entities.RPGgameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RPGgameRepository extends JpaRepository<RPGgameEntity, Long> {

    // Example of a custom query method specific to RPGgame fields
    List<RPGgameEntity> findByMainStoryHoursLessThanEqual(int hours);

    List<RPGgameEntity> findByHasCharacterCreationTrue();
}