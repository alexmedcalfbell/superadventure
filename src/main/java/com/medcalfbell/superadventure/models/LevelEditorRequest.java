package com.medcalfbell.superadventure.models;

public class LevelEditorRequest {

    //TODO: Make separate requests i.e. for location, target etc / add @NotNull etc
    private int currentLocationId;
    private int destinationLocationId;
    private int directionId;
    private int actionId;
    private int targetId;
    private String description;
    private String response;
    private String imageName;

    public String getDescription() {
        return description;
    }

    public LevelEditorRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getCurrentLocationId() {
        return currentLocationId;
    }

    public LevelEditorRequest setCurrentLocationId(int currentLocationId) {
        this.currentLocationId = currentLocationId;
        return this;
    }

    public int getDestinationLocationId() {
        return destinationLocationId;
    }

    public LevelEditorRequest setDestinationLocationId(int destinationLocationId) {
        this.destinationLocationId = destinationLocationId;
        return this;
    }

    public int getDirectionId() {
        return directionId;
    }

    public LevelEditorRequest setDirectionId(int directionId) {
        this.directionId = directionId;
        return this;
    }

    public int getActionId() {
        return actionId;
    }

    public LevelEditorRequest setActionId(int actionId) {
        this.actionId = actionId;
        return this;
    }

    public int getTargetId() {
        return targetId;
    }

    public LevelEditorRequest setTargetId(int targetId) {
        this.targetId = targetId;
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
