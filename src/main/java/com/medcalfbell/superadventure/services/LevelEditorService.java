package com.medcalfbell.superadventure.services;

import com.medcalfbell.superadventure.exceptions.ActionNotFoundException;
import com.medcalfbell.superadventure.exceptions.DuplicateActionException;
import com.medcalfbell.superadventure.exceptions.DuplicateDirectionLocationException;
import com.medcalfbell.superadventure.exceptions.DuplicateLocationActionTargetException;
import com.medcalfbell.superadventure.exceptions.DuplicateLocationException;
import com.medcalfbell.superadventure.exceptions.DuplicateTargetException;
import com.medcalfbell.superadventure.exceptions.LocationNotFoundException;
import com.medcalfbell.superadventure.exceptions.TargetNotFoundException;
import com.medcalfbell.superadventure.persistence.Action;
import com.medcalfbell.superadventure.persistence.Direction;
import com.medcalfbell.superadventure.persistence.DirectionLocation;
import com.medcalfbell.superadventure.persistence.Location;
import com.medcalfbell.superadventure.persistence.LocationActionTarget;
import com.medcalfbell.superadventure.persistence.Target;
import com.medcalfbell.superadventure.persistence.repositories.ActionRepository;
import com.medcalfbell.superadventure.persistence.repositories.DirectionLocationRepository;
import com.medcalfbell.superadventure.persistence.repositories.DirectionRepository;
import com.medcalfbell.superadventure.persistence.repositories.LocationActionTargetRepository;
import com.medcalfbell.superadventure.persistence.repositories.LocationRepository;
import com.medcalfbell.superadventure.persistence.repositories.TargetRepository;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class LevelEditorService {

    private static final Logger logger = LoggerFactory.getLogger(LevelEditorService.class);

    private LocationRepository locationRepository;
    private TargetRepository targetRepository;
    private LocationActionTargetRepository locationActionTargetRepository;
    private ActionRepository actionRepository;
    private DirectionRepository directionRepository;
    private DirectionLocationRepository directionLocationRepository;

    @Autowired
    public LevelEditorService(LocationRepository locationRepository,
            TargetRepository targetRepository,
            LocationActionTargetRepository locationActionTargetRepository,
            ActionRepository actionRepository,
            DirectionRepository directionRepository,
            DirectionLocationRepository directionLocationRepository) {
        this.locationRepository = locationRepository;
        this.targetRepository = targetRepository;
        this.locationActionTargetRepository = locationActionTargetRepository;
        this.actionRepository = actionRepository;
        this.directionRepository = directionRepository;
        this.directionLocationRepository = directionLocationRepository;
    }

    public List<Location> getLocations() {
        return locationRepository.findAll(Sort.by(Sort.Direction.ASC, "description"));
    }

    public List<Location> getLocationForLocationIds(String... locations) {
        return locationRepository.findAllByDescriptionIn(locations);
    }

    public Set<String> getActions() {
        return actionRepository.findAll().stream()
                .map(action -> action.getDescription())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Set<String> getTargets() {
        return targetRepository.findAll().stream()
                .map(target -> target.getDescription())
                .collect(Collectors.toCollection(TreeSet::new));
    }
    
    public List<Direction> getDirections() {
        return directionRepository.findAll(Sort.by(Sort.Direction.ASC, "description"));
    }

    public Location getLocation(String locationId) {
        return locationRepository.findByDescription(locationId)
                .orElseThrow(() -> new LocationNotFoundException("Location [" + locationId + "] not found."));
    }

    public Action getAction(String actionId) {
        return actionRepository.findByDescription(actionId)
                .orElseThrow(() -> new ActionNotFoundException("Action [" + actionId + "] not found."));
    }

    public Target getTarget(String targetId) {
        return targetRepository.findByDescription(targetId)
                .orElseThrow(() -> new TargetNotFoundException("Target [" + targetId + "] not found."));
    }

    public List<DirectionLocation> getDirectionLocations() {
        return directionLocationRepository.findAll();
    }


    public List<LocationActionTarget> getLocationActionTargets() {
        return locationActionTargetRepository.findAll();
    }

    public List<LocationActionTarget> getLocationActionTargetsForLocation(String locationId) {
        return locationActionTargetRepository.findAllByLocationId(locationId);
    }


    public LocationActionTarget getLocationActionTarget(String locationId, String actionId, String targetId) {
        return locationActionTargetRepository.findByLocationIdAndActionsDescriptionAndTargetsDescription(locationId,
                actionId, targetId)
                .orElseThrow(() -> new ActionNotFoundException("sdfsdfsdAction [" + actionId + "] not found."));
    }

    public void validateLocation(String locationId) {
        locationRepository.findByDescription(locationId).ifPresent(
                location -> {
                    throw new DuplicateLocationException(
                            "An entry already exists for Location [" + locationId + "]");
                });
    }

    public void validateAction(String description) {
        actionRepository.findByDescription(description).ifPresent(
                location -> {
                    throw new DuplicateActionException(
                            "An entry already exists for Action [" + description + "]");
                });
    }

    public void validateTarget(String description) {
        targetRepository.findByDescription(description).ifPresent(
                location -> {
                    throw new DuplicateTargetException(
                            "An entry already exists for Target [" + description + "]");
                });
    }

    public void validateLocationActionTarget(String locationId, String actionId, String targetId) {
        locationActionTargetRepository.findByLocationIdAndActionsDescriptionAndTargetsDescription(locationId, actionId,
                targetId).ifPresent(
                directionLocation -> {
                    throw new DuplicateLocationActionTargetException(
                            "An entry already exists for Location [" + locationId
                                    + "] with action [" + actionId + "] and target [" + targetId + "]");
                });
    }

    public void validateLocationDirection(String currentLocationId, String destinationLocationId,
            String directionId) {
        directionLocationRepository.findByCurrentLocationIdAndDestinationLocationIdAndDirectionIdsIn(
                currentLocationId, destinationLocationId, directionId).ifPresent(directionLocation -> {
            throw new DuplicateDirectionLocationException("An entry already exists for Location [" + currentLocationId
                    + "] with destination location [" + destinationLocationId + "] and direction [" + directionId
                    + "]");
        });
    }

    public List<DirectionLocation> getDirectionLocationsForCurrentLocation(String currentLocationId) {
        return directionLocationRepository.findByCurrentLocationId(currentLocationId);

    }
}
