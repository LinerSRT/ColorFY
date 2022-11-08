package ru.liner.colorfy;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import ru.liner.colorfy.core.Colorfy;
import ru.liner.colorfy.core.ColorfyActivity;
import ru.liner.colorfy.views.ColorfyButton;

public class MainActivity extends ColorfyActivity {
    private ImageView wallpaperView;
    private ColorfyButton colorfyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wallpaperView = findViewById(R.id.wallpaperView);
        colorfyButton = findViewById(R.id.autoColor);
        colorfyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.usesAutomaticSwatchFiltering = !Config.usesAutomaticSwatchFiltering;
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    return;
                if (colorfy != null) {
                    colorfy.requestColors(true);
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            wallpaperView.setImageBitmap(Colorfy.getInstance(this).getWallpaper());

    }

    @Override
    public void onWallpaperChanged(@NonNull Bitmap bitmap, boolean isLiveWallpaper) {
        super.onWallpaperChanged(bitmap, isLiveWallpaper);
        wallpaperView.setImageBitmap(bitmap);
    }
}