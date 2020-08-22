package com.medcalfbell.superadventure.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "target")
public class Target {

    public Target() {
    }

    public Target(Long id, String description,
            LocationActionTarget locationActionTarget) {
        this.id = id;
        this.description = description;
        this.locationActionTarget = locationActionTarget;
    }

    public Target(String description) {
        this.description = description;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "locationActionTarget")
    private LocationActionTarget locationActionTarget;

    public Long getId() {
        return id;
    }

    public Target setId(Long id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Target setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocationActionTarget getLocationActionTarget() {
        return locationActionTarget;
    }

    public Target setLocationActionTarget(
            LocationActionTarget locationActionTarget) {
        this.locationActionTarget = locationActionTarget;
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
