package com.epam.dmitriy_korobeinikov.memoryleakspractice.innerclass_leak;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.epam.dmitriy_korobeinikov.memoryleakspractice.R;

import java.lang.ref.WeakReference;

/**
 * Created by Dmitriy_Korobeinikov on 1/28/2016.
 */
public class GoodHandlerActivity extends AppCompatActivity {

    private Bitmap mBigImage;
    private MyHandler mHandler;

    private static class MyHandler extends Handler {
        private WeakReference<Context> mContextWeakRef;

        public MyHandler(Context context) {
            mContextWeakRef = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mContextWeakRef.get() != null) {
                Toast.makeText(mContextWeakRef.get(), "Hello from good handler", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innerclass_leak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBigImage = BitmapFactory.decodeResource(getResources(), R.drawable.corvus);

        mHandler = new MyHandler(this);
        mHandler.sendMessageDelayed(new Message(), 1000 * 5);
    }
}
