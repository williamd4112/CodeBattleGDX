package com.codebattle.gui.editor;

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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JSCodeEditorCore extends Widget {
    final private static int JUMP_UP = 0, JUMP_DOWN = 1, JUMP_LASTEND = 2;
    private final JSCodeEditorStyle style;

    private JSCodeEditorMode mode = JSCodeEditorMode.VISUAL;
    private final Lexer lexer;
    private final List<Line> lines;

    private final List<JSCodeEditorStateListener> codeEditorListeners;

    private int cursorRow = 0, cursorCol = 0;
    private int pivotRow = -1, pivotCol = -1;
    private int selectRow = -1, selectCol = -1;

    private boolean cursorBlinkSwitch = true;
    private final float padding = 20;

    public JSCodeEditorCore(final Skin skin) {
        this.lexer = new Lexer();
        this.lines = new ArrayList<Line>();
        this.codeEditorListeners = new LinkedList<JSCodeEditorStateListener>();
        this.lines.add(this.lexer.new Line());

        this.addListener(new JSCodeEditorListener());

        this.style = skin.get(JSCodeEditorStyle.class);

        this.initializeCursor();
    }

    public void addCodeEditorListener(final JSCodeEditorStateListener listener) {
        this.codeEditorListeners.add(listener);
    }

    public void emitCursorChangeEvent() {
        for (final JSCodeEditorStateListener listener : this.codeEditorListeners) {
            listener.onCursorChange(this.mode, this.cursorCol, this.cursorRow);
        }
    }

    public void emitCursorExceedWidthEvent() {
        for (final JSCodeEditorStateListener listener : this.codeEditorListeners) {
            listener.onCursorExceedWidth(this.getWidth());
        }
    }

    public void emitCursorExceedHeightEvent() {
        for (final JSCodeEditorStateListener listener : this.codeEditorListeners) {
            listener.onCursorExceedHeight(this.lines.size());
        }
    }

    private void initializeCursor() {
        final Timer blinkTimer = new Timer();
        blinkTimer.scheduleTask(new Timer.Task() {

            @Override
            public void run() {
                JSCodeEditorCore.this.cursorBlinkSwitch =
                        !JSCodeEditorCore.this.cursorBlinkSwitch;
            }

        }, 0.1f, 0.1f);
        blinkTimer.start();
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        super.draw(batch, parentAlpha);
        final float x = this.getX(), y = this.getY(), width = this.getWidth(), height =
                this.getHeight();

        this.drawBackground(batch, parentAlpha, x, y, width, height);
        this.drawLineNumberBackground(batch, parentAlpha, x, y, width, height);
        this.drawText(batch, parentAlpha, x, y, width, height);
        this.drawCursor(batch, parentAlpha, x, y);

        // System.out.printf("(%d , %d : %d , %d)\n", this.pivotRow, this.pivotCol,
        // this.selectRow, this.selectCol);

    }

    protected void drawBackground(final Batch batch, final float parentAlpha, final float x,
            final float y,
            final float width, final float height) {
        final Drawable background = this.style.background;
        batch.setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, batch.getColor().a
                * parentAlpha);
        background.draw(batch, x, y, width, height);
    }

    protected void drawLineNumberBackground(final Batch batch, final float parentAlpha,
            final float x, final float y,
            final float width, final float height) {
        final Drawable background = this.style.background;
        batch.setColor(Color.DARK_GRAY.r, Color.DARK_GRAY.g, Color.DARK_GRAY.b, parentAlpha);
        background.draw(batch, x, y, this.padding, height);
    }

    protected void drawText(final Batch batch, final float parentAlpha, final float x,
            final float y, final float width,
            final float height) {
        final BitmapFont font = this.style.font;
        float offsetX = this.padding, offsetY = y + this.getPrefHeight();
        for (int i = 0; i < this.lines.size(); i++) {

            // Draw Line Number
            font.setScale(0.5f);
            font.setColor(Color.YELLOW.r, Color.YELLOW.g, Color.YELLOW.b, parentAlpha);
            font.draw(batch, String.valueOf(i + 1), x + 5, offsetY - font.getLineHeight() / 4);

            // Draw
            font.setScale(1.0f);

            font.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, parentAlpha);
            final Line line = this.lines.get(i);
            for (final Token token : line.getTokens()) {
                font.setColor(token.color.r, token.color.g, token.color.b, parentAlpha);
                font.draw(batch, token.text, x + offsetX, offsetY);
                offsetX += font.getBounds(token.text).width;
            }

            offsetY -= font.getLineHeight();
            offsetX = this.padding;
        }
    }

    protected void drawCursor(final Batch batch, final float parentAlpha, final float x,
            final float y) {
        if (this.getStage().getKeyboardFocus() != this) {
            return;
        }
        batch.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, parentAlpha);
        final Drawable cursor = this.mode != JSCodeEditorMode.INSERT ? this.style.selection
                : this.style.cursor;
        final BitmapFont font = this.style.font;
        final float cursorX = this.colToX(this.cursorCol, this.style.font.getSpaceWidth()), cursorY =
                y
                        + this.getPrefHeight() - (this.cursorRow + 1) * font.getLineHeight();
        if (this.cursorBlinkSwitch) {
            cursor.draw(batch, x + cursorX + this.padding, cursorY, cursor.getMinWidth(),
                    font.getLineHeight());
        }
    }

    public void moveCursorRight() {
        if (this.lines.isEmpty()) {
            return;
        }
        if (this.cursorCol < this.lines.get(this.cursorRow).length()) {
            this.setCursorPosition(this.cursorCol + 1, this.cursorRow);
        }
    }

    public void moveCursorLeft() {
        if (this.lines.isEmpty()) {
            return;
        }
        if (this.cursorCol > 0) {
            this.setCursorPosition(this.cursorCol - 1, this.cursorRow);
        }
    }

    private float rowToY(final int row, final float lineHeight) {
        return this.getY() + this.getPrefHeight() - (this.cursorRow + 1)
                * this.style.font.getLineHeight();
    }

    private float colToX(int col, final float spaceWidth) {
        if (this.lines.isEmpty()) {
            return 0;
        }
        if (this.lines.get(this.cursorRow) == null) {
            return 0;
        }
        if (col >= this.lines.get(this.cursorRow).length()) {
            col = this.lines.get(this.cursorRow).length();
        }
        return this.style.font.getBounds(this.lines.get(this.cursorRow).toString(), 0, col).width;
    }

    private void insert(final int position, final StringBuffer dest, final String insertion) {
        if (dest.length() <= 0) {
            dest.append(insertion);
        } else {
            dest.insert(position, insertion);
        }
        this.moveCursorRight();

    }

    private void newline(final int position) {
        if (this.lines.isEmpty()) {
            this.lines.add(this.lexer.new Line());
        }
        if (this.lines.get(this.cursorRow) == null) {
            return;
        }
        if (position > this.lines.get(this.cursorRow).length()) {
            return;
        }
        final Line line = this.lines.get(this.cursorRow);
        if (position <= line.length()) {
            final Line newline = this.lexer.new Line();
            if (position < line.length()) {
                newline.append(line.substring(position));
                line.delete(position, line.length());
            }
            this.lines.add(this.cursorRow + 1, newline);
        }
    }

    private void jump(final int mode) {
        if (this.lines.isEmpty()) {
            return;
        }
        switch (mode) {
        case JUMP_DOWN:
            this.setCursorPosition(this.cursorCol, this.cursorRow + 1);
            this.repositionAfterJump();
            break;
        case JUMP_UP:
            this.setCursorPosition(this.cursorCol, this.cursorRow - 1);
            this.repositionAfterJump();
            break;
        case JUMP_LASTEND:
            this.setCursorPosition(this.cursorCol, this.cursorRow - 1);
            this.cursorCol = this.lines.get(this.cursorRow).length();
            break;
        default:
            break;
        }
    }

    private void backspace() {
        if (this.lines.isEmpty()) {
            return;
        }
        if (this.lines.get(this.cursorRow) == null) {
            return;
        }

        final Line line = this.lines.get(this.cursorRow);
        if (this.cursorCol > 0) {
            --this.cursorCol;
            line.deleteCharAt(this.cursorCol);
        } else {
            if (this.cursorCol <= 0) { // line delete
                if (this.lines.size() > 1) {
                    this.lines.remove(this.cursorRow);
                }
                this.jump(JUMP_LASTEND);
            }
        }
        this.setCursorPosition(this.cursorCol, this.cursorRow);

    }

    private void delete() {
        if (this.lines.isEmpty()) {
            return;
        }
        if (this.lines.get(this.cursorRow) == null) {
            return;
        }

    }

    private void repositionAfterJump() {
        if (this.lines.get(this.cursorRow) != null) {
            if (this.cursorCol >= this.lines.get(this.cursorRow).length()) {
                this.cursorCol = this.lines.get(this.cursorRow).length();
            }
        }
    }

    private void setCursorPosition(int col, int row) {
        try {
            if (row >= this.lines.size()) {
                row = this.lines.size() - 1;
            } else if (row < 0) {
                row = 0;
            }

            final Line line = this.lines.get(row);
            if (col >= line.length()) {
                col = line.length();
            }
            this.cursorRow = row;
            this.cursorCol = col;
            this.emitCursorChangeEvent();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private Vector2 mouseToPosition(final float x, final float y) {
        try {
            // System.out.printf("(%f , %f)\n", x, y);
            int row = (int) ((this.getHeight() - y) / this.style.font.getLineHeight());
            if (row >= this.lines.size() && row != 0) {
                row = this.lines.size() - 1;
            } else if (row <= 0) {
                row = 0;
            }
            final Line line = this.lines.get(row);
            int col = 0;
            for (; col < line.length(); col++) {
                if (this.style.font.getBounds(line.getText(), 0, col + 1).width
                        + this.getWidth()
                        * 0.05f >= x) {
                    break;
                }
            }
            // System.out.printf("     (%d , %d)\n", col, row);
            return new Vector2(col, row);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return new Vector2(0, 0);
    }

    public void parseLines() {
        for (final Line line : this.lines) {
            line.parse();
        }
    }

    public String getText() {
        String str = "";
        for (final Line line : this.lines) {
            str += line.toString();
        }
        return str;
    }

    public void setText(final String s) {
        final Line line = this.lines.get(this.cursorRow);
        for (int i = 0; i < s.length(); i++) {
            this.insert(this.cursorCol, line.getBuffer(), String.valueOf(s.charAt(i)));
            this.parseLines();
        }
    }

    public void setMode(final JSCodeEditorMode mode) {
        this.mode = mode;
        this.emitCursorChangeEvent();
    }

    public class JSCodeEditorListener extends ClickListener {

        private boolean leftPressed = false;

        public JSCodeEditorListener() {
            super();
        }

        @Override
        public void enter(final InputEvent event, final float x, final float y,
                final int pointer, final Actor fromActor) {
            // TODO Auto-generated method stub
            super.enter(event, x, y, pointer, fromActor);
            this.leftPressed = true;
            // System.out.println("enter : " + this.leftPressed);

        }

        @Override
        public void exit(final InputEvent event, final float x, final float y,
                final int pointer, final Actor toActor) {
            // TODO Auto-generated method stub
            super.exit(event, x, y, pointer, toActor);
            this.leftPressed = false;
            // System.out.println("exit : " + this.leftPressed);
        }

        @Override
        public void touchDragged(final InputEvent event, final float x, final float y,
                final int pointer) {
            super.touchDragged(event, x, y, pointer);
            System.out.printf("touch drag(%f , %f)\n", x, y);
            final Vector2 pos = JSCodeEditorCore.this.mouseToPosition(x, y);
            JSCodeEditorCore.this.setCursorPosition((int) pos.x, (int) pos.y);
        }

        @Override
        public boolean touchDown(final InputEvent event, final float x, final float y,
                final int pointer, final int button) {
            JSCodeEditorCore.this.getStage().setKeyboardFocus(JSCodeEditorCore.this);
            // System.out.printf("touch down(%f , %f)\n", x, y);
            switch (button) {
            case Input.Buttons.LEFT:
                final Vector2 pos = JSCodeEditorCore.this.mouseToPosition(x, y);
                JSCodeEditorCore.this.setCursorPosition((int) pos.x, (int) pos.y);
                JSCodeEditorCore.this.pivotRow = (int) pos.y;
                JSCodeEditorCore.this.pivotCol = (int) pos.x;
                break;
            default:
                break;
            }
            return super.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void touchUp(final InputEvent event, final float x, final float y,
                final int pointer, final int button) {
            // System.out.printf("touch up(%f , %f)\n", x, y);
            switch (button) {
            case Input.Buttons.LEFT:
                JSCodeEditorCore.this.selectRow = JSCodeEditorCore.this.cursorRow;
                JSCodeEditorCore.this.selectCol = JSCodeEditorCore.this.cursorCol;
                break;
            default:
                break;
            }
            super.touchUp(event, x, y, pointer, button);
        }

        @Override
        public boolean keyDown(final InputEvent event, final int keycode) {
            switch (keycode) {
            case Input.Keys.UP:
                JSCodeEditorCore.this.jump(JUMP_LASTEND);
                break;
            case Input.Keys.DOWN:
                JSCodeEditorCore.this.jump(JUMP_DOWN);
                break;
            case Input.Keys.LEFT:
                JSCodeEditorCore.this.moveCursorLeft();
                break;
            case Input.Keys.RIGHT:
                JSCodeEditorCore.this.moveCursorRight();
                break;
            case Input.Keys.BACKSPACE:
                JSCodeEditorCore.this.backspace();
                break;
            case Input.Keys.FORWARD_DEL:
                JSCodeEditorCore.this.delete();
                break;
            case Input.Keys.INSERT:
                JSCodeEditorCore.this.setMode(JSCodeEditorMode.INSERT);
                break;
            case Input.Keys.ESCAPE:
                JSCodeEditorCore.this.setMode(JSCodeEditorMode.VISUAL);
                break;
            default:
                break;
            }
            // System.out.printf("(%d , %d)\n", cursorCol, cursorRow);
            return super.keyDown(event, keycode);
        }

        @Override
        public boolean keyUp(final InputEvent event, final int keycode) {
            // TODO Auto-generated method stub
            return super.keyUp(event, keycode);
        }

        @Override
        public boolean keyTyped(final InputEvent event, char character) {
            if (character == '\r') {
                character = '\n';
            }
            final boolean add = JSCodeEditorCore.this.style.font.containsCharacter(character);
            final boolean newline = character == '\n';
            final boolean tab = character == '\t';

            if (add) {
                final Line line =
                        JSCodeEditorCore.this.lines.get(JSCodeEditorCore.this.cursorRow);
                JSCodeEditorCore.this.insert(JSCodeEditorCore.this.cursorCol,
                        line.getBuffer(), String.valueOf(character));
            }

            if (tab) {
                final Line line =
                        JSCodeEditorCore.this.lines.get(JSCodeEditorCore.this.cursorRow);
                for (int i = 0; i < 4; i++) {
                    JSCodeEditorCore.this.insert(JSCodeEditorCore.this.cursorCol,
                            line.getBuffer(), " ");
                }
            }

            if (newline) {
                JSCodeEditorCore.this.newline(JSCodeEditorCore.this.cursorCol);
                JSCodeEditorCore.this.jump(JUMP_DOWN);
            }
            JSCodeEditorCore.this.parseLines();
            return super.keyTyped(event, character);
        }

    }

    @Override
    public float getPrefWidth() {
        float maxLen = this.getWidth();
        for (final Line line : this.lines) {
            final float lineLen = this.style.font.getBounds(line.toString()).width
                    + this.style.font.getSpaceWidth() * 10;
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
        final float realHeight = this.lines.size() * this.style.font.getLineHeight();
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

        public JSCodeEditorStyle(final BitmapFont font, final Color fontColor,
                final Drawable cursor,
                final Drawable selection, final Drawable background) {
            this.background = background;
            this.cursor = cursor;
            this.font = font;
            this.fontColor = fontColor;
            this.selection = selection;
        }

        public JSCodeEditorStyle(final JSCodeEditorStyle style) {
            this.messageFont = style.messageFont;
            if (style.messageFontColor != null) {
                this.messageFontColor = new Color(style.messageFontColor);
            }
            this.background = style.background;
            this.focusedBackground = style.focusedBackground;
            this.disabledBackground = style.disabledBackground;
            this.cursor = style.cursor;
            this.font = style.font;
            if (style.fontColor != null) {
                this.fontColor = new Color(style.fontColor);
            }
            if (style.focusedFontColor != null) {
                this.focusedFontColor = new Color(style.focusedFontColor);
            }
            if (style.disabledFontColor != null) {
                this.disabledFontColor = new Color(style.disabledFontColor);
            }
            this.selection = style.selection;
        }
    }
}
