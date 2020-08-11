package com.medcalfbell.superadventure.persistence.repositories;

import com.medcalfbell.superadventure.persistence.Action;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {


    Optional<Action> findByDescriptionContains(String description);

    Optional<Action> findByDescription(String description);

    Optional<Action> findByActionId(int location);

    Action findTopByOrderByActionIdDesc();

}
