package ru.job4j.grabber.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class HabrCareerDateTimeParserTest {

    private HabrCareerDateTimeParser parser;

    @BeforeEach
    void setUp() {
        parser = new HabrCareerDateTimeParser();
    }

    @Test
    void dateWithMillisecondsThrowsException() {
        String dateTimeString = "2024-03-06T15:30:45.123";
        assertThrows(DateTimeParseException.class, () -> parser.parse(dateTimeString));
    }

    @Test
    void dateWithoutSecondsThrowsException() {
        String dateTimeString = "2024-03-06T15:30";
        assertThrows(DateTimeParseException.class, () -> parser.parse(dateTimeString));
    }

    @Test
    void dateWithLeadingOrTrailingSpacesThrowsException() {
        String dateTimeString = " 2024-03-06T15:30:45 ";
        assertThrows(DateTimeParseException.class, () -> parser.parse(dateTimeString));
    }

    @Test
    void dateWithExtraTextThrowsException() {
        String dateTimeString = "Дата: 2024-03-06T15:30:45";
        assertThrows(DateTimeParseException.class, () -> parser.parse(dateTimeString));
    }

    @Test
    void dateWithSingleFractionalSecondThrowsException() {
        String dateTimeString = "2024-03-06T15:30:45.1";
        assertThrows(DateTimeParseException.class, () -> parser.parse(dateTimeString));
    }

    @Test
    void dateWithWrongSeparatorsThrowsException() {
        String dateTimeString = "2024/03/06 15:30:45";
        assertThrows(DateTimeParseException.class, () -> parser.parse(dateTimeString));
    }
}