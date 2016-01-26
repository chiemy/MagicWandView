package com.chiemy.widget.pop.popdemo;

import android.content.res.Resources;

/**
 * Created by chiemy on 16/1/12.
 */
public class Utils {
    private Utils() {
    }

    /**
     * 像素密度
     */
    private static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;

    /**
     * 将dp 转为 像素
     * @param dp
     * @return
     */
    public static int dp2Px(int dp) {
        return Math.round(dp * DENSITY);
    }
}
