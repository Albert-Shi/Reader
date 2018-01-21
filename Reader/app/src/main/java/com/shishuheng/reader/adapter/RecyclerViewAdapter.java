//此处参考了 https://www.jianshu.com/p/4fc6164e4709
//以及 https://www.cnblogs.com/bugly/p/6264751.html
//RecyclerView动画参考 http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2017/0807/8348.html

package com.shishuheng.reader.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shishuheng.reader.R;
import com.shishuheng.reader.datastructure.TxtDetail;
import com.shishuheng.reader.process.Utilities;
import com.shishuheng.reader.ui.activities.MainActivity;
import com.shishuheng.reader.ui.activities.FullscreenActivity;

import java.util.ArrayList;

/**
 * Created by shishuheng on 2018/1/14.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<TxtDetail> mData;
    private MainActivity mainActivity;
    private RecyclerViewAdapter recyclerViewAdapter = this;
    public RecyclerViewAdapter(Activity activity, ArrayList<TxtDetail> list) {
        mainActivity = (MainActivity) activity;
        mData = list;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        mainActivity.currentTxt = mData.get(position);
        holder.name.setText(mData.get(position).getName());
        //设置作者
        //设置图片
        //此处读取Bitmap需要拷贝使用副本 因为不能直接修改资源图片 具体参看 http://blog.csdn.net/j_bing/article/details/45936929
///        Bitmap bitmap = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.book_open).copy(Bitmap.Config.ARGB_8888, true);
        Bitmap bitmap = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.book_cover).copy(Bitmap.Config.ARGB_8888, true);
        //*
        Paint p = new Paint();
        Canvas canvas = new Canvas(bitmap);
//        canvas.drawColor(Color.RED);
///        p.setTextSize(50);
        p.setTextSize(30);
        p.setAntiAlias(true);
//        p.setColor(Color.rgb(236, 106, 92));
        p.setColor(Color.rgb(41, 36, 33));
        p.setTypeface(Typeface.DEFAULT_BOLD);
        String text = mData.get(position).getName();
        int Y = 70;
        if (text.length() > 6)
            text = text.substring(0, 6) + "……";
        for (int i = 0; i < 7; i++) {
            if (i >= text.length())
                break;
            else {
///                canvas.drawText(text.substring(i,i+1), 150, Y, p);
                canvas.drawText(text.substring(i,i+1), 60, Y, p);
                Y += 35;
            }
        }
        //*/
        holder.cover.setImageBitmap(bitmap);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, FullscreenActivity.class);
                intent.putExtra("currentTextDetail", mData.get(position));
                intent.putExtra("TextSize", mData.get(position));
                mainActivity.currentTxt = mData.get(position);
                mainActivity.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                View list = LayoutInflater.from(mainActivity).inflate(R.layout.menu_display, null);
                TextView title = (TextView) list.findViewById(R.id.menu_title_text);
                title.setText(mData.get(position).getName());
                builder.setView(list);
                AlertDialog dialog = builder.create();
                Utilities.reloadMenuItem(mainActivity, list, dialog, recyclerViewAdapter, position);
                mainActivity.currentTxt = mData.get(position);
                dialog.show();
                return false;
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_display, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView author;
        public final ImageView cover;
        public ViewHolder(View view) {
            super(view);
            name = (TextView)view.findViewById(R.id.name_display);
            author = (TextView)view.findViewById(R.id.author_display);
            cover = (ImageView)view.findViewById(R.id.cover_display_list);
        }
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }
}
