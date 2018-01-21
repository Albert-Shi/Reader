package com.shishuheng.reader.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.shishuheng.reader.R;
import com.shishuheng.reader.ui.activities.MainActivity;

import static com.shishuheng.reader.process.Utilities.reloadHomeListView;

public class HomeFragment extends Fragment {
    MainActivity rootActivity = null;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_home, container, false);
        rootActivity = (MainActivity) getActivity();
        rootActivity.currentFragment = this;
        reloadHomeListView(rootActivity, view); //载入书籍listView
        return view;
    }
}