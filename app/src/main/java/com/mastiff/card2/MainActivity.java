package com.mastiff.card2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.core.view.WindowCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.fragment.app.FragmentManager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;

import com.mastiff.card2.R;
import com.mastiff.card2.animations.AlphaAnimation;
import com.mastiff.card2.animations.ScaleAnimation;
import com.mastiff.card2.utils.DpConvert;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderEffectBlur;

public class MainActivity extends AppCompatActivity {
    float oriX = 0;
    float oriY = 0;
    float oriRawX = 0;
    float oriRawY = 0;
    float oriHeight = 0;
    float oriWidth = 0;
    float realOriX = 0;
    float realOriY = 0;
    int pointerID = 0;
    boolean cardOpen = false;
    Context context;
    ScaleAnimation scaleAnimation;
    AlphaAnimation alphaAnimation;
    ScaleAnimation scaleAnimation2;
    AlphaAnimation alphaAnimation2;
    ObjectAnimator mAnimator;

    // ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
    ViewGroup rootView;
    int animationCancelled = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        View card = findViewById(R.id.card);

        realOriX = card.getX();
        realOriY = card.getY();
        SpringAnimation sa = new SpringAnimation(card, DynamicAnimation.TRANSLATION_Y);
        SpringForce sf = new SpringForce();
        sf.setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        sf.setStiffness(80f);
        sa.setSpring(sf);
        SpringAnimation sa2 = new SpringAnimation(card, DynamicAnimation.TRANSLATION_X);
        SpringForce sf2 = new SpringForce();
        sf2.setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        sf2.setStiffness(70f);
        sa2.setSpring(sf2);

        View root = findViewById(R.id.root);


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragmentContainerView, new BlankFragment()).commit();


        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        card.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(cardOpen){
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            animationCancelled = 0;
                            pointerID = event.getPointerId(0);
                            sa.cancel();
                            sa2.cancel();
                            if(mAnimator != null){
                                mAnimator.pause();
                            }
                            scaleAnimation.pause();
                            alphaAnimation.pause();
                            oriX = event.getX();
                            oriY = event.getY();
                            oriRawX = event.getRawX();
                            oriRawY = event.getRawY();
                            oriHeight = card.getHeight();
                            oriWidth = card.getWidth();
                            sf.setFinalPosition(realOriY);
                            sf2.setFinalPosition(realOriX);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if(pointerID == event.getPointerId(0)){
                                card.setX((event.getRawX() - oriX));
                                card.setY((event.getRawY() - oriY));
                            }
                            card.getLayoutParams().height = (int) (oriHeight - Math.abs((event.getRawY() - oriRawY)/6));
                            card.getLayoutParams().width = (int) (oriWidth - Math.abs((event.getRawY() - oriRawY)/8));
                            card.getLayoutParams().width = (int) (oriWidth - Math.abs((event.getRawX() - oriRawX)/4));
                            card.requestLayout();
                            System.out.println(Math.abs((event.getRawY() - oriRawY)/12) + " " + Math.abs((event.getRawX() - oriRawX)/8));
                            break;
                        case MotionEvent.ACTION_UP:
                            if(Math.abs(oriRawY - event.getRawY()) > 200 || Math.abs(oriRawX - event.getRawX()) > 200){
                                sa.start();
                                sa2.start();
                                rootView = (ViewGroup) findViewById(R.id.frameLayout);
                                BlurView blurView = ((BlurView)card.findViewById(R.id.blurView));
                                blurView.setupWith(rootView, new RenderEffectBlur());
                                animationCancelled = 2;
                                mAnimator = ObjectAnimator.ofFloat(blurView, "blurRadius", 40f, 0.0001f);
                                mAnimator.setDuration(600);  // 设置动画持续时间
                                mAnimator.setInterpolator(new PathInterpolator(0.4f,0.7f,0f,1f));
                                mAnimator.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        blurView.setBlurEnabled(true);
                                    }
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        if(animationCancelled == 2){
                                            blurView.setBlurEnabled(false);
                                        }
                                    }
                                });
                                mAnimator.start();  // 开始动画
                                scaleAnimation2 = new ScaleAnimation(card, (int) DpConvert.toPx(context, 120), (int) DpConvert.toPx(context, 160));
                                alphaAnimation2 = new AlphaAnimation(card.findViewById(R.id.frag_img), 1);
                                scaleAnimation2.setDuration(700);
                                alphaAnimation2.setDuration(400);
                                scaleAnimation2.setInterpolator(new PathInterpolator(0.28f,1.3f,0f,1f));
                                alphaAnimation2.setInterpolator(new PathInterpolator(0.4f,0.7f,0f,1f));
                                scaleAnimation2.start();
                                alphaAnimation2.start();
                                cardOpen = false;
                            }
                            else {
                                BlurView blurView = ((BlurView)card.findViewById(R.id.blurView));
                                blurView.setBlurEnabled(false);
                                scaleAnimation = new ScaleAnimation(card, root.getWidth() - 200, root.getHeight() -1000);
                                alphaAnimation = new AlphaAnimation(card.findViewById(R.id.frag_img), 0);
                                scaleAnimation.setDuration(300);
                                alphaAnimation.setDuration(200);
                                scaleAnimation.setInterpolator(new PathInterpolator(0.4f,0.7f,0f,1f));
                                alphaAnimation.setInterpolator(new PathInterpolator(0.7f,0f,0f,1f));
                                if(scaleAnimation2 != null){
                                    scaleAnimation2.pause();
                                    alphaAnimation2.pause();
                                }
                                scaleAnimation.start();
                                alphaAnimation.start();
                                sf.setFinalPosition(-400);
                                sf2.setFinalPosition(40);
                                sa.start();
                                sa2.start();
                                cardOpen = true;
                            }
                            break;
                    }
                }
                else {
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        rootView = (ViewGroup) findViewById(R.id.frameLayout);
                        BlurView blurView = ((BlurView)card.findViewById(R.id.blurView));
                        blurView.setupWith(rootView, new RenderEffectBlur());
                        mAnimator = ObjectAnimator.ofFloat(blurView, "blurRadius", 40f, 0.0001f);
                        mAnimator.setDuration(300);  // 设置动画持续时间
                        mAnimator.setInterpolator(new PathInterpolator(0.4f,0.7f,0f,1f));
                        animationCancelled = 1;
                        mAnimator.start();  // 开始动画
                        mAnimator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationEnd(animation);
                                blurView.setBlurEnabled(true);
                            }
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                if(animationCancelled == 1){
                                    blurView.setBlurEnabled(false);
                                }
                            }
                        });
                        scaleAnimation = new ScaleAnimation(card, root.getWidth() - 200, root.getHeight() -1000);
                        alphaAnimation = new AlphaAnimation(card.findViewById(R.id.frag_img), 0);
                        scaleAnimation.setDuration(500);
                        alphaAnimation.setDuration(400);
                        scaleAnimation.setInterpolator(new PathInterpolator(0.4f,0.7f,0f,1f));
                        alphaAnimation.setInterpolator(new PathInterpolator(0.7f,0f,0f,1f));
                        if(scaleAnimation2 != null){
                            scaleAnimation2.pause();
                            alphaAnimation2.pause();
                        }
                        scaleAnimation.start();
                        alphaAnimation.start();
                        sf.setFinalPosition(-400);
                        sf2.setFinalPosition(40);
                        sa.start();
                        sa2.start();
                        cardOpen = true;
                    }
                }
                return true;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}