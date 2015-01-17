package com.codebattle.gui.editor;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.XmlReader;
import com.codebattle.utility.GameConstants;
import com.codebattle.utility.XMLUtil;

public class Lexer {

    private HashMap<String, String> refs;
    private HashMap<String, Color> colorMap;
    private Pattern pattern;

    public Lexer() {
        this.refs = new HashMap<String, String>();
        this.colorMap = new HashMap<String, Color>();
        this.init();
    }

    private void init() {
        try {
            for (XmlReader.Element refElement : XMLUtil.readXMLFromFile(
                    GameConstants.EDITOR_KEYWORD_REF).getChildrenByName("keyword")) {
                String type = refElement.getAttribute("type");
                String name = refElement.getText();
                refs.put(name, type);
            }

            for (XmlReader.Element colorElement : XMLUtil.readXMLFromFile(
                    GameConstants.EDITOR_COLOR_REF).getChildrenByName("item")) {
                String type = colorElement.getAttribute("type");

                Color color = Color.valueOf(colorElement.getAttribute("color"));
                System.out.printf("%s -> %s\n", type, color.toString());
                colorMap.put(type, color);
            }

            // Built-in reference
            StringBuffer patternBuffer = new StringBuffer();
            for (TokenType type : TokenType.values()) {
                patternBuffer.append(String.format("|(?<%s>%s)", type.name(),
                        type.getPattern()));
            }

            // Plugin reference

            this.pattern = Pattern.compile(patternBuffer.substring(1));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private LinkedList<Token> parse(String text) {

        LinkedList<Token> tokens = new LinkedList<Token>();
        Matcher matcher = this.pattern.matcher(text);

        while (matcher.find()) {
            for (TokenType type : TokenType.values()) {
                String tokenText = matcher.group(type.name());
                // System.out.println("test " + type.name());
                if (tokenText != null) {
                    System.out.printf("(%s) %s\n", type.name(), tokenText);
                    Color color = colorMap.get(type.name());
                    if (color == null)
                        color = Color.WHITE;
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
        private StringBuffer text;
        private LinkedList<Token> tokens;

        public Line() {
            this.text = new StringBuffer();
            this.tokens = new LinkedList<Token>();
        }

        public void setText(String text) {
            this.text.append(text);
        }

        public String getText() {
            return this.text.toString();
        }

        public StringBuffer getBuffer() {
            return this.text;
        }

        public void parse() {
            this.tokens = Lexer.this.parse(text.toString());
        }

        public int length() {
            return this.text.length();
        }

        public LinkedList<Token> getTokens() {
            return this.tokens;
        }

        public String substring(int position) {
            return this.text.substring(position);
        }

        public void delete(int start, int end) {
            this.text.delete(start, end);
        }

        public void append(String s) {
            this.text.append(s);
        }

        public void deleteCharAt(int position) {
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

        public Token(String text, TokenType type, Color color) {
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

        private TokenType(String pattern) {
            this.pattern = pattern;
        }

        public String getPattern() {
            return this.pattern;
        }
    }
}
