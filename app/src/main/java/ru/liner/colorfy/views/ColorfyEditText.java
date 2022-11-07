package ru.liner.colorfy.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.view.ViewCompat;

import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.listener.IWallpaperDataListener;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 05.11.2022, суббота
 **/
public class ColorfyEditText extends AppCompatEditText implements IWallpaperDataListener {
    public ColorfyEditText(@NonNull Context context) {
        super(context);
    }

    public ColorfyEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorfyEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        setTextColor(wallpaperData.textColor);
        setLinkTextColor(wallpaperData.secondaryColor);
        setHighlightColor(wallpaperData.secondaryColor);
        getBackground().mutate().setColorFilter(wallpaperData.primaryColor, PorterDuff.Mode.SRC_ATOP);
        ViewCompat.setBackgroundTintList(this, ColorStateList.valueOf(wallpaperData.primaryColor));
    }
}
