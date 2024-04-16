package it.polimi.ingsw.controller.threads.message;

public interface MessageParser<T> {
   T parse(String message);
}