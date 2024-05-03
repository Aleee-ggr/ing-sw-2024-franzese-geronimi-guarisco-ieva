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
        out.print("[" + msg.status()  + ": ");
        out.print(new Timestamp(System.currentTimeMillis())+ "]:\t");
        out.print("type: %s\tplayer: %s\targs: ".formatted(msg.type(), msg.player()));
        out.print(Arrays.toString(msg.args()));
        out.println("\u001B[0m");
        out.flush();
    }
}
