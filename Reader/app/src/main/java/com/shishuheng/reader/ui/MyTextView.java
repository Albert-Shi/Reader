package com.shishuheng.reader.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by shishuheng on 2018/1/5.
 */

public class MyTextView extends AppCompatTextView {
    MyTextView(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        suitableText();
    }

    public int suitableText() {
        CharSequence oldText = getText();
        CharSequence newText = oldText.subSequence(0, getCharNum());
        setText(newText);
        return oldText.length()-newText.length();
    }

    public int getCharNum() {
        return getLayout().getLineEnd(getLineNum());
    }

    public int getLineNum() {
        Layout layout = getLayout();
        int h = getHeight() - getPaddingTop() - getPaddingBottom() - getLineHeight();
        return layout.getLineForVertical(h);
    }
}