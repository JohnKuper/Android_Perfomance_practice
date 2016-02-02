package com.epam.dmitriy_korobeinikov.memoryleakspractice.animator_leak;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.epam.dmitriy_korobeinikov.memoryleakspractice.R;

/**
 * Created by Dmitrii_Ivashkin on 2/2/2016.
 */
public class AnimatorActivity extends AppCompatActivity {
    public byte[] garbage = new byte[1024 * 1024 * 5];
    private ImageView mArrowView, mHandView;
    private Animator mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tip_pull_to_refresh);
        mArrowView = (ImageView) findViewById(R.id.iv_arrow);
        mHandView = (ImageView) findViewById(R.id.iv_cursor);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAnimator = animateTip();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Without this, it would be leaked
//        mAnimator.removeAllListeners();
//        mAnimator.cancel();
//        mAnimator = null;
    }

    public Animator animateTip() {
        resetInitialAnimValues();

        Animator fadeInHand = getFadeInAnim(300, mHandView);

        PropertyValuesHolder[] scaleValues = {PropertyValuesHolder.ofFloat("scaleX", 1.5f, 1.0f), PropertyValuesHolder.ofFloat("scaleY", 1.5f, 1.0f)};
        Animator scaleAnim = ObjectAnimator.ofPropertyValuesHolder(mHandView, scaleValues).setDuration(1000);

        Animator fadeArrowAnim = getFadeInAnim(500, mArrowView);

        ValueAnimator translateAnim = ValueAnimator.ofFloat(mHandView.getTranslationY(), mHandView.getTranslationY() + pxFromDp(48.0f)).setDuration(800);
        translateAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mHandView.setTranslationY(value);
                mArrowView.setTranslationY(value);
            }
        });

        Animator fadeOutScene = getFadeOutAnim(400, mHandView, mArrowView);
        fadeOutScene.setStartDelay(1000);

        final AnimatorSet set = new AnimatorSet();
        set.setStartDelay(1000);
        set.playSequentially(fadeInHand, scaleAnim, fadeArrowAnim, translateAnim, fadeOutScene);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                resetInitialAnimValues();
                set.start();
            }
        });
        set.start();
        return set;
    }

    private float pxFromDp(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

    private void resetInitialAnimValues() {
        mHandView.setTranslationY(0.0f);
        mArrowView.setTranslationY(0.0f);
        mHandView.setScaleX(1.5f);
        mHandView.setScaleY(1.5f);
        mArrowView.setAlpha(0.0f);
        mHandView.setAlpha(0.0f);
    }

    private Animator getFadeInAnim(int duration, View... views) {
        return getAlphaAnim(0.0f, 1.0f, duration, views);
    }

    private Animator getFadeOutAnim(int duration, final View... views) {
        return getAlphaAnim(1.0f, 0.0f, duration, views);
    }

    private Animator getAlphaAnim(float from, float to, final int duration, final View... views) {
        ValueAnimator animator = ValueAnimator.ofFloat(from, to).setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                for (View view : views) {
                    view.setAlpha(value);
                }
            }
        });
        return animator;
    }
}
