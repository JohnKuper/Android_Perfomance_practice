package com.epam.dmitriy_korobeinikov.memoryleakspractice.spannable_leak;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epam.dmitriy_korobeinikov.memoryleakspractice.MyApplication;
import com.epam.dmitriy_korobeinikov.memoryleakspractice.R;
import com.squareup.leakcanary.RefWatcher;

import java.lang.ref.WeakReference;

/**
 * Created by Dmitriy_Korobeinikov on 1/28/2016.
 */
public class SpannableLeakFragment extends Fragment {
    private static final String TAG = SpannableLeakFragment.class.getSimpleName();

    private OnSpanClickListener mOnSpanClickListener;
    private RefWatcher mRefWatcher;

    public static SpannableLeakFragment newInstance() {
        return new SpannableLeakFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnSpanClickListener = (OnSpanClickListener) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRefWatcher = MyApplication.getRefWatcher(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_spannable_leak, container, false);
        Log.d(TAG, "onCreateView() Activity: " + getActivity().hashCode());
        Log.d(TAG, "onCreateView() SpannableLeakFragment: " + SpannableLeakFragment.this.hashCode());

        SpannableTextView spanText = (SpannableTextView) rootView.findViewById(R.id.tvSpannableText);
        setBillMeLaterTermsInfoLinkableText(spanText);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnSpanClickListener = null;
    }

    private void setBillMeLaterTermsInfoLinkableText(TextView textView) {
        CharSequence sequence = Html.fromHtml(getString(R.string.est_del_date_text));
        Spannable strBuilder = new SpannableStringBuilder(sequence);
        UnderlineSpan[] underlines = strBuilder.getSpans(0, sequence.length(), UnderlineSpan.class);
        for (UnderlineSpan span : underlines) {
            int start = strBuilder.getSpanStart(span);
            int end = strBuilder.getSpanEnd(span);
            ClickableSpan detailsLink = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick() SpannableLeakFragment: " + SpannableLeakFragment.this.hashCode());
                    Log.d(TAG, "onClick() Activity: " + getActivity().hashCode());
                    mOnSpanClickListener.onSpanClick();
                    mRefWatcher.watch(SpannableLeakFragment.this);
                }
            };
            strBuilder.setSpan(detailsLink, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mRefWatcher.watch(detailsLink);
        }

        textView.setText(strBuilder, TextView.BufferType.SPANNABLE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
