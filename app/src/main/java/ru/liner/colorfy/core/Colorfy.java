package ru.liner.colorfy.core;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.service.wallpaper.WallpaperService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import java.util.LinkedList;
import java.util.List;

import ru.liner.colorfy.listener.IWallpaperDataListener;
import ru.liner.colorfy.listener.IWallpaperListener;
import ru.liner.colorfy.utils.Utils;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 04.11.2022, пятница
 * <p>
 * Color-fy module for interact with Android wallpapers
 **/
@SuppressWarnings({"deprecation", "unused"})
@SuppressLint("MissingPermission,StaticFieldLeak")
public class Colorfy {
    private static Colorfy INSTANCE;
    private final Context context;
    private boolean wallpaperReceiverRegistered;
    private final WallpaperManager wallpaperManager;
    private final PackageManager packageManager;
    private final BroadcastReceiver wallpaperChangedReceiver;
    private final LinkedList<IWallpaperListener> wallpaperChangedListeners;
    private final LinkedList<IWallpaperDataListener> wallpaperDataListeners;
    @Nullable
    private WallpaperData currentWallpaperData;
    @Nullable
    private WallpaperData tempWallpaperData;

    /**
     * Get instance of Color-fy
     *
     * @param context ActivityContext or ApplicationContext
     * @return Color-fy
     */
    public static Colorfy getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new Colorfy(context);
        return INSTANCE;
    }

    /**
     * Private constructor, not accessible outside
     *
     * @param context ActivityContext or ApplicationContext
     */
    private Colorfy(Context context) {
        this.wallpaperChangedListeners = new LinkedList<>();
        this.wallpaperDataListeners = new LinkedList<>();
        this.context = context;
        this.wallpaperManager = WallpaperManager.getInstance(context);
        this.packageManager = context.getPackageManager();
        this.wallpaperChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_WALLPAPER_CHANGED)) {
                    boolean isLiveWallpaper = isWallpaperLive();
                    Bitmap bitmap = Utils.drawableToBitmap(isLiveWallpaper ? wallpaperManager.getWallpaperInfo().loadThumbnail(packageManager) : wallpaperManager.getDrawable());
                    for (IWallpaperListener iwallpaper : wallpaperChangedListeners)
                        iwallpaper.onWallpaperChanged(bitmap, isLiveWallpaper);
                    requestColors(true);
                }
            }
        };
        this.context.registerReceiver(wallpaperChangedReceiver, new IntentFilter(Intent.ACTION_WALLPAPER_CHANGED));
        this.wallpaperReceiverRegistered = true;
    }


    /**
     * Request colors from installed wallpaper from device
     *
     * @param force generate color even if wallpaper not changed, or its first request
     *              Requires READ_EXTERNAL_STORAGE permission!
     */
    @RequiresPermission(anyOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    public void requestColors(boolean force) {
        Bitmap bitmap = getWallpaper();
        for (IWallpaperListener iwallpaper : wallpaperChangedListeners)
            iwallpaper.onWallpaperChanged(bitmap, isWallpaperLive());
        WallpaperData.from(context, bitmap, wallpaperData -> {
            if (currentWallpaperData == null || force) {
                currentWallpaperData = wallpaperData;
                for (IWallpaperDataListener wallpaperDataListener : wallpaperDataListeners)
                    wallpaperDataListener.onChanged(wallpaperData);
            } else if (!currentWallpaperData.isSame(wallpaperData))
                for (IWallpaperDataListener wallpaperDataListener : wallpaperDataListeners)
                    wallpaperDataListener.onChanged(wallpaperData);
            tempWallpaperData = wallpaperData;
        });
    }

    /**
     * Return temporary wallpaper data;
     * @return data
     */
    @Nullable
    public WallpaperData getTempWallpaperData() {
        return tempWallpaperData;
    }

    /**
     * Request colors from installed wallpaper from device
     */
    public void requestColors() {
        requestColors(false);
    }

    /**
     * Subscribe for wallpaper changes
     *
     * @param wallpaper interface
     */
    public void addWallpaperListener(@NonNull IWallpaperListener wallpaper) {
        if (!wallpaperReceiverRegistered) {
            context.registerReceiver(wallpaperChangedReceiver, new IntentFilter(Intent.ACTION_WALLPAPER_CHANGED));
            wallpaperReceiverRegistered = true;
        }
        wallpaperChangedListeners.add(wallpaper);
    }

    /**
     * Subscribe for colors update
     *
     * @param wallpaperDataListener interface
     */
    public void addWallpaperDataListener(@NonNull IWallpaperDataListener wallpaperDataListener) {
        wallpaperDataListeners.add(wallpaperDataListener);
    }

    /**
     * Unsubscribe from wallpaper changes
     *
     * @param wallpaper interface
     */
    public void removeWallpaperListener(@NonNull IWallpaperListener wallpaper) {
        wallpaperChangedListeners.remove(wallpaper);
        if (wallpaperChangedListeners.isEmpty()) {
            context.unregisterReceiver(wallpaperChangedReceiver);
            wallpaperReceiverRegistered = false;
        }
    }

    /**
     * Unsubscribe from color changes
     *
     * @param wallpaperDataListener interface
     */
    public void removeWallpaperDataListener(@NonNull IWallpaperDataListener wallpaperDataListener) {
        wallpaperDataListeners.remove(wallpaperDataListener);
    }

    /**
     * Determinate current wallpaper is live or not
     *
     * @return true if wallpaper is live
     */
    private boolean isWallpaperLive() {
        WallpaperInfo wallpaperInfo = wallpaperManager.getWallpaperInfo();
        if (packageManager == null || wallpaperInfo == null)
            return false;
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentServices(new Intent(WallpaperService.SERVICE_INTERFACE), PackageManager.GET_META_DATA);
        for (ResolveInfo resolveInfo : resolveInfoList)
            return wallpaperInfo.getServiceName().equals(resolveInfo.serviceInfo.name);
        return false;
    }

    /**
     * Get current wallpaper
     *
     * @return Thumbnail if current wallpaper is live, bitmap other cases
     */
    private Bitmap getWallpaper() {
        return Utils.drawableToBitmap(isWallpaperLive() ? wallpaperManager.getWallpaperInfo().loadThumbnail(packageManager) : wallpaperManager.getDrawable());
    }
}
