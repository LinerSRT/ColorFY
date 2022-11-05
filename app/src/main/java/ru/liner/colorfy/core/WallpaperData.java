package ru.liner.colorfy.core;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;

import ru.liner.colorfy.utils.ColorUtils;
import ru.liner.colorfy.utils.Utils;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 04.11.2022, пятница
 **/
public class WallpaperData {
    @NonNull
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

    private WallpaperData(@NonNull Context context, @NonNull Bitmap bitmap) {
        this.bitmap = bitmap;
        this.isDarkTheme = Utils.isNightTheme(context);
    }

    private WallpaperData(@NonNull Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public static void from(@NonNull Context context, @NonNull Bitmap bitmap, @NonNull IGenerate generate) {
        WallpaperData wallpaperData = new WallpaperData(context, bitmap);
        new Thread(() -> {
            Palette palette = Palette.from(bitmap).generate();
            Palette.Swatch swatch = palette.getVibrantSwatch();
            if(swatch == null)
                swatch = palette.getLightVibrantSwatch();
            if(swatch == null)
                swatch = palette.getDarkVibrantSwatch();
            if(swatch == null)
                swatch = palette.getDarkMutedSwatch();
            if(swatch == null)
                swatch = palette.getDarkVibrantSwatch();
            if(swatch == null)
                swatch = palette.getLightMutedSwatch();
            if(swatch == null)
                swatch = palette.getDarkMutedSwatch();
            wallpaperData.primaryColor = swatch.getRgb();
            wallpaperData.secondaryColor = ColorUtils.darkerColor(wallpaperData.primaryColor, 0.2f);
            wallpaperData.textOnPrimaryColor = swatch.getBodyTextColor();
            wallpaperData.backgroundColor =	wallpaperData.isDarkTheme ? ColorUtils.darkerColor(wallpaperData.primaryColor, 0.92f) : ColorUtils.lightenColor(wallpaperData.primaryColor, 0.2f);
            wallpaperData.textColor = ColorUtils.isColorDark(wallpaperData.backgroundColor) ? ColorUtils.lightenColor(wallpaperData.primaryColor, 0.2f) : ColorUtils.darkerColor(wallpaperData.primaryColor, 0.92f);
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

    public interface IGenerate{
        void onGenerated(WallpaperData wallpaperData);
    }



  public static WallpaperData clone(WallpaperData from){
        WallpaperData wallpaperData = new WallpaperData(from.bitmap);
        wallpaperData.isDarkTheme = from.isDarkTheme;
        wallpaperData.primaryColor = from.primaryColor;;
        wallpaperData.secondaryColor = from.secondaryColor;;
        wallpaperData.textOnPrimaryColor = from.textOnPrimaryColor;
        wallpaperData.textColor = from.textColor;;
        wallpaperData.backgroundColor = from.backgroundColor;
        return wallpaperData;
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
