package com.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args) throws IOException {

        List<Path> paths = Files.list(Paths.get("./")).collect(Collectors.toList());
        File file = paths.stream().filter(path -> path.getFileName().startsWith("inputs_v2.zip")).findFirst().get().toFile();
        byte[] archive = Files.readAllBytes(file.toPath());

        Path writenTo = Files.write(Paths.get("D:\\inputs_v2.zip"), archive);

        System.out.println(Arrays.toString(paths.toArray()));
        System.out.println("Done!");
    }


}
