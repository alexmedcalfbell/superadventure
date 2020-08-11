package com.medcalfbell.superadventure.controllers;

import com.medcalfbell.superadventure.enums.MovementAction;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelpController {


    @GetMapping(value = "/help")
    public String getHelpInfo(Model model) {

        final List<String> movementActions = Arrays.stream(MovementAction.values())
                .map(MovementAction::getIdentifier)
                .collect(Collectors.toList());

        model.addAttribute("movementActions", movementActions);

        return "help";
    }

}
