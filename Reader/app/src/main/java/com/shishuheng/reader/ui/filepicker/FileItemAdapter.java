package com.shishuheng.reader.ui.filepicker;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shishuheng.reader.R;
import com.shishuheng.reader.process.Utilities;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by shishuheng on 2018/1/23.
 */

public class FileItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<File> list;
    public FileItemAdapter(Context context, ArrayList<File> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.fileitemadapter_layout, null);
        File file = list.get(position);
        ImageView imageView = itemView.findViewById(R.id.file_icon);
        TextView filename = itemView.findViewById(R.id.file_name);
        TextView filesize = itemView.findViewById(R.id.file_size);
        String name = file.getName();
        String extension_name = "";
        if (name.lastIndexOf('.') > -1)
            extension_name = name.substring(name.lastIndexOf('.'));
        if (file.isDirectory()) {
            imageView.setImageResource(R.drawable.fileicon_folder);
        } else if (extension_name.equalsIgnoreCase(".txt")) {
            imageView.setImageResource(R.drawable.fileicon_txt);
        } else if (extension_name.equalsIgnoreCase(".doc") || extension_name.equalsIgnoreCase(".docx")) {
            imageView.setImageResource(R.drawable.fileicon_word);
        } else if (extension_name.equalsIgnoreCase(".xls") || extension_name.equalsIgnoreCase(".xlsx")) {
            imageView.setImageResource(R.drawable.fileicon_excel);
        } else if (extension_name.equalsIgnoreCase(".ppt") || extension_name.equalsIgnoreCase(".pptx")) {
            imageView.setImageResource(R.drawable.fileicon_ppt);
        } else if (extension_name.equalsIgnoreCase(".pdf")) {
            imageView.setImageResource(R.drawable.fileicon_pdf);
        } else if (extension_name.equalsIgnoreCase(".apk")) {
            imageView.setImageResource(R.drawable.fileicon_apk);
        } else if (extension_name.equalsIgnoreCase(".jpg") || extension_name.equalsIgnoreCase(".jpeg") || extension_name.equalsIgnoreCase(".gif") || extension_name.equalsIgnoreCase(".png") || extension_name.equalsIgnoreCase(".psd") || extension_name.equalsIgnoreCase(".bmp")) {
            imageView.setImageResource(R.drawable.fileicon_image);
        } else if (extension_name.equalsIgnoreCase(".mid") || extension_name.equalsIgnoreCase(".ogg") || extension_name.equalsIgnoreCase(".wma") || extension_name.equalsIgnoreCase(".ape") || extension_name.equalsIgnoreCase(".flac") || extension_name.equalsIgnoreCase(".aac") || extension_name.equalsIgnoreCase(".mp3")) {
            imageView.setImageResource(R.drawable.fileicon_audio);
        } else if (extension_name.equalsIgnoreCase(".mov") || extension_name.equalsIgnoreCase(".avi") || extension_name.equalsIgnoreCase(".swf") || extension_name.equalsIgnoreCase(".flv") || extension_name.equalsIgnoreCase(".3gp") || extension_name.equalsIgnoreCase(".mkv") || extension_name.equalsIgnoreCase(".mp4") || extension_name.equalsIgnoreCase(".rm") || extension_name.equalsIgnoreCase(".rmvb")) {
            imageView.setImageResource(R.drawable.fileicon_video);
        } else {
            imageView.setImageResource(R.drawable.fileicon_other);
        }
        filename.setText(name);
        if (file.isFile())
            filesize.setText(Utilities.getFileSize(file.length()));
        return itemView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
