package com.medcalfbell.superadventure.persistence.repositories;

import com.medcalfbell.superadventure.persistence.Target;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetRepository extends JpaRepository<Target, Long> {

    Optional<Target> findByDescription(String description);

    Optional<Target> findByTargetId(int targetId);
    List<Target> findByTargetIdIn(List<Integer> targetIds);

    Target findTopByOrderByTargetIdDesc();
}
