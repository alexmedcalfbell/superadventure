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
import com.medcalfbell.superadventure.persistence.repositories.ActionRepository;
import com.medcalfbell.superadventure.persistence.Direction;
import com.medcalfbell.superadventure.persistence.DirectionLocation;
import com.medcalfbell.superadventure.persistence.repositories.DirectionLocationRepository;
import com.medcalfbell.superadventure.persistence.repositories.DirectionRepository;
import com.medcalfbell.superadventure.persistence.Location;
import com.medcalfbell.superadventure.persistence.LocationActionTarget;
import com.medcalfbell.superadventure.persistence.repositories.LocationActionTargetRepository;
import com.medcalfbell.superadventure.persistence.repositories.LocationRepository;
import com.medcalfbell.superadventure.persistence.Target;
import com.medcalfbell.superadventure.persistence.repositories.TargetRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class LevelEditorService {

    private static final Logger logger = LoggerFactory.getLogger(LevelEditorService.class);

    private static final String IMAGES_ROOT = "images/";

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
        return locationRepository.findAll(Sort.by(Sort.Direction.ASC, "locationId"));
    }

    public List<Location> getLocationForLocationIds(int... locations) {
        return locationRepository.findAllByLocationIdIn(locations);
    }

    public List<Action> getActions() {
        return actionRepository.findAll(Sort.by(Sort.Direction.ASC, "actionId"));
    }

    public List<Target> getTargets() {
        return targetRepository.findAll(Sort.by(Sort.Direction.ASC, "targetId"));
    }

    public List<Direction> getDirections() {
        return directionRepository.findAll(Sort.by(Sort.Direction.ASC, "directionId"));
    }

    public Location getLocation(int locationId) {
        return locationRepository.findByLocationId(locationId)
                .orElseThrow(() -> new LocationNotFoundException("Location [" + locationId + "] not found."));
    }

    public Action getAction(int actionId) {
        return actionRepository.findByActionId(actionId)
                .orElseThrow(() -> new ActionNotFoundException("Action [" + actionId + "] not found."));
    }

    public Target getTarget(int targetId) {
        return targetRepository.findByTargetId(targetId)
                .orElseThrow(() -> new TargetNotFoundException("Target [" + targetId + "] not found."));
    }

    public List<DirectionLocation> getDirectionLocations() {
        return directionLocationRepository.findAll();
    }


    public List<LocationActionTarget> getLocationActionTargets() {
        return locationActionTargetRepository.findAll();
    }

    public List<LocationActionTarget> getLocationActionTargetsForLocation(int locationId) {
        return locationActionTargetRepository.findAllByLocationId(locationId);
    }


    public LocationActionTarget getLocationActionTarget(int locationId, int actionId, int targetId) {
        return locationActionTargetRepository.findByLocationIdAndActionIdAndTargetId(locationId, actionId, targetId)
                .orElseThrow(() -> new ActionNotFoundException("sdfsdfsdAction [" + actionId + "] not found."));
    }

    public int getNextLocationId() {
        return locationRepository.findTopByOrderByLocationIdDesc().getLocationId() + 1;
    }

    public int getNextTargetId() {
        return targetRepository.findTopByOrderByTargetIdDesc().getTargetId() + 1;
    }

    public int getNextActionId() {
        return actionRepository.findTopByOrderByActionIdDesc().getActionId() + 1;
    }

    public void validateLocation(int locationId) {
        locationRepository.findByLocationId(locationId).ifPresent(
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

    public void validateLocationActionTarget(int locationId, int actionId, int targetId) {
        locationActionTargetRepository.findByLocationIdAndActionIdAndTargetId(locationId, actionId, targetId).ifPresent(
                directionLocation -> {
                    throw new DuplicateLocationActionTargetException(
                            "An entry already exists for Location [" + locationId
                                    + "] with action [" + actionId + "] and target [" + targetId + "]");
                });
    }

    public void validateLocationDirection(int currentLocationId, int destinationLocationId,
            int directionId) {
        directionLocationRepository.findByCurrentLocationIdAndDestinationLocationIdAndDirectionId(
                currentLocationId, destinationLocationId, directionId).ifPresent(directionLocation -> {
            throw new DuplicateDirectionLocationException("An entry already exists for Location [" + currentLocationId
                    + "] with destination location [" + destinationLocationId + "] and direction [" + directionId
                    + "]");
        });
    }

    public List<DirectionLocation> getPrimaryDirectionLocationsForCurrentLocation(int currentLocationId) {
        return directionLocationRepository.findByCurrentLocationIdAndDirectionIdIn(currentLocationId, 1, 2, 3, 4);

    }
}
