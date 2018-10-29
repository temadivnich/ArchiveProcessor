package com.tutorials;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AnotherZip {
    public static void main(String[] args) throws Exception {
        new Test().main();
    }
}

class Test {
    public void main() throws Exception {
        File archive = new File("./folder/inputs_v2.zip");
        FileInputStream fis = new FileInputStream(archive);
        ZipInputStream zis = new ZipInputStream(fis);
        readZip(zis);
    }

    public void readZip(InputStream is) throws Exception {
        ZipInputStream zis;
        if (is instanceof ZipInputStream) {
            ZipEntry zipEntry = null;
            zis = (ZipInputStream) is;
            while ((zipEntry = zis.getNextEntry()) != null) {
                String name = zipEntry.getName();
                System.out.println("Next entry:\t" + (zipEntry.isDirectory() ? "Dir: \t" : "File:\t") + name);

                if (zipEntry.isDirectory()) {
                    readZip(zis);
                }
                if (!zipEntry.isDirectory() && name.endsWith(".zip")) {
                    ZipInputStream zippedZis = new ZipInputStream(zis);
                    readZip(zippedZis);
                }
                if (!zipEntry.isDirectory() && name.endsWith(".txt")) {
//                    readTxtFile(zis);
                    readWithScanner(zis);
                }
                if (!zipEntry.isDirectory() && name.endsWith(".gz")) {
                    GZIPInputStream gzipped = new GZIPInputStream(zis);
                    readZip(gzipped);
                }
            }
        }
        if (is instanceof GZIPInputStream) {
//            readTxtFile(is);
            readWithScanner(is);
        }
    }

    public void readTxtFile(InputStream inputStream) throws IOException {
        LineIterator it = IOUtils.lineIterator(inputStream, Charset.defaultCharset());
        int cnt = 0;
        try {
            while (it.hasNext()) {
                it.nextLine();
                cnt++;
            }
        } finally {
//            LineIterator.closeQuietly(it);
            System.out.println("\tLines count:\t" + cnt);
        }
    }

    public void readWithScanner(InputStream inputStream) throws IOException {
        Scanner sc = new Scanner(inputStream, "UTF-8");
        int cnt = 0;
        while (sc.hasNextLine()) {
            sc.nextLine();
            cnt++;
        }
        // note that Scanner suppresses exceptions
        if (sc.ioException() != null) {
            throw sc.ioException();
        }
        System.out.println("\tLines count:\t" + cnt);
//        finally {
//            if (sc != null) {
//                sc.close();
//            }
    }
}
