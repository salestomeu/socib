package com.socib.ui.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;

public class Device {
    private static String TAG = "Device";

    public static int MIN_TABLET_DIAGONAL = 7;
    private static Boolean isTablet = null;

    /**
     * Returns Device's diagonal in inches
     * @param activity
     * @return double
     */
    public static double getDiagonal(Activity activity) {

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float widthDpi = metrics.xdpi;
        float heightDpi = metrics.ydpi;

        float widthInches = widthPixels / widthDpi;
        float heightInches = heightPixels / heightDpi;

        double diagonalInches = Math.sqrt((widthInches * widthInches) + (heightInches * heightInches));

        Log.i(TAG, "Device diagonal: " + diagonalInches);
        return diagonalInches;
    }

    /**
     * Determine if this device is a tablet or not, depending on its diagonal size
     * @param activity
     * @return boolean
     */
    public static boolean isTablet(Activity activity) {

        if (isTablet == null) {
            isTablet = getDiagonal(activity) >= (double) MIN_TABLET_DIAGONAL;
        }

        return isTablet;
    }
}
