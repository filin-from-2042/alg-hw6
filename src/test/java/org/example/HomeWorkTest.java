package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeWorkTest {

    MorseTranslator morseTranslator = new MorseTranslatorImpl();

    @ParameterizedTest
    @MethodSource("getValidArguments")
    public void should_decodeValid(String word, String code) {
        Assertions.assertEquals(word, morseTranslator.decode(code));
    }

    @ParameterizedTest
    @MethodSource("getValidArguments")
    public void should_encodeValid(String code, String word) {
        Assertions.assertEquals(word, morseTranslator.encode(code));
    }

    private static Stream<Arguments> getValidArguments() {
        return Stream.of(
                Arguments.of("hello world", ".... . .-.. .-.. --- / .-- --- .-. .-.. -.."),
                Arguments.of("morse code", "-- --- .-. ... . / -.-. --- -.. ."),
                Arguments.of("thank you", "- .... .- -. -.- / -.-- --- ..-"),
                Arguments.of("see you", "... . . / -.-- --- ..-"),
                Arguments.of("take care", "- .- -.- . / -.-. .- .-. ."),
                Arguments.of("happy birthday", ".... .- .--. .--. -.-- / -... .. .-. - .... -.. .- -.--"),
                Arguments.of("time flies", "- .. -- . / ..-. .-.. .. . ..."),
                Arguments.of("stay safe", "... - .- -.-- / ... .- ..-. ."),
                Arguments.of("dream big", "-.. .-. . .- -- / -... .. --."),
                Arguments.of("love life", ".-.. --- ...- . / .-.. .. ..-. .")
        );
    }

    @Test
    public void when_wordArgumentIsInvalid_then_throwException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> morseTranslator.decode("abc"));
    }
    @Test
    public void when_codeArgumentIsInvalid_then_throwException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> morseTranslator.encode("..."));
    }
}