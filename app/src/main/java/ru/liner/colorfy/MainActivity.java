package ru.liner.colorfy;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import ru.liner.colorfy.core.Colorfy;
import ru.liner.colorfy.core.ColorfyActivity;
import ru.liner.colorfy.dialogs.ColorfyDialog;
import ru.liner.colorfy.views.ColorfyButton;
import ru.liner.colorfy.views.ColorfyToast;

public class MainActivity extends ColorfyActivity {
    private ImageView wallpaperView;
    private ColorfyButton colorfyButton;

    private ColorfyButton alertDialogButton;
    private ColorfyButton addIndex;
    private ColorfyButton minusIndex;
    private ColorfyButton toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alertDialogButton = findViewById(R.id.alertDialogTest);
        wallpaperView = findViewById(R.id.wallpaperView);
        addIndex = findViewById(R.id.addIndex);
        minusIndex = findViewById(R.id.minusIndex);
        toast = findViewById(R.id.toast);
        colorfyButton = findViewById(R.id.autoColor);
        colorfyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.enableColorsSwitch = !Config.enableColorsSwitch;
                colorfyButton.setText(String.format("enableColorsSwitch: %s", Config.enableColorsSwitch));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    return;
                if (colorfy != null) {
                    colorfy.requestColors(true);
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            wallpaperView.setImageBitmap(Colorfy.getInstance(this).getWallpaper());


        alertDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(colorfy != null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Color-FY Alert");
                    alertDialog.setMessage("This is simple default alert dialog");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Close", (dialogInterface, i) -> dialogInterface.dismiss());
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialogInterface, i) -> {
                    });
                    alertDialog.show();
                    ColorfyDialog.apply(alertDialog, colorfy.getLastWallpaperData());
                }
            }
        });
        minusIndex.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                Config.colorIndex = Math.max(0, Config.colorIndex - 1);
                if (colorfy != null) {
                    colorfy.requestColors(true);
                }

            }
        });
        addIndex.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                Config.colorIndex = Math.min(Config.maxColorIndex, Config.colorIndex + 1);
                if (colorfy != null) {
                    colorfy.requestColors(true);
                }
            }
        });
        toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorfyToast.show(MainActivity.this, "Hello, im Color-fy toast!", Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onWallpaperChanged(@NonNull Bitmap bitmap, boolean isLiveWallpaper) {
        super.onWallpaperChanged(bitmap, isLiveWallpaper);
        wallpaperView.setImageBitmap(bitmap);
    }
}