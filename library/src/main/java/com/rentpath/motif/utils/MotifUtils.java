package com.rentpath.motif.utils;

import android.os.Build;

public class MotifUtils {

    private static Boolean sToolbarCheck = null;
    private static Boolean sAppCompatViewCheck = null;

    /**
     * See if the user has added appcompat-v7, this is done at runtime, so we only check once.
     *
     * @return true if the v7.Toolbar is on the classpath
     */
    public static boolean canCheckForV7Toolbar() {
        if (sToolbarCheck == null) {
            try {
                Class.forName("android.support.v7.widget.Toolbar");
                sToolbarCheck = Boolean.TRUE;
            } catch (ClassNotFoundException e) {
                sToolbarCheck = Boolean.FALSE;
            }
        }
        return sToolbarCheck;
    }

    /**
     * See if the user has added appcompat-v7 with AppCompatViews
     *
     * @return true if AppcompatTextView is on the classpath
     */
    public static boolean canAddV7AppCompatViews() {
        if (sAppCompatViewCheck == null) {
            try {
                Class.forName("android.support.v7.widget.AppCompatTextView");
                sAppCompatViewCheck = Boolean.TRUE;
            } catch (ClassNotFoundException e) {
                sAppCompatViewCheck = Boolean.FALSE;
            }
        }
        return sAppCompatViewCheck;
    }

    /**
     * Checks to see if we are running lollipop or greater
     *
     * @return true if version is at least lollipop
     */
    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    private MotifUtils() {
    }
}
