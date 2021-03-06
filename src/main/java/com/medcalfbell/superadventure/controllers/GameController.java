package com.medcalfbell.superadventure.controllers;

import com.medcalfbell.superadventure.enums.MovementAction;
import com.medcalfbell.superadventure.exceptions.LocationNotFoundException;
import com.medcalfbell.superadventure.models.CommandRequest;
import com.medcalfbell.superadventure.models.CommandResponse;
import com.medcalfbell.superadventure.persistence.Location;
import com.medcalfbell.superadventure.persistence.Player;
import com.medcalfbell.superadventure.persistence.repositories.LocationRepository;
import com.medcalfbell.superadventure.services.GameService;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for primary game functions.
 *
 * @author Alexander Medcalf-Bell.
 */
@Controller
public class GameController {

    private GameService gameService;
    private LocationRepository locationRepository;

    @Autowired
    public GameController(GameService gameService,
            LocationRepository locationRepository) {
        this.gameService = gameService;
        this.locationRepository = locationRepository;
    }

    @ResponseBody
    @GetMapping(value = "/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandResponse startGame(HttpServletResponse response) {

        gameService.reset();

        final Location location = locationRepository.findByDescription(GameService.DEFAULT_LOCATION)
                .orElseThrow(() -> new LocationNotFoundException(
                        "Location [" + GameService.DEFAULT_LOCATION + "] not found"));


        final Player player = gameService.createPlayer();
        response.addCookie(new Cookie("player_id", String.valueOf(player.getId())));

        return new CommandResponse()
                .setImagePath(location.getImagePath())
                .setAssets(location.getAssets())
                .setResponse(location.getResponse());
    }

    @GetMapping(value = "/")
    public String getGameInfo(Model model) {

        final List<String> movementActions = Arrays.stream(MovementAction.values())
                .map(MovementAction::getIdentifier)
                .collect(Collectors.toList());

        model.addAttribute("movementActions", movementActions);

        return "game";
    }

    @ResponseBody
    @PostMapping(value = "/command", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommandResponse processCommand(@CookieValue(value = "player_id") long playerId,
            @RequestBody CommandRequest request) {
        gameService.setPlayerId(playerId);
        return gameService.processCommand(request.getCommand());
    }

}
