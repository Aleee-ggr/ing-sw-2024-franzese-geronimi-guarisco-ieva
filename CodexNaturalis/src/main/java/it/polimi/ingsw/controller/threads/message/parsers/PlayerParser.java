package it.polimi.ingsw.controller.threads.message.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class PlayerParser implements MessageParser<String> {
    public String parse(String message) {
        JsonObject object = new Gson().fromJson(message, JsonObject.class);
        return object.get("player").getAsString();
    }
}
