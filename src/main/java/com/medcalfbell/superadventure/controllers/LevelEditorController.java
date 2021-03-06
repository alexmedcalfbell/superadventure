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
import com.medcalfbell.superadventure.persistence.Location;
import com.medcalfbell.superadventure.persistence.Target;
import com.medcalfbell.superadventure.persistence.repositories.ActionRepository;
import com.medcalfbell.superadventure.persistence.repositories.LocationActionTargetRepository;
import com.medcalfbell.superadventure.persistence.repositories.LocationRepository;
import com.medcalfbell.superadventure.persistence.repositories.TargetRepository;
import com.medcalfbell.superadventure.services.AwsService;
import com.medcalfbell.superadventure.services.LevelEditorService;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/editor")
public class LevelEditorController {

    private static final Logger logger = LoggerFactory.getLogger(LevelEditorController.class);

    private LevelEditorService levelEditorService;
    private LocationRepository locationRepository;
    private LocationActionTargetRepository locationActionTargetRepository;
    private ActionRepository actionRepository;
    private TargetRepository targetRepository;

    private AwsService awsService;

    @Value("classpath:static/images/assets/*")
    private Resource[] assets;

    @Autowired
    public LevelEditorController(LevelEditorService levelEditorService,
            LocationRepository locationRepository,
            LocationActionTargetRepository locationActionTargetRepository,
            ActionRepository actionRepository,
            TargetRepository targetRepository, AwsService awsService) {
        this.levelEditorService = levelEditorService;
        this.locationRepository = locationRepository;
        this.locationActionTargetRepository = locationActionTargetRepository;
        this.actionRepository = actionRepository;
        this.targetRepository = targetRepository;
        this.awsService = awsService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String getLevelData(Model model) {

        model.addAttribute("locations", levelEditorService.getLocations());
        model.addAttribute("movementActions",
                Arrays.stream(MovementAction.values())
                        .collect(Collectors.toList())
        );
        model.addAttribute("directions", levelEditorService.getDirections());
        model.addAttribute("actions", levelEditorService.getActions());
        model.addAttribute("targets", levelEditorService.getTargets());
        model.addAttribute("directionLocations", levelEditorService.getDirectionLocations());
        model.addAttribute("locationActionTargets", levelEditorService.getLocationActionTargets());
        model.addAttribute("assets", Arrays.stream(assets)
                .map(resource -> resource.getFilename())
                .collect(Collectors.toList())
        );

        return "editor";
    }

    @PostMapping(value = "/location")
    @ResponseBody
    public LocationResponse addLocation(@RequestParam("file") MultipartFile file,
            @RequestParam("description") String description,
            @RequestParam("response") String response) throws IOException {

        final String assetUrl = awsService.uploadAsset(file);

        //Persist location
        final Location location = new Location()
                .setDescription(description)
                .setResponse(response)
                .setImagePath(assetUrl);

        locationRepository.save(location);

        return new LocationResponse()
                .setLocation(new Location())
                .setDescription(description)
                .setResponse(response)
                .setImagePath(assetUrl);
    }

    @PostMapping(value = "/direction", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DirectionResponse addDirection(@RequestBody LevelEditorRequest request) {

        return new DirectionResponse()
                .setDescription(request.getDescription());
    }

    @PostMapping(value = "/action", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ActionResponse addAction(@RequestBody @Valid LevelEditorRequest request) {

        levelEditorService.validateAction(request.getDescription());

        actionRepository.save(new Action().setDescription(request.getDescription()));
        logger.info("Saved action [{}]", request.getDescription());

        return new ActionResponse()
                .setDescription(request.getDescription());
    }

    @PostMapping(value = "/target", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public TargetResponse addTarget(@RequestBody @Valid LevelEditorRequest request) {

        levelEditorService.validateTarget(request.getDescription());

        targetRepository.save(new Target().setDescription(request.getDescription()));
        logger.info("Saved target [{}]", request.getDescription());

        return new TargetResponse()
                .setDescription(request.getDescription());
    }

    @PostMapping(value = "/action-target", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ActionTargetResponse addActionTarget(@RequestBody LevelEditorRequest request) {

        final String locationId = request.getCurrentLocationId();
        final List<String> actions = request.getActions();
        final List<String> targets = request.getTargets();
        final List<String> assets = request.getAssets().stream()
                .map(asset -> "/images/assets/" + asset)
                .collect(Collectors.toList());

        //TODO: Move business logic to service.
        //        levelEditorService.validateLocationActionTarget(locationId, actionId, targetId);

        final Location location = levelEditorService.getLocation(locationId);

        final String description = String.format("Location [%s] linked to actions %s and targets %s",
                location.getDescription(), actions, targets);

        return new ActionTargetResponse()
                .setDescription(description)
                .setLocation(location)
                .setActions(actions)
                .setTargets(targets)
                .setFatal(request.isFatal())
                .setResponse(request.getResponse())
                .setAssets(assets);
    }

    @PostMapping(value = "/location-direction", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DirectionLocationResponse addLocationDirection(@RequestBody LevelEditorRequest request) {

        final String currentLocationId = request.getCurrentLocationId();
        final String destinationLocationId = request.getDestinationLocationId();
        final String directionId = request.getDirectionId();

        //TODO: Move logic to service.
        levelEditorService.validateLocationDirection(currentLocationId, destinationLocationId, directionId);

        final Location currentLocation = levelEditorService.getLocation(currentLocationId);
        final Location destinationLocation = levelEditorService.getLocation(destinationLocationId);

        final DirectionLocationResponse response = new DirectionLocationResponse();
        levelEditorService.getDirectionLocationsForCurrentLocation(currentLocationId).stream()
                .forEach(dirLoc -> {
                    final Location location = levelEditorService.getLocation(dirLoc.getDestinationLocationId());
                    if (dirLoc.getDirectionIds().contains("north")) {
                        response.setLocationNorth(location);
                    } else if (dirLoc.getDirectionIds().contains("south")) {
                        response.setLocationSouth(location);
                    } else if (dirLoc.getDirectionIds().contains("east")) {
                        response.setLocationEast(location);
                    } else if (dirLoc.getDirectionIds().contains("west")) {
                        response.setLocationWest(location);
                    }
                });

        response.setCurrentLocation(levelEditorService.getLocation(currentLocationId));

        //TODO: Add support for directionIds / targetids

        final String description = String.format("Location [%s] linked to location [%s] via direction [%s]",
                currentLocation.getDescription(), destinationLocation.getDescription(), directionId);

        return response
                .setDescription(description)
                .setCurrentLocation(currentLocation)
                .setDestinationLocation(destinationLocation)
                .setDirectionId(directionId);
    }

    @GetMapping(value = "/linked-locations/{currentLocation}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DirectionLocationResponse getLinkedLocationsForCurrentLocation(@PathVariable String currentLocation) {

        final DirectionLocationResponse response = new DirectionLocationResponse();

        levelEditorService.getDirectionLocationsForCurrentLocation(currentLocation).stream()
                .forEach(dirLoc -> {
                    final Location location = levelEditorService.getLocation(dirLoc.getDestinationLocationId());
                    if (dirLoc.getDirectionIds().contains("north")) {
                        response.setLocationNorth(location);
                    } else if (dirLoc.getDirectionIds().contains("south")) {
                        response.setLocationSouth(location);
                    } else if (dirLoc.getDirectionIds().contains("east")) {
                        response.setLocationEast(location);
                    } else if (dirLoc.getDirectionIds().contains("west")) {
                        response.setLocationWest(location);
                    }
                });

        //Set action/targets for the current location
        response.setActionTargets(
                locationActionTargetRepository.findAllByLocationId(currentLocation).stream()
                        .map(lat -> " linked to actions [<keyword>" + lat.getActions().stream()
                                .map(t -> t.getDescription()).collect(Collectors.joining(", ")) + "</keyword>]"
                                + " and targets [<keyword>" + lat.getTargets().stream()
                                .map(t -> t.getDescription()).collect(Collectors.joining(", ")) + "</keyword>]"
                        )
                        .collect(Collectors.toList())
        );
        response.setCurrentLocation(levelEditorService.getLocation(currentLocation));

        return response;
    }

}
