package ru.liner.colorfy;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import ru.liner.colorfy.core.Colorfy;
import ru.liner.colorfy.core.ColorfyActivity;
import ru.liner.colorfy.dialogs.ColorfyDialog;
import ru.liner.colorfy.views.ColorfyButton;

public class MainActivity extends ColorfyActivity {
    private ImageView wallpaperView;
    private ColorfyButton colorfyButton;
    private ColorfyButton colorfyButton1;

    private ColorfyButton alertDialogButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alertDialogButton = findViewById(R.id.alertDialogTest);
        wallpaperView = findViewById(R.id.wallpaperView);
        colorfyButton = findViewById(R.id.autoColor);
        colorfyButton1 = findViewById(R.id.autoLuminanceColor);
        colorfyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.usesAutomaticSwatchFiltering = !Config.usesAutomaticSwatchFiltering;
                colorfyButton.setText(String.format("Switch automatic color: %s", Config.usesAutomaticSwatchFiltering));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    return;
                if (colorfy != null) {
                    colorfy.requestColors(true);
                }
            }
        });
        colorfyButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.usesMostLuminanceDetectedSwatch = !Config.usesMostLuminanceDetectedSwatch;
                colorfyButton1.setText(String.format("Switch automatic luminance: %s", Config.usesMostLuminanceDetectedSwatch));
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
    }

    @Override
    public void onWallpaperChanged(@NonNull Bitmap bitmap, boolean isLiveWallpaper) {
        super.onWallpaperChanged(bitmap, isLiveWallpaper);
        wallpaperView.setImageBitmap(bitmap);
    }
}