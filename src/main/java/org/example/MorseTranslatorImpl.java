package org.example;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class MorseTranslatorImpl implements MorseTranslator {

    private final Map<Character, String> dictionary = new HashMap<>();
    private final Node root = new Node(' ');

    public MorseTranslatorImpl() {
        loadDictionary();
    }

    @Override
    public String decode(@NotNull String morseCode) {
        Objects.requireNonNull(morseCode);
        if(!morseCode.matches("[-./\\s]+"))
            throw new IllegalArgumentException();

        List<String> result = new ArrayList<>();
        String[] words = morseCode.split(" / ");
        for (String word : words) {
            result.add(decodeWord(word.toLowerCase()));
        }
        return String.join(" ", result);
    }

    private String decodeWord(String word) {
        String[] letters = word.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String letter : letters) {
            Node current = root;
            for (char code : letter.toCharArray()) {
                current = current.findChild(code);
            }
            sb.append(current.letter);
        }
        return sb.toString();
    }

    @Override
    public String encode(@NotNull String source) {
        Objects.requireNonNull(source);
        if(!source.matches("[a-zA-Z0-9\\s]+"))
            throw new IllegalArgumentException();

        String[] words = source.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            for (char c : word.toCharArray()) {
                sb.append(dictionary.get(c));
                sb.append(" ");
            }
            sb.append("/ ");
        }

        sb.delete(sb.length() - 3, sb.length());
        return sb.toString();
    }

    public void addCode(@NotNull String code, char letter) {
        code = code.toLowerCase();
        Node current = root;

        for (char c : code.toCharArray()) {
            current = current.addChild(c);
        }
        current.letter = letter;
    }

    static class Node {
        Node parent;
        Node left;
        Node right;
        char code;
        char letter;

        public Node(char code) {
            this.code = code;
        }

        Node addChild(char code) {
            Node found = findChild(code);
            if (found != null) {
                return found;
            }
            Node newNode = new Node(code);
            switch (code) {
                case '.':
                    left = newNode;
                    break;
                case '-':
                    right = newNode;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid code");
            }

            newNode.parent = this;
            return newNode;
        }

        Node findChild(char code) {
            switch (code) {
                case '.':
                    return left;
                case '-':
                    return right;
                default:
                    throw new IllegalArgumentException("Invalid code");
            }
        }
    }

    @SneakyThrows
    void loadDictionary() {
        Files.lines(Path.of("src/main/resources/dictionary.txt"))
                .filter((line) -> !line.startsWith("#"))
                .forEach(s -> {
                    String[] code = s.split(";");
                    this.addCode(code[1], code[0].charAt(0));
                    dictionary.put(code[0].charAt(0), code[1]);
                });
    }
}
