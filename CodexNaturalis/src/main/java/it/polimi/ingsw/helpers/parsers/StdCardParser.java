package it.polimi.ingsw.helpers.parsers;

import com.google.gson.*;
import it.polimi.ingsw.helpers.exceptions.model.JsonFormatException;
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
    public StdCardParser readFile(Path path) throws IOException {
        json = Files.readString(path);
        return this;
    }

    @Override
    public StdCardParser readString(String json) {
        this.json = json;
        return this;
    }

    @Override
    public Deck<StdCard> parse() throws JsonFormatException {
        Gson gson = new Gson();
        JsonArray cards = gson.fromJson(json, JsonObject.class)
                .getAsJsonArray("stdcards");
        ArrayList<StdCard> deck = new ArrayList<>();

        for (JsonElement card : cards) {
            JsonObject card_obj = card.getAsJsonObject();
            JsonElement jid = card_obj.get("id");
            if (jid == null) {
                throw new JsonFormatException("id: tag not found");
            }
            int id = jid.getAsInt();

            JsonElement jres = card_obj.get("resource");
            if (jres == null) {
                throw new JsonFormatException("resource: tag not found");
            }
            Resource resource = getResource(jres);

            JsonElement jpoints = card_obj.get("points");
            if (jpoints == null) {
                throw new JsonFormatException("points: tag not found");
            }
            int points = jpoints.getAsInt();
            JsonArray jcorners = card_obj.getAsJsonArray("corners");
            if (jcorners == null) {
                throw new JsonFormatException("corners: tag not found!");
            }
            Corner[] corners = getCorners(jcorners);
            
            deck.add(
                    new StdCard(id, corners, resource, points != 0)
            );
        }

        return new Deck<>(deck);
    }
}
