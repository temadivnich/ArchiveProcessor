package com.zip;

import com.zip.processor.ZipProcessor;

import java.io.File;

import static com.zip.processor.Utils.loadFile;
import static com.zip.processor.Utils.logJavaProperties;

public class ZipLoader {

    private static ZipProcessor processor = new ZipProcessor();

    public static void main(String[] args) {
        logJavaProperties();
        try {
            File inputFile = loadFile(args).orElseThrow();
            File outputFile = processor.process(inputFile);
            System.out.println("Finished successfully. Output file location:" + outputFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Caught an error while processing the file");
            e.printStackTrace();
        }
    }
}