package it.polimi.ingsw.helpers.builders;

import it.polimi.ingsw.helpers.exceptions.InvalidTypeException;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.player.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FunctionBuilder {

    private static final String[] validTypes = {
            "none",
            "pattern",
            "resources",
            "FUNGI",
            "PLANT",
            "INSECT",
            "ANIMAL",
            "QUILL",
            "INKWELL",
            "MANUSCRIPT"
    };
    private int points;
    private String type;
    private  Map<Resource, Integer> resources;
    private Resource[][] pattern;

    private Resource resource;

    public Map<Resource, Integer> getResources() {
        return resources;
    }

    public FunctionBuilder setResources(Map<Resource, Integer> resources) {
        this.resources = resources;
        return this;
    }

    public FunctionBuilder setPoints(int points) {
        this.points = points;
        return this;
    }

    public FunctionBuilder setPattern(Resource[][] pattern) {
        this.pattern = pattern;
        return this;
    }

    public FunctionBuilder setType(String type) throws InvalidTypeException {
        if (!Arrays.asList(validTypes).contains(type)) {
            throw new InvalidTypeException(type + " is not a valid type");
        }
        this.type = type;
        return this;
    }

    public Function<Player, Integer> build() {
        return switch (type) {
            case "none" ->
                    (Player player) -> points;

            case "pattern" ->
                    (Player player) -> {
                    //TODO implement the actual function
                    return points;
                };

            case "resources" ->
                (Player player) -> {
                    Map<Resource, Integer> playerResources = player.getResources();
                    int resourcePoints;
                    Map<Resource, Integer> result = new HashMap<>();
                    for (Resource res : resources.keySet()) {
                        resourcePoints = (playerResources.get(res) / resources.get(res)) * points;
                        result.put(res, resourcePoints);
                    }
                    return Collections.min(result.values());
                };
            default ->
                (Player player) -> {
                    Map<Resource, Integer> playerResources = player.getResources();
                    return playerResources.get(resource) * points;
                };
        };
    }

    public Resource getResource() {
        return resource;
    }

    public FunctionBuilder setResource(Resource resource) {
        this.resource = resource;
        return this;
    }
}
