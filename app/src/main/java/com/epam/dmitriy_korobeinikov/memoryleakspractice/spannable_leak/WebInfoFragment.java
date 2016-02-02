package com.epam.dmitriy_korobeinikov.memoryleakspractice.spannable_leak;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epam.dmitriy_korobeinikov.memoryleakspractice.R;

/**
 * Created by Dmitriy_Korobeinikov on 1/28/2016.
 */
public class WebInfoFragment extends Fragment {

    public static WebInfoFragment newInstance() {
        return new WebInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_info, container, false);
    }
}
