package com.arkavyapar.Utils

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.res.Resources
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.view.Display
import android.view.View
import android.view.WindowManager


public class  ServiceUtils {
    /**
     * Get default display
     *
     * @param context
     * @return display
     */
    fun getDisplay(context: Context): Display {
        return (context.getSystemService(WINDOW_SERVICE) as WindowManager)
            .defaultDisplay
    }

    /**
     * Get default display
     *
     * @param view
     * @return display
     */
    fun getDisplay(view: View): Display {
        return getDisplay(view.getContext())
    }

    /**
     * Get default display width
     *
     * @param context
     * @return display
     */
    fun getDisplayWidth(context: Context?): Int {
        return getDisplay(context!!).getWidth()
    }

    /**
     * Get default display width
     *
     * @param view
     * @return display
     */
    fun getDisplayWidth(view: View): Int {
        return getDisplayWidth(view.getContext())
    }

    /**
     * Get pixels from dps
     *
     * @param view
     * @param dp
     * @return pixels
     */
    fun getPixels(view: View, dp: Int): Float {
        return getPixels(view.getResources(), dp)
    }

    /**
     * Get pixels from dps
     *
     * @param resources
     * @param dp
     * @return pixels
     */
    fun getPixels(resources: Resources, dp: Int): Float {
        return TypedValue.applyDimension(
            COMPLEX_UNIT_DIP, dp.toFloat(),
            resources.getDisplayMetrics()
        )
    }

    /**
     * Get pixels from dps
     *
     * @param view
     * @param dp
     * @return pixels
     */
    fun getIntPixels(view: View, dp: Int): Int {
        return getIntPixels(view.getResources(), dp)
    }

    /**
     * Get pixels from dps
     *
     * @param context
     * @param dp
     * @return pixels
     */
    fun getIntPixels(context: Context, dp: Int): Int {
        return getIntPixels(context.getResources(), dp)
    }

    /**
     * Get pixels from dps
     *
     * @param resources
     * @param dp
     * @return pixels
     */
    fun getIntPixels(resources: Resources, dp: Int): Int {
        val pixels = TypedValue.applyDimension(
            COMPLEX_UNIT_DIP, dp.toFloat(),
            resources.getDisplayMetrics()
        )
        return Math.floor(pixels + 0.5f.toDouble()).toInt()
    }
}