package ru.liner.colorfy.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;

import ru.liner.colorfy.Config;
import ru.liner.colorfy.utils.ColorUtils;
import ru.liner.colorfy.utils.Utils;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 04.11.2022, пятница
 **/
public class WallpaperData {
    private static final float MIN_CONTRAST_BODY_TEXT = 4.5f;
    public Bitmap bitmap;
    @ColorInt
    public int primaryColor;
    @ColorInt
    public int secondaryColor;
    @ColorInt
    public int textOnPrimaryColor;
    @ColorInt
    public int textColor;
    @ColorInt
    public int backgroundColor;
    public boolean isDarkTheme;
    public boolean isCustomData;
    private boolean isTextColorGenerated;

    private WallpaperData(@NonNull Context context, @NonNull Bitmap bitmap) {
        this.bitmap = bitmap;
        if (Config.forceDayTheme) {
            this.isDarkTheme = false;
        } else if (Config.forceNightTheme) {
            this.isDarkTheme = true;
        } else {
            this.isDarkTheme = Utils.isNightTheme(context);
        }
    }

    private WallpaperData(@NonNull Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private WallpaperData(Context context, @ColorInt int primaryColor) {
        if (Config.forceDayTheme) {
            this.isDarkTheme = false;
        } else if (Config.forceNightTheme) {
            this.isDarkTheme = true;
        } else {
            this.isDarkTheme = Utils.isNightTheme(context);
        }
        this.isCustomData = true;
        this.primaryColor = primaryColor;
        this.secondaryColor = ColorUtils.darkerColor(primaryColor, 0.2f);
        ensureTextColorsGenerated();
        this.backgroundColor = this.isDarkTheme ? ColorUtils.darkerColor(this.primaryColor, 1f - Config.backgroundToneAmount) : ColorUtils.lightenColor(this.primaryColor, Config.backgroundToneAmount);
        this.textColor = ColorUtils.isColorDark(this.backgroundColor) ? ColorUtils.lightenColor(this.primaryColor, Config.textToneAmount) : ColorUtils.darkerColor(this.primaryColor, 1f - Config.textToneAmount);
    }

    public static WallpaperData fromColor(@NonNull Context context, @ColorInt int color) {
        return new WallpaperData(context, color);
    }

    public static void from(@NonNull Context context, @NonNull Bitmap bitmap, @NonNull IGenerate generate) {
        WallpaperData wallpaperData = new WallpaperData(context, bitmap);
        new Thread(() -> {
            Palette palette = Palette.from(bitmap).generate();
            Palette.Swatch swatch = palette.getVibrantSwatch();
            if (swatch == null)
                swatch = palette.getLightVibrantSwatch();
            if (swatch == null)
                swatch = palette.getDarkVibrantSwatch();
            if (swatch == null)
                swatch = palette.getDarkMutedSwatch();
            if (swatch == null)
                swatch = palette.getDarkVibrantSwatch();
            if (swatch == null)
                swatch = palette.getLightMutedSwatch();
            if (swatch == null)
                swatch = palette.getDarkMutedSwatch();
            wallpaperData.primaryColor = swatch.getRgb();
            wallpaperData.secondaryColor = ColorUtils.darkerColor(wallpaperData.primaryColor, 0.2f);
            wallpaperData.textOnPrimaryColor = swatch.getBodyTextColor();
            wallpaperData.backgroundColor = wallpaperData.isDarkTheme ? ColorUtils.darkerColor(wallpaperData.primaryColor, 1f - Config.backgroundToneAmount) : ColorUtils.lightenColor(wallpaperData.primaryColor, Config.backgroundToneAmount);
            wallpaperData.textColor = ColorUtils.isColorDark(wallpaperData.backgroundColor) ? ColorUtils.lightenColor(wallpaperData.primaryColor, Config.textToneAmount) : ColorUtils.darkerColor(wallpaperData.primaryColor, 1f - Config.textToneAmount);
            generate.onGenerated(wallpaperData);
        }).start();
    }

    public boolean isSame(@NonNull WallpaperData other) {
        return primaryColor == other.primaryColor &&
                secondaryColor == other.secondaryColor &&
                textOnPrimaryColor == other.textOnPrimaryColor &&
                textColor == other.textColor &&
                backgroundColor == other.backgroundColor;
    }

    public interface IGenerate {
        void onGenerated(WallpaperData wallpaperData);
    }


    public static WallpaperData clone(WallpaperData from) {
        WallpaperData wallpaperData = new WallpaperData(from.bitmap);
        wallpaperData.isDarkTheme = from.isDarkTheme;
        wallpaperData.primaryColor = from.primaryColor;
        ;
        wallpaperData.secondaryColor = from.secondaryColor;
        ;
        wallpaperData.textOnPrimaryColor = from.textOnPrimaryColor;
        wallpaperData.textColor = from.textColor;
        ;
        wallpaperData.backgroundColor = from.backgroundColor;
        return wallpaperData;
    }

    private void ensureTextColorsGenerated() {
        if (!isTextColorGenerated) {
            final int lightBodyAlpha = androidx.core.graphics.ColorUtils.calculateMinimumAlpha(Color.WHITE, primaryColor, MIN_CONTRAST_BODY_TEXT);
            if (lightBodyAlpha != -1) {
                textOnPrimaryColor = androidx.core.graphics.ColorUtils.setAlphaComponent(Color.WHITE, lightBodyAlpha);
                isTextColorGenerated = true;
                return;
            }
            final int darkBodyAlpha = androidx.core.graphics.ColorUtils.calculateMinimumAlpha(Color.BLACK, primaryColor, MIN_CONTRAST_BODY_TEXT);
            if (darkBodyAlpha != -1) {
                textOnPrimaryColor = androidx.core.graphics.ColorUtils.setAlphaComponent(Color.BLACK, darkBodyAlpha);
                isTextColorGenerated = true;
            }
        }
    }

    @Override
    public String toString() {
        return "WallpaperData{" +
                "primaryColor=" + primaryColor +
                ", secondaryColor=" + secondaryColor +
                ", textOnPrimaryColor=" + textOnPrimaryColor +
                ", textColor=" + textColor +
                ", backgroundColor=" + backgroundColor +
                ", isDarkTheme=" + isDarkTheme +
                '}';
    }
}
