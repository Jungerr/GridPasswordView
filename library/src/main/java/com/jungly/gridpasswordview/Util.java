package com.jungly.gridpasswordview;

import android.content.Context;

/**
 * @author Jungly
 * @mail jungly.ik@gmail.com
 * @date 15/3/8 10:07
 */
public class Util {

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

}
