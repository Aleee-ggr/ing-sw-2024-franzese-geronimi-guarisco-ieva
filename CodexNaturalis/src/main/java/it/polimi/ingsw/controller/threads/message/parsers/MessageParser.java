package it.polimi.ingsw.controller.threads.message.parsers;

public interface MessageParser<T> {
   T parse(String message);
}