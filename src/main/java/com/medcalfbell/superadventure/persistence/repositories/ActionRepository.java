package com.medcalfbell.superadventure.persistence.repositories;

import com.medcalfbell.superadventure.persistence.Action;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

    Optional<Action> findByDescription(String description);
}
