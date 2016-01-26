package com.chiemy.widget.pop.popdemo;


import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.ValueAnimator;

import java.util.Random;

/**
 * Created by chiemy on 16/1/12.
 */
public class SparkAnimator extends ValueAnimator {
    private static final int PARTICLES_NUM = 6;
    private static final int MIN_RADIUS = Utils.dp2Px(3); // 颗粒最小半径
    private static final int RADIUS_BASE = Utils.dp2Px(4);
    private static final int OFFSET_BASE = Utils.dp2Px(10); // 颗粒距离中心点的基数
    private static final float END_VALUE = 0.9f; // 动画结束值
    private static final float MAX_ALPHA_FACTOR = 0.8f;
    private static final float MIN_ALPHA_FACTOR = 0.5f;
    private static final float MIN_FLY_DISTANCE = Utils.dp2Px(20); // 颗粒飞行最短距离
    private static final float FLY_DISTANCE_BASE = Utils.dp2Px(50); // 颗粒飞行距离基数

    private Particle [] mParticles;

    private Paint paint;
    private Paint blurPaint;
    private int particleColor;
    private float centerX;
    private float centerY;
    private View containerView;

    public SparkAnimator(View container, float centerX, float centerY, int color) {
        paint = new Paint();
        paint.setAntiAlias(true);
        blurPaint = new Paint();
        blurPaint.setAntiAlias(true);
        blurPaint.setColor(Color.parseColor("#D4D4D4"));
        BlurMaskFilter blurMaskFilter = new BlurMaskFilter(Utils.dp2Px(3), BlurMaskFilter.Blur.OUTER);
        blurPaint.setMaskFilter(blurMaskFilter);

        particleColor = color;
        this.centerX = centerX;
        this.centerY = centerY;
        containerView = container;
        setFloatValues(0, END_VALUE);
        setInterpolator(new DecelerateInterpolator());
        initParticles();
    }

    private void initParticles(){
        mParticles = new Particle[PARTICLES_NUM];
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0 ; i < PARTICLES_NUM ; i++){
            mParticles[i] = generateParticle(particleColor, random);
        }
    }

    private Particle generateParticle(int color, Random random){
        Particle particle = new Particle(MIN_RADIUS + RADIUS_BASE * random.nextFloat());
        particle.color = color;
        particle.alpha = (int)(255 * (MAX_ALPHA_FACTOR - MIN_ALPHA_FACTOR * random.nextFloat()));
        particle.baseCx = centerX + OFFSET_BASE * random.nextFloat();
        particle.baseCy = centerY + OFFSET_BASE * random.nextFloat();
        particle.flyAngle = (float)(random.nextFloat() * Math.PI * 2);
        particle.flyDistance = MIN_FLY_DISTANCE + random.nextFloat() * FLY_DISTANCE_BASE;
        return particle;
    }

    public boolean draw(Canvas canvas) {
        if (!isStarted()) {
            return false;
        }
        drawParticle(canvas);
        containerView.invalidate();
        return true;
    }

    private void drawParticle(Canvas canvas){
        for (Particle particle : mParticles) {
            particle.advance((float) getAnimatedValue());
            //如果不是透明的 那就绘制出来
            if (particle.alpha > 0f) {
                particle.drawBlur(canvas);
                paint.setColor(particle.color);
                paint.setAlpha(particle.alpha);
                canvas.drawCircle(particle.cx, particle.cy, particle.radius, paint);
            }
        }
    }

    @Override
    public void start() {
        super.start();
        containerView.invalidate();
    }

    private class Particle {
        int alpha = 255;
        int color;
        float baseCx;
        float baseCy;
        float cx;
        float cy;
        float radius;
        float baseRadius;
        Canvas mCanvas;
        Bitmap bitmap;
        float flyAngle;
        float flyDistance;

        public Particle(float baseRadius){
            this.baseRadius = baseRadius;
            int size = (int)(baseRadius +  Utils.dp2Px(5) + 0.5) * 2;
            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(bitmap);
        }

        public void drawBlur(Canvas canvas){
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mCanvas.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, radius, blurPaint);
            canvas.drawBitmap(bitmap, cx - bitmap.getWidth()/2, cy - bitmap.getHeight()/2, null);
        }

        public void advance(float factor) {
            radius = baseRadius * (1 - factor);
            updatePosition(factor);
        }

        private void updatePosition(float factor){
            float x = (float)(flyDistance * Math.cos(flyAngle)) * factor;
            float y = (float)(flyDistance * Math.sin(flyAngle)) * factor;
            cx = baseCx + x;
            cy = baseCy - y;
        }
    }
}
