package com.medcalfbell.superadventure.models;

import com.medcalfbell.superadventure.persistence.Location;

public class ActionTargetResponse {

    private String description;
    private Location location;
    private String actionId;
    private String targetId;
    private String response;
    private String imagePath;


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

    public String getActionId() {
        return actionId;
    }

    public ActionTargetResponse setActionId(String actionId) {
        this.actionId = actionId;
        return this;
    }

    public String getTargetId() {
        return targetId;
    }

    public ActionTargetResponse setTargetId(String targetId) {
        this.targetId = targetId;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public ActionTargetResponse setResponse(String response) {
        this.response = response;
        return this;
    }

    public String getImagePath() {
        return imagePath;
    }

    public ActionTargetResponse setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }
}
