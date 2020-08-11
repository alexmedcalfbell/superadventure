package com.medcalfbell.superadventure.models;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CommandResponse {

    private String response;
    private String command;
    private String imagePath;
    private int location;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("response", response)
                .append("command", command)
                .append("image", imagePath)
                .append("location", location)
                .toString();
    }
}