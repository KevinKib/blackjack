package org.kevinkib;

import java.util.Arrays;

public class Logger {

    public void write(String... args) {
        System.out.println(Arrays.toString(args));
    }

}
