package com.codebattle.gui.editor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;
import com.codebattle.gui.editor.Lexer.Line;
import com.codebattle.gui.editor.Lexer.Token;

public class JSCodeEditorCore extends Widget {
    final private static int JUMP_UP = 0, JUMP_DOWN = 1, JUMP_LASTEND = 2;
    private JSCodeEditorStyle style;

    private JSCodeEditorMode mode = JSCodeEditorMode.VISUAL;
    private Lexer lexer;
    private List<Line> lines;

    private List<JSCodeEditorStateListener> codeEditorListeners;

    private int cursorRow = 0, cursorCol = 0;
    private int pivotRow = -1, pivotCol = -1;
    private int selectRow = -1, selectCol = -1;

    private boolean cursorBlinkSwitch = true;
    private float padding = 20;

    public JSCodeEditorCore(Skin skin) {
        this.lexer = new Lexer();
        this.lines = new ArrayList<Line>();
        this.codeEditorListeners = new LinkedList<JSCodeEditorStateListener>();
        this.lines.add(lexer.new Line());

        this.addListener(new JSCodeEditorListener());

        this.style = skin.get(JSCodeEditorStyle.class);

        this.initializeCursor();
    }

    public void addCodeEditorListener(JSCodeEditorStateListener listener) {
        this.codeEditorListeners.add(listener);
    }

    public void emitCursorChangeEvent() {
        for (JSCodeEditorStateListener listener : this.codeEditorListeners) {
            listener.onCursorChange(mode, cursorCol, cursorRow);
        }
    }

    public void emitCursorExceedWidthEvent() {
        for (JSCodeEditorStateListener listener : this.codeEditorListeners) {
            listener.onCursorExceedWidth(getWidth());
        }
    }

    public void emitCursorExceedHeightEvent() {
        for (JSCodeEditorStateListener listener : this.codeEditorListeners) {
            listener.onCursorExceedHeight(lines.size());
        }
    }

    private void initializeCursor() {
        Timer blinkTimer = new Timer();
        blinkTimer.scheduleTask(new Timer.Task() {

            @Override
            public void run() {
                cursorBlinkSwitch = !cursorBlinkSwitch;
            }

        }, 0.1f, 0.1f);
        blinkTimer.start();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float x = getX(), y = getY(), width = getWidth(), height = getHeight();

        drawBackground(batch, parentAlpha, x, y, width, height);
        drawLineNumberBackground(batch, parentAlpha, x, y, width, height);
        drawText(batch, parentAlpha, x, y, width, height);
        drawCursor(batch, parentAlpha, x, y);

        // System.out.printf("(%d , %d : %d , %d)\n", this.pivotRow, this.pivotCol,
        // this.selectRow, this.selectCol);

    }

