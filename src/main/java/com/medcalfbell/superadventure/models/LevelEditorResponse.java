package com.medcalfbell.superadventure.models;

import com.medcalfbell.superadventure.persistence.Action;
import com.medcalfbell.superadventure.persistence.Location;
import java.util.List;

public class LevelEditorResponse {

    private List<Location> locations;

    private List<Action> actions;


    //TODO: Add directionlocations / aciton locations


    public List<Location> getLocations() {
        return locations;
    }

    public LevelEditorResponse setLocations(List<Location> locations) {
        this.locations = locations;
        return this;
    }

    public List<Action> getActions() {
        return actions;
    }

    public LevelEditorResponse setActions(List<Action> actions) {
        this.actions = actions;
        return this;
    }
}
