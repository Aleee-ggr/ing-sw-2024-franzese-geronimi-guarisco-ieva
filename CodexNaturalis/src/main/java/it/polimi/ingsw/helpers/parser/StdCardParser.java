package it.polimi.ingsw.helpers.parser;

import com.google.gson.*;
import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.StdCard;
import it.polimi.ingsw.model.enums.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class StdCardParser {
    private String json;
    public StdCardParser() {
    }

    public void readFile(Path path) throws IOException {
        json = Files.readString(path);
    }

    public Deck<StdCard> parse() {
        Gson gson = new Gson();
        JsonArray cards = gson.fromJson(json, JsonObject.class)
                .getAsJsonArray("stdcards");
        ArrayList<StdCard> deck = new ArrayList<>();
        for (var card : cards) {
            var card_obj = card.getAsJsonObject();
            int id = card_obj.get("id").getAsInt();
            Resource resource = getResource(card_obj.get("resource"));
            int points = card_obj.get("points").getAsInt();
            var corners = card_obj.getAsJsonArray("corners").asList();
            Corner[] front_corners = new Corner[4];
            for (int i = 0; i < 4; i++) {
                if (corners.get(i).getAsString().equals("NONCOVERABLE")) {
                    front_corners[i] = new Corner(Resource.NONE, false);
                    continue;
                }
                front_corners[i] = new Corner(getResource(corners.get(i)), true);
            }
            deck.add(
                    new StdCard(id, front_corners, resource, points == 1)
            );
        }

        return new Deck<>(deck);
    }

    private Resource getResource(JsonElement element) {
        return GameConsts.resourceMap.get(element.getAsString().toUpperCase());
    }
}
