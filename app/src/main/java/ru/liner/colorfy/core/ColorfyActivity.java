package ru.liner.colorfy.core;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import ru.liner.colorfy.Config;
import ru.liner.colorfy.listener.IWallpaperDataListener;
import ru.liner.colorfy.listener.IWallpaperListener;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 04.11.2022, пятница
 **/
public class ColorfyActivity extends AppCompatActivity implements IWallpaperDataListener, IWallpaperListener {
    protected Colorfy colorfy;
    private Window window;
    private ActionBar actionBar;
    private ViewGroup rootView;
    private boolean acceptColorChanged;

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        colorfy = Colorfy.getInstance(this);
        colorfy.addWallpaperDataListener(this);
        colorfy.addWallpaperListener(this);
        window = getWindow();
        acceptColorChanged = false;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 4879);
    }

    @CallSuper
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        actionBar = getSupportActionBar();
        acceptColorChanged = rootView != null;
        if (acceptColorChanged)
            colorfy.requestColors();
    }

    @CallSuper
    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        runOnUiThread(() -> {
            if (acceptColorChanged) {
                if (Config.changeActivityBackground)
                    rootView.setBackgroundColor(wallpaperData.backgroundColor);
                if (Config.changeSystemBars) {
                    window.setNavigationBarColor(wallpaperData.backgroundColor);
                    window.setStatusBarColor(wallpaperData.secondaryColor);
                }
                if (actionBar != null) {
                    actionBar.setBackgroundDrawable(new ColorDrawable(wallpaperData.primaryColor));
                    Spannable text = new SpannableString(actionBar.getTitle());
                    text.setSpan(new ForegroundColorSpan(wallpaperData.textOnPrimaryColor), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    actionBar.setTitle(text);
                }
                if (ActivityCompat.checkSelfPermission(ColorfyActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    return;
                applyWallpaperData(rootView, wallpaperData);
            }
        });
    }

    @RequiresPermission(anyOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    private void applyWallpaperData(ViewGroup viewGroup, WallpaperData wallpaperData) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof IWallpaperDataListener) {
                if (child instanceof ViewGroup) {
                    applyWallpaperData((ViewGroup) child, wallpaperData);
                }
                ((IWallpaperDataListener) child).onChanged(wallpaperData);
            } else if (child instanceof ViewGroup) {
                applyWallpaperData((ViewGroup) child, wallpaperData);
            }
        }
    }

    @Override
    public void onWallpaperChanged(@NonNull Bitmap bitmap, boolean isLiveWallpaper) {

    }

    @CallSuper
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 4879) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        colorfy.requestColors(true);
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
