package ru.liner.colorfy;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import ru.liner.colorfy.core.Colorfy;
import ru.liner.colorfy.core.ColorfyActivity;

public class MainActivity extends ColorfyActivity {
    private ImageView wallpaperView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wallpaperView = findViewById(R.id.wallpaperView);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            wallpaperView.setImageBitmap(Colorfy.getInstance(this).getWallpaper());

    }

    @Override
    public void onWallpaperChanged(@NonNull Bitmap bitmap, boolean isLiveWallpaper) {
        super.onWallpaperChanged(bitmap, isLiveWallpaper);
        wallpaperView.setImageBitmap(bitmap);
    }
}