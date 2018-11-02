package com.tutorials;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.zip.*;

public class AnotherZip {
    public static void main(String[] args) throws Exception {
        new Test().main();
    }
}

class Test {
    public void main() throws Exception {
        File archive = new File("./inputs_v2.zip");
        FileInputStream fis = new FileInputStream(archive);
        ZipInputStream zis = new ZipInputStream(fis);

        File output = new File("inputs_v2 - Copy.zip");
        FileOutputStream fos = new FileOutputStream(output);
        ZipOutputStream zos = new ZipOutputStream(fos);
        zos.putNextEntry(new ZipEntry("inputs_v2 - Copy/"));
        process(zis, zos);

        zis.close();
        fis.close();
        zos.close();
        fos.close();
    }


    public void process(ZipInputStream zis, ZipOutputStream zos) throws Exception {
        ZipEntry zipEntry = null;
        while ((zipEntry = zis.getNextEntry()) != null) {
            String name = zipEntry.getName();
            System.out.println("Next entry \t" + (zipEntry.isDirectory() ? "Dir: \t" : "File:\t") + name);

            if (name.endsWith(".zip")) {
                ZipInputStream zippedZis = new ZipInputStream(zis);
                ZipOutputStream zippedZos = new ZipOutputStream(zos);
                zippedZos.putNextEntry(zipEntry);
                process(zippedZis, zippedZos);
            }
            if (name.endsWith(".gz")) {
                GZIPInputStream gzipped = new GZIPInputStream(zis);
                GZIPOutputStream gzipOut = new GZIPOutputStream(zos);
                readAndWrite(gzipped, gzipOut);
                gzipOut.finish();
            }
            if (name.endsWith(".txt")) {
                readAndWrite(zis, zos);
            }
        }
    }

    public void readAndWrite(InputStream is, OutputStream zos) throws Exception {
        int read;
        byte[] buff = new byte[1024 * 1024 * 5];
        try {
            while ((read = is.read(buff)) != -1) {
                zos.write(buff, 0, read);
                zos.flush();
            }
        } catch (IOException e) {
        }
    }

    private void testCompressFolder() throws Exception {
        Path path = Paths.get("./folder");
        List<Path> paths = Files.list(path).collect(Collectors.toList());
        List<File> files = paths.stream().map(Path::toFile).collect(Collectors.toList());
//        File file = new File("./folder/phone.txt");

        File output = new File("phone.zip");
        FileOutputStream fos = new FileOutputStream(output);
        ZipOutputStream zos = new ZipOutputStream(fos);

        files.forEach(file -> {
            try {
                FileInputStream fis = new FileInputStream(file);

                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);

                int read;
                byte[] buff = new byte[1024 * 1024];
                while ((read = fis.read(buff)) != -1) {
                    zos.write(buff, 0, read);
                }
                fis.close();
            } catch (Exception e) { //ignore
            }
        });
        zos.closeEntry();
        zos.close();
    }

    class LogMem extends TimerTask {
        public void run() {
            System.out.println("freeMemory: " + Runtime.getRuntime().freeMemory() / 1048576);
            System.out.println("maxMemory: " + Runtime.getRuntime().maxMemory() / 1048576);
            System.out.println("totalMemory: " + Runtime.getRuntime().totalMemory() / 1048576);
            System.out.println("=======================================");
        }
    }
}
