package com.zip.processor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Accumulates summary info about emails and phone numbers
 * and generates summary files in archive root
 */

public class Reporter {
    private static final String EMAILS_TXT = "emails.txt";
    private static final String PHONES_TXT = "phones.txt";
    private static final String DELIMITERS = "-,; \t\r\n";
    private static final String ORG_SUFFIX = ".org";
    private static final String AT_SIGN = "@";
    private final Set<String> emails = new HashSet<>();
    private final Set<String> phones = new HashSet<>();

    public Set<String> getEmails() {
        return emails;
    }

    public Set<String> getPhones() {
        return phones;
    }

    public void add(String line) {
        if (!line.isBlank())
            addToReport(line);
    }

    void generateFinalReport(ZipOutputStream zipInputStream) {
        logResults();
        try {
            writeEmails(zipInputStream);
            writePhones(zipInputStream);
        } catch (IOException e) {
            System.err.println("Error occurred while writing zip file.");
            e.printStackTrace();
        }
    }

    private void addToReport(String line) {
        StringBuilder phoneBuilder = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(line, DELIMITERS);
        while (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            if (!token.contains(AT_SIGN)) {
                phoneBuilder.append(token);
            } else if (token.contains(AT_SIGN) && token.endsWith(ORG_SUFFIX)) {
                emails.add(token);
            }
        }
        phones.add(phoneBuilder.toString());
    }

    private void writeEmails(ZipOutputStream zipOutputStream) throws IOException {
        ZipEntry entry = new ZipEntry(EMAILS_TXT);
        writeZipEntry(zipOutputStream, entry, emails);
    }

    private void writePhones(ZipOutputStream zipOutputStream) throws IOException {
        ZipEntry entry = new ZipEntry(PHONES_TXT);
        writeZipEntry(zipOutputStream, entry, phones);
    }

    private void writeZipEntry(ZipOutputStream zipOutputStream, ZipEntry zipEntry, Set<String> summaryInfo) throws IOException {
        zipOutputStream.putNextEntry(zipEntry);
        PrintWriter writer = new PrintWriter(zipOutputStream);
        summaryInfo.forEach(email -> {
            writer.println(email);
            writer.flush();
        });
        zipOutputStream.closeEntry();
    }

    private void logResults() {
        System.out.println("File processed successfully");
        System.out.println("Phones list: " + phones.size() + " total");
        phones.forEach(System.out::println);

        System.out.println("Emails list: " + emails.size() + " total");
        emails.forEach(System.out::println);
    }
}
