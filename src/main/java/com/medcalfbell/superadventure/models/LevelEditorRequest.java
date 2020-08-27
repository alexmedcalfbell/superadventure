package com.medcalfbell.superadventure.models;

import java.util.List;

public class LevelEditorRequest {

    //TODO: Make separate requests i.e. for location, target etc / add @NotNull etc
    private String currentLocationId;
    private String destinationLocationId;
    private String directionId;
    private String actionId;
    private List<String> actions;
    private List<String> targets;
    private List<String> assets;
    private boolean fatal;
    private String targetId;
    private String description;
    private String response;
    private String imageName;

    public String getCurrentLocationId() {
        return currentLocationId;
    }

    public LevelEditorRequest setCurrentLocationId(String currentLocationId) {
        this.currentLocationId = currentLocationId;
        return this;
    }

    public String getDestinationLocationId() {
        return destinationLocationId;
    }

    public LevelEditorRequest setDestinationLocationId(String destinationLocationId) {
        this.destinationLocationId = destinationLocationId;
        return this;
    }

    public String getDirectionId() {
        return directionId;
    }

    public LevelEditorRequest setDirectionId(String directionId) {
        this.directionId = directionId;
        return this;
    }

    public String getActionId() {
        return actionId;
    }

    public LevelEditorRequest setActionId(String actionId) {
        this.actionId = actionId;
        return this;
    }

    public List<String> getActions() {
        return actions;
    }

    public LevelEditorRequest setActions(List<String> actions) {
        this.actions = actions;
        return this;
    }

    public List<String> getTargets() {
        return targets;
    }

    public LevelEditorRequest setTargets(List<String> targets) {
        this.targets = targets;
        return this;
    }

    public List<String> getAssets() {
        return assets;
    }

    public LevelEditorRequest setAssets(List<String> assets) {
        this.assets = assets;
        return this;
    }

    public boolean isFatal() {
        return fatal;
    }

    public LevelEditorRequest setFatal(boolean fatal) {
        this.fatal = fatal;
        return this;
    }

    public String getTargetId() {
        return targetId;
    }

    public LevelEditorRequest setTargetId(String targetId) {
        this.targetId = targetId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public LevelEditorRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public LevelEditorRequest setResponse(String response) {
        this.response = response;
        return this;
    }

    public String getImageName() {
        return imageName;
    }

    public LevelEditorRequest setImageName(String imageName) {
        this.imageName = imageName;
        return this;
    }
}
