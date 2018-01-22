package com.shishuheng.reader.ui.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shishuheng.reader.R;
import com.shishuheng.reader.datastructure.ActivitySerializable;
import com.shishuheng.reader.datastructure.TxtDetail;
import com.shishuheng.reader.process.BookInformationDatabaseOpenHelper;
import com.shishuheng.reader.process.Utilities;
import com.shishuheng.reader.ui.coustomize.ReadView;
import com.shishuheng.reader.ui.fragment.TextFragment;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    private MainActivity mainActivity;
    private TxtDetail currentTxt;
    private int position;
    private int screenTextSize;
    private ActivitySerializable activitySerializable;
    private TextFragment fragment;

    private int textSize_Settings = 3;

    //电池信息接收器
    private BroadcastReceiver batteryReceiver;
    //电量百分比
    private String batteryPercent = "正在获取数据";

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        Transition animation = TransitionInflater.from(getApplicationContext()).inflateTransition(R.transition.slide);
        getWindow().setEnterTransition(animation);
        //全屏显示Activity（不显示状态栏）
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.argb(0,0,0,0));

        //屏幕常亮 参考 http://blog.csdn.net/a57565587/article/details/51669520
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        currentTxt = (TxtDetail) getIntent().getSerializableExtra("currentTextDetail");
//        screenTextSize = getIntent().getIntExtra("TextSize", 512);



        // Set up the user interaction to manually show or hide the system UI.
        //用不到 注释掉
        /*
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        */

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
//        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        //获取书籍和设置信息
        try {
            BookInformationDatabaseOpenHelper helper = new BookInformationDatabaseOpenHelper(this, Utilities.DATABASE_NAME, null, Utilities.DATABASE_VERSION);
            SQLiteDatabase db = helper.getWritableDatabase();
            //书籍信息查询
            String q1 = "select readPointer from Books where path=?";
            String q2 = "select codingFormat from Books where path=?";
            String q3 = "select totality from Books where path=?";
            //设置信息查询
            String q4 = "select textSize from Settings where id=?";
            Cursor cursor = db.rawQuery(q1, new String[]{currentTxt.getPath()});
            int position;
            if (cursor.moveToFirst()) {
                position = cursor.getInt(cursor.getColumnIndex("readPointer"));
                currentTxt.setHasReadPointer(position);
            }
            cursor = db.rawQuery(q2, new String[]{currentTxt.getPath()});
            if (cursor.moveToFirst()) {
                position = cursor.getInt(cursor.getColumnIndex("codingFormat"));
                currentTxt.setCodingFormat(position);
            }
            cursor = db.rawQuery(q3, new String[]{currentTxt.getPath()});
            if (cursor.moveToFirst()) {
                position = cursor.getInt(cursor.getColumnIndex("totality"));
                currentTxt.setTotality(position);
            }
            cursor = db.rawQuery(q4, new String[]{"1"});
            if (cursor.moveToFirst()) {
                position = cursor.getInt(cursor.getColumnIndex("textSize"));
                textSize_Settings = position;
            }

            db.close();
        } catch (Exception e) {
            Toast.makeText(this, "读取数据库出错，可尝试清除应用数据，或将应用卸载后重新安装", Toast.LENGTH_SHORT).show();
        }

        //创建电池信息接收器 此处参考 http://www.jb51.net/article/72799.htm
        batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int current=intent.getExtras().getInt("level");//获得当前电量
                int total=intent.getExtras().getInt("scale");//获得总电量
                int percent=current*100/total;
                batteryPercent = percent+"%";
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);

        //设置文本显示
        setTextContent();

        //设置ActionBar自定义背景
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(currentTxt.getName());
        actionBar.setBackgroundDrawable(getDrawable(R.drawable.toolbar_bg_fullscreen));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    public void toggle() {
        if (mVisible) {
            hide();
  //          fragment.getMainDisplay().setMovementMethod(null);
        } else {
            show();
//            fragment.getMainDisplay().setMovementMethod(ScrollingMovementMethod.getInstance());
            mControlsView.bringToFront();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setTxtDetail(TxtDetail txtDetail) {
        this.currentTxt = txtDetail;
    }

    public TxtDetail getTxtDetail() {
        return currentTxt;
    }

    public void startFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.fullscreen_content, fragment).commit();
    }

    public void setTextContent() {
        fragment = new TextFragment();
        startFragment(fragment);
    }

    /*
    private void setText() {
        TextView mainDisplay = (TextView) findViewById(R.id.fullscreen_content);
        byte[] text = Utilities.readRandomFile(this, this.getTxtDetail().getPath(), 0, Utilities.getFinalTextSize(this, 2, 16));
        String utf8text = null;
        try {
            utf8text = new String(text, "GBK");
        } catch (Exception e) {
            utf8text = "不支持utf8的编码文件";
            e.printStackTrace();
        }
        mainDisplay.setText(utf8text);
        mainDisplay.setTextSize(40);
        mainDisplay.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
    */

    public TextFragment getFragment() {
        return fragment;
    }

    @Override
    public void onBackPressed() {
        if (mVisible) {
            toggle();
        } else {
            //保存书籍信息到数据库
            if (currentTxt.getHasReadPointer() < 0)
                currentTxt.setHasReadPointer(0);
            BookInformationDatabaseOpenHelper helper = new BookInformationDatabaseOpenHelper(this, Utilities.DATABASE_NAME, null, Utilities.DATABASE_VERSION);
            SQLiteDatabase db = helper.getReadableDatabase();

            String q = "select readPointer from Books where path=?";
            ContentValues values = new ContentValues();
            values.put("readPointer", currentTxt.getHasReadPointer());
            values.put("codingFormat", currentTxt.getCodingFormat());
            db.update("Books", values, "path=?", new String[]{currentTxt.getPath()});
            db.close();

            //注销电池信息接收器
            unregisterReceiver(batteryReceiver);

            super.onBackPressed();
        }
    }

    public int getTextSize_Settings() {
        return textSize_Settings;
    }

    public void setTextSize_Settings(int textSize_Settings) {
        this.textSize_Settings = textSize_Settings;
    }

    public String getBatteryPercent() {
        return batteryPercent;
    }
}
