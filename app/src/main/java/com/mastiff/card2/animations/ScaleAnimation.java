package com.mastiff.card2.animations;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;

public class ScaleAnimation {
    private final View mView;
    private final int mWidth;
    private final int mHeight;
    private final int mStartWidth;
    private final int mStartHeight;
    private int duration;
    private TimeInterpolator interpolator;
    private Animator mAnimator;

    public ScaleAnimation(View view, int width, int height) {
        mView = view;
        mWidth = width;
        mHeight = height;
        mStartWidth = view.getWidth();
        mStartHeight = view.getHeight();
    }

    public void start() {
        // 创建一个动画集，包含宽度和高度的动画
        AnimatorSet animatorSet = new AnimatorSet();

        ValueAnimator widthAnimator = ValueAnimator.ofInt(mStartWidth, mWidth);
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mView.getLayoutParams().width = (Integer) animation.getAnimatedValue();
                mView.requestLayout();
            }
        });

        ValueAnimator heightAnimator = ValueAnimator.ofInt(mStartHeight, mHeight);
        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mView.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                mView.requestLayout();
            }
        });

        // 将宽度和高度的动画放入动画集中，同时进行
        animatorSet.playTogether(widthAnimator, heightAnimator);
        animatorSet.setDuration(duration);  // 设置动画持续时间
        animatorSet.setInterpolator(interpolator);
        animatorSet.start();  // 开始动画

        mAnimator = animatorSet;
    }

    public void pause() {
        if (mAnimator != null) {
            mAnimator.pause();
        }
    }

    public void setDuration(int duration){
        this.duration = duration;
    }


    public void setInterpolator(TimeInterpolator interpolator){
        this.interpolator = interpolator;
    }



    public void resume() {
        if (mAnimator != null) {
            mAnimator.resume();
        }
    }
}
