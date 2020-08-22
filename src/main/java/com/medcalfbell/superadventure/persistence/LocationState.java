package com.medcalfbell.superadventure.persistence;

import static javax.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

//TODO: Global jackson config
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "location_state")
public class LocationState {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonIgnore
    private int id;

    @Column
    private String locationId;

    @Column
    private String actionId;

    @Column
    private String targetId;

    @Column
    private String stateFlag;

    public int getId() {
        return id;
    }

    public LocationState setId(int id) {
        this.id = id;
        return this;
    }

    public String getLocationId() {
        return locationId;
    }

    public LocationState setLocationId(String locationId) {
        this.locationId = locationId;
        return this;
    }

    public String getActionId() {
        return actionId;
    }

    public LocationState setActionId(String actionId) {
        this.actionId = actionId;
        return this;
    }

    public String getTargetId() {
        return targetId;
    }

    public LocationState setTargetId(String targetId) {
        this.targetId = targetId;
        return this;
    }

    public String getStateFlag() {
        return stateFlag;
    }

    public LocationState setStateFlag(String stateFlag) {
        this.stateFlag = stateFlag;
        return this;
    }
}
