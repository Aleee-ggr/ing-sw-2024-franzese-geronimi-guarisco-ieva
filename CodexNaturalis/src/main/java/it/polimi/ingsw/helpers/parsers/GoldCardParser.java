package it.polimi.ingsw.helpers.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.builders.FunctionBuilder;
import it.polimi.ingsw.helpers.exceptions.model.InvalidTypeException;
import it.polimi.ingsw.helpers.exceptions.model.JsonFormatException;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.enums.Resource;
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

public class GoldCardParser implements JsonParser<Deck<GoldCard>> {
    private String json;

    @Override
    public GoldCardParser readFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(path))));

        json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        return this;
    }

    @Override
    public GoldCardParser readString(String json) {
        this.json = json;
        return this;
    }

    @Override
    public Deck<GoldCard> parse() throws JsonFormatException {
        Gson gson = new Gson();
        JsonArray cards = gson.fromJson(json, JsonObject.class).getAsJsonArray("goldcards");
        ArrayList<GoldCard> deck = new ArrayList<>();

        for (JsonElement card : cards) {
            JsonObject card_obj = card.getAsJsonObject();

            JsonElement jid = card_obj.get("id");
            if (jid == null) {
                throw new JsonFormatException("id: tag not found!");
            }
            int id = jid.getAsInt();

            JsonElement jres = card_obj.get("resource");
            if (jres == null) {
                throw new JsonFormatException("resource: tag not found!");
            }
            Resource resource = JsonParser.getResource(jres);

            JsonObject jreq = card_obj.getAsJsonObject("requirements");
            if (jreq == null) {
                throw new JsonFormatException("requirements: tag not found!");
            }
            Map<Resource, Integer> card_requirements = getRequirements(jreq);

            JsonObject jpoints = card_obj.getAsJsonObject("points");
            if (jpoints == null) {
                throw new JsonFormatException("points: tag not found!");
            }
            String type = jpoints.get("type").getAsString();
            int points = jpoints.get("value").getAsInt();
            Function<Player, Integer> point_calculator = getPointCalculator(jpoints);

            JsonArray jcorners = card_obj.getAsJsonArray("corners");
            if (jcorners == null) {
                throw new JsonFormatException("corners: tag not found!");
            }
            Corner[] corners = getCorners(jcorners);

            deck.add(new GoldCard(id, corners, resource, card_requirements, point_calculator, points, type));
        }
        return new Deck<>(deck);
    }

    /**
     * Helper function for the parser.<br/>
     * obtains a map of requirements from a JsonObject obtained by using
     * {@link JsonObject#getAsJsonObject(String) card_obj.getAsJsonObject("requirements")}
     *
     * @param requirements the jsonObject obtained by using
     *                     {@link JsonObject#getAsJsonObject(String) card_obj.getAsJsonObject("requirements")}
     * @return a map of requirements for the card constructor
     */
    private Map<Resource, Integer> getRequirements(JsonObject requirements) {
        Map<Resource, Integer> card_requirements = new HashMap<>();

        for (String res : GameConsts.requirementsList) {
            int count = 0;

            if (requirements.get(res) != null) {
                count = requirements.get(res).getAsInt();
            }
            card_requirements.put(GameConsts.resourceMap.get(res), count);
        }
        return card_requirements;
    }

    /**
     * Helper function for the parser.<br/>
     * obtains a Function to calculate the points given by the card from a JsonObject obtained by using
     * {@link JsonObject#getAsJsonObject(String) card_obj.getAsJsonObject("points")}
     *
     * @param points the jsonObject obtained by using
     *               {@link JsonObject#getAsJsonObject(String) card_obj.getAsJsonObject("points")}
     * @return a map of requirements for the card constructor
     */
    private Function<Player, Integer> getPointCalculator(JsonObject points) throws JsonFormatException {
        String type = points.get("type").getAsString();
        try {
            return switch (type) {
                case "none", "cover" ->
                        new FunctionBuilder().setType(type).setPoints(points.get("value").getAsInt()).build();
                default ->
                        new FunctionBuilder().setType("resource").setPoints(points.get("value").getAsInt()).setResource(JsonParser.getResource(points.get("type"))).build();
            };
        } catch (InvalidTypeException e) {
            throw new JsonFormatException(e);
        }
    }
}
