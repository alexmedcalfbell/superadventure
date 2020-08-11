package com.medcalfbell.superadventure.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;


@Entity
@Table(name = "target")
public class Target {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private int targetId;

    @Column(columnDefinition = "text")
    private String description;

    public Long getId() {
        return id;
    }

    public Target setId(Long id) {
        this.id = id;
        return this;
    }

    public int getTargetId() {
        return targetId;
    }

    public Target setTargetId(int targetId) {
        this.targetId = targetId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Target setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("targetId", targetId)
                .append("description", description)
                .toString();
    }
}
