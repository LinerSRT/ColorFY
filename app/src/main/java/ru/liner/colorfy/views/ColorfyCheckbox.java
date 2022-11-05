package ru.liner.colorfy.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.listener.IWallpaperDataListener;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 04.11.2022, пятница
 **/
public class ColorfyCheckbox extends AppCompatCheckBox implements IWallpaperDataListener {


    public ColorfyCheckbox(@NonNull Context context) {
        super(context);
    }

    public ColorfyCheckbox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorfyCheckbox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        setButtonTintList(ColorStateList.valueOf(wallpaperData.primaryColor));
        setTextColor(wallpaperData.textColor);
    }
}