    protected void drawBackground(Batch batch, float parentAlpha, float x, float y,
            float width, float height) {
        Drawable background = style.background;
        batch.setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, batch.getColor().a
                * parentAlpha);
        background.draw(batch, x, y, width, height);
    }

    protected void drawLineNumberBackground(Batch batch, float parentAlpha, float x, float y,
            float width, float height) {
        Drawable background = style.background;
        batch.setColor(Color.DARK_GRAY.r, Color.DARK_GRAY.g, Color.DARK_GRAY.b, parentAlpha);
        background.draw(batch, x, y, padding, height);
    }

    protected void drawText(Batch batch, float parentAlpha, float x, float y, float width,
            float height) {
        BitmapFont font = style.font;
        float offsetX = padding, offsetY = y + this.getPrefHeight();
        for (int i = 0; i < lines.size(); i++) {

            // Draw Line Number
            font.setScale(0.5f);
            font.setColor(Color.YELLOW.r, Color.YELLOW.g, Color.YELLOW.b, parentAlpha);
            font.draw(batch, String.valueOf(i + 1), x + 5, offsetY - font.getLineHeight() / 4);

            // Draw
            font.setScale(1.0f);

            font.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, parentAlpha);
            Line line = lines.get(i);
            for (Token token : line.getTokens()) {
                font.setColor(token.color.r, token.color.g, token.color.b, parentAlpha);
                font.draw(batch, token.text, x + offsetX, offsetY);
                offsetX += font.getBounds(token.text).width;
            }

            offsetY -= font.getLineHeight();
            offsetX = padding;
        }
    }

    protected void drawCursor(Batch batch, float parentAlpha, float x, float y) {
        if (this.getStage().getKeyboardFocus() != this)
            return;
        batch.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, parentAlpha);
        Drawable cursor = (this.mode != JSCodeEditorMode.INSERT) ? style.selection
                : style.cursor;
        BitmapFont font = style.font;
        float cursorX = colToX(cursorCol, style.font.getSpaceWidth()), cursorY = y
                + this.getPrefHeight() - (cursorRow + 1) * font.getLineHeight();
        if (this.cursorBlinkSwitch)
            cursor.draw(batch, x + cursorX + padding, cursorY, cursor.getMinWidth(),
                    font.getLineHeight());
    }

    public void moveCursorRight() {
        if (this.lines.isEmpty())
            return;
        if (this.cursorCol < this.lines.get(cursorRow).length())
            this.setCursorPosition(cursorCol + 1, cursorRow);
    }

    public void moveCursorLeft() {
        if (this.lines.isEmpty())
            return;
        if (this.cursorCol > 0)
            this.setCursorPosition(cursorCol - 1, cursorRow);
    }

    private float rowToY(int row, float lineHeight) {
        return this.getY() + this.getPrefHeight() - (cursorRow + 1)
                * style.font.getLineHeight();
    }

    private float colToX(int col, float spaceWidth) {
        if (lines.isEmpty())
            return 0;
        if (this.lines.get(cursorRow) == null)
            return 0;
        if (col >= this.lines.get(cursorRow).length())
            col = this.lines.get(cursorRow).length();
        return style.font.getBounds(this.lines.get(cursorRow).toString(), 0, col).width;
    }

    private void insert(int position, StringBuffer dest, String insertion) {
        if (dest.length() <= 0)
            dest.append(insertion);
        else {
            dest.insert(position, insertion);
        }
        this.moveCursorRight();

    }

    private void newline(int position) {
        if (this.lines.isEmpty()) // Empty lines
            this.lines.add(lexer.new Line());
        if (this.lines.get(cursorRow) == null) // null line
            return;
        if (position > this.lines.get(cursorRow).length()) // exceed position
            return;
        Line line = this.lines.get(cursorRow);
        if (position <= line.length()) {
            Line newline = lexer.new Line();
            if (position < line.length()) {
                newline.append(line.substring(position));
                line.delete(position, line.length());
            }
            this.lines.add(cursorRow + 1, newline);
        }
    }

    private void jump(int mode) {
        if (lines.isEmpty())
            return;
        switch (mode) {
        case JUMP_DOWN:
            this.setCursorPosition(cursorCol, cursorRow + 1);
            this.repositionAfterJump();
            break;
        case JUMP_UP:
            this.setCursorPosition(cursorCol, cursorRow - 1);
            this.repositionAfterJump();
            break;
        case JUMP_LASTEND:
            this.setCursorPosition(cursorCol, cursorRow - 1);
            this.cursorCol = this.lines.get(cursorRow).length();
            break;
        default:
            break;
        }
    }

    private void backspace() {
        if (lines.isEmpty())
            return;
        if (lines.get(cursorRow) == null)
            return;

        Line line = lines.get(cursorRow);
        if (cursorCol > 0) {
            --cursorCol;
            line.deleteCharAt(cursorCol);
        } else {
            if (cursorCol <= 0) { // line delete
                if (lines.size() > 1)
                    lines.remove(cursorRow);
                this.jump(JUMP_LASTEND);
            }
        }
        this.setCursorPosition(cursorCol, cursorRow);

    }

    private void delete() {
        if (lines.isEmpty())
            return;
        if (lines.get(cursorRow) == null)
            return;

    }

    private void repositionAfterJump() {
        if (this.lines.get(cursorRow) != null) {
            if (cursorCol >= this.lines.get(cursorRow).length()) {
                cursorCol = this.lines.get(cursorRow).length();
            }
        }
    }

    private void setCursorPosition(int col, int row) {
        try {
            if (row >= this.lines.size())
                row = this.lines.size() - 1;
            else if (row < 0)
                row = 0;

            Line line = lines.get(row);
            if (col >= line.length())
                col = line.length();
            this.cursorRow = row;
            this.cursorCol = col;
            this.emitCursorChangeEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Vector2 mouseToPosition(float x, float y) {
        try {
            // System.out.printf("(%f , %f)\n", x, y);
            int row = (int) ((this.getHeight() - y) / this.style.font.getLineHeight());
            if (row >= lines.size() && row != 0)
                row = lines.size() - 1;
            else if (row <= 0)
                row = 0;
            Line line = lines.get(row);
            int col = 0;
            for (; col < line.length(); col++)
                if (style.font.getBounds(line.getText(), 0, col + 1).width + this.getWidth()
                        * 0.05f >= x)
                    break;
            // System.out.printf("     (%d , %d)\n", col, row);
            return new Vector2(col, row);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Vector2(0, 0);
    }

    public void parseLines() {
        for (Line line : this.lines) {
            line.parse();
        }
    }

    public String getText() {
        String str = "";
        for (Line line : lines)
            str += line.toString();
        return str;
    }

    public void setText(String s) {
        Line line = this.lines.get(cursorRow);
        for (int i = 0; i < s.length(); i++) {
            this.insert(cursorCol, line.getBuffer(), String.valueOf(s.charAt(i)));
            parseLines();
        }
    }

    public void setMode(JSCodeEditorMode mode) {
        this.mode = mode;
        this.emitCursorChangeEvent();
    }

    public class JSCodeEditorListener extends ClickListener {

        private boolean leftPressed = false;

        public JSCodeEditorListener() {
            super();
        }

        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            // TODO Auto-generated method stub
            super.enter(event, x, y, pointer, fromActor);
            this.leftPressed = true;
            // System.out.println("enter : " + this.leftPressed);

        }

        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            // TODO Auto-generated method stub
            super.exit(event, x, y, pointer, toActor);
            this.leftPressed = false;
            // System.out.println("exit : " + this.leftPressed);
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            super.touchDragged(event, x, y, pointer);
            System.out.printf("touch drag(%f , %f)\n", x, y);
            Vector2 pos = mouseToPosition(x, y);
            setCursorPosition((int) pos.x, (int) pos.y);
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            getStage().setKeyboardFocus(JSCodeEditorCore.this);
            // System.out.printf("touch down(%f , %f)\n", x, y);
            switch (button) {
            case Input.Buttons.LEFT:
                Vector2 pos = mouseToPosition(x, y);
                setCursorPosition((int) pos.x, (int) pos.y);
                pivotRow = (int) pos.y;
                pivotCol = (int) pos.x;
                break;
            default:
                break;
            }
            return super.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            // System.out.printf("touch up(%f , %f)\n", x, y);
            switch (button) {
            case Input.Buttons.LEFT:
                selectRow = cursorRow;
                selectCol = cursorCol;
                break;
            default:
                break;
            }
            super.touchUp(event, x, y, pointer, button);
        }

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            switch (keycode) {
            case Input.Keys.UP:
                jump(JUMP_LASTEND);
                break;
            case Input.Keys.DOWN:
                jump(JUMP_DOWN);
                break;
            case Input.Keys.LEFT:
                moveCursorLeft();
                break;
            case Input.Keys.RIGHT:
                moveCursorRight();
                break;
            case Input.Keys.BACKSPACE:
                backspace();
                break;
            case Input.Keys.FORWARD_DEL:
                delete();
                break;
            case Input.Keys.INSERT:
                setMode(JSCodeEditorMode.INSERT);
                break;
            case Input.Keys.ESCAPE:
                setMode(JSCodeEditorMode.VISUAL);
                break;
            default:
                break;
            }
            // System.out.printf("(%d , %d)\n", cursorCol, cursorRow);
            return super.keyDown(event, keycode);
        }

        @Override
        public boolean keyUp(InputEvent event, int keycode) {
            // TODO Auto-generated method stub
            return super.keyUp(event, keycode);
        }

        @Override
        public boolean keyTyped(InputEvent event, char character) {
            if (character == '\r')
                character = '\n';
            boolean add = style.font.containsCharacter(character);
            boolean newline = (character == '\n');
            boolean tab = (character == '\t');

            if (add) {
                Line line = lines.get(cursorRow);
                insert(cursorCol, line.getBuffer(), String.valueOf(character));
            }

            if (tab) {
                Line line = lines.get(cursorRow);
                for (int i = 0; i < 4; i++)
                    insert(cursorCol, line.getBuffer(), " ");
            }

            if (newline) {
                newline(cursorCol);
                jump(JUMP_DOWN);
            }
            parseLines();
            return super.keyTyped(event, character);
        }

    }

    @Override
    public float getPrefWidth() {
        float maxLen = this.getWidth();
        for (Line line : lines) {
            float lineLen = style.font.getBounds(line.toString()).width
                    + style.font.getSpaceWidth() * 10;
            if (lineLen > maxLen) {
                maxLen = lineLen;
                this.fire(new ChangeListener.ChangeEvent());
            }
        }
        this.invalidateHierarchy();

        return maxLen;
    }

    @Override
    public float getPrefHeight() {
        float realHeight = this.lines.size() * style.font.getLineHeight();
        if (realHeight > this.getHeight()) {
            this.setHeight(realHeight);
        }
        this.invalidateHierarchy();
        return this.getHeight();
    }

    static public class JSCodeEditorStyle {
        public BitmapFont font;
        public Color fontColor, focusedFontColor, disabledFontColor;
        /** Optional. */
        public Drawable background, focusedBackground, disabledBackground, cursor, selection;
        /** Optional. */
        public BitmapFont messageFont;
        /** Optional. */
        public Color messageFontColor;

        public JSCodeEditorStyle() {
        }

        public JSCodeEditorStyle(BitmapFont font, Color fontColor, Drawable cursor,
                Drawable selection, Drawable background) {
            this.background = background;
            this.cursor = cursor;
            this.font = font;
            this.fontColor = fontColor;
            this.selection = selection;
        }

        public JSCodeEditorStyle(JSCodeEditorStyle style) {
            this.messageFont = style.messageFont;
            if (style.messageFontColor != null)
                this.messageFontColor = new Color(style.messageFontColor);
            this.background = style.background;
            this.focusedBackground = style.focusedBackground;
            this.disabledBackground = style.disabledBackground;
            this.cursor = style.cursor;
            this.font = style.font;
            if (style.fontColor != null)
                this.fontColor = new Color(style.fontColor);
            if (style.focusedFontColor != null)
                this.focusedFontColor = new Color(style.focusedFontColor);
            if (style.disabledFontColor != null)
                this.disabledFontColor = new Color(style.disabledFontColor);
            this.selection = style.selection;
        }
    }
}
