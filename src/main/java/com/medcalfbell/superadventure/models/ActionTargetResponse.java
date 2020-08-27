package com.medcalfbell.superadventure.models;

import com.medcalfbell.superadventure.persistence.Location;
import java.util.List;

public class ActionTargetResponse {

    private String description;
    private Location location;
    private List<String> actions;
    private List<String> targets;
    private boolean fatal;
    private String response;
    private List<String> assets;

    public String getDescription() {
        return description;
    }

    public ActionTargetResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public ActionTargetResponse setLocation(Location location) {
        this.location = location;
        return this;
    }

    public List<String> getActions() {
        return actions;
    }

    public ActionTargetResponse setActions(List<String> actions) {
        this.actions = actions;
        return this;
    }

    public List<String> getTargets() {
        return targets;
    }

    public ActionTargetResponse setTargets(List<String> targets) {
        this.targets = targets;
        return this;
    }

    public boolean isFatal() {
        return fatal;
    }

    public ActionTargetResponse setFatal(boolean fatal) {
        this.fatal = fatal;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public ActionTargetResponse setResponse(String response) {
        this.response = response;
        return this;
    }

    public List<String> getAssets() {
        return assets;
    }

    public ActionTargetResponse setAssets(List<String> assets) {
        this.assets = assets;
        return this;
    }
}
