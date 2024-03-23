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

    default Resource getResource(JsonElement element) {
        return GameConsts.resourceMap.get(element.getAsString().toUpperCase());
    }

    /**
     * Parse the corners in the given json and return an array of corners of size 4
     * @param corners a JsonArray obtained from the element "corner" in the json
     * @return a corner array of size 4 generated from the given json element
     */
    default Corner[] getCorners(JsonArray corners) {
        Corner[] front_corners = new Corner[4];
        int pos = 0;
        for (var corner : corners) {
            if (corners.get(pos).getAsString().equals("NONCOVERABLE")) {
                front_corners[pos] = new Corner(Resource.NONE, false);
            }
            else {
                front_corners[pos] = new Corner(getResource(corners.get(pos)), true);
            }
            pos++;
        }
        for (var c : front_corners) {
            System.out.println(c);
        }
        return front_corners;
    }
}
