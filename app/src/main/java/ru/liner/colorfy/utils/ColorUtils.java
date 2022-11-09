package ru.liner.colorfy.utils;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;

import java.util.Random;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 04.11.2022, пятница
 **/
public class ColorUtils {
    public static int calculateMinAlpha(@ColorInt int color, @ColorInt int toColor){
        return androidx.core.graphics.ColorUtils.calculateMinimumAlpha(toColor, color, 4.5f);
    }

    @ColorInt
    public static int mixedColor(@ColorInt int color, @ColorInt int mixColor, float ratio) {
        return androidx.core.graphics.ColorUtils.blendARGB(color, mixColor, ratio);
    }

    @ColorInt
    public static int darkerColor(@ColorInt int color, float ratio) {
        return androidx.core.graphics.ColorUtils.blendARGB(color, Color.BLACK, ratio);
    }

    @ColorInt
    public static int lightenColor(@ColorInt int color, float ratio) {
        return androidx.core.graphics.ColorUtils.blendARGB(Color.WHITE, color, ratio);
    }

    public static boolean isColorDark(@ColorInt int color) {
        return androidx.core.graphics.ColorUtils.calculateLuminance(color) < 0.5;
    }

    public static boolean isColorLight(@ColorInt int color) {
        return !isColorDark(color);
    }

    public static boolean isGray(@ColorInt int color) {
        int red = (color >> 16) & 0xff;
        int green = (color >> 8) & 0xff;
        int blue = (color) & 0xff;
        int redGreenChannelDifference = red - green;
        int redBlueChannelDifference = red - blue;
        int tolerance = 10;
        if (redGreenChannelDifference > tolerance || redGreenChannelDifference < -tolerance)
            return redBlueChannelDifference <= tolerance && redBlueChannelDifference >= -tolerance;
        return true;
    }

    public static boolean isSimilarColor(@ColorInt int sourceColor, @ColorInt int checkColor, @IntRange(from = 5, to = 100) int colorShift) {
        int red1 = (sourceColor >>> 16) & 0xFF;
        int green1 = (sourceColor >>> 8) & 0xFF;
        int blue1 = (sourceColor) & 0xFF;
        int alpha2 = (checkColor >>> 24) & 0xFF;
        int red2 = (checkColor >>> 16) & 0xFF;
        int green2 = (checkColor >>> 8) & 0xFF;
        int blue2 = (checkColor) & 0xFF;
        if (alpha2 == Color.TRANSPARENT)
            return true;
        if (red1 >= (red2 - colorShift) && red1 <= (red2 + colorShift))
            if (green1 >= (green2 - colorShift) && green1 <= (green2 + colorShift))
                return blue1 >= (blue2 - colorShift) && blue1 <= (blue2 + colorShift);
        return false;
    }

    @ColorInt
    public static int getAttrColor(Activity activity, int attrColor) {
        TypedValue typedValue = new TypedValue();
        int color;
        if (activity.getTheme().resolveAttribute(attrColor, typedValue, true)) {
            switch (typedValue.type) {
                case TypedValue.TYPE_INT_COLOR_ARGB4:
                    color = Color.argb((typedValue.data & 0xf000) >> 8, (typedValue.data & 0xf00) >> 4,
                            typedValue.data & 0xf0, (typedValue.data & 0xf) << 4);
                    break;
                case TypedValue.TYPE_INT_COLOR_RGB4:
                    color = Color.rgb((typedValue.data & 0xf00) >> 4, typedValue.data & 0xf0, (typedValue.data & 0xf) << 4);
                    break;
                case TypedValue.TYPE_INT_COLOR_ARGB8:
                    color = typedValue.data;
                    break;
                case TypedValue.TYPE_INT_COLOR_RGB8:
                    color = Color.rgb((typedValue.data & 0xff0000) >> 16, (typedValue.data & 0xff00) >> 8,
                            typedValue.data & 0xff);
                    break;
                default:
                    throw new RuntimeException("Couldn't parse theme background color attribute " + typedValue.toString());
            }
        } else {
            throw new RuntimeException("Couldn't find background color in theme");
        }
        return color;
    }

    @ColorInt
    public static int randomColor() {
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}
