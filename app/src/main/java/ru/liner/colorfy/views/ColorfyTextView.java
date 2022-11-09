package ru.liner.colorfy.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.listener.IWallpaperDataListener;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 04.11.2022, пятница
 **/
public class ColorfyTextView extends AppCompatTextView implements IWallpaperDataListener {
    public ColorfyTextView(@NonNull Context context) {
        super(context);
    }

    public ColorfyTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorfyTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        if(isEnabled()) {
            setTextColor(wallpaperData.textColor);
            setLinkTextColor(wallpaperData.secondaryColor);
        } else {
            setTextColor(wallpaperData.disabledTextColor);
        }
    }
}
