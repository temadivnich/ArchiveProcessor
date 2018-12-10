package com.zip.processor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;

public final class Utils {

    static String replaceExtension(String line) {
        return line.replace("(101)", "(401)")
                .replace("(202)", "(802)")
                .replace("(301)", "(321)").intern();
    }

    static void logResults(Set<String> emails, Set<String> phones) {
        log("File processed successfully");
        log("Phones list: {} total " + phones.size());
        phones.forEach(Utils::log);

        log("Emails list: {} total " + emails.size());
        emails.forEach(Utils::log);
    }

    public static void logJavaProperties() {
        log("Java version: " + System.getProperty("java.version"));
        log("Max memory size, Mb: " + Runtime.getRuntime().maxMemory() / 1048576);
    }

    public static Optional<File> loadFile(String[] args) {
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

    private static void log(String msg) {
        System.out.println(msg);
    }
}
