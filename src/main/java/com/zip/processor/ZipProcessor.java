package com.zip.processor;


import java.io.*;
import java.util.zip.*;

import static com.zip.processor.StringConstants.*;
import static com.zip.processor.Utils.replaceExtension;

/**
 * Responsible for zip processing logic
 */

public class ZipProcessor {
    private static final int BUFFER_SIZE = 100;
    private static ReportGenerator reporter = new ReportGenerator();

    public File process(File inputFile) throws Exception {
        File outputFile = new File(OUTPUT_FILE);
        try (var fileInputStream = new FileInputStream(inputFile);
             var zipInputStream = new ZipInputStream(fileInputStream);
             var fileOutputStream = new FileOutputStream(outputFile);
             var zipOutputStream = new ZipOutputStream(fileOutputStream)) {

            processRecursively(zipInputStream, zipOutputStream);
            reporter.generateFinalReport(zipOutputStream);
        }
        return outputFile;
    }

    private void processRecursively(ZipInputStream zis, ZipOutputStream zos) throws IOException {
        ZipEntry zipEntry;
        while ((zipEntry = zis.getNextEntry()) != null) {
            String entryName = zipEntry.getName();
            System.out.println("Current entry: " + entryName);

            zos.putNextEntry(new ZipEntry(entryName));
            if (entryName.endsWith(ZIP_SUFFIX)) {
                processNestedZip(zis, zos);
            } else if (entryName.endsWith(GZIP_SUFFIX)) {
                processNestedGzip(zis, zos);

            } else if (!zipEntry.isDirectory()) {
                System.out.println("Reading the file: " + entryName);
                processEndFile(zis, zos);
            }
            zos.closeEntry();
            zis.closeEntry();
        }
    }

    private void processNestedGzip(ZipInputStream zis, ZipOutputStream zos) throws IOException {
        var nestedInput = new GZIPInputStream(zis);
        var nestedOutput = new GZIPOutputStream(zos);
        processEndFile(nestedInput, nestedOutput);
        nestedOutput.finish();
    }

    private void processNestedZip(ZipInputStream zis, ZipOutputStream zos) throws IOException {
        var nestedInput = new ZipInputStream(zis);
        var nestedOutput = new ZipOutputStream(zos);
        processRecursively(nestedInput, nestedOutput);
        nestedOutput.finish();
    }

    private void processEndFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        var inputStreamReader = new InputStreamReader(inputStream);
        var reader = new BufferedReader(inputStreamReader, BUFFER_SIZE);
        var writer = new PrintWriter(outputStream);

        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            String output = replaceExtension(inputLine.intern()).intern();
            writer.println(output);
            writer.flush();

            reporter.add(output);
        }
    }
}
