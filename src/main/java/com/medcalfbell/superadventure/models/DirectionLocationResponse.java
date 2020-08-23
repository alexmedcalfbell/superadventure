package com.medcalfbell.superadventure.models;

import com.medcalfbell.superadventure.persistence.Location;
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

    private List<String> actionTargets;

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

    public List<String> getActionTargets() {
        return actionTargets;
    }

    public DirectionLocationResponse setActionTargets(List<String> actionTargets) {
        this.actionTargets = actionTargets;
        return this;
    }
}
