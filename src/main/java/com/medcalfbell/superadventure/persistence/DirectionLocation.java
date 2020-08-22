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
import org.apache.commons.lang3.builder.ToStringBuilder;

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
    private String currentLocationId;

    @Column
    private String destinationLocationId;

    @Column
    @ElementCollection
    private List<String> directionIds;


    public int getId() {
        return id;
    }

    public DirectionLocation setId(int id) {
        this.id = id;
        return this;
    }

    public String getCurrentLocationId() {
        return currentLocationId;
    }

    public DirectionLocation setCurrentLocationId(String currentLocationId) {
        this.currentLocationId = currentLocationId;
        return this;
    }

    public String getDestinationLocationId() {
        return destinationLocationId;
    }

    public DirectionLocation setDestinationLocationId(String destinationLocationId) {
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

    public List<String> getDirectionIds() {
        return directionIds;
    }

    public DirectionLocation setDirectionIds(List<String> directionIds) {
        this.directionIds = directionIds;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("description", description)
                .append("currentLocationId", currentLocationId)
                .append("destinationLocationId", destinationLocationId)
                .append("directionIds", directionIds)
                .toString();
    }
}
