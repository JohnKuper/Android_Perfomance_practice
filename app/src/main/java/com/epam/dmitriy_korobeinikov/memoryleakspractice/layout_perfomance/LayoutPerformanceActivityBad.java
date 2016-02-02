package com.epam.dmitriy_korobeinikov.memoryleakspractice.layout_perfomance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.epam.dmitriy_korobeinikov.memoryleakspractice.R;

/**
 * Created by Dmitriy_Korobeinikov on 2/1/2016.
 */
public class LayoutPerformanceActivityBad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_bad);

        findViewById(R.id.open_optimal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LayoutPerformanceActivityBad.this, LayoutPerformanceActivityGood.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
