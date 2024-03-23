package it.polimi.ingsw.helpers.parser;

import java.io.IOException;
import java.nio.file.Path;

public interface JsonParser<Class> {

    void readFile(Path path) throws IOException;

    Class parse();
}
