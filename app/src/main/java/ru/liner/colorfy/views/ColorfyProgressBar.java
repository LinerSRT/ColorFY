package ru.liner.colorfy.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.listener.IWallpaperDataListener;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 04.11.2022, пятница
 **/
public class ColorfyProgressBar extends ProgressBar implements IWallpaperDataListener {
    public ColorfyProgressBar(Context context) {
        super(context);
    }

    public ColorfyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorfyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ColorfyProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        Drawable drawable = isIndeterminate() ? getIndeterminateDrawable() : getProgressDrawable();
        drawable.setColorFilter(wallpaperData.primaryColor, android.graphics.PorterDuff.Mode.SRC_IN);
        setProgressDrawable(drawable);
    }
}
