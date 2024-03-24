package it.polimi.ingsw.helpers.parsers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.enums.Resource;

import java.io.IOException;
import java.nio.file.Path;

public interface JsonParser<Class> {

    void readFile(Path path) throws IOException;

    void readString(String json);

    Class parse();

    /**
     * Parse the resource data within the card json to use for both the goldCard and the stdCard constructors.<br/>
     * takes as input the object obtained by using the card {@link com.google.gson.JsonObject#get(String)  JsonObject.get("resource")}
     * @param resource the JsonElement obtained from the element "resource" in cards.json
     * @return the resource on the back of the card, (MUSHROOM, WOLF, LEAF or BUTTERFLY)
     */
    default Resource getResource(JsonElement resource) {
        return GameConsts.resourceMap.get(resource.getAsString().toUpperCase());
    }

    /**
     * Parse the corners in the given json and return an array of corners of size 4 for the card constructor.<br/>
     * the jsonArray can be obtained by using
     * {@link com.google.gson.JsonObject#getAsJsonArray(String)  JsonObject.getAsJsonArray("corners")}
     * @param corners a JsonArray obtained from the element "corner" in the json
     * @return a corner array of size 4 generated from the given json element
     */
    default Corner[] getCorners(JsonArray corners) {
        Corner[] front_corners = new Corner[4];
        for (int pos = 0; pos < 4; pos++) {
            if (corners.get(pos).getAsString().equals("NONCOVERABLE")) {
                front_corners[pos] = new Corner(Resource.NONE, false);
            }
            else {
                front_corners[pos] = new Corner(getResource(corners.get(pos)), true);
            }
        }
        return front_corners;
    }
}
