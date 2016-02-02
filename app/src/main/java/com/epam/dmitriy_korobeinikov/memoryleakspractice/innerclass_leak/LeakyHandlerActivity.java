package com.epam.dmitriy_korobeinikov.memoryleakspractice.innerclass_leak;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.epam.dmitriy_korobeinikov.memoryleakspractice.MyApplication;
import com.epam.dmitriy_korobeinikov.memoryleakspractice.R;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Dmitriy_Korobeinikov on 1/28/2016.
 */
public class LeakyHandlerActivity extends AppCompatActivity {

    private Bitmap mBigImage;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(LeakyHandlerActivity.this, "Hello from leaky handler", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innerclass_leak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBigImage = BitmapFactory.decodeResource(getResources(), R.drawable.corvus);
        mHandler.sendMessageDelayed(new Message(), 1000 * 10);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(mBigImage);
    }
}
