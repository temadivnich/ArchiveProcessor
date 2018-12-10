package com.zip.processor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.zip.processor.StringConstants.*;
import static com.zip.processor.Utils.logResults;

/**
 * Accumulates summary info about emails and phone numbers
 * and generates summary files in archive root
 */

public class ReportGenerator {
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

    void generateFinalReport(ZipOutputStream zipInputStream) throws IOException {
        logResults(emails, phones);
        writeEmailsToZip(zipInputStream);
        writePhonesToZip(zipInputStream);
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

    private void writeEmailsToZip(ZipOutputStream zipOutputStream) throws IOException {
        ZipEntry entry = new ZipEntry(EMAILS_TXT);
        writeZipEntry(zipOutputStream, entry, emails);
    }

    private void writePhonesToZip(ZipOutputStream zipOutputStream) throws IOException {
        ZipEntry entry = new ZipEntry(PHONES_TXT);
        writeZipEntry(zipOutputStream, entry, phones);
    }

    private void writeZipEntry(ZipOutputStream zipOutputStream, ZipEntry zipEntry, Set<String> summaryInfo) throws IOException {
        zipOutputStream.putNextEntry(zipEntry);
        PrintWriter writer = new PrintWriter(zipOutputStream, true);
        summaryInfo.forEach(writer::println);
        zipOutputStream.closeEntry();
    }
}
