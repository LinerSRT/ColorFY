package ru.liner.colorfy.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;

import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.listener.IWallpaperDataListener;
import ru.liner.colorfy.utils.ColorUtils;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 04.11.2022, пятница
 **/
public class ColorfySwitch extends SwitchCompat implements IWallpaperDataListener {
    public ColorfySwitch(Context context) {
        super(context);
    }

    public ColorfySwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorfySwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked},
        };
        int[] thumbColors = new int[]{
                wallpaperData.isDarkTheme ? ColorUtils.lightenColor(wallpaperData.backgroundColor, 0.3f) : ColorUtils.darkerColor(wallpaperData.backgroundColor, 0.3f),
                wallpaperData.primaryColor,
        };
        int[] trackColors = new int[]{
                wallpaperData.isDarkTheme ? ColorUtils.lightenColor(wallpaperData.backgroundColor, 0.2f) : ColorUtils.darkerColor(wallpaperData.backgroundColor, 0.2f),
                wallpaperData.primaryColor,
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setTrackTintList(new ColorStateList(states, trackColors));
            setThumbTintList(new ColorStateList(states, thumbColors));
        }
        setTextColor(wallpaperData.textColor);
    }
}
