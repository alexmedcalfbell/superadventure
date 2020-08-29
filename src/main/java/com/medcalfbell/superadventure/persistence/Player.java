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

@Entity
@Table(name = "player")
public class Player {

    public Player() {
    }

    public Player(int id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonIgnore
    private int id;

    @Column(columnDefinition = "text")
    private String name;

    @Column
    private int hp = 100;

    @ManyToOne
    @JoinColumn(name = "inventory")
    private Inventory inventory;

    //TODO: Join to state table


    public int getId() {
        return id;
    }

    public Player setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public int getHp() {
        return hp;
    }

    public Player setHp(int hp) {
        this.hp = hp;
        return this;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Player setInventory(Inventory inventory) {
        this.inventory = inventory;
        return this;
    }
}
