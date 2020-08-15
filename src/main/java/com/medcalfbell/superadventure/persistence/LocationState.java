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
    private int locationId;

    @Column
    private int actionId;

    @Column
    private int targetId;

    @Column
    private String stateFlag;

    public int getId() {
        return id;
    }

    public LocationState setId(int id) {
        this.id = id;
        return this;
    }

    public int getLocationId() {
        return locationId;
    }

    public LocationState setLocationId(int locationId) {
        this.locationId = locationId;
        return this;
    }

    public int getActionId() {
        return actionId;
    }

    public LocationState setActionId(int actionId) {
        this.actionId = actionId;
        return this;
    }

    public int getTargetId() {
        return targetId;
    }

    public LocationState setTargetId(int targetId) {
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
