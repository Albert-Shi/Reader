package com.shishuheng.reader.ui.coustomize;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by shishuheng on 2018/1/17.
 */

public class ReadView extends View {
    private String mText;
    private Paint mPaint;
    //字体大小
    private int mTextSize = 45;
    //字体颜色
    private int mTextColor = Color.BLACK;
    //字体x轴方向缩放
    private float mScaleX = 1;
    //字间距
    private float mLetterSpacing = 0.1f;
    //行间距
    private float mLineSpacing = 16;
    //四周Padding
    private int mPaddingTop = 0;
    private int mPaddingBottom = 0;
    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;
    //相同字号下 英文字母与汉字的比例
//    private float mRate = 0.8f;
    //屏幕大小
    private int mContentWidth = 0;
    private int mContentHeight = 0;
    //用来装屏幕上每一行的文本内容 最后一个元素是所有文本所占的byte数(使用前需要将其转换为整型)
    private ArrayList<String> textLines = null;
    //默认的canvas
    private Canvas mCanvas = null;

    public ReadView(Context context) {
        super(context);
        mPaint = new Paint();
        setPaint();
    }

    public ReadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        setPaint();
    }

    public ReadView(Context context, AttributeSet attrs, int defStyleAttrs) {
        super(context, attrs, defStyleAttrs);
        mPaint = new Paint();
        setPaint();
    }

    public void setText(ArrayList text) {
//        this.mText = text;
        this.textLines = text;
        if (textLines != null && textLines.size() > 1) {
//            draw(mCanvas);
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mCanvas = canvas;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
//            clearCanvas(canvas);
//        int availableHeight = mContentHeight-mPaddingTop-mPaddingBottom;
//            canvas.drawColor(Color.rgb(240, 235, 213));
            int availableWidth = mContentWidth-mPaddingLeft-mPaddingRight;
            int Y = 60;
            if (textLines != null && textLines.size() != 0 && mPaint != null && mContentHeight > 0 && mContentWidth > 0) {
                for (int i = 0; i < textLines.size()-1; i++) {
                    mPaint.setTextScaleX(mScaleX);
                    mPaint.setLetterSpacing(mLetterSpacing);
                    String line = textLines.get(i);
                    //匹配英文字母数 若英文字母大于n个 则 将letterSpacing设置为0
                    String regex = ".*(\\p{Alnum}|\\p{Punct}|\\s){10,}.*";
                    if (line.matches(regex))
                        mPaint.setLetterSpacing(0.0f);
                    float textLength = mPaint.measureText(line);
                    if (Math.abs(availableWidth - textLength) <= 4*mTextSize ) {
                        float scale = availableWidth / textLength;
                        mPaint.setTextScaleX(scale);
                    }
                    canvas.drawText(line, getPaddingLeft(), Y, mPaint);
                    Y += (mTextSize+mLineSpacing);
                }
            } else {
                canvas.drawText("请检查是否设置了控件的宽与高", 0, 50, mPaint);
            }
        } else {
            Log.v("错误", "canvas 为 null");
        }
    }

    /*
    ArrayList<String> suitableText(String str, int hanziByte) {
        ArrayList<String> arrayList = new ArrayList<>();
        String text = str;
        int lineNum = (int)((mContentHeight-mPaddingTop-mPaddingBottom)/(mTextSize+mLineSpacing));
        int lineCharNum = (int)((mContentWidth-mPaddingLeft-mPaddingRight)/(mTextSize+mLetterSpacing));
        int count = 0;
        for (int i = 0; i < lineNum; i++) {
            if (text.length() == 0 || text == null || text.equals(""))
                break;
            int j = 0;
            float linCharCount = 0;
            int lineByteCount = 0;
            while (linCharCount < lineCharNum && j < lineCharNum && j < text.length()) {
                if (text.charAt(j) >= 40 && text.charAt(j) <= 176) {
                    linCharCount += mRate;
                    lineByteCount++;
                } else if (text.charAt(j) == '\n' || text.charAt(j) == '\r' || text.charAt(j) == '\t' || text.charAt(j) == '\b' || text.charAt(j) == '\f') {
                    lineByteCount++;
                } else {
                    lineByteCount += hanziByte;
                    linCharCount++;
                }
                j++;
            }
            String line = text.substring(0, j);
            arrayList.add(line);
            text = text.substring(j);
            count += lineByteCount;
        }
        //arrayList的最后一个元素存储总共读取的byte数
        arrayList.add(count+"");
        textLines = arrayList;
        return arrayList;
    }
    */

    void setPaint() {
        mPaint.setTextScaleX(mScaleX);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        mPaint.setLetterSpacing(mLetterSpacing);
        mPaint.setAntiAlias(true);
        if (this.mCanvas != null) {
            draw(mCanvas);
        }
    }

    void clearCanvas(Canvas canvas) {
        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        canvas.drawColor(Color.WHITE);
        canvas.drawPaint(p);
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setmScaleX(float scaleX) {
        this.mScaleX = scaleX;
    }

    public float getmScaleX() {
        return mScaleX;
    }

    public void setmPaddingTop(int paddingTop) {
        this.mPaddingTop = paddingTop;
    }

    public int getmPaddingTop() {
        return mPaddingTop;
    }

    public void setmPaddingBottom(int paddingBottom) {
        this.mPaddingBottom = paddingBottom;
    }

    public int getmPaddingBottom() {
        return mPaddingBottom;
    }

    public void setmPaddingLeft(int paddingLeft) {
        this.mPaddingLeft = paddingLeft;
    }

    public int getmPaddingLeft() {
        return mPaddingLeft;
    }

    public void setmPaddingRight(int paddingRight) {
        this.mPaddingRight = paddingRight;
    }

    public int getmPaddingRight() {
        return mPaddingRight;
    }

    public void setLetterSpacing(float letterSpacing) {
        this.mLetterSpacing = letterSpacing;
    }

    public float getmLetterSpacing() {
        return mLetterSpacing;
    }

    public void setLineSpacing(float lineSpacing) {
        this.mLineSpacing = lineSpacing;
    }

    public float getmLineSpacing() {
        return mLineSpacing;
    }

    public void setContentWidth(int contentWidth) {
        this.mContentWidth = contentWidth;
    }

    public int getContentWidth() {
        return mContentWidth;
    }

    public void setContentHeight(int contentHeight) {
        this.mContentHeight = contentHeight;
    }

    public int getContentHeight() {
        return mContentHeight;
    }

    public void setTextLines(ArrayList<String> textLines) {
        this.textLines = textLines;
    }
}
