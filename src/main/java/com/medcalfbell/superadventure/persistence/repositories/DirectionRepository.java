package com.medcalfbell.superadventure.persistence.repositories;

import com.medcalfbell.superadventure.persistence.Direction;
import com.medcalfbell.superadventure.persistence.Location;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectionRepository extends JpaRepository<Direction, Long> {


    Optional<Direction> findByDirectionId(int directionId);
    Optional<Direction> findByDescriptionContains(String description);
    Direction findTopByOrderByDirectionIdDesc();

}
