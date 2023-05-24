package ru.liner.colorfy.dialogs;

import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.utils.Reflect;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 08.11.2022, вторник
 **/
public class ColorfyDialogUtils {

    public static void apply(@NonNull AlertDialog alertDialog, @Nullable WallpaperData wallpaperData) {
        if (!alertDialog.isShowing())
            throw new RuntimeException("Colorfy cannot be applied to invisible dialogs!");
        if (wallpaperData == null)
            return;
        Object alertController = Reflect.getFieldSafety(alertDialog, "mAlert");
        if (alertController != null) {
            applyBackgroundColor(alertController, "mView", wallpaperData.backgroundColor);
            applyTextColor(alertController, "mTitleView", wallpaperData.primaryColor);
            applyTextColor(alertController, "mMessageView", wallpaperData.textColor);
            applyTextColor(alertController, "mButtonPositive", wallpaperData.secondaryColor);
            applyTextColor(alertController, "mButtonNegative", wallpaperData.secondaryColor);
            applyTextColor(alertController, "mButtonNeutralMessage", wallpaperData.secondaryColor);
        }
    }

    private static void applyTextColor(@NonNull Object alertController, @NonNull String viewName, @ColorInt int color) {
        Object alertTitleView = Reflect.getFieldSafety(alertController, viewName);
        if (alertTitleView != null)
            Reflect.invokeSafety(alertTitleView, "setTextColor", new Class[]{int.class}, color);
    }

    private static void applyBackgroundColor(@NonNull Object alertController, @NonNull String viewName, @ColorInt int color) {
        Object view = Reflect.getFieldSafety(alertController, viewName);
        if (view != null) {
            Object viewBackground = Reflect.invokeSafety(view, "getBackground", new Class[]{});
            Reflect.invokeSafety(viewBackground, "setColorFilter", new Class[]{ColorFilter.class}, new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        } else {
            Object window = Reflect.getFieldSafety(alertController, "mWindow");
            Object decorView = Reflect.invokeSafety(window, "getDecorView", new Class[]{});
            Object viewBackground = Reflect.invokeSafety(decorView, "getBackground", new Class[]{});
            Reflect.invokeSafety(viewBackground, "setColorFilter", new Class[]{ColorFilter.class}, new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        }
    }


}
