//此处参考了 https://www.jianshu.com/p/4fc6164e4709
//以及 https://www.cnblogs.com/bugly/p/6264751.html
//RecyclerView动画参考 http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2017/0807/8348.html

package com.shishuheng.reader.ui;

import android.app.Activity;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shishuheng on 2018/1/14.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<TxtDetail> mData;
    private MainActivity mainActivity;
    public RecyclerViewAdapter(Activity activity, ArrayList<TxtDetail> list) {
        mainActivity = (MainActivity) activity;
        mData = list;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mainActivity.currentTxt = mData.get(position);
        holder.name.setText(mData.get(position).getName());
        //设置作者
        //设置图片
        holder.cover.setImageResource(R.mipmap.ic_launcher);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, FullscreenActivity.class);
                intent.putExtra("currentTextDetail", mainActivity.currentTxt);
                intent.putExtra("TextSize", mainActivity.TextSize);
                mainActivity.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                View list = LayoutInflater.from(mainActivity).inflate(R.layout.menu_display, null);
                TextView title = (TextView) list.findViewById(R.id.menu_title_text);
                title.setText(mainActivity.currentTxt.getName());
                Utilities.reloadMenuItem(mainActivity, list);

                builder.setView(list);
                builder.create().show();
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
}
