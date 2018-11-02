package com.tutorials;

import java.io.*;
import java.util.zip.*;

class Runner {
    public static void main(String[] args) throws Exception {
        new ZipProcessor().run();
    }
}

class ZipProcessor {
    void run() throws Exception {
        FileInputStream fis = new FileInputStream(new File("./inputs_v2.zip"));
        ZipInputStream zis = new ZipInputStream(fis);

        FileOutputStream fos = new FileOutputStream(new File("copy.zip"));
        ZipOutputStream zos = new ZipOutputStream(fos);

        process(zis, zos);

        fis.close();
        zis.close();
        zos.close();
        fos.close();
    }

    private void process(ZipInputStream zis, ZipOutputStream zos) throws Exception {
        ZipEntry zipEntry;
        while ((zipEntry = zis.getNextEntry()) != null) {
            String name = zipEntry.getName();
            zos.putNextEntry(new ZipEntry(name));
            System.out.println("Next entry \t" + (zipEntry.isDirectory() ? "Dir: \t" : "File:\t") + name);

//            if (zipEntry.isDirectory()) {
//                process(zis, zos);
//            }
            if (name.endsWith(".zip")) {
                ZipInputStream zippedZis = new ZipInputStream(zis);
                ZipOutputStream zippedZos = new ZipOutputStream(zos);
                process(zippedZis, zippedZos);
                zippedZos.closeEntry();
            }
            if (name.endsWith(".gz")) {
                GZIPInputStream gzippedZis = new GZIPInputStream(zis);
                GZIPOutputStream gzipZos = new GZIPOutputStream(zos);
                readAndWrite(gzippedZis, gzipZos);
                gzipZos.finish();
            }
            if (name.endsWith(".txt")) {
                readAndWrite(zis, zos);
            }
        }
    }

    private void readAndWrite(InputStream is, OutputStream zos) throws Exception {
        int read;
        byte[] buff = new byte[1024 * 1024 * 5];
        try {
            while ((read = is.read(buff)) != -1) {
//                System.out.println("\t" + new String(buff, 0, read));
                zos.write(buff, 0, read);
                zos.flush();
            }
        } catch (IOException e) {
        }
        System.out.println("---- End of file ----");
    }

}
