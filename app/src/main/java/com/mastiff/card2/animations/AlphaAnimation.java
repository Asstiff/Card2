package com.mastiff.card2.animations;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.annotation.NonNull;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderEffectBlur;

public class AlphaAnimation {
    private final View mView;
    private final float mAlpha;
    private final float mStartAlpha;
    private int duration;
    private TimeInterpolator interpolator;
    private ObjectAnimator mAnimator;

    public AlphaAnimation(View view, float alpha) {
        mView = view;
        mAlpha = alpha;
        mStartAlpha = view.getAlpha();
    }

    public void start() {
        mAnimator = ObjectAnimator.ofFloat(mView, "alpha", mStartAlpha, mAlpha);
        mAnimator.setDuration(duration);  // 设置动画持续时间
        mAnimator.setInterpolator(interpolator);
        mAnimator.start();  // 开始动画
    }

    public void pause() {
        if (mAnimator != null) {
            mAnimator.pause();
        }
    }

    public void cancel() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    public void resume() {
        if (mAnimator != null) {
            mAnimator.resume();
        }
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    public void setInterpolator(TimeInterpolator interpolator){
        this.interpolator = interpolator;
    }
}
