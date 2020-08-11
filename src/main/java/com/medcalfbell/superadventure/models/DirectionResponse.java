package com.medcalfbell.superadventure.models;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DirectionResponse {

    private int directionId;
    private String description;

    public int getDirectionId() {
        return directionId;
    }

    public DirectionResponse setDirectionId(int directionId) {
        this.directionId = directionId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DirectionResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("directionId", directionId)
                .append("description", description)
                .toString();
    }
}
