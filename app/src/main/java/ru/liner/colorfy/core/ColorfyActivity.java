package ru.liner.colorfy.core;


import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import ru.liner.colorfy.Config;
import ru.liner.colorfy.listener.IWallpaperDataListener;
import ru.liner.colorfy.listener.IWallpaperListener;
import ru.liner.colorfy.utils.Utils;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 04.11.2022, пятница
 **/
public class ColorfyActivity extends AppCompatActivity implements IWallpaperDataListener, IWallpaperListener {
    @Nullable
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
    protected void onPause() {
        super.onPause();
        if (colorfy != null) {
            colorfy.removeWallpaperDataListener(this);
            colorfy.removeWallpaperListener(this);
        }
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        if (acceptColorChanged && colorfy != null) {
            WallpaperData wallpaperData = colorfy.getLastWallpaperData();
            if (wallpaperData == null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    colorfy.requestColors();
            } else {
                onChanged(wallpaperData);
            }
        }
    }

    @CallSuper
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(colorfy != null){
            WallpaperData wallpaperData = colorfy.getLastWallpaperData();
            if(wallpaperData != null && wallpaperData.isDarkTheme != Utils.isNightTheme(this)){
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    colorfy.requestColors();
            }
        }
    }

    @CallSuper
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        actionBar = getSupportActionBar();
        acceptColorChanged = rootView != null;
        if (acceptColorChanged && colorfy != null && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            colorfy.requestColors();
    }

    @CallSuper
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (colorfy == null)
            return;
        WallpaperData wallpaperData = colorfy.getLastWallpaperData();
        if (wallpaperData != null && Config.changeFragmentBackground) {
            ViewGroup fragmentView = (ViewGroup) fragment.getView();
            if (fragmentView != null && fragmentView.getChildCount() >= 1) {
                View fragmentRootView = fragmentView.getChildAt(0);
                fragmentRootView.setBackgroundColor(wallpaperData.backgroundColor);
            }
        }
    }

    @CallSuper
    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        runOnUiThread(() -> {
            if (ActivityCompat.checkSelfPermission(ColorfyActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                return;
            if (acceptColorChanged) {
                if (Config.changeActivityBackground)
                    rootView.setBackgroundColor(wallpaperData.backgroundColor);
                if (Config.changeNavigationBar)
                    window.setNavigationBarColor(wallpaperData.backgroundColor);
                if (Config.changeStatusBar)
                    window.setStatusBarColor(wallpaperData.secondaryColor);
                if (actionBar != null && Config.changeApplicationBar) {
                    actionBar.setBackgroundDrawable(new ColorDrawable(wallpaperData.primaryColor));
                    Spannable text = new SpannableString(actionBar.getTitle());
                    text.setSpan(new ForegroundColorSpan(wallpaperData.textOnPrimaryColor), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    actionBar.setTitle(text);
                }
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
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && colorfy != null)
                        colorfy.requestColors(true);
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @CallSuper
    public void setAcceptColorChanged(boolean acceptColorChanged) {
        this.acceptColorChanged = acceptColorChanged;
    }
}
