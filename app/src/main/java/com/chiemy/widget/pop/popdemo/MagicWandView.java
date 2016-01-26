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
public class MagicWandView extends View {
    private static final long ANIM_START_DELAY = 200;
    private static final long ANIM_DURATION = 1000;

    private List<MagicWandAnimator> popAnimators = new ArrayList<>();

    public MagicWandView(Context context) {
        super(context);
    }

    public MagicWandView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MagicWandView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE){
            MagicWandAnimator popAnimator  = new MagicWandAnimator(this, event.getX(), event.getY(), Color.WHITE);
            popAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    popAnimators.remove(animation);
                }
            });
            popAnimator.setStartDelay(ANIM_START_DELAY);
            popAnimator.setDuration(ANIM_DURATION);
            popAnimator.start();
            popAnimators.add(popAnimator);
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //disableHardwareRendering();
        for (MagicWandAnimator popAnimator : popAnimators){
            popAnimator.draw(canvas);
        }
    }

    public void disableHardwareRendering() {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    public void clear() {
        popAnimators.clear();
        invalidate();
    }
}
