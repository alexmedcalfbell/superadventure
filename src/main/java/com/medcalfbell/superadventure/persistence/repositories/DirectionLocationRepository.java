package com.medcalfbell.superadventure.persistence.repositories;

import com.medcalfbell.superadventure.persistence.DirectionLocation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectionLocationRepository extends JpaRepository<DirectionLocation, Long> {

    Optional<DirectionLocation> findByCurrentLocationIdAndDirectionIdsIn(String currentLocationId,
            String... directionIds);
    Optional<DirectionLocation> findByCurrentLocationIdAndDestinationLocationIdAndDirectionIdsIn(
            String currentLocationId,
            String destinationLocationId,
            String... directionId);

    List<DirectionLocation> findByCurrentLocationId(String currentLocationId);

}
