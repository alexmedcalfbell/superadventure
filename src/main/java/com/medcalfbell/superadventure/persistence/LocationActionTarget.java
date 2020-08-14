package com.medcalfbell.superadventure.persistence;

import static javax.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "location_action_target")
public class LocationActionTarget {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonIgnore
    private int id;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private String imagePath;

    @Column
    private int locationId;

    @Column
    private int actionId;

    @Column
    private int targetId;

    @Column(columnDefinition = "text")
    private String response;

    @Column
    private boolean fatal;

    @Column
    @ElementCollection
    private List<String> assets;

    public int getId() {
        return id;
    }

    public LocationActionTarget setId(int id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public LocationActionTarget setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImagePath() {
        return imagePath;
    }

    public LocationActionTarget setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public int getLocationId() {
        return locationId;
    }

    public LocationActionTarget setLocationId(int locationId) {
        this.locationId = locationId;
        return this;
    }

    public int getActionId() {
        return actionId;
    }

    public LocationActionTarget setActionId(int actionId) {
        this.actionId = actionId;
        return this;
    }

    public int getTargetId() {
        return targetId;
    }

    public LocationActionTarget setTargetId(int targetId) {
        this.targetId = targetId;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public LocationActionTarget setResponse(String response) {
        this.response = response;
        return this;
    }

    public List<String> getAssets() {
        return assets;
    }

    public LocationActionTarget setAssets(List<String> assets) {
        this.assets = assets;
        return this;
    }

    public boolean isFatal() {
        return fatal;
    }

    public LocationActionTarget setFatal(boolean fatal) {
        this.fatal = fatal;
        return this;
    }
}
