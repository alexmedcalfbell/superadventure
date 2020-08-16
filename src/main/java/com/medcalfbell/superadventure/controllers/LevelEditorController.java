package com.medcalfbell.superadventure.controllers;

import com.medcalfbell.superadventure.enums.MovementAction;
import com.medcalfbell.superadventure.models.ActionResponse;
import com.medcalfbell.superadventure.models.ActionTargetResponse;
import com.medcalfbell.superadventure.models.DirectionLocationResponse;
import com.medcalfbell.superadventure.models.DirectionResponse;
import com.medcalfbell.superadventure.models.LevelEditorRequest;
import com.medcalfbell.superadventure.models.LocationResponse;
import com.medcalfbell.superadventure.models.TargetResponse;
import com.medcalfbell.superadventure.persistence.Action;
import com.medcalfbell.superadventure.persistence.Direction;
import com.medcalfbell.superadventure.persistence.DirectionLocation;
import com.medcalfbell.superadventure.persistence.Location;
import com.medcalfbell.superadventure.persistence.LocationActionTarget;
import com.medcalfbell.superadventure.persistence.Target;
import com.medcalfbell.superadventure.services.LevelEditorService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/editor")
public class LevelEditorController {

    private static final Logger logger = LoggerFactory.getLogger(LevelEditorController.class);

    private LevelEditorService levelEditorService;

