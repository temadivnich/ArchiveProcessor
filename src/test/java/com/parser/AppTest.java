package com.parser;

import org.testng.annotations.Test;

import java.io.IOException;

public class AppTest {

    @Test
    public void testName() throws IOException {
        System.out.println("freeMemory: " + Runtime.getRuntime().freeMemory() / 1048576);
        System.out.println("maxMemory: " + Runtime.getRuntime().maxMemory() / 1048576);
        System.out.println("totalMemory: " + Runtime.getRuntime().totalMemory() / 1048576);

    }

}
