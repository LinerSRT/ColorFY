package ru.liner.colorfy.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;

import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.listener.IWallpaperDataListener;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 04.11.2022, пятница
 **/
public class ColorfySeekBar extends AppCompatSeekBar implements IWallpaperDataListener {
    public ColorfySeekBar(@NonNull Context context) {
        super(context);
    }

    public ColorfySeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorfySeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        getProgressDrawable().setColorFilter(wallpaperData.secondaryColor, PorterDuff.Mode.SRC_IN);
        getThumb().setColorFilter(wallpaperData.secondaryColor, PorterDuff.Mode.SRC_IN);
    }
}
