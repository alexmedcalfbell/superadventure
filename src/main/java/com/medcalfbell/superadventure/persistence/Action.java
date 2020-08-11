package com.medcalfbell.superadventure.persistence;

import static javax.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "action")
public class Action {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonIgnore
    private int id;

    @Column
    private int actionId;

    @Column(columnDefinition = "text")
    private String description;

    public int getId() {
        return id;
    }

    public Action setId(int id) {
        this.id = id;
        return this;
    }

    public int getActionId() {
        return actionId;
    }

    public Action setActionId(int actionId) {
        this.actionId = actionId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Action setDescription(String description) {
        this.description = description;
        return this;
    }
}
