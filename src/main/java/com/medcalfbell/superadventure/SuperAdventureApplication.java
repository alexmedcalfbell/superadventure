package com.medcalfbell.superadventure;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medcalfbell.superadventure.persistence.Action;
import com.medcalfbell.superadventure.persistence.Direction;
import com.medcalfbell.superadventure.persistence.DirectionLocation;
import com.medcalfbell.superadventure.persistence.Location;
import com.medcalfbell.superadventure.persistence.LocationActionTarget;
import com.medcalfbell.superadventure.persistence.Target;
import com.medcalfbell.superadventure.persistence.repositories.ActionRepository;
import com.medcalfbell.superadventure.persistence.repositories.DirectionLocationRepository;
import com.medcalfbell.superadventure.persistence.repositories.DirectionRepository;
import com.medcalfbell.superadventure.persistence.repositories.LocationActionTargetRepository;
import com.medcalfbell.superadventure.persistence.repositories.LocationRepository;
import com.medcalfbell.superadventure.persistence.repositories.TargetRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class SuperAdventureApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuperAdventureApplication.class, args);
    }

    @Value("classpath:test.json")
    private Resource actions;

    @Bean
    CommandLineRunner runner(LocationRepository locationRepository,
            TargetRepository targetRepository,
            LocationActionTargetRepository locationActionTargetRepository,
            ActionRepository actionRepository,
            DirectionRepository directionRepository,
            DirectionLocationRepository directionLocationRepository) {
        return args -> {

            //TODO: Generic method to read resources.
            final ObjectMapper objectMapper = new ObjectMapper();

            //Locations
            List<Location> locations = objectMapper.readValue(
                    getClass().getResourceAsStream("/json/locations.json"),
                    new TypeReference<>() {});
            locationRepository.saveAll(locations);

            List<LocationActionTarget> locationActionTargets = objectMapper.readValue(
                    getClass().getResourceAsStream("/json/locationActionTargets.json"),
                    new TypeReference<>() {});
            locationActionTargetRepository.saveAll(locationActionTargets);

            List<DirectionLocation> directionLocations = objectMapper.readValue(
                    getClass().getResourceAsStream("/json/directionLocations.json"),
                    new TypeReference<>() {});
            directionLocationRepository.saveAll(directionLocations);

            //Directions
            List<Direction> directions = objectMapper.readValue(
                    getClass().getResourceAsStream("/json/directions.json"),
                    new TypeReference<>() {});
            directionRepository.saveAll(directions);

            //Actions (e.g. talk)
            List<Action> actions = objectMapper.readValue(
                    getClass().getResourceAsStream("/json/actions.json"),
                    new TypeReference<>() {});
            actionRepository.saveAll(actions);

            //Targets (e.g. jester)
            List<Target> targets = objectMapper.readValue(
                    getClass().getResourceAsStream("/json/targets.json"),
                    new TypeReference<>() {});
            targetRepository.saveAll(targets);

        };
    }

}
