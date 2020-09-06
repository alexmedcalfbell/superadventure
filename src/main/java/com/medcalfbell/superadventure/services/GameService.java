package com.medcalfbell.superadventure.services;

import com.medcalfbell.superadventure.enums.InventoryAction;
import com.medcalfbell.superadventure.enums.MovementAction;
import com.medcalfbell.superadventure.exceptions.ActionNotFoundException;
import com.medcalfbell.superadventure.exceptions.DirectionNotFoundException;
import com.medcalfbell.superadventure.exceptions.InventoryActionNotFoundException;
import com.medcalfbell.superadventure.exceptions.ItemNotFoundException;
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
import com.medcalfbell.superadventure.persistence.Player;
import com.medcalfbell.superadventure.persistence.Target;
import com.medcalfbell.superadventure.persistence.repositories.ActionRepository;
import com.medcalfbell.superadventure.persistence.repositories.DirectionLocationRepository;
import com.medcalfbell.superadventure.persistence.repositories.DirectionRepository;
import com.medcalfbell.superadventure.persistence.repositories.ItemRepository;
import com.medcalfbell.superadventure.persistence.repositories.LocationActionTargetRepository;
import com.medcalfbell.superadventure.persistence.repositories.LocationRepository;
import com.medcalfbell.superadventure.persistence.repositories.LocationStateRepository;
import com.medcalfbell.superadventure.persistence.repositories.PlayerRepository;
import com.medcalfbell.superadventure.persistence.repositories.TargetRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    public static final String DEFAULT_LOCATION = "home";
    private static final int DEAD = 0;

    private long playerId;
    private String currentLocation = DEFAULT_LOCATION;
    private List<String> locationHistory = new ArrayList<>();

    private LocationRepository locationRepository;
    private TargetRepository targetRepository;
    private LocationActionTargetRepository locationActionTargetRepository;
    private ActionRepository actionRepository;
    private DirectionRepository directionRepository;
    private DirectionLocationRepository directionLocationRepository;
    private LocationStateRepository locationStateRepository;
    private PlayerRepository playerRepository;
    private ItemRepository itemRepository;

    public GameService(LocationRepository locationRepository,
            TargetRepository targetRepository,
            LocationActionTargetRepository locationActionTargetRepository,
            ActionRepository actionRepository,
            DirectionRepository directionRepository,
            DirectionLocationRepository directionLocationRepository,
            LocationStateRepository locationStateRepository,
            PlayerRepository playerRepository,
            ItemRepository itemRepository) {
        this.locationRepository = locationRepository;
        this.targetRepository = targetRepository;
        this.locationActionTargetRepository = locationActionTargetRepository;
        this.actionRepository = actionRepository;
        this.directionRepository = directionRepository;
        this.directionLocationRepository = directionLocationRepository;
        this.locationStateRepository = locationStateRepository;
        this.playerRepository = playerRepository;
        this.itemRepository = itemRepository;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    //TODO: Map. Do we want them to be able to see the map? If so re-use level editor ui.
    public CommandResponse getMap() {
        return null;
    }

    /**
     * Reset the current location / state in case of a page refresh.
     */
    public void reset() {
        currentLocation = DEFAULT_LOCATION;
        locationHistory.clear();
        locationStateRepository.deleteAll();
    }

    public CommandResponse processCommand(String command) {

        if (isHelp(command)) {
            return getHelp(command);
        }
        if (isWhere(command)) {
            return getWhere(command);
        }
        if (isMovementAction(command)) {
            return processMovementAction(command);
        }
        if (isInventoryAction(command)) {
            return processInventoryAction(command);
        }
        if (isItems(command)) {
            return getItems(command);
        }
        //TODO: List available items

        //Process action / target
        return processAction(command);
    }


    private void updatePlayerHp(int hp) {
        //        playerRepository.save();
    }

    /**
     * Returns true if command contains 'help' keyword. 'Help' is different from 'where'. Help is more of a 'cheat',
     * whereas 'where' just describes the scene.
     */
    private boolean isHelp(String command) {
        return command.toLowerCase().contains("help");
    }

    /**
     * Returns true if command contains 'where' keyword.
     */
    private boolean isWhere(String command) {
        return command.toLowerCase().contains("where");
    }


    /* Returns true if command contains 'items' keyword.
     */
    private boolean isItems(String command) {
        return command.toLowerCase().contains("items") || command.toLowerCase().equalsIgnoreCase("i");
    }

    private CommandResponse getItems(String command) {


        String items = playerRepository.findById(playerId)
                .filter(player -> !player.getItems().isEmpty())
                .map(player -> player.getItems())
                .orElse(List.of("Nothing")).stream()
                .map(item -> "<li><keyword>" + item + "</keyword></li>")
                .collect(Collectors.joining());


        final String response = String.format("You have <ul>%s</ul>", items);

        return new CommandResponse()
                .setCommand(command)
                .setResponse(response);
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
        Optional<Location> location = locationRepository.findByDescription(currentLocation);

        String targets = targetRepository.findByDescriptionIn(
                locationActionTargetRepository.findAllByLocationId(currentLocation).stream()
                        .flatMap(actionTarget -> actionTarget.getTargets().stream())
                        .map(target -> target.getDescription())
                        .collect(Collectors.toList())
        ).stream()
                .map(target -> target.getDescription())
                .collect(Collectors.toCollection(TreeSet::new)).stream()
                .map(target -> "<li><help>" + target + "</help></li>")
                .collect(Collectors.joining());

        if (StringUtils.isEmpty(targets)) {
            targets = "<help>Nothing to interact with here.</help>";
        }

        final String locations = directionRepository.findByDescriptionIn(
                directionLocationRepository.findByCurrentLocationId(currentLocation).stream()
                        .flatMap(d -> d.getDirectionIds().stream())
                        .collect(Collectors.toList())).stream()
                .map(d -> "<li><help>" + d.getDescription() + "</help></li>")
                .collect(Collectors.joining());

        final String response = String.format(
                "You're at: <help>%s</help>. You can see: <ul>%s</ul> You can move: <ul>%s</ul>",
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

    public Player createPlayer() {
        return playerRepository.save(new Player());
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
     * Processes actions that interact with the inventory e.g. 'pick up'. If the item is found then it is added to the
     * player's inventory.
     *
     * @param command The supplied command text.
     * @return {@link CommandResponse}
     */
    private CommandResponse processInventoryAction(String command) {

        logger.info("Processing inventory action for command [{}]", command);

        return itemRepository.findAll().stream()
                .filter(item -> containsExactly(command, item.getName()))
                .findFirst()
                .map(item -> {

                    logger.info("Found item [{}] at location [{}]", item.getDescription(), currentLocation);

                    playerRepository.findById(playerId).ifPresent(player -> {
                        player.getItems().add(item.getName());
                        playerRepository.save(player);
                        logger.info("Added item [{}] to player [{}]", item.getName(), playerId);
                    });

                    return new CommandResponse()
                            .setCommand(command)
                            .setResponse("you pick up " + item.getDescription());
                })
                .orElseThrow(() -> new InventoryActionNotFoundException("You can't get that."));
    }

    /**
     * Reads any state entries for the supplied location and consolidates any assets related to those states. For
     * example: you stab the witch, leave the location and then come back. This will load the assets associated with the
     * witch being stabbed rather than the default assets for that location.
     *
     * @param location The supplied {@link Location}.
     */
    private void setStateAssets(Location location) {

        final List<String> assets = locationStateRepository.findByLocationId(location.getDescription())
                .stream()
                .map(l -> locationActionTargetRepository.findByLocationIdAndActionsDescriptionAndTargetsDescription(
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
                currentLocation, direction.getDescription());

        if (isGoBack(direction) && !locationHistory.isEmpty()) {
            return goBack();
        }

        return directionLocationRepository.findByCurrentLocationIdAndDirectionIdsIn(currentLocation,
                direction.getDescription())
                .map(directionLocation -> setLocationHistory(directionLocation))
                .map(c -> locationRepository.findByDescription(currentLocation))
                .map(Optional::get)
                .orElseThrow(() -> new DirectionNotFoundException());
    }

    /**
     * Returns the player to the previously recorded location, for as many locations that have been stored.
     */
    private Location goBack() {
        final String previousLocation = locationHistory.get(locationHistory.size() - 1);
        locationHistory.remove(locationHistory.size() - 1);
        currentLocation = previousLocation;

        return locationRepository.findByDescription(previousLocation)
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

        itemRepository.findByName(target.getDescription()).ifPresent(item -> {
            logger.info("Target [{}] is an item. Checking player inventory.", target.getDescription());

            playerRepository.findById(playerId)
                    .filter(player -> player.getItems().contains(item.getName()))
                    .orElseThrow(() -> new ItemNotFoundException("You don't have that item."));

        });

        //Check if the player already did an action and prevent it from being repeated.
        locationStateRepository.findByLocationIdAndTargetIdAndActionId(currentLocation, target.getDescription(),
                action.getDescription()).ifPresent(state -> {
            throw new StateHashExistsException("You already did that!");
        });

        final List<String> stateFlags = locationStateRepository.findByLocationId(currentLocation)
                .stream()
                .map(l -> l.getStateFlag())
                .collect(Collectors.toList());


        logger.info("location [{}], action[{}], target [{}]", currentLocation, target.getDescription(),
                action.getDescription());
        final LocationActionTarget locationActionTarget = locationActionTargetRepository.findByLocationIdAndActionsDescriptionAndTargetsDescription(
                currentLocation, action.getDescription(), target.getDescription())
                .filter(l -> !l.getBlockers().stream().anyMatch(stateBlocker -> stateFlags.contains(stateBlocker)))
                .orElseThrow(() -> new TargetNotFoundException("You can't do that."));

        //Persist the resulting state
        //TODO: state should support multiple actions / targets
        locationStateRepository.save(new LocationState()
                .setLocationId(currentLocation)
                .setActionId(action.getDescription())
                .setTargetId(target.getDescription())
                .setStateFlag(locationActionTarget.getStateFlag()));

        return new CommandResponse()
                .setCommand(command)
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


    /**
     * Determines if the command the user typed contains an 'inventory' keyword. {@link InventoryAction}
     *
     * @param command The string the user has submitted.
     * @return Returns true if the command contains an {@link InventoryAction}.
     */
    private boolean isInventoryAction(String command) {
        return Arrays.stream(InventoryAction.values())
                .anyMatch(inventoryAction -> command.toLowerCase().contains(inventoryAction.getIdentifier()));
    }

    private boolean isGoBack(Direction direction) {
        return direction.getDescription().equalsIgnoreCase("back");
    }

}
