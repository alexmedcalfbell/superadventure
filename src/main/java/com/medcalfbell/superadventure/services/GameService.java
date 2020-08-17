package com.medcalfbell.superadventure.services;

import com.medcalfbell.superadventure.enums.MovementAction;
import com.medcalfbell.superadventure.exceptions.ActionNotFoundException;
import com.medcalfbell.superadventure.exceptions.DirectionNotFoundException;
import com.medcalfbell.superadventure.exceptions.LocationNotFoundException;
import com.medcalfbell.superadventure.exceptions.StateHashExistsException;
import com.medcalfbell.superadventure.exceptions.TargetNotFoundException;
import com.medcalfbell.superadventure.models.CommandResponse;
import com.medcalfbell.superadventure.persistence.Action;
import com.medcalfbell.superadventure.persistence.Direction;
import com.medcalfbell.superadventure.persistence.DirectionLocation;
import com.medcalfbell.superadventure.persistence.Location;
import com.medcalfbell.superadventure.persistence.LocationActionTarget;
import com.medcalfbell.superadventure.persistence.LocationState;
import com.medcalfbell.superadventure.persistence.Target;
import com.medcalfbell.superadventure.persistence.repositories.ActionRepository;
import com.medcalfbell.superadventure.persistence.repositories.DirectionLocationRepository;
import com.medcalfbell.superadventure.persistence.repositories.DirectionRepository;
import com.medcalfbell.superadventure.persistence.repositories.LocationActionTargetRepository;
import com.medcalfbell.superadventure.persistence.repositories.LocationRepository;
import com.medcalfbell.superadventure.persistence.repositories.LocationStateRepository;
import com.medcalfbell.superadventure.persistence.repositories.TargetRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    public static final int DEFAULT_LOCATION = 0;
    private int currentLocation = DEFAULT_LOCATION;
    private List<Integer> locationHistory = new ArrayList<>();

    private LocationRepository locationRepository;
    private TargetRepository targetRepository;
    private LocationActionTargetRepository locationActionTargetRepository;
    private ActionRepository actionRepository;
    private DirectionRepository directionRepository;
    private DirectionLocationRepository directionLocationRepository;
    private LocationStateRepository locationStateRepository;

    @Autowired
    public GameService(LocationRepository locationRepository,
            TargetRepository targetRepository,
            LocationActionTargetRepository locationActionTargetRepository,
            ActionRepository actionRepository,
            DirectionRepository directionRepository,
            DirectionLocationRepository directionLocationRepository,
            LocationStateRepository locationStateRepository) {
        this.locationRepository = locationRepository;
        this.targetRepository = targetRepository;
        this.locationActionTargetRepository = locationActionTargetRepository;
        this.actionRepository = actionRepository;
        this.directionRepository = directionRepository;
        this.directionLocationRepository = directionLocationRepository;
        this.locationStateRepository = locationStateRepository;
    }

    //TODO: Should do foreign key setup in the tables.
    //TODO: Map. Do we want them to be able to see the map? If so re-use level editor ui.
    public CommandResponse getMap() {
        return null;
    }

    /**
     * Reset the current location / state in case of a page refresh.
     */
    public void reset() {
        currentLocation = DEFAULT_LOCATION;
        locationStateRepository.deleteAll();
    }

    public CommandResponse processCommand(String command) {

        //TODO: 'Help' is different from 'where am I'. help is more of a 'cheat', whereas where am I just describes the scene.
        if (command.contains("help")) {
            return getHelp(command);
        }

        if (command.contains("where")) {
            return getWhere(command);
        }

        if (isMovementAction(command)) {
            return processMovementAction(command);
        }

        //Process action / target
        return processAction(command);
    }

    /**
     * Returns the actions / targets associated with the current location and wraps them in <help></help> tags for
     * formatting.
     *
     * @param command The supplied command text.
     * @return {@link CommandResponse}
     */
    private CommandResponse getHelp(String command) {
        final List<LocationActionTarget> targetActions = locationActionTargetRepository.findAllByLocationId(
                currentLocation);

        String descriptions = targetActions.stream()
                .map(targetAction -> "<help>" + targetAction.getDescription() + "</help><br>")
                .collect(Collectors.joining());

        if (StringUtils.isEmpty(descriptions)) {
            descriptions = "<help>There's nothing to interact with here.</help>";
        }
        return new CommandResponse()
                .setCommand(command)
                .setResponse(descriptions);
    }

    /**
     * Returns information about the current location and its associated targets / directions. Does not include
     * actions.
     *
     * @param command The supplied command text.
     * @return {@link CommandResponse}
     */
    private CommandResponse getWhere(String command) {
        Optional<Location> location = locationRepository.findByLocationId(currentLocation);

        String targets = targetRepository.findByTargetIdIn(
                locationActionTargetRepository.findAllByLocationId(currentLocation).stream()
                        .map(actionTarget -> actionTarget.getTargetId()).collect(Collectors.toList())
        ).stream()
                .map(target -> "<li><help>" + target.getDescription() + "</help></li>")
                .collect(Collectors.joining());

        if (StringUtils.isEmpty(targets)) {
            targets = "<help>Nothing to interact with here.</help>";
        }

        final String locations = directionRepository.findByDirectionIdIn(
                directionLocationRepository.findByCurrentLocationId(
                        currentLocation).stream()
                        .map(d -> d.getDirectionId()).collect(Collectors.toList())).stream()
                .map(d -> "<li><help>" + d.getDescription() + "</help></li>")
                .collect(Collectors.joining());

        String response = String.format(
                "You're at the <help>%s</help>. You can see: <ul>%s</ul> You can go: <ul>%s</ul>",
                location.get().getDescription(),
                targets,
                locations);

        return new CommandResponse()
                .setCommand(command)
                .setResponse(response);
    }

    /**
     * See if the command contains exactly a word we're looking for using boundaries. I.e. north not nort or northh
     */
    private static boolean containsExactly(String command, String word) {
        return Pattern.compile("\\b" + word.toLowerCase() + "\\b")
                .matcher(command.toLowerCase()).find();
    }

    private CommandResponse processMovementAction(String command) {

        logger.info("Processing movement action for command [{}]", command);

        return directionRepository.findAll().stream()
                .filter(direction -> containsExactly(command, direction.getDescription()))
                .findFirst()
                .map(direction -> {
                    final Location location = processDirection(direction);

                    //Restore assets associated with this location/state (if any).
                    setStateAssets(location);

                    return new CommandResponse()
                            .setCommand(command)
                            .setResponse(location.getResponse())
                            .setAssets(location.getAssets())
                            .setImagePath(location.getImagePath());
                })
                .orElseThrow(() -> new DirectionNotFoundException("You can't go that way."));
    }

    /**
     * Reads any state entries for the supplied location and consolidates any assets related to those states. For
     * example: you stab the witch, leave the location and then come back. This will load the assets associated with the
     * witch being stabbed rather than the default assets for that location.
     *
     * @param location The supplied {@link Location}.
     */
    private void setStateAssets(Location location) {

        final List<String> assets = locationStateRepository.findByLocationId(location.getLocationId())
                .stream()
                .map(l -> locationActionTargetRepository.findByLocationIdAndActionIdAndTargetId(
                        l.getLocationId(), l.getActionId(), l.getTargetId()))
                .map(Optional::get)
                .map(l -> l.getAssets())
                .collect(Collectors.toList())
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        if (!assets.isEmpty()) {
            location.setAssets(assets);
        }
    }

    private Location processDirection(Direction direction) {

        logger.info("Processing direction [{}] for location [{}] and direction id [{}]", direction.getDescription(),
                currentLocation, direction.getDirectionId());

        if (isGoBack(direction) && !locationHistory.isEmpty()) {
            return goBack();
        }

        return directionLocationRepository.findByCurrentLocationIdAndDirectionIdsIn(currentLocation,
                direction.getDirectionId())
                .map(directionLocation -> setLocationHistory(directionLocation))
                .map(c -> locationRepository.findByLocationId(currentLocation))
                .map(Optional::get)
                .orElseThrow(() -> new DirectionNotFoundException());
    }

    /**
     * Returns the player to the previously recorded location, for as many locations that have been stored.
     */
    private Location goBack() {
        final int previousLocation = locationHistory.get(locationHistory.size() - 1);
        locationHistory.remove(locationHistory.size() - 1);
        currentLocation = previousLocation;

        return locationRepository.findByLocationId(previousLocation)
                .orElseThrow(() -> new LocationNotFoundException("You can't go that way."));
    }

    private DirectionLocation setLocationHistory(DirectionLocation directionLocation) {
        locationHistory.add(currentLocation);
        currentLocation = directionLocation.getDestinationLocationId();

        return directionLocation;
    }

    /**
     * Takes the command submitted by the player and attempts to find an {@link Action} / {@link Target}. If the
     * combination is found, then we also check for any associated state flags, that will either stop the player from
     * repeating an action or stop them performing a conflicting action.
     */
    public CommandResponse processAction(String command) {

        logger.info("Processing action for command [{}]", command);

        final Action action = actionRepository.findAll().stream()
                .filter(a -> command.toLowerCase().contains(a.getDescription().toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new ActionNotFoundException("I don't understand [" + command + "]."));

        final Target target = targetRepository.findAll().stream()
                .filter(t -> command.toLowerCase().contains(t.getDescription().toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new TargetNotFoundException("I don't understand [" + command + "]."));


        //Check if the player already did an action and prevent it from being repeated.
        locationStateRepository.findByLocationIdAndTargetIdAndActionId(currentLocation, target.getTargetId(),
                action.getActionId()).ifPresent(state -> {
            throw new StateHashExistsException("You already did that!");
        });

        final List<String> stateFlags = locationStateRepository.findByLocationId(currentLocation)
                .stream()
                .map(l -> l.getStateFlag())
                .collect(Collectors.toList());

        final LocationActionTarget locationActionTarget = locationActionTargetRepository.findByLocationIdAndActionIdAndTargetId(
                currentLocation, action.getActionId(), target.getTargetId())
                .filter(l -> !l.getBlockers().stream().anyMatch(stateBlocker -> stateFlags.contains(stateBlocker)))
                .orElseThrow(() -> new TargetNotFoundException("You can't do that."));


        //Persist the resulting state
        locationStateRepository.save(new LocationState()
                .setLocationId(currentLocation)
                .setActionId(action.getActionId())
                .setTargetId(target.getTargetId())
                .setStateFlag(locationActionTarget.getStateFlag()));

        logger.info("locationActionTarget [{}]",
                ToStringBuilder.reflectionToString(locationActionTarget, ToStringStyle.MULTI_LINE_STYLE));

        return new CommandResponse()
                .setCommand(command)
                .setImagePath(locationActionTarget.getImagePath())
                .setAssets(locationActionTarget.getAssets())
                .setFatal(locationActionTarget.isFatal())
                .setResponse(locationActionTarget.getResponse())
                .setLocation(currentLocation);
    }

    /**
     * Determines if the command the user typed contains a 'movement' keyword. {@link MovementAction}
     *
     * @param command The string the user has submitted.
     * @return Returns true if the command contains a {@link MovementAction}.
     */
    private boolean isMovementAction(String command) {
        return Arrays.stream(MovementAction.values())
                .anyMatch(movementAction -> command.toLowerCase().contains(movementAction.getIdentifier()));
    }

    private boolean isGoBack(Direction direction) {
        return direction.getDescription().equalsIgnoreCase("back");
    }

}
