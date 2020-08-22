package com.medcalfbell.superadventure.models;

import com.medcalfbell.superadventure.persistence.Location;
import com.medcalfbell.superadventure.persistence.LocationActionTarget;
import java.util.List;

public class DirectionLocationResponse {

    private String description;

    private Location currentLocation;

    private Location destinationLocation;

    private String directionId;

    private Location locationNorth;
    private Location locationSouth;
    private Location locationEast;
    private Location locationWest;

    private List<LocationActionTarget> actionTargetsCurrent;
    private List<LocationActionTarget> actionTargetsNorth;
    private List<LocationActionTarget> actionTargetsSouth;
    private List<LocationActionTarget> actionTargetsEast;
    private List<LocationActionTarget> actionTargetsWest;

    public String getDescription() {
        return description;
    }

    public DirectionLocationResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public DirectionLocationResponse setCurrentLocation(
            Location currentLocation) {
        this.currentLocation = currentLocation;
        return this;
    }

    public Location getDestinationLocation() {
        return destinationLocation;
    }

    public DirectionLocationResponse setDestinationLocation(
            Location destinationLocation) {
        this.destinationLocation = destinationLocation;
        return this;
    }

    public String getDirectionId() {
        return directionId;
    }

    public DirectionLocationResponse setDirectionId(String directionId) {
        this.directionId = directionId;
        return this;
    }

    public Location getLocationNorth() {
        return locationNorth;
    }

    public DirectionLocationResponse setLocationNorth(Location locationNorth) {
        this.locationNorth = locationNorth;
        return this;
    }

    public Location getLocationSouth() {
        return locationSouth;
    }

    public DirectionLocationResponse setLocationSouth(Location locationSouth) {
        this.locationSouth = locationSouth;
        return this;
    }

    public Location getLocationEast() {
        return locationEast;
    }

    public DirectionLocationResponse setLocationEast(Location locationEast) {
        this.locationEast = locationEast;
        return this;
    }

    public Location getLocationWest() {
        return locationWest;
    }

    public DirectionLocationResponse setLocationWest(Location locationWest) {
        this.locationWest = locationWest;
        return this;
    }

    public List<LocationActionTarget> getActionTargetsCurrent() {
        return actionTargetsCurrent;
    }

    public DirectionLocationResponse setActionTargetsCurrent(
            List<LocationActionTarget> actionTargetsCurrent) {
        this.actionTargetsCurrent = actionTargetsCurrent;
        return this;
    }

    public List<LocationActionTarget> getActionTargetsNorth() {
        return actionTargetsNorth;
    }

    public DirectionLocationResponse setActionTargetsNorth(
            List<LocationActionTarget> actionTargetsNorth) {
        this.actionTargetsNorth = actionTargetsNorth;
        return this;
    }

    public List<LocationActionTarget> getActionTargetsSouth() {
        return actionTargetsSouth;
    }

    public DirectionLocationResponse setActionTargetsSouth(
            List<LocationActionTarget> actionTargetsSouth) {
        this.actionTargetsSouth = actionTargetsSouth;
        return this;
    }

    public List<LocationActionTarget> getActionTargetsEast() {
        return actionTargetsEast;
    }

    public DirectionLocationResponse setActionTargetsEast(
            List<LocationActionTarget> actionTargetsEast) {
        this.actionTargetsEast = actionTargetsEast;
        return this;
    }

    public List<LocationActionTarget> getActionTargetsWest() {
        return actionTargetsWest;
    }

    public DirectionLocationResponse setActionTargetsWest(
            List<LocationActionTarget> actionTargetsWest) {
        this.actionTargetsWest = actionTargetsWest;
        return this;
    }
}
