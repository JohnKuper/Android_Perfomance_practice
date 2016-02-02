package com.epam.dmitriy_korobeinikov.memoryleakspractice.spannable_leak;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.epam.dmitriy_korobeinikov.memoryleakspractice.R;

/**
 * Created by Dmitriy_Korobeinikov on 1/28/2016.
 */
public class SpannableActivity extends AppCompatActivity implements OnSpanClickListener {

    private FragmentManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spannable_leak);

        mManager = getSupportFragmentManager();
        if (mManager.findFragmentById(R.id.container) == null) {
            mManager.beginTransaction().add(R.id.container, SpannableLeakFragment.newInstance()).commit();
        }
    }

    @Override
    public void onSpanClick() {
        mManager.beginTransaction().replace(R.id.container, WebInfoFragment.newInstance()).addToBackStack("").commit();
    }
}
