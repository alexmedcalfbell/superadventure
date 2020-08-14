package com.medcalfbell.superadventure.persistence.repositories;

import com.medcalfbell.superadventure.persistence.DirectionLocation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectionLocationRepository extends JpaRepository<DirectionLocation, Long> {


    Optional<DirectionLocation> findByDirectionId(int directionId);
    Optional<DirectionLocation> findByCurrentLocationIdAndDirectionId(int currentLocationId, int directionId);
    Optional<DirectionLocation> findByCurrentLocationIdAndDirectionIdsIn(int currentLocationId, int... directionIds);
    Optional<DirectionLocation> findByCurrentLocationIdAndDestinationLocationIdAndDirectionId(int currentLocationId,
            int destinationLocationId, int directionId);

    List<DirectionLocation> findByCurrentLocationIdAndDirectionIdIn(int currentLocationId, int... directionIds);

}
