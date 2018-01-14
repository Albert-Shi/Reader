package com.shishuheng.reader.ui;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shishuheng.reader.R;
import com.shishuheng.reader.datastructure.ScreenSize;
import com.shishuheng.reader.datastructure.TxtDetail;
import com.shishuheng.reader.process.Book;
import com.shishuheng.reader.process.Utilities;

import java.io.File;
import java.util.ArrayList;

//用于显示具体文字的页面
public class TextFragment extends Fragment {
    private Activity rootActivity = null;
    private TextView mainDisplay = null;
//    private EditText mainDisplay = null;

    private LinearLayout textMenu;
    private GestureDetector gestureDetector;

    public LinearLayout getTextMenu() {
        return textMenu;
    }

    public TextView getMainDisplay() {
        return mainDisplay;
    }

    private int displayLineNumber = 0;

    private ScreenSize screenSize;

    String text = "";

    private Book currenBook;

    long needToadd = 0;

    //获取页面设置参数
    LinearLayout ll;
    float textSize;
    float lineHeight;
    float letterSpacing;
    float paddingTop;
    float paddingBottom;
    float paddingLeft;
    float paddingRight;

    TxtDetail txt;
    int lineCharacterNumber;
    int lineTotalNumber;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TextFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TextFragment newInstance(String param1, String param2) {
        TextFragment fragment = new TextFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        rootActivity = getActivity();
//        rootActivity.currentFragment =this;
        mainDisplay =  view.findViewById(R.id.text_container);

        //获取页面设置参数
        ll = (LinearLayout)view.findViewById(R.id.ceshi);
        textSize = mainDisplay.getPaint().getTextSize();
        lineHeight = mainDisplay.getLineHeight();
        letterSpacing = mainDisplay.getLetterSpacing();
        paddingTop = ll.getPaddingTop();
        paddingBottom = ll.getPaddingBottom();
        paddingLeft = ll.getPaddingLeft();
        paddingRight = ll.getPaddingRight();

        screenSize = Utilities.getScreenSize(getActivity(), textSize, lineHeight, paddingTop, letterSpacing);

        txt = ((FullscreenActivity)rootActivity).getTxtDetail();
        lineCharacterNumber = screenSize.getLineCharacterNumber();
        lineTotalNumber = screenSize.getLineTotalNumber();


        Button next_button = (Button) getActivity().findViewById(R.id.nextPage_button);
        Button last_button = (Button) getActivity().findViewById(R.id.lastPage_button);
//        byte[] text = Utilities.readRandomFile(rootActivity, ((FullscreenActivity)rootActivity).getTxtDetail().getPath(), 0, 1024);
//        int fullTextLength = Utilities.getFullScreenCharactorNumbers(Utilities.getScreenSize(getActivity()), textSize, lineHeight, paddingRight, letterSpacing);
//        String utf8text = null;
        /*
        try {
            utf8text = new String(text, "GBK");
            utf8text = utf8text.substring(0, fullTextLength);
        } catch (Exception e) {
            utf8text = "不支持utf8的编码文件";
            e.printStackTrace();
        }
        */
        final Book book = new Book(new File(txt.getPath()), lineTotalNumber, lineCharacterNumber, txt.getHasReadPointer());
        currenBook = book;

        /*
//        book.run();
        book.openBookFile(lineTotalNumber);

//        txt.setHasReadPointer(book.getReadPointer());

        /*
        //统计自带换行符
        int brCount = 0;
        int count = 0;
        String temp = "";
        if (book.getBook().size() != 0) {
            for (int i = displayLineNumber; i < lineTotalNumber + displayLineNumber; i++) {
                temp = book.getBook().get(i);
                if (temp.indexOf('\n') > -1) {
                    count += (temp.length()*2)-1;
                    text += temp;
                    brCount++;
                } else {
                    text += temp + "\n";
                    count += (temp.length() * 2);
                }
            }
        }
        displayLineNumber += lineTotalNumber;
        */

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage(book);
            }
        });
        last_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastPage(book);
            }
        });

//        needToadd = nextPage(book);
//!!        txt.setHasReadPointer(book.getReadPointer());
        /*
        int cslength = 0;
        int countnumber = 0;
        int index = 0;
        int abr = 0;
        while ((index = book.getBookAllLine().get(countnumber).indexOf("第二卷")) == -1 && book.getBookAllLine().size() >= countnumber) {
            cslength += book.getBookAllLine().get(countnumber).length();
            if (book.getBookAllLine().get(countnumber).indexOf('\n') != -1) {
                abr++;
                cslength--;
            }
            countnumber++;
        }
        cslength = (cslength*2)+abr;
        */

//        needToadd = 2*(text.length()-lineTotalNumber)+brCount;
        mainDisplay.setText("正在打开书籍，请等待...");
        long record = txt.getHasReadPointer();
        text = book.readByte(txt.getHasReadPointer());
        txt.setHasReadPointer(record);
        mainDisplay.setText(text);
//        mainDisplay.setFocusable(true);
//        mainDisplay.setClickable(true);
//        mainDisplay.setLongClickable(true);
        mainDisplay.setKeyListener(null);
//        mainDisplay.setMovementMethod(ScrollingMovementMethod.getInstance());
        GestureDetector.SimpleOnGestureListener sogl = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float interval_X = e2.getX() - e1.getX();
                float interval_Y = e2.getY() - e1.getY();
                float sensitivity = 100;
                float minVelocityX = 20;
                float minVelocityY = 20;
                if (Math.abs(interval_X) > sensitivity && Math.abs(interval_X) > Math.abs(interval_Y*1.5) && velocityX != 0 && Math.abs(interval_X) > Math.abs(interval_Y)/2) {
                    if (interval_X < 0) {
                        nextPage(book);
                    } else if (interval_X > 0) {
                        lastPage(book);
                    }
                }
                Log.v("手势：","X:"+interval_X+" Y:"+interval_Y);
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (e.getRawX() >= screenSize.getWidth()/4 && e.getRawX() <= (screenSize.getWidth()/4*3) && e.getRawY() >= screenSize.getHeight()/4 && e.getRawY() <= (screenSize.getHeight()/4*3)) {
                    ((FullscreenActivity) rootActivity).toggle();
                } else if (e.getRawX() > (screenSize.getWidth()/2)) {
                    nextPage(book);
                } else if (e.getRawX() < (screenSize.getWidth()/2)) {
                    lastPage(book);
                }
                return super.onSingleTapConfirmed(e);
            }
        };

        gestureDetector = new GestureDetector(getActivity(), sogl);

        mainDisplay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v("动作：","X:"+event.getX()+" Y:"+event.getY());
                return gestureDetector.onTouchEvent(event);
            }
        });

        mainDisplay.setTextIsSelectable(true);
        return view;
    }

    long nextPage(Book book) {
        book.setReadPointer(txt.getHasReadPointer());
        book.nextPage();
        txt.setHasReadPointer(book.getReadPointer());
        text = book.nextPage();
        book.setReadPointer(txt.getHasReadPointer());
        mainDisplay.setText(text);
        return 0;
    }

    long lastPage(Book book) {
        if (book.getReadPointer() > 0) {
            text = book.lastPage();
            mainDisplay.setText(text);
            txt.setHasReadPointer(book.getReadPointer());
        } else {
            txt.setHasReadPointer(0);
            book.setReadPointer(0);
        }
        return 0;
    }

    public Book getCurrenBook() {
        return currenBook;
    }
}