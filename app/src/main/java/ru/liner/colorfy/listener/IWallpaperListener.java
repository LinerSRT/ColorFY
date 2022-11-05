package ru.liner.colorfy.listener;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public interface IWallpaperListener {
    void onWallpaperChanged(@NonNull Bitmap bitmap, boolean isLiveWallpaper);
}
