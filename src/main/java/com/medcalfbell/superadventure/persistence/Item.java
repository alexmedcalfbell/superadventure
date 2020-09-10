package com.medcalfbell.superadventure.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "item")
public class Item {

    public Item() {
    }

    public Item(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Item(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Item(Long id, String name, String description, Location location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
    }

    public Item(String description) {
        this.description = description;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(columnDefinition = "text")
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "items")
    @JsonBackReference
    private Location location;

    public Long getId() {
        return id;
    }

    public Item setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Item setDescription(String description) {
        this.description = description;
        return this;
    }

    //    public Player getPlayer() {
    //        return player;
    //    }
    //
    //    public Item setPlayer(Player player) {
    //        this.player = player;
    //        return this;
    //    }

    public Location getLocation() {
        return location;
    }

    public Item setLocation(Location location) {
        this.location = location;
        return this;
    }
}
