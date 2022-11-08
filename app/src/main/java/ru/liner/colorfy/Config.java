package ru.liner.colorfy;

import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;

import ru.liner.colorfy.utils.ColorUtils;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 04.11.2022, пятница
 **/
public class Config {
    /**
     * Force night theme
     */
    public static boolean forceNightTheme = false;
    /**
     * Force day theme
     */
    public static boolean forceDayTheme = false;
    /**
     * Change background color of root view in activity
     */
    public static boolean changeActivityBackground = true;
    /**
     * Change background color of root view in fragment
     */
    public static boolean changeFragmentBackground = true;
    /**
     * Change application bar background and text color
     */
    public static boolean changeApplicationBar = true;
    /**
     * Change navigation bar color
     */
    public static boolean changeNavigationBar = true;
    /**
     * Change status bar color
     */
    public static boolean changeStatusBar = true;
    /**
     * Determinate how much apply primary color to background color
     */
    @FloatRange(from = 0.1f, to = 0.4f)
    public static float backgroundToneAmount = 0.1f;
    /**
     * Determinate how much apply primary color to text color
     */
    @FloatRange(from = 0.1f, to = 0.4f)
    public static float textToneAmount = 0.1f;

    /**
     * Define true to use own color for theming
     */
    public static boolean usesCustomColor = false;
    /**
     * Define default primary color for theming
     */
    @ColorInt
    public static int customPrimaryColor = Color.RED;

    /**
     * Try to detect most common color for human eye automatically
     */
    public static boolean usesAutomaticSwatchFiltering = true;
    /**
     * Use most luminance color from automatic detected colors, if its false - use average color
     */
    public static boolean usesMostLuminanceDetectedSwatch = false;

    /**
     * Register and unregister wallpaper colors listeners on activity lifecycle
     */
    public static boolean automaticListenersLifecycle = false;
}
