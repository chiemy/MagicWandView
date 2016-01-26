package com.chiemy.widget.pop.popdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chiemy on 16/1/12.
 */
public class SparkView extends View{
    private static final long ANIM_START_DELAY = 200;
    private static final long ANIM_DURATION = 1000;

    private List<SparkAnimator> sparkAnimators = new ArrayList<>();

    public SparkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SparkView(Context context) {
        super(context);
    }

    public SparkView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE){
            SparkAnimator sparkAnimator  = new SparkAnimator(this, event.getX(), event.getY(), Color.WHITE);
            sparkAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    sparkAnimators.remove(animation);
                }
            });
            sparkAnimator.setStartDelay(ANIM_START_DELAY);
            sparkAnimator.setDuration(ANIM_DURATION);
            sparkAnimator.start();
            sparkAnimators.add(sparkAnimator);
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (SparkAnimator animator : sparkAnimators){
            animator.draw(canvas);
        }
    }

}
