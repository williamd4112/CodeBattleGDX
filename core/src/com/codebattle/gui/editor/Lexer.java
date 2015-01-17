package com.codebattle.gui.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.XMLUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private final HashMap<String, String> refs;
    private final HashMap<String, Color> colorMap;
    private Pattern pattern;

    public Lexer() {
        this.refs = new HashMap<String, String>();
        this.colorMap = new HashMap<String, Color>();
        this.init();
    }

    private void init() {
        try {
            for (final XmlReader.Element refElement : XMLUtil.readXMLFromFile(
                    GameConstants.EDITOR_KEYWORD_REF).getChildrenByName("keyword")) {
                final String type = refElement.getAttribute("type");
                final String name = refElement.getText();
                this.refs.put(name, type);
            }

            for (final XmlReader.Element colorElement : XMLUtil.readXMLFromFile(
                    GameConstants.EDITOR_COLOR_REF).getChildrenByName("item")) {
                final String type = colorElement.getAttribute("type");

                final Color color = Color.valueOf(colorElement.getAttribute("color"));
                System.out.printf("%s -> %s\n", type, color.toString());
                this.colorMap.put(type, color);
            }

            // Built-in reference
            final StringBuffer patternBuffer = new StringBuffer();
            for (final TokenType type : TokenType.values()) {
                patternBuffer.append(String.format("|(?<%s>%s)", type.name(),
                        type.getPattern()));
            }

            // Plugin reference

            this.pattern = Pattern.compile(patternBuffer.substring(1));

        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private LinkedList<Token> parse(final String text) {

        final LinkedList<Token> tokens = new LinkedList<Token>();
        final Matcher matcher = this.pattern.matcher(text);

        while (matcher.find()) {
            for (final TokenType type : TokenType.values()) {
                final String tokenText = matcher.group(type.name());
                // System.out.println("test " + type.name());
                if (tokenText != null) {
                    System.out.printf("(%s) %s\n", type.name(), tokenText);
                    Color color = this.colorMap.get(type.name());
                    if (color == null) {
                        color = Color.WHITE;
                    }
                    tokens.add(new Token(tokenText, type, color));
                    // System.out.println(String.format("%s : %s", type.name(),
                    // matcher.group(type.name())));
                    break;
                }
            }
        }

        System.out.println();
        return tokens;
    }

    public class Line {
        private final StringBuffer text;
        private LinkedList<Token> tokens;

        public Line() {
            this.text = new StringBuffer();
            this.tokens = new LinkedList<Token>();
        }

        public void setText(final String text) {
            this.text.append(text);
        }

        public String getText() {
            return this.text.toString();
        }

        public StringBuffer getBuffer() {
            return this.text;
        }

        public void parse() {
            this.tokens = Lexer.this.parse(this.text.toString());
        }

        public int length() {
            return this.text.length();
        }

        public LinkedList<Token> getTokens() {
            return this.tokens;
        }

        public String substring(final int position) {
            return this.text.substring(position);
        }

        public void delete(final int start, final int end) {
            this.text.delete(start, end);
        }

        public void append(final String s) {
            this.text.append(s);
        }

        public void deleteCharAt(final int position) {
            this.text.deleteCharAt(position);
        }

        @Override
        public String toString() {
            return this.text.toString();
        }
    }

    public class Token {
        public Color color;
        public TokenType type;
        public String text;

        public Token(final String text, final TokenType type, final Color color) {
            this.text = text;
            this.type = type;
            this.color = color;
        }
    }

    public enum TokenType {
        Comment("^(//).*"),
        Identifier("(var|function|if|else|for|while|do|try|catch|finally|new)(?!\\w)"),
        SEPARATOR("\\s"),
        PLAIN("\\S+");

        private String pattern;

        private TokenType(final String pattern) {
            this.pattern = pattern;
        }

        public String getPattern() {
            return this.pattern;
        }
    }
}
