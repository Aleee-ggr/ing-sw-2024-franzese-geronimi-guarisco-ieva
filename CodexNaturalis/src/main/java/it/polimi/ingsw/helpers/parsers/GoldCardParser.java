package it.polimi.ingsw.helpers.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.builders.FunctionBuilder;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.enums.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GoldCardParser implements JsonParser<Deck<GoldCard>> {
    private String json;

    /**
     * Load a file from the given path in the parser so that it can be later parsed
     * also see {@link #parse()} method for more details
     * @param path path of the file from which to load the data for the cards
     * @throws IOException Throws exception when the given file is missing
     */
    @Override
    public void readFile(Path path) throws IOException {
        json = Files.readString(path);
    }

    @Override
    public void readString(String json) {
        this.json = json;
    }

    /**
     *
     * @return a new Deck of goldCard with the data specified in
     * {@link #readFile(Path) readFile(path)} or {@link #readString(String) readString(json)}
     */
    @Override
    public Deck<GoldCard> parse() {
        Gson gson = new Gson();
        JsonArray cards = gson.fromJson(json, JsonObject.class)
                .getAsJsonArray("goldcards");
        ArrayList<GoldCard> deck = new ArrayList<>();

        for (var card : cards) {
            var card_obj = card.getAsJsonObject();

            int id = card_obj.get("id").getAsInt();

            Resource resource = getResource(card_obj.get("resource"));

            var requirements = card_obj.get("requirements").getAsJsonObject();

            Map<Resource, Integer> card_requirements = getRequirements(requirements);

            var points = card_obj.getAsJsonObject("points");

            Function<Game, Integer> point_calculator = getPointCalculator(points);


            Corner[] corners = getCorners(
                    card_obj.getAsJsonArray("corners")
            );
            deck.add(
                    new GoldCard(id, corners, resource, card_requirements, point_calculator)
            );
        }
        return new Deck<>(deck);
    }

    private Map<Resource, Integer> getRequirements(JsonObject requirements) {
        Map<Resource, Integer> card_requirements = new HashMap();

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
    private Function<Game, Integer> getPointCalculator(JsonObject points) {
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
