package it.polimi.ingsw.helpers.builders;

import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.player.Player;

import java.util.Map;
import java.util.function.Function;

public class FunctionBuilder {
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

    public FunctionBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public Function<Player, Integer> build() {
        switch (type) {
            case "none":
                return (Player player) -> points;

            default:
                return (Player player) -> {
                    //TODO implement the actual function
                    return points;
                };
        }
    }

    public Resource getResource() {
        return resource;
    }

    public FunctionBuilder setResource(Resource resource) {
        this.resource = resource;
        return this;
    }
}
