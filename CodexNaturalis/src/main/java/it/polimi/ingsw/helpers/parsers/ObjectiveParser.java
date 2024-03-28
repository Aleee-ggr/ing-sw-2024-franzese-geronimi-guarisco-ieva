package it.polimi.ingsw.helpers.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.helpers.builders.FunctionBuilder;
import it.polimi.ingsw.helpers.exceptions.InvalidTypeException;
import it.polimi.ingsw.helpers.exceptions.JsonFormatException;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static it.polimi.ingsw.GameConsts.resourceMap;

public class ObjectiveParser implements JsonParser<Deck<Objective>> {
    private String json;

    @Override
    public void readFile(Path path) throws IOException {
        json = Files.readString(path);
    }

    @Override
    public void readString(String json) {
        this.json = json;
    }

    @Override
    public Deck<Objective> parse() throws JsonFormatException {
        Gson gson = new Gson();
        JsonArray jsonObjectives = gson.fromJson(json, JsonObject.class)
                .getAsJsonArray("objectives");
        ArrayList<Objective> objectives = new ArrayList<>();

        for (JsonElement objective : jsonObjectives) {
            JsonObject jsonObjective = objective.getAsJsonObject();

            int id = jsonObjective.get("id").getAsInt();


            String type = jsonObjective.get("type").getAsString();

            int points =  jsonObjective.get("points").getAsInt();

            Function<Player, Integer> point_function =  null;

            try {
                switch (type) {
                    case "resources" -> new FunctionBuilder()
                            .setType(type)
                            .setPoints(points)
                            .setResources(
                                    getResources(jsonObjective.get("requirements"))
                            )
                            .build();

                    case "pattern" -> new FunctionBuilder()
                            .setType(type)
                            .setPoints(points)
                            .setPattern(
                                    getPattern(jsonObjective.get("requirements"))
                            )
                            .build();
                    default -> throw new IllegalStateException("Unexpected value: " + type);
                };
            } catch (InvalidTypeException e) {
                throw new JsonFormatException(e);
            }

            objectives.add(new Objective(point_function));
        }

        return new Deck<>(objectives);
    }

    private Map<Resource, Integer> getResources(JsonElement resources) {
        HashMap<Resource, Integer> resMap = new HashMap<>();
        JsonObject res = resources.getAsJsonObject();
        for (String resource : resourceMap.keySet()) {
            if (res.get(resource) != null) {
                resMap.put(resourceMap.get(resource), res.get(resource).getAsInt());
            }
        }
        return resMap;
    }

    private Resource[][] getPattern(JsonElement pattern) {
        Resource[][] out = new Resource[3][3];
        JsonArray array = pattern.getAsJsonArray();
        int i = 0;
        for (JsonElement res : array) {
            out[i / 3][i % 3] = getResource(res);
            i++;
        }
        return out;
    }
}
