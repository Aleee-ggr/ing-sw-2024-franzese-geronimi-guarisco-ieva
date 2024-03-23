package it.polimi.ingsw.helpers.builders;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.Resource;

import java.util.function.Function;

public class FunctionBuilder {
    private int points;
    private String type;
    private Resource resource;
    private Resource[][] pattern;
    public FunctionBuilder setPoints(int points) {
        this.points = points;
        return this;
    }

    public FunctionBuilder setResource(Resource resource) {
        this.resource = resource;
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

    public Function<Game, Integer> build() {
        switch (type) {
            case "none":
                return (Game game) -> points;

            default:
                return (Game game) -> {
                    //TODO implement the actual function
                    return -1;
                };
        }
    }
}
