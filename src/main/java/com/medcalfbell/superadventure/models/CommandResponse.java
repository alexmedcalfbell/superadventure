package com.medcalfbell.superadventure.models;

import java.util.List;

public class CommandResponse {

    private String response;
    private String command;
    private String imagePath;
    private int location;
    private List<String> assets;
    private boolean isFatal;

    public String getResponse() {
        return response;
    }

    public CommandResponse setResponse(String response) {
        this.response = response;
        return this;
    }

    public String getCommand() {
        return command;
    }

    public CommandResponse setCommand(String command) {
        this.command = command;
        return this;
    }

    public String getImagePath() {
        return imagePath;
    }

    public CommandResponse setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public int getLocation() {
        return location;
    }

    public CommandResponse setLocation(int location) {
        this.location = location;
        return this;
    }

    public List<String> getAssets() {
        return assets;
    }

    public CommandResponse setAssets(List<String> assets) {
        this.assets = assets;
        return this;
    }

    public boolean isFatal() {
        return isFatal;
    }

    public CommandResponse setFatal(boolean fatal) {
        isFatal = fatal;
        return this;
    }
}