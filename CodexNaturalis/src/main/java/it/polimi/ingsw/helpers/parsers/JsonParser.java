package it.polimi.ingsw.helpers.parsers;

import com.google.gson.JsonElement;
import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.enums.Resource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface JsonParser<Class> {

    void readFile(Path path) throws IOException;

    void readString(String json);

    Class parse();

    default Resource getResource(JsonElement element) {
        return GameConsts.resourceMap.get(element.getAsString().toUpperCase());
    }

    default Corner[] getCorners(List<JsonElement> corners) {
        Corner[] front_corners = new Corner[4];
        for (int i = 0; i < 4; i++) {
            if (corners.get(i).getAsString().equals("NONCOVERABLE")) {
                front_corners[i] = new Corner(Resource.NONE, false);
                continue;
            }
            front_corners[i] = new Corner(getResource(corners.get(i)), true);
        }
        return front_corners;
    }
}
