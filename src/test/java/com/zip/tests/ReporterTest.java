package com.zip.tests;


import com.zip.processor.Reporter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReporterTest {
    private final Reporter reporter = new Reporter();
    private static List<String> text = List.of(
            "+7 (101) 111-222-11  abc@ert.com, def@sdf.org\n",
            "+1 (102) 123532-2 some@mail.ru\n",
            "+44 (301) 123 23 45 7zip@site.edu; ret@ghjj.org",
            "+5 (202) 1111111 aaa@b.org",
            "+8 (202) 2345 2345 ert@yandex.ru ttt@info.com; uuu@ttt.org",
            "+1(301) 2345-2345-2345 123.1234.1234@domain.org",
            "+1(234) 2323-33312;;;abc@domain.org",
            "+1 (101) 345 2345 2345 we@jyt.com",
            "+1 (301)",
            "+4 (202) 234523 wer@t.org, wer@t.org, wer@t.org, wer@t.org, \r\n");

    @Test
    void whenSupplyLine_thenAddedToReport() {
        text.forEach(reporter::add);
        assertEquals(reporter.getEmails().size(), 7);
        assertEquals(reporter.getPhones().size(), 10);
    }
}
