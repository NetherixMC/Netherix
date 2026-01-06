
package com.moonx;

import com.moonx.bootstrap.RuntimeBootstrap;

public class Main {
    public static void main(String[] args) {
        RuntimeBootstrap.setupShutdownHook();
        RuntimeBootstrap.start();
    }
}