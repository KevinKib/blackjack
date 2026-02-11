package org.kevinkib;

import java.util.Arrays;

public class Logger {

    private boolean canLog;

    public Logger(boolean canLog) {
        this.canLog = canLog;
    }

    public void write(String... args) {
        if (canLog) {
            forceWrite(args);
        }
    }

    public void forceWrite(String... args) {
        System.out.println(Arrays.toString(args));
    }

}
