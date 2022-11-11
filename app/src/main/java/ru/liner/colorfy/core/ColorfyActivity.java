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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.liner.colorfy.Config;
import ru.liner.colorfy.listener.IWallpaperDataListener;
import ru.liner.colorfy.listener.IWallpaperListener;
import ru.liner.colorfy.utils.Utils;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 04.11.2022, пятница
 **/
@SuppressWarnings("unused")
public class ColorfyActivity extends AppCompatActivity implements IWallpaperDataListener, IWallpaperListener {
    @Nullable
    protected Colorfy colorfy;
    private Window window;
    private ActionBar actionBar;
    private ViewGroup rootView;
    private boolean acceptColorChanged;
    private List<RecyclerViewListener> recyclerViewListenerPool;

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        recyclerViewListenerPool = new ArrayList<>();
        colorfy = Colorfy.getInstance(this);
        colorfy.addWallpaperDataListener(getClass().getSimpleName(), this);
        colorfy.addWallpaperListener(getClass().getSimpleName(), this);
        window = getWindow();
        acceptColorChanged = false;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 4879);
    }

    @CallSuper
    @Override
    protected void onPause() {
        super.onPause();
        if (colorfy != null && Config.automaticListenersLifecycle) {
            colorfy.removeWallpaperDataListener(getClass().getSimpleName(), this);
            colorfy.removeWallpaperListener(getClass().getSimpleName(), this);
        }
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        acceptColorChanged = rootView != null;
        if (colorfy != null) {
            if (Config.automaticListenersLifecycle) {
                colorfy.addWallpaperDataListener(getClass().getSimpleName(), this);
                colorfy.addWallpaperListener(getClass().getSimpleName(), this);
            }
            WallpaperData wallpaperData = colorfy.getLastWallpaperData();
            if (wallpaperData == null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    colorfy.requestColors(true);
            } else {
                onChanged(wallpaperData);
            }
        }
    }

    @CallSuper
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (colorfy != null) {
            WallpaperData wallpaperData = colorfy.getLastWallpaperData();
            if (wallpaperData != null && wallpaperData.isDarkTheme != Utils.isNightTheme(this)) {
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
        if (wallpaperData == null || ActivityCompat.checkSelfPermission(ColorfyActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            return;
        if (fragment instanceof IWallpaperDataListener) {
            ((IWallpaperDataListener) fragment).onChanged(wallpaperData);
        }
        if (Config.changeFragmentBackground) {
            ViewGroup fragmentView = (ViewGroup) fragment.getView();
            if (fragmentView != null) {
                applyWallpaperData(fragmentView, wallpaperData);
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

    private void processRecyclerView(RecyclerView recyclerView) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || colorfy == null)
            return;
        WallpaperData wallpaperData = colorfy.getCurrentWallpaperData(); if (wallpaperData == null)
            return;
        applyWallpaperData(recyclerView, wallpaperData);
        String hashCode = recyclerView.getClass().getSimpleName() + recyclerView.getId();
        for (RecyclerViewListener item : recyclerViewListenerPool)
            if (item.getHashCode().equals(hashCode))
                return;
        RecyclerViewListener listener = new RecyclerViewListener(hashCode) {
            @Override
            public void requestColors(RecyclerView recyclerView) {
                applyWallpaperData(recyclerView, wallpaperData);
            }
        };
        recyclerViewListenerPool.add(listener);
        recyclerView.addOnScrollListener(listener);
    }

    private void applyWallpaperData(@NonNull RecyclerView recyclerView, @NonNull WallpaperData wallpaperData) {
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            if (holder instanceof IWallpaperDataListener)
                ((IWallpaperDataListener) holder).onChanged(wallpaperData);
        }
    }

    @RequiresPermission(anyOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    private void applyWallpaperData(@NonNull ViewGroup viewGroup, WallpaperData wallpaperData) {
        if (wallpaperData == null)
            return;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof IWallpaperDataListener) {
                if (child instanceof ViewGroup)
                    applyWallpaperData((ViewGroup) child, wallpaperData);
                ((IWallpaperDataListener) child).onChanged(wallpaperData);
            } else if (child instanceof RecyclerView) {
                processRecyclerView((RecyclerView) child);
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
}
