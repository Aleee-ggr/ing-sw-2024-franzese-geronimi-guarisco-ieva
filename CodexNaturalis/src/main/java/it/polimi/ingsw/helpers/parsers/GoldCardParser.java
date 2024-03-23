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
                .getAsJsonArray("stdcards");
        ArrayList<GoldCard> deck = new ArrayList<>();

        for (var card : cards) {
            var card_obj = card.getAsJsonObject();
            int id = card_obj.get("id").getAsInt();
            Resource resource = getResource(card_obj.get("resource"));

            var requirements = card_obj.getAsJsonArray("requirements");
            Map<Resource, Integer> card_requirements = new HashMap();
            for (var req : requirements) {
                for (String res : GameConsts.resourceMap.keySet()) {
                    card_requirements.put(
                            GameConsts.resourceMap.get(res),
                            req.getAsJsonObject().get(res).getAsInt()
                    );
                }
            }

            var points = card_obj.getAsJsonObject("points");
            String type = points.get("type").getAsString();
            Function<Game, Integer> point_calculator;
            switch (type) {
                case "none", "cover":
                    point_calculator = new FunctionBuilder()
                            .setType(type)
                            .setPoints(points.get("value").getAsInt())
                            .build();
                    break;
                default:
                    point_calculator = new FunctionBuilder()
                            .setType(type)
                            .setPoints(points.get("value").getAsInt())
                            .setResource(getResource(points.get("type")))
                            .build();
            }

            Corner[] corners = getCorners(
                    card_obj.getAsJsonArray("corners")
            );
            deck.add(
                    new GoldCard(id, corners)
            );
        }
        return new Deck<>(deck);
    }
}
