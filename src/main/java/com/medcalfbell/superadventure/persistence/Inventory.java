package com.medcalfbell.superadventure.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "inventory")
public class Inventory {

    public Inventory() {
    }

    public Inventory(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Inventory(String description) {
        this.description = description;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(columnDefinition = "text")
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    //TODO: Link to 'player' table to separate different users.
    public Long getId() {
        return id;
    }

    public Inventory setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Inventory setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Inventory setDescription(String description) {
        this.description = description;
        return this;
    }

}
