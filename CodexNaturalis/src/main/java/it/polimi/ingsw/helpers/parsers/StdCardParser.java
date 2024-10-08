package it.polimi.ingsw.helpers.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.helpers.exceptions.model.JsonFormatException;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.StdCard;
import it.polimi.ingsw.model.enums.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class StdCardParser implements JsonParser<Deck<StdCard>> {
    private String json;

    @Override
    public StdCardParser readFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(path))));

        json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
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
        JsonArray cards = gson.fromJson(json, JsonObject.class).getAsJsonArray("stdcards");
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
            Resource resource = JsonParser.getResource(jres);

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

            deck.add(new StdCard(id, corners, resource, points != 0));
        }

        return new Deck<>(deck);
    }
}
