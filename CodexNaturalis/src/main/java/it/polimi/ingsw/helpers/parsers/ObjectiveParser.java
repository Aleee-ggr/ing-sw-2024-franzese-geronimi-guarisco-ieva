package it.polimi.ingsw.helpers.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.helpers.builders.FunctionBuilder;
import it.polimi.ingsw.helpers.exceptions.model.InvalidTypeException;
import it.polimi.ingsw.helpers.exceptions.model.JsonFormatException;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.model.player.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static it.polimi.ingsw.GameConsts.resourceMap;

public class ObjectiveParser implements JsonParser<Deck<Objective>> {
    private String json;

    /**
     * Get a map of the resources described by an objective
     *
     * @param resources a jsonElement with tag "resource"
     * @return a map to associate the resource type to its amount
     */
    public static Map<Resource, Integer> getResources(JsonElement resources) {
        HashMap<Resource, Integer> resMap = new HashMap<>();
        JsonObject res = resources.getAsJsonObject();
        for (String resource : resourceMap.keySet()) {
            if (res.get(resource) != null) {
                resMap.put(resourceMap.get(resource), res.get(resource).getAsInt());
            }
        }
        return resMap;
    }

    /**
     * Parse a pattern from a list in the jsonElement with tag "pattern"
     *
     * @param pattern a jsonElement with tag "pattern"
     * @return a 3x3 matrix of resources
     */
    public static Resource[][] getPattern(JsonElement pattern) {
        Resource[][] out = new Resource[3][3];
        JsonArray array = pattern.getAsJsonArray();
        int i = 0;
        for (JsonElement res : array) {
            out[i / 3][i % 3] = JsonParser.getResource(res);
            i++;
        }
        return out;
    }

    @Override
    public ObjectiveParser readFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(path))));

        json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        return this;
    }

    @Override
    public ObjectiveParser readString(String json) {
        this.json = json;
        return this;
    }

    @Override
    public Deck<Objective> parse() throws JsonFormatException {
        Gson gson = new Gson();
        JsonArray jsonObjectives = gson.fromJson(json, JsonObject.class).getAsJsonArray("objectives");
        ArrayList<Objective> objectives = new ArrayList<>();

        for (JsonElement objective : jsonObjectives) {
            JsonObject jsonObjective = objective.getAsJsonObject();

            JsonElement jid = jsonObjective.get("id");
            if (jid == null) {
                throw new JsonFormatException("id: tag not found!");
            }
            int id = jid.getAsInt();

            JsonElement jtype = jsonObjective.get("type");
            if (jtype == null) {
                throw new JsonFormatException("type: tag not found!");
            }
            String type = jtype.getAsString();

            JsonElement jpoints = jsonObjective.get("points");
            if (jpoints == null) {
                throw new JsonFormatException("points: tag not found!");
            }
            int points = jpoints.getAsInt();

            Function<Player, Integer> point_function;
            JsonElement requirements = jsonObjective.get("requirements");
            try {
                point_function = switch (type) {
                    case "resources" ->
                            new FunctionBuilder().setType(type).setPoints(points).setResources(getResources(requirements)).build();

                    case "pattern" ->
                            new FunctionBuilder().setType(type).setPoints(points).setPattern(getPattern(requirements)).build();
                    default -> throw new JsonFormatException("Unexpected value: %s for points".formatted(type));
                };
            } catch (InvalidTypeException e) {
                throw new JsonFormatException(e);
            }

            objectives.add(new Objective(point_function, id, type, requirements, points));
        }

        return new Deck<>(objectives);
    }
}
