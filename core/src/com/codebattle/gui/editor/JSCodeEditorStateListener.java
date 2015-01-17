package com.codebattle.gui.editor;

public interface JSCodeEditorStateListener {
    public void onCursorExceedHeight(int lines);

    public void onCursorExceedWidth(float width);

    public void onCursorChange(JSCodeEditorMode mode, int col, int row);

}
