package ru.liner.colorfy.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ru.liner.colorfy.R;
import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.listener.IWallpaperDataListener;
import ru.liner.colorfy.utils.ColorUtils;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 10.11.2022, четверг
 **/
public class ColorfyLinearLayout extends LinearLayout implements IWallpaperDataListener {
    private boolean primaryBackgroundColor;

    public ColorfyLinearLayout(@NonNull Context context) {
        this(context, null);
    }

    public ColorfyLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorfyLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorfyLinearLayout, defStyleAttr, 0);
        primaryBackgroundColor = typedArray.getBoolean(R.styleable.ColorfyLinearLayout_ll_primaryBackgroundColor, false);
        typedArray.recycle();
    }

    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        setBackgroundTintList(ColorStateList.valueOf(primaryBackgroundColor ? wallpaperData.primaryColor : ColorUtils.darkerColor(wallpaperData.backgroundColor, 0.2f)));
    }
}
