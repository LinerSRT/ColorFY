package ru.liner.colorfy.core;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private static final int MAX_COLOR_COUNT = 128;
    private static final int MIN_COLOR_COUNT = 5;
    @ColorInt
    public int primaryColor;
    @ColorInt
    public int secondaryColor;
    @ColorInt
    public int textOnPrimaryColor;
    @ColorInt
    public int textColor;
    @ColorInt
    public int disabledTextColor;
    @ColorInt
    public int backgroundColor;
    public boolean isDarkTheme;
    private boolean isTextColorGenerated;
    public List<ColorVariant> colorVariantList;

    private WallpaperData(@NonNull Context context) {
        this.colorVariantList = new ArrayList<>();
        if (Config.forceDayTheme) {
            this.isDarkTheme = false;
        } else if (Config.forceNightTheme) {
            this.isDarkTheme = true;
        } else {
            this.isDarkTheme = Utils.isNightTheme(context);
        }
    }

    private WallpaperData() {
    }


    public static void fromColor(@NonNull Context context, @ColorInt int primaryColor, @NonNull IGenerate generate) {
        new Thread(() -> {
            WallpaperData wallpaperData = new WallpaperData(context);
            wallpaperData.colorVariantList = new ArrayList<>();
            wallpaperData.primaryColor = primaryColor;
            wallpaperData.secondaryColor = ColorUtils.darkerColor(wallpaperData.primaryColor, 0.2f);
            int lightAlpha = ColorUtils.calculateMinAlpha(primaryColor, Color.WHITE);
            int darkAlpha = ColorUtils.calculateMinAlpha(primaryColor, Color.BLACK);
            wallpaperData.textOnPrimaryColor = lightAlpha != -1 ?
                    androidx.core.graphics.ColorUtils.setAlphaComponent(Color.WHITE, lightAlpha) :
                    darkAlpha != -1 ? androidx.core.graphics.ColorUtils.setAlphaComponent(Color.BLACK, darkAlpha) : Color.WHITE;

            wallpaperData.backgroundColor = wallpaperData.isDarkTheme ? ColorUtils.darkerColor(wallpaperData.primaryColor, 1f - Config.backgroundToneAmount) : ColorUtils.lightenColor(wallpaperData.primaryColor, Config.backgroundToneAmount);
            wallpaperData.textColor = ColorUtils.isColorDark(wallpaperData.backgroundColor) ? ColorUtils.lightenColor(wallpaperData.primaryColor, Config.textToneAmount) : ColorUtils.darkerColor(wallpaperData.primaryColor, 1f - Config.textToneAmount);
            wallpaperData.disabledTextColor = ColorUtils.mixedColor(Color.LTGRAY, wallpaperData.textColor, 0.3f);
            generate.onGenerated(wallpaperData);
        }).start();
    }

    public static void fromBitmap(@NonNull Context context, @NonNull Bitmap bitmap, @NonNull IGenerate generate) {
        new Thread(() -> {
            Palette palette = Palette.from(bitmap).maximumColorCount(((ActivityManager) context.getSystemService(ACTIVITY_SERVICE)).isLowRamDevice() ? MIN_COLOR_COUNT : MAX_COLOR_COUNT).generate();
            List<ColorVariant> colorVariantList = getColorVariantList(palette);
            WallpaperData wallpaperData = new WallpaperData(context);
            int primaryColor;
            if (Config.enableColorsSwitch) {
                primaryColor = colorVariantList.get(Config.colorIndex).color;
            } else {
                Palette.Swatch mostCommonSwatch = palette.getVibrantSwatch();
                if (mostCommonSwatch == null)
                    mostCommonSwatch = palette.getLightVibrantSwatch();
                if (mostCommonSwatch == null)
                    mostCommonSwatch = palette.getDarkVibrantSwatch();
                if (mostCommonSwatch == null)
                    mostCommonSwatch = palette.getDarkMutedSwatch();
                if (mostCommonSwatch == null)
                    mostCommonSwatch = palette.getDarkVibrantSwatch();
                if (mostCommonSwatch == null)
                    mostCommonSwatch = palette.getLightMutedSwatch();
                if (mostCommonSwatch == null)
                    mostCommonSwatch = palette.getDarkMutedSwatch();
                primaryColor = mostCommonSwatch.getRgb();
            }
            wallpaperData.colorVariantList = colorVariantList;
            wallpaperData.primaryColor = primaryColor;
            wallpaperData.secondaryColor = ColorUtils.darkerColor(wallpaperData.primaryColor, 0.2f);
            int lightAlpha = ColorUtils.calculateMinAlpha(primaryColor, Color.WHITE);
            int darkAlpha = ColorUtils.calculateMinAlpha(primaryColor, Color.BLACK);
            wallpaperData.textOnPrimaryColor = lightAlpha != -1 ?
                    androidx.core.graphics.ColorUtils.setAlphaComponent(Color.WHITE, lightAlpha) :
                    darkAlpha != -1 ? androidx.core.graphics.ColorUtils.setAlphaComponent(Color.BLACK, darkAlpha) : Color.WHITE;

            wallpaperData.backgroundColor = wallpaperData.isDarkTheme ? ColorUtils.darkerColor(wallpaperData.primaryColor, 1f - Config.backgroundToneAmount) : ColorUtils.lightenColor(wallpaperData.primaryColor, Config.backgroundToneAmount);
            wallpaperData.textColor = ColorUtils.isColorDark(wallpaperData.backgroundColor) ? ColorUtils.lightenColor(wallpaperData.primaryColor, Config.textToneAmount) : ColorUtils.darkerColor(wallpaperData.primaryColor, 1f - Config.textToneAmount);
            wallpaperData.disabledTextColor = ColorUtils.mixedColor(Color.LTGRAY, wallpaperData.textColor, 0.3f);
            generate.onGenerated(wallpaperData);
        }).start();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isSame(@NonNull WallpaperData other) {
        return primaryColor == other.primaryColor &&
                secondaryColor == other.secondaryColor &&
                textOnPrimaryColor == other.textOnPrimaryColor &&
                textColor == other.textColor &&
                disabledTextColor == other.disabledTextColor &&
                backgroundColor == other.backgroundColor;
    }

    @SuppressWarnings("unused")
    public static WallpaperData clone(WallpaperData from) {
        WallpaperData wallpaperData = new WallpaperData();
        wallpaperData.colorVariantList = from.colorVariantList;
        wallpaperData.isDarkTheme = from.isDarkTheme;
        wallpaperData.primaryColor = from.primaryColor;
        wallpaperData.secondaryColor = from.secondaryColor;
        wallpaperData.textOnPrimaryColor = from.textOnPrimaryColor;
        wallpaperData.textColor = from.textColor;
        wallpaperData.disabledTextColor = from.disabledTextColor;
        wallpaperData.backgroundColor = from.backgroundColor;
        return wallpaperData;
    }

    public interface IGenerate {
        void onGenerated(WallpaperData wallpaperData);
    }


    private static boolean containSimilarColor(List<ColorVariant> colorVariantList, @ColorInt int color) {
        for (ColorVariant colorVariant : colorVariantList) {
            if (ColorUtils.isSimilarColor(colorVariant.color, color, Config.similarityColorsShift))
                return true;
        }
        return false;
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

    private static List<ColorVariant> getColorVariantList(Palette palette) {
        List<ColorVariant> colorVariantList = new ArrayList<>();
        List<Palette.Swatch> swatchList = new ArrayList<>(palette.getSwatches());
        Collections.sort(swatchList, (a, b) -> b.getPopulation() - a.getPopulation());
        Collections.sort(swatchList, (a, b) -> Double.compare(androidx.core.graphics.ColorUtils.calculateLuminance(b.getRgb()), androidx.core.graphics.ColorUtils.calculateLuminance(a.getRgb())));
        for (Palette.Swatch swatch : swatchList) {
            if (!ColorUtils.isGray(swatch.getRgb()) && !ColorUtils.isColorLight(swatch.getRgb()) && !containSimilarColor(colorVariantList, swatch.getRgb()))
                colorVariantList.add(new ColorVariant(colorVariantList.isEmpty() ? 0 : colorVariantList.size(), swatch.getRgb()));
        }
        Config.maxColorIndex = colorVariantList.size() - 1;
        return colorVariantList;
    }


    @NonNull
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
