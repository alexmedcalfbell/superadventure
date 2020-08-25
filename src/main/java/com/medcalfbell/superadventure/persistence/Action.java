package com.medcalfbell.superadventure.persistence;

import static javax.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "action")
public class Action {

    public Action() {
    }

    public Action(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public Action(String description) {
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonIgnore
    private int id;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "locationActionTarget")
    private LocationActionTarget locationActionTarget;

    public int getId() {
        return id;
    }

    public Action setId(int id) {
        this.id = id;
        return this;
    }

    public LocationActionTarget getLocationActionTarget() {
        return locationActionTarget;
    }

    public Action setLocationActionTarget(
            LocationActionTarget locationActionTarget) {
        this.locationActionTarget = locationActionTarget;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Action setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("description", description)
                .append("locationActionTarget", locationActionTarget)
                .toString();
    }
}
