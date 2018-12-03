package com.zip.processor;

import java.io.*;
import java.util.zip.*;

import static com.zip.processor.StringUtils.replaceExtension;

/**
 * Responsible for zip processing logic
 */

public class ZipProcessor {
    private static final String OUTPUT_FILE = "inputsv2.zip";
    private static final String ZIP_SUFFIX = ".zip";
    private static final String GZIP_SUFFIX = ".gz";
    private static Reporter reporter = new Reporter();

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
            log("Current entry: " + entryName);

            zos.putNextEntry(new ZipEntry(entryName));
            if (entryName.endsWith(ZIP_SUFFIX)) {
                processNestedZip(zis, zos);
            } else if (entryName.endsWith(GZIP_SUFFIX)) {
                processNestedGzip(zis, zos);

            } else if (!zipEntry.isDirectory()) {
                log("Reading the file: " + entryName);
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
        var reader = new BufferedReader(inputStreamReader);
        var writer = new PrintWriter(outputStream);

        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            String output = replaceExtension(inputLine.intern()).intern();
            writer.println(output);
            writer.flush();

            reporter.add(output);
        }
    }

    private void log(String msg) {
        if (msg.length() < 400)
            System.out.println(msg);
        else
            System.out.println(msg.substring(0, 400));
    }
}
