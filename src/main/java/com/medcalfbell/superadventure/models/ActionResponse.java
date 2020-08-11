package com.medcalfbell.superadventure.models;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ActionResponse {

    private int actionId;
    private String description;

    public int getActionId() {
        return actionId;
    }

    public ActionResponse setActionId(int actionId) {
        this.actionId = actionId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ActionResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("actionId", actionId)
                .append("description", description)
                .toString();
    }
}
