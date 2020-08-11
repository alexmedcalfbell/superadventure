package com.medcalfbell.superadventure.persistence;

import static javax.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "direction")
public class Direction {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonIgnore
    private int id;

    @Column
    private int directionId;

    @Column(columnDefinition = "text")
    private String description;


    public int getId() {
        return id;
    }

    public Direction setId(int id) {
        this.id = id;
        return this;
    }

    public int getDirectionId() {
        return directionId;
    }

    public Direction setDirectionId(int directionId) {
        this.directionId = directionId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Direction setDescription(String description) {
        this.description = description;
        return this;
    }
}
