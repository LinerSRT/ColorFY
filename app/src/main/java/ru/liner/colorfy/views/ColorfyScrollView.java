package ru.liner.colorfy.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import ru.liner.colorfy.R;
import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.listener.IWallpaperDataListener;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 05.11.2022, суббота
 **/
public class ColorfyScrollView extends ScrollView implements IWallpaperDataListener {
    public ColorfyScrollView(Context context) {
        super(context);
    }

    public ColorfyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorfyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ColorfyScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.scrollbar);
        Objects.requireNonNull(drawable).setColorFilter(new PorterDuffColorFilter(wallpaperData.primaryColor, PorterDuff.Mode.SRC_IN));
        if (isVerticalScrollBarEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setVerticalScrollbarThumbDrawable(drawable);
            } else {
                setVerticalThumbDrawable(drawable);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setHorizontalScrollbarThumbDrawable(drawable);
            } else {
                setHorizontalThumbDrawable(drawable);
            }
        }
    }

    @SuppressWarnings("JavaReflectionMemberAccess")
    @SuppressLint("DiscouragedPrivateApi")
    public void setVerticalThumbDrawable(Drawable drawable) {
        try {
            Field scrollCacheField = View.class.getDeclaredField("mScrollCache");
            scrollCacheField.setAccessible(true);
            Object scrollCache = scrollCacheField.get(this);
            Field scrollBarField = Objects.requireNonNull(scrollCache).getClass().getDeclaredField("scrollBar");
            scrollBarField.setAccessible(true);
            Object scrollbar = scrollBarField.get(scrollCache);
            Method setVerticalThumbDrawable = Objects.requireNonNull(scrollbar).getClass().getDeclaredMethod("setVerticalThumbDrawable", Drawable.class);
            setVerticalThumbDrawable.invoke(scrollbar, drawable);
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("JavaReflectionMemberAccess")
    @SuppressLint("DiscouragedPrivateApi")
    public void setHorizontalThumbDrawable(Drawable drawable) {
        try {
            Field scrollCacheField = View.class.getDeclaredField("mScrollCache");
            scrollCacheField.setAccessible(true);
            Object scrollCache = scrollCacheField.get(this);
            Field scrollBarField = Objects.requireNonNull(scrollCache).getClass().getDeclaredField("scrollBar");
            scrollBarField.setAccessible(true);
            Object scrollbar = scrollBarField.get(scrollCache);
            Method setVerticalThumbDrawable = Objects.requireNonNull(scrollbar).getClass().getDeclaredMethod("setHorizontalThumbDrawable", Drawable.class);
            setVerticalThumbDrawable.invoke(scrollbar, drawable);
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
