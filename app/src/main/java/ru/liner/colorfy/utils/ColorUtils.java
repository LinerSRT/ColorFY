package ru.liner.colorfy.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;

import androidx.annotation.ColorInt;

import ru.liner.colorfy.Config;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 04.11.2022, пятница
 **/
public class ColorUtils {
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
}
