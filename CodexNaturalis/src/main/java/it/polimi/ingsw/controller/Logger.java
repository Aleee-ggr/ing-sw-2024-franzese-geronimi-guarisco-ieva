package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.threads.ThreadMessage;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Arrays;

public abstract class Logger {
    static final PrintWriter out = new PrintWriter(System.out);

    public static void log(ThreadMessage msg) {

        if (msg.status() == it.polimi.ingsw.controller.threads.Status.ERROR) {
            out.print("\u001B[31m");
        }
        out.print("[" + new Timestamp(System.currentTimeMillis()) + ":");
        out.print(msg.status() + "]:\t");
        out.print("type: %s\tplayer: %s\targs: %s".formatted(msg.type(), msg.player(), msg.args()));
        out.println("\u001B[0m");
        out.flush();
    }

    private enum Status {
        INFO,
        WARNING,
        FATAL,
    }
}
