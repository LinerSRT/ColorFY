package ru.liner.colorfy.example;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Random;

import ru.liner.colorfy.R;
import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.ladaper.LBinder;
import ru.liner.colorfy.views.ColorfyTextView;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 11.11.2022, пятница
 **/
public class TestBinder extends LBinder<String> {
    private Random random;
    private ColorfyTextView textView;
    private TextView androidTextView;
    private View binderView;

    @Override
    public void init() {
        random = new Random();
        textView = find(R.id.binder_colorfy_text);
        androidTextView = find(R.id.binder_android_text);
        binderView = find(R.id.binder_view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bind(@NonNull String model) {
        textView.setText("Color-FY Text #"+random.nextInt(1000));
        androidTextView.setText("Android Text #"+random.nextInt(1000));
    }

    @Override
    public int getDragDirections() {
        return 0;
    }

    @Override
    public int getSwipeDirections() {
        return 0;
    }

    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        binderView.setBackgroundTintList(ColorStateList.valueOf(wallpaperData.primaryColor));
    }
}
