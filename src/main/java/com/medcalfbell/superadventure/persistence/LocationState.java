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
    private int stateHash;

    public int getId() {
        return id;
    }

    public LocationState setId(int id) {
        this.id = id;
        return this;
    }

    public int getStateHash() {
        return stateHash;
    }

    public LocationState setStateHash(int stateHash) {
        this.stateHash = stateHash;
        return this;
    }
}
