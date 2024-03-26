package it.polimi.ingsw.helpers.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.builders.FunctionBuilder;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GoldCardParser implements JsonParser<Deck<GoldCard>> {
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
    public Deck<GoldCard> parse() {
        Gson gson = new Gson();
        JsonArray cards = gson.fromJson(json, JsonObject.class)
                .getAsJsonArray("goldcards");
        ArrayList<GoldCard> deck = new ArrayList<>();

        for (JsonElement card : cards) {
            JsonObject card_obj = card.getAsJsonObject();

            int id = card_obj.get("id").getAsInt();

            Resource resource = getResource(
                    card_obj.get("resource")
            );
            Map<Resource, Integer> card_requirements = getRequirements(
                    card_obj.getAsJsonObject("requirements")
            );
            Function<Player, Integer> point_calculator = getPointCalculator(
                    card_obj.getAsJsonObject("points")
            );
            Corner[] corners = getCorners(
                    card_obj.getAsJsonArray("corners")
            );

            deck.add(
                    new GoldCard(id, corners, resource, card_requirements, point_calculator)
            );
        }
        return new Deck<>(deck);
    }

    /**
     * Helper function for the parser.<br/>
     * obtains a map of requirements from a JsonObject obtained by using
     *  {@link JsonObject#getAsJsonObject(String) card_obj.getAsJsonObject("requirements")}
     * @param requirements the jsonObject obtained by using
     *  {@link JsonObject#getAsJsonObject(String) card_obj.getAsJsonObject("requirements")}
     * @return a map of requirements for the card constructor
     */
    private Map<Resource, Integer> getRequirements(JsonObject requirements) {
        Map<Resource, Integer> card_requirements = new HashMap<>();

        for (String res : GameConsts.requirementsList) {
            int count = 0;

            if (requirements.get(res) != null) {
                count = requirements.get(res).getAsInt();
            }
            card_requirements.put(
                    GameConsts.resourceMap.get(res),
                    count
            );
        }
        return card_requirements;
    }

    /**
     * Helper function for the parser.<br/>
     * obtains a Function to calculate the points given by the card from a JsonObject obtained by using
     *  {@link JsonObject#getAsJsonObject(String) card_obj.getAsJsonObject("points")}
     * @param points the jsonObject obtained by using
     *  {@link JsonObject#getAsJsonObject(String) card_obj.getAsJsonObject("points")}
     * @return a map of requirements for the card constructor
     */
    private Function<Player, Integer> getPointCalculator(JsonObject points) {
        String type = points.get("type").getAsString();
        return switch (type) {
            case "none", "cover" -> new FunctionBuilder()
                    .setType(type)
                    .setPoints(points.get("value").getAsInt())
                    .build();
            default -> new FunctionBuilder()
                    .setType(type)
                    .setPoints(points.get("value").getAsInt())
                    .setResource(getResource(points.get("type")))
                    .build();
        };
    }
}
