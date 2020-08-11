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
import java.util.regex.Pattern;
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

    /**
     * Reset the current location in case of a page refresh.
     */
    public void resetLocation() {
        currentLocation = DEFAULT_LOCATION;
    }

    public CommandResponse processCommand(String command) {

        logger.info("current location [{}]", currentLocation);
        System.out.println("history empty? " + locationHistory.isEmpty());
        locationHistory.forEach(h -> System.out.println(h));

        if (isMovementAction(command)) {
            return processMovementAction(command);
        }

        //Process action / target
        return processAction(command);
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

                    return new CommandResponse()
                            .setCommand(command)
                            .setResponse(location.getResponse())
                            .setImagePath(location.getImagePath());
                })
                .orElseThrow(() -> new DirectionNotFoundException("You can't go that way."));
    }

    private Location processDirection(Direction direction) {

        logger.info("Processing direction [{}] for location [{}] and direction id [{}]", direction.getDescription(),
                currentLocation, direction.getDirectionId());

        final String error = "You can't go that way.";

        if (isGoBack(direction) && !locationHistory.isEmpty()) {
            final int previousLocation = locationHistory.get(locationHistory.size() - 1);
            locationHistory.remove(locationHistory.size() - 1);
            currentLocation = previousLocation;

            return locationRepository.findByLocationId(previousLocation)
                    .orElseThrow(() -> new LocationNotFoundException(error));
        }


        directionLocationRepository.findByCurrentLocationIdAndDirectionId(currentLocation, direction.getDirectionId())
                .map(directionLocation -> setLocationHistory(directionLocation))
                .orElseThrow(() -> new DirectionNotFoundException(error));


        return locationRepository.findByLocationId(currentLocation)
                .orElseThrow(() -> new LocationNotFoundException(error));
    }

    private DirectionLocation setLocationHistory(DirectionLocation directionLocation) {
        locationHistory.add(currentLocation);
        currentLocation = directionLocation.getDestinationLocationId();

        return directionLocation;
    }

    /**
     * Takes the command submitted by the player and attempts to find an {@link Action} / {@link Target}
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


        final int stateHash = currentLocation + action.getActionId() + target.getTargetId();
        locationStateRepository.findByStateHash(stateHash).ifPresent(state -> {
            throw new StateHashExistsException(
                    "You already did that!");
        });
        //TODO: need to read state when moving between locations to re-apply any given states..

        final LocationActionTarget locationActionTarget = locationActionTargetRepository.findByLocationIdAndActionIdAndTargetId(
                currentLocation, action.getActionId(), target.getTargetId())
                .orElseThrow(() -> new TargetNotFoundException("You can't do that here."));

        locationStateRepository.save(new LocationState().setStateHash(stateHash));

        logger.info("locationActionTarget [{}]",
                ToStringBuilder.reflectionToString(locationActionTarget, ToStringStyle.MULTI_LINE_STYLE));

        return new CommandResponse()
                .setCommand(command)
                .setImagePath(locationActionTarget.getImagePath())
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
