package com.tutorials;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;
import java.nio.charset.Charset;
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
        File archive = new File("./inputs_v2.zip");
        FileInputStream fis = new FileInputStream(archive);
        ZipInputStream zis = new ZipInputStream(fis);

        readZip(zis);

        zis.close();
        fis.close();
    }

    public File readAndWrite(InputStream is) throws Exception {
        File output = new File("inputs_v2 - Copy.zip");
        FileOutputStream os = new FileOutputStream(output);

        byte[] buff = new byte[1024 * 1024];
        int read;
        try {
            while ((read = is.read(buff)) != -1) {
                os.write(buff, 0, read);
                os.flush();
            }
        } catch (IOException e) {
            //ignore
        }
        os.close();
        return output;
    }


    public void readZip(ZipInputStream zis) throws Exception {
        ZipEntry zipEntry = null;
        while ((zipEntry = zis.getNextEntry()) != null) {
            String name = zipEntry.getName();
            System.out.println("Next entry \t" + (zipEntry.isDirectory() ? "Dir: \t" : "File:\t") + name);

            if (name.endsWith(".zip")) {
                ZipInputStream zippedZis = new ZipInputStream(zis);
                readZip(zippedZis);
            }
            if (name.endsWith(".txt")) {
                read(zis);
//                readTxtFile(zis);
            }
            if (name.endsWith(".gz")) {
                GZIPInputStream gzipped = new GZIPInputStream(zis);
                read(gzipped);
//                readTxtFile(gzipped);
            }
        }
    }

    public void read(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is);
        char[] buff = new char[1024 * 1024];
        int read;
        int cnt = 0;
        try {
            while ((read = isr.read(buff)) != -1) {
                cnt++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\tCount: " + cnt);
    }

    public void readTxtFile(InputStream inputStream) throws IOException {
        LineIterator it = IOUtils.lineIterator(inputStream, Charset.defaultCharset());
        int cnt = 0;
        try {
            while (it.hasNext()) {
                String s = it.nextLine();
                if (cnt <= 10) System.out.println(s);
                cnt++;
            }
        } finally {
//            LineIterator.closeQuietly(it);
            System.out.println("\tLines count:\t" + cnt);
        }
    }

}
