package com.chiemy.widget.pop.popdemo;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.view.View;

import com.nineoldandroids.animation.ValueAnimator;

import java.util.Random;

/**
 * Created by chiemy on 16/1/12.
 */
public class MagicWandAnimator extends ValueAnimator {
    private static final int PARTICLES_NUM = 4;
    private static final int MIN_RADIUS = Utils.dp2Px(5); // 颗粒最小半径
    private static final int RADIUS_BASE = Utils.dp2Px(10);
    private static final int OFFSET_BASE = Utils.dp2Px(10); // 颗粒之间距离的基数
    private static final float END_VALUE = 0.9f;
    private static final float MAX_ALPHA_FACTOR = 0.8f;
    private static final float MIN_ALPHA_FACTOR = 0.5f;

    private Paint paint;
    private Paint blurPaint;
    private int particleColor; // 颗粒颜色
    private Particle [] mParticles;
    private float centerX;
    private float centerY;
    private View containerView;

    public MagicWandAnimator(View container, PointF pointF, int color) {
        this(container, pointF.x, pointF.y, color);
    }

    public MagicWandAnimator(View container, float centerX, float centerY, int color) {
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
        particle.cx = centerX + OFFSET_BASE * random.nextFloat();
        particle.cy = centerY + OFFSET_BASE * random.nextFloat();
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
        float cx;
        float cy;
        float radius;
        float baseRadius;
        Canvas mCanvas;
        Bitmap bitmap;

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
        }
    }
}
