package com.medcalfbell.superadventure.persistence.repositories;

import com.medcalfbell.superadventure.persistence.LocationActionTarget;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationActionTargetRepository extends JpaRepository<LocationActionTarget, Long> {

    Optional<LocationActionTarget> findByLocationIdAndActionsDescriptionAndTargetsDescription(String location,
            String action, String target);
    List<LocationActionTarget> findAllByLocationId(String locationId);
}
