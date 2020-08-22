package com.medcalfbell.superadventure.persistence.repositories;

import com.medcalfbell.superadventure.persistence.Direction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectionRepository extends JpaRepository<Direction, Long> {

    List<Direction> findByDescriptionIn(List<String> directionIds);
}
