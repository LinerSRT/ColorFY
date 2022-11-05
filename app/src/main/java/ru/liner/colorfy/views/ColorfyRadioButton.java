package ru.liner.colorfy.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.listener.IWallpaperDataListener;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 05.11.2022, суббота
 **/
public class ColorfyRadioButton extends AppCompatRadioButton implements IWallpaperDataListener {
    public ColorfyRadioButton(Context context) {
        super(context);
    }

    public ColorfyRadioButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorfyRadioButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        setButtonTintList(ColorStateList.valueOf(wallpaperData.primaryColor));
        setTextColor(wallpaperData.textColor);
    }
}
