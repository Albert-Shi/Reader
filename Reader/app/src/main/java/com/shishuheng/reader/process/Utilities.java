package com.shishuheng.reader.process;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.shishuheng.reader.R;
import com.shishuheng.reader.datastructure.ActivitySerializable;
import com.shishuheng.reader.datastructure.ScreenSize;
import com.shishuheng.reader.datastructure.TxtDetail;
import com.shishuheng.reader.ui.DisplayAdapter;
import com.shishuheng.reader.ui.FullscreenActivity;
import com.shishuheng.reader.ui.ItemMenuAdapter;
import com.shishuheng.reader.ui.MainActivity;
import com.shishuheng.reader.ui.TextFragment;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shishuheng on 2018/1/2.
 */
//工具类 用来各种数据处理
public class Utilities {
    //数据库名称
    public static String DATABASE_NAME = "BookReader.db";
    public static int DATABASE_VERSION = 1;
    //获取 /sdcard/EBooks/ 目录下的所有txt文件
    public static ArrayList<TxtDetail> getDirectoryTXTFiles() {
        File path = new File(Environment.getExternalStorageDirectory()+"/EBooks");
        if (path.exists() && path.isDirectory()) {
            ArrayList<TxtDetail> txtDetails = new ArrayList<>();
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getAbsolutePath().endsWith(".txt")) {
                        TxtDetail txtDetail = new TxtDetail();
                        txtDetail.setName(file.getName().replace(".txt",""));
                        txtDetail.setPath(file.getAbsolutePath());
                        txtDetails.add(txtDetail);
                    }
                }
            }
            return txtDetails;
        } else
            return null;
    }

    public static void reloadHomeListView(final MainActivity activity, View view) {
        final ArrayList<TxtDetail> txts = Utilities.getDirectoryTXTFiles();
        //书籍信息写入数据库 此处参考 https://www.jianshu.com/p/0d8fa55d603b
        BookInformationDatabaseOpenHelper helper = new BookInformationDatabaseOpenHelper(activity, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int i = 0; i < txts.size(); i++) {

            //String sql = "insert into Books(path, author, title, category, image, id, readPointer) values (?, ?, ?, ?, ?, ?, ?)";

            values.clear();
            values.put("path", txts.get(i).getPath());
            values.put("author", "");
            values.put("title", txts.get(i).getName().replace(".txt", ""));
            values.put("category", "");
            values.put("image", "");
            values.put("id", i);
            values.put("readPointer", 0);
            database.insert("Books", null, values);

//            database.execSQL(sql, new Object[] {txts.get(i).getPath(), "", txts.get(i).getName().replace(".txt", ""), "", "", i, 0});
        }
        database.close();

        DisplayAdapter adapter = new DisplayAdapter(activity, txts);
        ListView display = (ListView) view.findViewById(R.id.display_listView);
        display.setAdapter(adapter);
        display.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                View list = LayoutInflater.from(activity).inflate(R.layout.menu_display, null);
                TextView title = (TextView) list.findViewById(R.id.menu_title_text);
                title.setText(txts.get(position).getName());
                reloadMenuItem(activity, list);

                builder.setView(list);
                builder.create().show();

                return false;
            }
        });

        display.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activity.currentTxt = txts.get(position);
//                ActivitySerializable as = new ActivitySerializable();
//                as.setMainActivity(activity);
                Intent intent = new Intent(activity, FullscreenActivity.class);
                intent.putExtra("currentTextDetail", activity.currentTxt);
                intent.putExtra("TextSize", activity.TextSize);
//                intent.putExtra()
//                intent.putExtra("RootActivity", as);
                activity.startActivity(intent);
            }
        });
    }

    public static void reloadMenuItem(Context context, View view) {
        ArrayList<String> items = new ArrayList<>();
        items.add("删除");
        items.add("详情");
        ItemMenuAdapter adapter = new ItemMenuAdapter(context, items);
//        View view = LayoutInflater.from(context).inflate(R.layout.menu_display, null);
        ListView menu = (ListView) view.findViewById(R.id.menu_listVew);
        menu.setAdapter(adapter);
    }

    //获取手机高度dp 和 宽度dp
    public static ScreenSize getScreenSize(Activity activity, float letterSize, float lineHeight, float padding, float letterSpacing) {
        ScreenSize size = new ScreenSize();
        WindowManager wm = activity.getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int height = (int) (dm.heightPixels/density);
        int width = (int) (dm.widthPixels/density);
        size.setHeightDp(height);
        size.setWidthDp(width);
        size.setHeight(dm.heightPixels);
        size.setWidth(dm.widthPixels);
        size.setLineTotalNumber((int)((size.getHeight()-(padding*2))/lineHeight));
        size.setLineCharacterNumber((int)((size.getWidth()-(padding*2))/(letterSize + letterSpacing)));
        return size;
    }

    //计算一个屏幕的字符数
    public static int getFullScreenCharactorNumbers(ScreenSize size, float letterSize, float lineHeight, float padding, float letterSpacing) {
        int num = (int)(((size.getHeight()-(padding*2))/lineHeight) * ((size.getWidth()-(padding*2))/(letterSize + letterSpacing)));
        return num;
    }

    //byte size
    public static int getFinalTextSize(Activity activity, int c2bSize, float letterSize, float lineHeight, float padding, float letterSpacing) {
        int size = c2bSize * getFullScreenCharactorNumbers(getScreenSize(activity, letterSize, lineHeight, padding, letterSpacing), letterSize, lineHeight, padding, letterSpacing);
        return size;
    }

    //随机读文本 参考 https://www.cnblogs.com/zuochengsi-9/p/6485737.html
    public static byte[] readRandomFile(Activity activity, String path, int pointer, int size) {
        byte[] buff = null;
        try {
            RandomAccessFile raf = new RandomAccessFile(new File(path), "r");
            raf.seek(0);
            buff = new byte[size];
            long hasRead = 0;
            for (int i = 0; i < size; i++) {
                buff[i] = raf.readByte();
            }
            ((FullscreenActivity)activity).getTxtDetail().setHasReadPointer(pointer+size);
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buff;
    }
}