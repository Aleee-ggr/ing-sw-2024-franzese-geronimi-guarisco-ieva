package it.polimi.ingsw.controller.threads.message.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class IdParser implements MessageParser<Integer> {
    @Override
    public Integer parse(String message) {
         JsonObject object = new Gson().fromJson(message, JsonObject.class);
         if (object.get("type").getAsString().equals("id")) {
             return object.get("id").getAsInt();
         }
         return null;
    }
}
