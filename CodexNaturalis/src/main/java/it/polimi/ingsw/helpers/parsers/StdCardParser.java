package it.polimi.ingsw.helpers.parsers;

import com.google.gson.*;
import it.polimi.ingsw.helpers.exceptions.JsonFormatException;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.StdCard;
import it.polimi.ingsw.model.enums.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class StdCardParser implements JsonParser<Deck<StdCard>> {
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
    public Deck<StdCard> parse() throws JsonFormatException {
        Gson gson = new Gson();
        JsonArray cards = gson.fromJson(json, JsonObject.class)
                .getAsJsonArray("stdcards");
        ArrayList<StdCard> deck = new ArrayList<>();

        for (JsonElement card : cards) {
            JsonObject card_obj = card.getAsJsonObject();
            int id = card_obj.get("id").getAsInt();
            Resource resource = getResource(card_obj.get("resource"));
            int points = card_obj.get("points").getAsInt();

            Corner[] corners = getCorners(
                    card_obj.getAsJsonArray("corners")
            );
            deck.add(
                    new StdCard(id, corners, resource, points == 1)
            );
        }

        return new Deck<>(deck);
    }
}
