package com.shishuheng.reader.datastructure;

/**
 * Created by shishuheng on 2018/1/4.
 */

public class ScreenSize {
    private int widthDp;
    private int heightDp;
    private int width;
    private int height;
    private int lineCharacterNumber;
    private int lineTotalNumber;

    public void setHeightDp(int heightDp) {
        this.heightDp = heightDp;
    }

    public void setWidthDp(int widthDp) {
        this.widthDp = widthDp;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeightDp() {
        return heightDp;
    }

    public int getWidthDp() {
        return widthDp;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getLineCharacterNumber() {
        return lineCharacterNumber;
    }

    public int getLineTotalNumber() {
        return lineTotalNumber;
    }

    public void setLineCharacterNumber(int lineCharacterNumber) {
        this.lineCharacterNumber = lineCharacterNumber;
    }

    public void setLineTotalNumber(int lineTotalNumber) {
        this.lineTotalNumber = lineTotalNumber;
    }
}
