package com.medcalfbell.superadventure.persistence;

import static javax.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonIgnore
    private int id;

    @Column
    private int locationId;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String response;

    @Column
    private String imagePath;

    @Column
    @ElementCollection
    private List<String> assets;

    public int getId() {
        return id;
    }

    public Location setId(int id) {
        this.id = id;
        return this;
    }

    public int getLocationId() {
        return locationId;
    }

    public Location setLocationId(int locationId) {
        this.locationId = locationId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Location setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public Location setResponse(String response) {
        this.response = response;
        return this;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Location setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public List<String> getAssets() {
        return assets;
    }

    public Location setAssets(List<String> assets) {
        this.assets = assets;
        return this;
    }
}
