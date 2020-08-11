package com.medcalfbell.superadventure.models;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CommandRequest {

    private String command;

    public String getCommand() {
        return command;
    }

    public CommandRequest setCommand(String command) {
        this.command = command;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("command", command)
                .toString();
    }
}
