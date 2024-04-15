package it.polimi.ingsw.helpers.parsers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.exceptions.model.JsonFormatException;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.enums.Resource;

import java.io.IOException;
import java.nio.file.Path;

public interface JsonParser<Class> {

    /**
     * Load a file from the given path in the parser so that it can be later parsed.<br/>
     * the expected format is:<br>
     * <pre>{@code
     *  {
     *      "stdcards":[
     *          {
     *              "id":0,
     *              "resources": "resource",
     *              "corners": ["resource","resource","resource","resource"],
     *              "points": 0
     *          }, ...
     *      ],
     *      "goldcards":[
     *          {
     *              "id": 0,
     *              "resources": "resource",
     *              "corners": ["resource","resource","resource","resource"],
     *              "points": {
     *                  "value":0,
     *                  "type":"type"
     *              },
     *              "requirements":{
     *                  "RESOURCE":0
     *              }
     *          }, ...
     *      ],
     *      "startingcards":[
     *          {
     *              "id": 0,
     *              "resource": ["resource"],
     *              "frontcorners": ["resource","resource","resource","resource"],
     *              "backcorners": ["resource","resource","resource","resource"]
     *          }, ...
     *      ],
     *      "objectives":[
     *          {
     *              "id":0,
     *              "points":0,
     *              "type":type,
     *              "requirements": ["resource"] | {"resource": 0}
     *          }, ...
     *      ]
     *  }
     * }</pre>
     * @see #parse()
     * @param path path of the file from which to load the data for the cards
     * @throws IOException Throws exception when the given file is missing
     */
    JsonParser<Class> readFile(Path path) throws IOException;

    /**
     * load a json as a string in the parser
     * @param json a string formatted as a json
     */
    JsonParser<Class> readString(String json);


    /**
     * Returns a new object of the type specified by the parser from a json previously loaded in this object
     * @return a new object of the parser type by reading the json string obtained using
     * {@link #readFile(Path) readFile(path)} or {@link #readString(String) readString(json)}
     * @throws JsonFormatException when it encounters some json tags it cannot parse
     */
    Class parse() throws JsonFormatException;

    /**
     * Parse the resource data within the card json to use for both the goldCard and the stdCard constructors.<br/>
     * takes as input the object obtained by using the card {@link com.google.gson.JsonObject#get(String)  JsonObject.get("resource")}
     * @param resource the JsonElement obtained from the element "resource" in cards.json
     * @return the resource on the back of the card, (FUNGI, ANIMAL, PLANT or INSECT)
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
