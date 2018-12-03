package com.zip.processor;

class StringUtils {


    static String replaceExtension(String line) {
        return line.replace("(101)", "(401)")
                .replace("(202)", "(802)")
                .replace("(301)", "(321)").intern();
    }
}
