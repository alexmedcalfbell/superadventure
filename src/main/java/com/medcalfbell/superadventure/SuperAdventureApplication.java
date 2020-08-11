package com.medcalfbell.superadventure;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medcalfbell.superadventure.persistence.Action;
import com.medcalfbell.superadventure.persistence.repositories.ActionRepository;
import com.medcalfbell.superadventure.persistence.Direction;
import com.medcalfbell.superadventure.persistence.DirectionLocation;
import com.medcalfbell.superadventure.persistence.repositories.DirectionLocationRepository;
import com.medcalfbell.superadventure.persistence.repositories.DirectionRepository;
import com.medcalfbell.superadventure.persistence.Location;
import com.medcalfbell.superadventure.persistence.LocationActionTarget;
import com.medcalfbell.superadventure.persistence.repositories.LocationActionTargetRepository;
import com.medcalfbell.superadventure.persistence.repositories.LocationRepository;
import com.medcalfbell.superadventure.persistence.Target;
import com.medcalfbell.superadventure.persistence.repositories.TargetRepository;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            List<Action> action = objectMapper.readValue(
                    new File(actions.getFile().getPath()),
                    new TypeReference<>() {});
            actionRepository.saveAll(action);

            Files.walk(Path.of(getClass().getResource("/json/locations").toURI()))
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            final List<Location> locations = objectMapper.readValue(
                                    file.toFile(),
                                    new TypeReference<>() {});
                            locationRepository.saveAll(locations);
                        } catch (IOException e) {
                            System.out.println("Failed to read location" + e.getMessage());
                        }
                    });

            //Directions
            List<Direction> directions = objectMapper.readValue(
                    new File(getClass().getResource("/json/directions.json").getPath()),
                    new TypeReference<>() {});
            directionRepository.saveAll(directions);

            // Direction Locations (links locations to directions)
            Files.walk(Path.of(getClass().getResource("/json/direction_locations").toURI()))
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            final List<DirectionLocation> directionLocations = objectMapper.readValue(
                                    file.toFile(),
                                    new TypeReference<>() {});
                            directionLocationRepository.saveAll(directionLocations);
                        } catch (IOException e) {
                            System.out.println("Failed to read direction location" + e.getMessage());
                        }
                    });

            //Actions (e.g. talk)
            List<Action> actions = objectMapper.readValue(
                    new File(getClass().getResource("/json/actions.json").getPath()),
                    new TypeReference<>() {});
            actionRepository.saveAll(actions);

            //Targets (e.g. jester)
            List<Target> targets = objectMapper.readValue(
                    new File(getClass().getResource("/json/targets.json").getPath()),
                    new TypeReference<>() {});
            targetRepository.saveAll(targets);

            //TODO: Make function for these
            //Location action targets (links actions and targets to a location)
            //Locations
            Files.walk(Path.of(getClass().getResource("/json/location_action_targets").toURI()))
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            final List<LocationActionTarget> locationActionTargets = objectMapper.readValue(
                                    file.toFile(),
                                    new TypeReference<>() {});
                            locationActionTargetRepository.saveAll(locationActionTargets);
                        } catch (IOException e) {
                            System.out.println("Failed to read location action targets" + e.getMessage());
                        }
                    });
        };
    }

}