    @Autowired
    public LevelEditorController(LevelEditorService levelEditorService) {
        this.levelEditorService = levelEditorService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String getLevelData(Model model) {

        final List<Location> locations = levelEditorService.getLocations();
        final List<Action> actions = levelEditorService.getActions();
        final List<Target> targets = levelEditorService.getTargets();
        final List<Direction> directions = levelEditorService.getDirections();
        final List<DirectionLocation> directionLocations = levelEditorService.getDirectionLocations();
        final List<LocationActionTarget> locationActionTargets = levelEditorService.getLocationActionTargets();

        final List<MovementAction> movementActions = Arrays.stream(MovementAction.values()).collect(
                Collectors.toList());

        model.addAttribute("locations", locations);
        model.addAttribute("movementActions", movementActions);
        model.addAttribute("directions", directions);
        model.addAttribute("actions", actions);
        model.addAttribute("targets", targets);
        model.addAttribute("directionLocations", directionLocations);
        model.addAttribute("locationActionTargets", locationActionTargets);

        return "editor";
    }

    @PostMapping(value = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public LocationResponse addLocation(@RequestBody LevelEditorRequest request) {

        final int locationId = levelEditorService.getNextLocationId();

        final String imagePath = String.format("/images/locations/%s", request.getImageName());
        //TODO: set /images/no-image.jpg if nothing set

        return new LocationResponse()
                .setLocation(new Location().setLocationId(locationId))
                .setDescription(request.getDescription())
                .setResponse(request.getResponse())
                .setImagePath(imagePath);
    }

    @PostMapping(value = "/direction", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DirectionResponse addDirection(@RequestBody LevelEditorRequest request) {

        final int directionId = levelEditorService.getNextDirectionId();

        return new DirectionResponse()
                .setDirectionId(directionId)
                .setDescription(request.getDescription());
    }

    @PostMapping(value = "/target", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public TargetResponse addTarget(@RequestBody @Valid LevelEditorRequest request) {

        final int targetId = levelEditorService.getNextTargetId();

        levelEditorService.validateTarget(request.getDescription());

        return new TargetResponse()
                .setTargetId(targetId)
                .setDescription(request.getDescription());
    }

    @PostMapping(value = "/action", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ActionResponse addAction(@RequestBody @Valid LevelEditorRequest request) {

        final int actionId = levelEditorService.getNextActionId();

        levelEditorService.validateAction(request.getDescription());

        return new ActionResponse()
                .setActionId(actionId)
                .setDescription(request.getDescription());
    }

    @PostMapping(value = "/action-target", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ActionTargetResponse addLocationDirection(@RequestBody LevelEditorRequest request) {

        final int locationId = request.getCurrentLocationId();
        final int actionId = request.getActionId();
        final int targetId = request.getTargetId();

        //TODO: Move business logic to service.
        levelEditorService.validateLocationActionTarget(locationId, actionId, targetId);

        final Location location = levelEditorService.getLocation(locationId);
        final Action action = levelEditorService.getAction(actionId);
        final Target target = levelEditorService.getTarget(targetId);

        final String description = String.format("Location [%s] linked to action [%s] and target [%s]",
                location.getDescription(), action.getDescription(), target.getDescription());

        final String imagePath = String.format("/images/locations/%s", request.getImageName());

        return new ActionTargetResponse()
                .setDescription(description)
                .setLocation(location)
                .setActionId(actionId)
                .setTargetId(targetId)
                .setResponse(request.getResponse())
                .setImagePath(imagePath);
    }

    @PostMapping(value = "/location-direction", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DirectionLocationResponse addActionTarget(@RequestBody LevelEditorRequest request) {

        final int currentLocationId = request.getCurrentLocationId();
        final int destinationLocationId = request.getDestinationLocationId();
        final int directionId = request.getDirectionId();

        //TODO: Move logic to service.
        levelEditorService.validateLocationDirection(currentLocationId, destinationLocationId, directionId);

        final Location currentLocation = levelEditorService.getLocation(currentLocationId);
        final Location destinationLocation = levelEditorService.getLocation(destinationLocationId);

        final List<DirectionLocation> primaryLocationsForCurrentLocation = levelEditorService.getPrimaryDirectionLocationsForCurrentLocation(
                currentLocationId);


        //TODO: Refactor.
        Optional<Location> north = primaryLocationsForCurrentLocation.stream().filter(d -> d.getDirectionId() == 1)
                .map(location -> levelEditorService.getLocation(location.getDestinationLocationId()))
                .findFirst();
        Optional<Location> south = primaryLocationsForCurrentLocation.stream().filter(d -> d.getDirectionId() == 2)
                .map(location -> levelEditorService.getLocation(location.getDestinationLocationId()))
                .findFirst();
        Optional<Location> east = primaryLocationsForCurrentLocation.stream().filter(d -> d.getDirectionId() == 3)
                .map(location -> levelEditorService.getLocation(location.getDestinationLocationId()))
                .findFirst();
        Optional<Location> west = primaryLocationsForCurrentLocation.stream().filter(d -> d.getDirectionId() == 4)
                .map(location -> levelEditorService.getLocation(location.getDestinationLocationId()))
                .findFirst();

        final DirectionLocationResponse directionLocationResponse = new DirectionLocationResponse();
        north.ifPresent(location -> directionLocationResponse.setLocationNorth(location));
        south.ifPresent(location -> directionLocationResponse.setLocationSouth(location));
        east.ifPresent(location -> directionLocationResponse.setLocationEast(location));
        west.ifPresent(location -> directionLocationResponse.setLocationWest(location));
        if (directionId == 1) {
            directionLocationResponse.setLocationNorth(destinationLocation);
        } else if (directionId == 2) {
            directionLocationResponse.setLocationSouth(destinationLocation);
        } else if (directionId == 3) {
            directionLocationResponse.setLocationEast(destinationLocation);
        } else if (directionId == 4) {
            directionLocationResponse.setLocationWest(destinationLocation);
        }
        directionLocationResponse.setCurrentLocation(levelEditorService.getLocation(currentLocationId));

        final String description = String.format("Location [%s] linked to location [%s] via direction [%s]",
                currentLocation.getDescription(), destinationLocation.getDescription(), directionId);

        return directionLocationResponse
                .setDescription(description)
                .setCurrentLocation(currentLocation)
                .setDestinationLocation(destinationLocation)
                .setDirectionId(directionId);
    }

    @GetMapping(value = "/linked-locations/{currentLocationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DirectionLocationResponse getLinkedLocationsForCurrentLocation(@PathVariable int currentLocationId) {
        List<DirectionLocation> primaryLocationsForCurrentLocation = levelEditorService.getPrimaryDirectionLocationsForCurrentLocation(
                currentLocationId);

        final DirectionLocationResponse directionLocationResponse = new DirectionLocationResponse();
        primaryLocationsForCurrentLocation.stream().filter(d -> d.getDirectionId() == 1)
                .map(l -> levelEditorService.getLocation(l.getDestinationLocationId()))
                .findFirst()
                .map(location -> directionLocationResponse.setLocationNorth(location))
                .map(location -> directionLocationResponse.setActionTargetsNorth(
                        levelEditorService.getLocationActionTargetsForLocation(
                                location.getLocationNorth().getLocationId()))
                );

        primaryLocationsForCurrentLocation.stream().filter(d -> d.getDirectionId() == 2)
                .map(l -> levelEditorService.getLocation(l.getDestinationLocationId()))
                .findFirst()
                .map(location -> directionLocationResponse.setLocationSouth(location))
                .map(location -> directionLocationResponse.setActionTargetsSouth(
                        levelEditorService.getLocationActionTargetsForLocation(
                                location.getLocationSouth().getLocationId()))
                );

        primaryLocationsForCurrentLocation.stream().filter(d -> d.getDirectionId() == 3)
                .map(l -> levelEditorService.getLocation(l.getDestinationLocationId()))
                .findFirst()
                .map(location -> directionLocationResponse.setLocationEast(location))
                .map(location -> directionLocationResponse.setActionTargetsEast(
                        levelEditorService.getLocationActionTargetsForLocation(
                                location.getLocationEast().getLocationId()))
                );

        primaryLocationsForCurrentLocation.stream().filter(d -> d.getDirectionId() == 4)
                .map(l -> levelEditorService.getLocation(l.getDestinationLocationId()))
                .findFirst()
                .map(location -> directionLocationResponse.setLocationWest(location))
                .map(location -> directionLocationResponse.setActionTargetsWest(
                        levelEditorService.getLocationActionTargetsForLocation(
                                location.getLocationWest().getLocationId()))
                );

        directionLocationResponse.setCurrentLocation(levelEditorService.getLocation(currentLocationId));
        directionLocationResponse.setActionTargetsCurrent(
                levelEditorService.getLocationActionTargetsForLocation(currentLocationId));


        return directionLocationResponse;
    }

}
