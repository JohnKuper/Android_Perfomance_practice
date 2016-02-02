package com.epam.dmitriy_korobeinikov.memoryleakspractice.spannable_leak;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by Dmitriy_Korobeinikov on 1/28/2016.
 */
public class SpannableTextView extends TextView {
    private static final String TAG = SpannableTextView.class.getSimpleName();

    public SpannableTextView(Context context) {
        super(context);
    }

    public SpannableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpannableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        SavedState savedState = (SavedState) super.onSaveInstanceState();
        try {
            Field field = savedState.getClass().getDeclaredField("text");
            field.setAccessible(true);
            field.set(savedState, null);
        } catch (Exception e) {
            Log.e(TAG, "Error during onSaveInstanceState", e);
        }
        return savedState;
    }
}
