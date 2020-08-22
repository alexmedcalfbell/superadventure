package com.medcalfbell.superadventure.persistence.repositories;

import com.medcalfbell.superadventure.persistence.Location;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByDescription(String description);

    List<Location> findAllByDescriptionIn(String... locations);
}
