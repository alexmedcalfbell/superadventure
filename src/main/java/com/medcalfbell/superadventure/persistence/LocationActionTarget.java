package com.medcalfbell.superadventure.persistence;

import static javax.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Stores the mapping between {@link Location}, {@link Action} and {@link Target}. This is used to determine the result
 * of a player being in x location and perform x action on x target. For example in location (swamp), (talk), (witch).
 * <p>
 * Stores paths to the assets (images) related to that combination of location, action and target.
 * <p>
 * Stores information relating to the state of an action (cause and effect). This includes: fatal: if the action the
 * player takes is fatal, then the game is over.
 * <p>
 * Stores the state flag associated with this combination, e.g. if the witch is killed, then the flag `WITCH_DEAD` is
 * stored.
 * <p>
 * Stores states that block the supplied action from taking place. For example, stabbing the witch results in the state
 * `WITCH_DEAD`, therefore any further action toward the witch is blocked as she is in a final state.
 */
@Entity
@Table(name = "location_action_target")
public class LocationActionTarget {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonIgnore
    private int id;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private String locationId;

    @Column
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "locationActionTarget")
    private List<Action> actions;

    @Column
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "locationActionTarget")
    private List<Target> targets;

    @Column(columnDefinition = "text")
    private String response;

    @Column
    private boolean fatal;

    @Column
    @ElementCollection
    private List<String> assets;

    @Column(columnDefinition = "text")
    private String stateFlag;

    @Column
    @ElementCollection
    private List<String> blockers;


    public int getId() {
        return id;
    }

    public LocationActionTarget setId(int id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public LocationActionTarget setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getLocationId() {
        return locationId;
    }

    public LocationActionTarget setLocationId(String locationId) {
        this.locationId = locationId;
        return this;
    }

    public List<Target> getTargets() {
        return targets;
    }

    public LocationActionTarget setTargets(List<Target> targets) {
        this.targets = targets;
        return this;
    }

    public List<Action> getActions() {
        return actions;
    }

    public LocationActionTarget setActions(List<Action> actions) {
        this.actions = actions;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public LocationActionTarget setResponse(String response) {
        this.response = response;
        return this;
    }

    public List<String> getAssets() {
        return assets;
    }

    public LocationActionTarget setAssets(List<String> assets) {
        this.assets = assets;
        return this;
    }

    public boolean isFatal() {
        return fatal;
    }

    public LocationActionTarget setFatal(boolean fatal) {
        this.fatal = fatal;
        return this;
    }

    public String getStateFlag() {
        return stateFlag;
    }

    public LocationActionTarget setStateFlag(String stateFlag) {
        this.stateFlag = stateFlag;
        return this;
    }

    public List<String> getBlockers() {
        return blockers;
    }

    public LocationActionTarget setBlockers(List<String> blockers) {
        this.blockers = blockers;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("description", description)
                .append("locationId", locationId)
                .append("actions", actions)
                .append("targets", targets)
                .append("response", response)
                .append("fatal", fatal)
                .append("assets", assets)
                .append("stateFlag", stateFlag)
                .append("blockers", blockers)
                .toString();
    }
}
