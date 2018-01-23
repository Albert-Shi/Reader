package com.shishuheng.reader.ui.filepicker;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.shishuheng.reader.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by shishuheng on 2018/1/23.
 */

public class FilePicker extends AlertDialog.Builder {
    View view;
    Context context;
    File directory = null;
    ArrayList<File> list;
    ListView filesList;
    TextView returnToUp;
    AlertDialog dialog;
    public FilePicker(Context context, String path) {
        super(context);
        this.context = context;
        directory = new File(path);
        list = new ArrayList<>();
        view = LayoutInflater.from(context).inflate(R.layout.dialog_filepicker, null);
        returnToUp = view.findViewById(R.id.back_FilePicker);
        filesList = view.findViewById(R.id.listView_FilePicker);
        setView(view);
        fillList();
    }

    void fillList() {
        if (directory.isDirectory() && directory != null) {
            list.clear();
            setTitle(directory.getName());
            if (dialog != null)
                dialog.setTitle(directory.getName());
            File[] files = directory.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    list.add(file);
                }
            }
            //按字典序排序
            Collections.sort(list);

            FileItemAdapter adapter = new FileItemAdapter(context, list);
            filesList.setAdapter(adapter);
            filesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    File file = list.get(position);
                    if (file.isDirectory()) {
                        directory = file;
                        fillList();
                    } else {
                        Log.v("文件选定", file.getAbsolutePath());
                    }
                }
            });

            returnToUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!directory.getAbsolutePath().equals("\\")) {
                        File parent = directory.getParentFile();
                        if (parent != null && parent.exists()) {
                            directory = directory.getParentFile();
                            fillList();
                        }
                    }
                }
            });
        }
    }

    @Override
    public AlertDialog create() {
        dialog = super.create();
        return dialog;
    }
}
