package it.polimi.ingsw.helpers.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.helpers.exceptions.JsonFormatException;
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
    public Deck<StartingCard> parse() throws JsonFormatException {
        Gson gson = new Gson();
        JsonArray cards = gson.fromJson(json, JsonObject.class)
                .getAsJsonArray("startingcards");
        ArrayList<StartingCard> deck = new ArrayList<>();

        for (JsonElement card : cards) {
            JsonObject card_obj = card.getAsJsonObject();
            JsonElement jid = card_obj.get("id");
            if (jid == null) {
                throw new JsonFormatException("id: tag not found");
            }
            int id = jid.getAsInt();

            JsonArray jres = card_obj.getAsJsonArray("resource");
            if (jres == null) {
                throw new JsonFormatException("resource: tag not found");
            }
            ArrayList<Resource> frontResources = getFrontResources(jres);

            JsonArray jfcorners = card_obj.getAsJsonArray("frontcorners");
            if (jfcorners == null) {
                throw new JsonFormatException("frontcorners: tag not found!");
            }
            Corner[] frontCorners = getCorners(jfcorners);

            JsonArray jbcorners = card_obj.getAsJsonArray("backcorners");
            if (jbcorners == null) {
                throw new JsonFormatException("corners: tag not found!");
            }
            Corner[] backCorners = getCorners(jbcorners);


            deck.add(new StartingCard(id, frontCorners, backCorners, frontResources));
        }
        return new Deck<>(deck);
    }


    /**
     *  Parse the resource data within the card json to use for the StartingCard constructor..<br/>
     *  takes as input the object obtained by using the card {@link com.google.gson.JsonObject#get(String)  JsonObject.get("resource")}
     *  @param resources the JsonElement obtained from the element "resource" in cards.json
     *  @return an arrayList with the resources on the front of the card, (MUSHROOM, WOLF, LEAF or BUTTERFLY)
     *  @see StartingCard
     */
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
