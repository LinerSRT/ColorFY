package ru.liner.colorfy.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.TintInfo;
import androidx.core.graphics.drawable.WrappedDrawable;

import ru.liner.colorfy.core.Colorfy;
import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.listener.IWallpaperDataListener;
import ru.liner.colorfy.utils.ColorUtils;
import ru.liner.colorfy.utils.Reflect;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 04.11.2022, пятница
 **/
public class ColorfyToggleButton extends AppCompatToggleButton implements IWallpaperDataListener {
    private WallpaperData wallpaperData;
    public ColorfyToggleButton(@NonNull Context context) {
        super(context);
    }

    public ColorfyToggleButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorfyToggleButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(wallpaperData != null) {
                setBackgroundTintList(ColorStateList.valueOf(!isChecked() ? wallpaperData.primaryColor : ColorUtils.lightenColor(wallpaperData.backgroundColor, 0.9f)));
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setChecked(boolean checked) {
        if(wallpaperData != null) {
            setBackgroundTintList(ColorStateList.valueOf(!isChecked() ? wallpaperData.primaryColor : ColorUtils.lightenColor(wallpaperData.backgroundColor, 0.9f)));
        }
        super.setChecked(checked);
    }

    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        this.wallpaperData = wallpaperData;
        setBackgroundTintList(ColorStateList.valueOf(!isChecked() ? wallpaperData.primaryColor : ColorUtils.lightenColor(wallpaperData.backgroundColor, 0.9f)));
        setTextColor(wallpaperData.textOnPrimaryColor);
    }

}
