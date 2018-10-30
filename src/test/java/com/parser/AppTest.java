package com.parser;

import org.apache.commons.codec.digest.DigestUtils;
import org.testng.annotations.Test;

import java.io.*;

public class AppTest {

    @Test
    public void testName() throws IOException {
        System.out.println("freeMemory: " + Runtime.getRuntime().freeMemory() / 1048576);
        System.out.println("maxMemory: " + Runtime.getRuntime().maxMemory() / 1048576);
        System.out.println("totalMemory: " + Runtime.getRuntime().totalMemory() / 1048576);

    }

    @Test
    public void testFileMd5() throws Exception {
        File orig = new File("./folder/phone.txt");
        FileInputStream fis = new FileInputStream(orig);
        File output = readWrite(fis);
        fis.close();

        String md5HexOrig = DigestUtils.md5Hex(new FileInputStream(orig)).toUpperCase();
        String md5HexOutput = DigestUtils.md5Hex(new FileInputStream(output)).toUpperCase();

        System.out.println(md5HexOrig);
        System.out.println(md5HexOrig.equalsIgnoreCase(md5HexOutput) ? "equal" : "not equal");
        System.out.println(md5HexOutput);
    }

    private File readWrite(InputStream is) throws FileNotFoundException {
        InputStreamReader isr = new InputStreamReader(is);

        File output = new File("phone.txt");
        OutputStream os = new FileOutputStream(output);
        PrintWriter writer = new PrintWriter(os);

        char[] buff = new char[1024 * 1024];
        int read;
        try {
            while ((read = isr.read(buff)) != -1) {
//                System.out.println(new String(buff, 0, read));
                writer.write(buff, 0, read);
                writer.flush();
            }
        } catch (IOException e) {
        }

        writer.close();
        return output;
    }


}
