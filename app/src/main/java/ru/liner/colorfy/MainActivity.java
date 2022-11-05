package ru.liner.colorfy;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import ru.liner.colorfy.core.Colorfy;
import ru.liner.colorfy.core.ColorfyActivity;

public class MainActivity extends ColorfyActivity {
    private Colorfy colorfy;
    private ImageView wallpaperView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colorfy = Colorfy.getInstance(this);
        wallpaperView = findViewById(R.id.wallpaperView);
    }

    @Override
    public void onWallpaperChanged(@NonNull Bitmap bitmap, boolean isLiveWallpaper) {
        super.onWallpaperChanged(bitmap, isLiveWallpaper);
        wallpaperView.setImageBitmap(bitmap);
    }
}