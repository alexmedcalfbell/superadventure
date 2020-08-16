package com.medcalfbell.superadventure.persistence;

import static javax.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

//TODO: Global jackson config
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "direction_location")
public class DirectionLocation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonIgnore
    private int id;

    @Column
    private String description;

    @Column
    private int currentLocationId;

    @Column
    private int destinationLocationId;

    @Column
    private int directionId;

    //TODO: Maybe just use the description rather than the id
    @Column
    @ElementCollection
    private List<Integer> directionIds;


    public int getId() {
        return id;
    }

    public DirectionLocation setId(int id) {
        this.id = id;
        return this;
    }

    public int getDirectionId() {
        return directionId;
    }

    public DirectionLocation setDirectionId(int directionId) {
        this.directionId = directionId;
        return this;
    }

    public int getCurrentLocationId() {
        return currentLocationId;
    }

    public DirectionLocation setCurrentLocationId(int currentLocationId) {
        this.currentLocationId = currentLocationId;
        return this;
    }

    public int getDestinationLocationId() {
        return destinationLocationId;
    }

    public DirectionLocation setDestinationLocationId(int destinationLocationId) {
        this.destinationLocationId = destinationLocationId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DirectionLocation setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<Integer> getDirectionIds() {
        return directionIds;
    }

    public DirectionLocation setDirectionIds(List<Integer> directionIds) {
        this.directionIds = directionIds;
        return this;
    }
}
