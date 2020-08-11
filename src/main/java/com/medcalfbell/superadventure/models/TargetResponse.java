package com.medcalfbell.superadventure.models;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TargetResponse {

    private int targetId;
    private String description;

    public int getTargetId() {
        return targetId;
    }

    public TargetResponse setTargetId(int targetId) {
        this.targetId = targetId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TargetResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("targetId", targetId)
                .append("description", description)
                .toString();
    }
}
