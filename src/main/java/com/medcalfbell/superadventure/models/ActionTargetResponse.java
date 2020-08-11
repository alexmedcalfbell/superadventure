package com.medcalfbell.superadventure.models;

import com.medcalfbell.superadventure.persistence.Location;

public class ActionTargetResponse {

    private String description;
    private Location location;
    private int actionId;
    private int targetId;
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

    public int getActionId() {
        return actionId;
    }

    public ActionTargetResponse setActionId(int actionId) {
        this.actionId = actionId;
        return this;
    }

    public int getTargetId() {
        return targetId;
    }

    public ActionTargetResponse setTargetId(int targetId) {
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
