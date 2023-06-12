package com.cardboardcritic.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PagingUtilTest {

    @ParameterizedTest
    @CsvSource(textBlock = """
            ,,0
            1,next,2
            2,next,3
            2,previous,1
            1,previous,0
            0,previous,0
            """)
    void getNewPage(Integer pageInput, String pageActionInput, int expected) {
        Optional<Integer> page = Optional.ofNullable(pageInput);
        Optional<String> pageAction = Optional.ofNullable(pageActionInput);
        int actual = PagingUtil.getNewPageNumber(page, pageAction);
        assertEquals(expected, actual);
    }
}