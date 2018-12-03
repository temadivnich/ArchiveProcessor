package com.zip;

import com.zip.processor.ZipProcessor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * DataArt Jav02 course
 */

public class ZipLoader {

    public static void main(String[] args) {
        logJavaProperties();

        Optional<File> inputFile = loadFile(args);
        ZipProcessor processor = new ZipProcessor();
        try {
            File output = processor.process(inputFile.orElseThrow());
            System.out.printf("Finished successfully. Output file location: %s", output.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Caught an error while processing the file");
        }
    }

    private static Optional<File> loadFile(String[] args) {
        String name;
        if (args.length > 0) {
            name = args[0];
        } else {
            System.err.println("Please provide input file name!");
            return Optional.empty();
        }

        File file = new File(name);
        if (!file.exists() || file.isDirectory()) {
            Path root = Paths.get("").toAbsolutePath();
            System.err.println("File doesn't exist or is directory. Please put the file to the directory " + root);
            return Optional.empty();
        }
        return Optional.of(file);
    }

    private static void logJavaProperties() {
        System.out.println("Java version: " + System.getProperty("java.version"));
        System.out.println("Max memory size: " + Runtime.getRuntime().maxMemory() / 1048576);
        System.out.println();
    }
}
