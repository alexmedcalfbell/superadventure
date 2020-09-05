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
    private long id;

    @Column(columnDefinition = "text")
    private String name;

    @Column
    private int hp = 100;

    @Column
    @ElementCollection
    private List<String> items;

    //TODO: Join to state table

    public long getId() {
        return id;
    }

    public Player setId(long id) {
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

    public List<String> getItems() {
        return items;
    }

    public Player setItems(List<String> item) {
        this.items = item;
        return this;
    }
}
