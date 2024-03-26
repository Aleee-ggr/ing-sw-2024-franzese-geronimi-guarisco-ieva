package it.polimi.ingsw.helpers.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.enums.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class StartingParser implements JsonParser<Deck<StartingCard>> {
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
    public Deck<StartingCard> parse() {
        Gson gson = new Gson();
        JsonArray cards = gson.fromJson(json, JsonObject.class)
                .getAsJsonArray("startingcards");
        ArrayList<StartingCard> deck = new ArrayList<>();

        for (JsonElement card : cards) {
            JsonObject card_obj = card.getAsJsonObject();
            int id = card_obj.get("id").getAsInt();

            ArrayList<Resource> frontResources = getFrontResources(
                    card_obj.getAsJsonArray("resource")
            );

            Corner[] frontCorners = getCorners(
                    card_obj.getAsJsonArray("frontcorners")
            );

            Corner[] backCorners = getCorners(
                    card_obj.getAsJsonArray("backcorners")
            );

            deck.add(new StartingCard(id, frontCorners, backCorners, frontResources));
        }
        return new Deck<>(deck);
    }

    private ArrayList<Resource> getFrontResources(JsonArray resources) {
        ArrayList<Resource> front_resources = new ArrayList<>();
        for (JsonElement resource : resources) {
            front_resources.add(
                    getResource(resource)
            );
        }
        return front_resources;
    }
}
