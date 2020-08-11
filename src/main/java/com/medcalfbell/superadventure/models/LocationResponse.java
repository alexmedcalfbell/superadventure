package com.medcalfbell.superadventure.models;

import com.medcalfbell.superadventure.persistence.Location;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class LocationResponse {

    private Location location;
    private String description;
    private String response;
    private String imagePath;

    public Location getLocation() {
        return location;
    }

    public LocationResponse setLocation(Location location) {
        this.location = location;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public LocationResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public LocationResponse setResponse(String response) {
        this.response = response;
        return this;
    }

    public String getImagePath() {
        return imagePath;
    }

    public LocationResponse setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("location", location)
                .append("description", description)
                .append("response", response)
                .append("imagePath", imagePath)
                .toString();
    }
}
