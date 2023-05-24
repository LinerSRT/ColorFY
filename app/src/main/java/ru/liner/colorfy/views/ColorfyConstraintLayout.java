package ru.liner.colorfy.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import ru.liner.colorfy.R;
import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.listener.IWallpaperDataListener;
import ru.liner.colorfy.utils.ColorUtils;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 10.11.2022, четверг
 **/
public class ColorfyConstraintLayout extends ConstraintLayout implements IWallpaperDataListener {
    private boolean primaryBackgroundColor;
    public ColorfyConstraintLayout(@NonNull Context context) {
        this(context, null);
    }

    public ColorfyConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorfyConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorfyConstraintLayout, defStyleAttr, 0);
        primaryBackgroundColor = typedArray.getBoolean(R.styleable.ColorfyConstraintLayout_cl_primaryBackgroundColor, false);
        typedArray.recycle();
    }


    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        setBackgroundTintList(ColorStateList.valueOf(primaryBackgroundColor ? wallpaperData.primaryColor : ColorUtils.lightenColor(wallpaperData.backgroundColor, 0.9f)));
    }
}
