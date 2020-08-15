package com.medcalfbell.superadventure.persistence.repositories;

import com.medcalfbell.superadventure.persistence.LocationState;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationStateRepository extends JpaRepository<LocationState, Long> {

    Optional<LocationState> findByLocationIdAndTargetIdAndActionId(int locationId, int targetId, int actionId);
    List<LocationState> findByLocationId(int locationId);
}
