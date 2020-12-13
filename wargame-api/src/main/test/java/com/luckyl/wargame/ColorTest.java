package com.luckyl.wargame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ColorTest {
    private Color color;

    @Test
    void invalidColor() {
        color = Color.INVALID;
        assertEquals("", color.getRgb());
        assertEquals(Color.INVALID, color.flip());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void nullColor(String colorText) {
        color = Color.fromRgb(colorText);
        assertEquals(Color.INVALID, color);
    }

    @ParameterizedTest
    @EnumSource(value=Color.class, names = {"BLUE", "BLUE_DOWN"})
    void blueTest(Color color) {
        assertTrue(color.isBlue());
    }

    @ParameterizedTest
    @EnumSource(value=Color.class, names = {"RED", "RED_DOWN"})
    public void RedTest(Color color) {
        assertTrue(color.isRed());
    }

    @ParameterizedTest
    @EnumSource(value=Color.class, names = {"WHITE"})
    void whiteTest(Color color) {
        assertTrue(color.isWhite());
    }

    @ParameterizedTest
    @EnumSource(value=Color.class, names = {"GRAY"})
    void grayTest(Color color) {
        assertTrue(color.isGray());
    }

    @ParameterizedTest
    @MethodSource("providerColors")
    void flipTest(Color input, Color expected) {
        final Color actual = input.flip();
        assertEquals(actual, expected);
    }

    private static Stream<Arguments> providerColors() {
        return Stream.of(
            Arguments.of(Color.BLUE, Color.BLUE_DOWN),
            Arguments.of(Color.BLUE_DOWN, Color.BLUE),
            Arguments.of(Color.GRAY, Color.GRAY),
            Arguments.of(Color.INVALID, Color.INVALID),
            Arguments.of(Color.RED, Color.RED_DOWN),
            Arguments.of(Color.RED_DOWN, Color.RED),
            Arguments.of(Color.WHITE, Color.WHITE)
        );
    }

}