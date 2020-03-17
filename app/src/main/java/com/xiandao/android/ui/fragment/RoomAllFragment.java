package com.xiandao.android.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiandao.android.R;

/**
 * Created by Loong on 2020/2/12.
 * Version: 1.0
 * Describe: 首页
 */
public class RoomAllFragment extends Fragment {
    public RoomAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_all, container, false);
    }


}
