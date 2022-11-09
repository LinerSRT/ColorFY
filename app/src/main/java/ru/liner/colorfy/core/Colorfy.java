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
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.liner.colorfy.Config;
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
public class Colorfy implements WallpaperData.IGenerate {
    private static final String TAG = Colorfy.class.getSimpleName();
    private static Colorfy INSTANCE;
    private final Context context;
    private boolean wallpaperReceiverRegistered;
    private final WallpaperManager wallpaperManager;
    private final PackageManager packageManager;
    private final BroadcastReceiver wallpaperChangedReceiver;
    private final HashMap<String, IWallpaperListener> wallpaperChangedListeners;
    private final HashMap<String, IWallpaperDataListener> wallpaperDataListeners;
    @Nullable
    private WallpaperData currentWallpaperData;
    @Nullable
    private WallpaperData lastWallpaperData;

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
        this.wallpaperChangedListeners = new HashMap<>();
        this.wallpaperDataListeners = new HashMap<>();
        this.context = context;
        this.wallpaperManager = WallpaperManager.getInstance(context);
        this.packageManager = context.getPackageManager();
        this.wallpaperChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_WALLPAPER_CHANGED)) {
                    if (!Config.usesCustomColor) {
                        if (!wallpaperChangedListeners.isEmpty())
                            for (Map.Entry<String, IWallpaperListener> entry : wallpaperChangedListeners.entrySet())
                                entry.getValue().onWallpaperChanged(getWallpaper(), isWallpaperLive());
                        requestColors(true);
                    }
                }
            }
        };
        this.context.registerReceiver(wallpaperChangedReceiver, new IntentFilter(Intent.ACTION_WALLPAPER_CHANGED));
        this.wallpaperReceiverRegistered = true;
    }


    @Override
    public void onGenerated(WallpaperData wallpaperData) {
        if (wallpaperData != null) {
            if (currentWallpaperData != null && !currentWallpaperData.isSame(wallpaperData)) {
                if (lastWallpaperData == null || !lastWallpaperData.isSame(currentWallpaperData))
                    lastWallpaperData = currentWallpaperData;
                if (!wallpaperDataListeners.isEmpty())
                    for (Map.Entry<String, IWallpaperDataListener> entry : wallpaperDataListeners.entrySet())
                        entry.getValue().onChanged(currentWallpaperData);
            } else if (currentWallpaperData == null) {
                currentWallpaperData = wallpaperData;
                lastWallpaperData = currentWallpaperData;
                if (!wallpaperDataListeners.isEmpty())
                    for (Map.Entry<String, IWallpaperDataListener> entry : wallpaperDataListeners.entrySet())
                        entry.getValue().onChanged(currentWallpaperData);
            }
        } else if (currentWallpaperData != null) {
            if (!wallpaperDataListeners.isEmpty())
                for (Map.Entry<String, IWallpaperDataListener> entry : wallpaperDataListeners.entrySet())
                    entry.getValue().onChanged(currentWallpaperData);
        } else if(lastWallpaperData != null){
            if (!wallpaperDataListeners.isEmpty())
                for (Map.Entry<String, IWallpaperDataListener> entry : wallpaperDataListeners.entrySet())
                    entry.getValue().onChanged(currentWallpaperData);
        }
    }

    /**
     * Request colors from installed wallpaper from device
     *
     * @param force generate color even if wallpaper not changed, or its first request
     *              Requires READ_EXTERNAL_STORAGE permission!
     */
    @RequiresPermission(anyOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    public void requestColors(boolean force) {
        if (force) {
            currentWallpaperData = null;
            lastWallpaperData = null;
        }
        if (Config.usesCustomColor) {
            WallpaperData.fromColor(context, Config.customPrimaryColor, this);
        } else {
            WallpaperData.fromBitmap(context, getWallpaper(), this);
        }
    }


    /**
     * Get last known wallpaper data
     *
     * @return wallpaper data
     */
    @Nullable
    public WallpaperData getLastWallpaperData() {
        return lastWallpaperData;
    }

    @Nullable
    public WallpaperData getCurrentWallpaperData() {
        return currentWallpaperData;
    }

    /**
     * Request colors from installed wallpaper from device
     */
    @RequiresPermission(anyOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    public void requestColors() {
        requestColors(false);
    }

    /**
     * Call from anywhere to use own color for theming
     *
     * @param value true for enable
     */
    public void setUseCustomColor(boolean value) {
        Config.usesCustomColor = value;
        requestColors(true);
    }

    /**
     * Call from anywhere to use own color for theming
     *
     * @param value true for enable
     * @param color color for theming
     */
    public void setUseCustomColor(boolean value, @ColorInt int color) {
        Config.usesCustomColor = value;
        Config.customPrimaryColor = color;
        requestColors(true);
    }

    /**
     * Subscribe for wallpaper changes
     *
     * @param wallpaper interface
     */
    public void addWallpaperListener(@NonNull String className, @NonNull IWallpaperListener wallpaper) {
        if (!wallpaperReceiverRegistered) {
            context.registerReceiver(wallpaperChangedReceiver, new IntentFilter(Intent.ACTION_WALLPAPER_CHANGED));
            wallpaperReceiverRegistered = true;
        }
        if (containWallpaperListener(className))
            return;
        wallpaperChangedListeners.put(className, wallpaper);
    }

    /**
     * Subscribe for colors update
     *
     * @param wallpaperDataListener interface
     */
    public void addWallpaperDataListener(@NonNull String className, @NonNull IWallpaperDataListener wallpaperDataListener) {
        if (containWallpaperDataListener(className))
            return;
        wallpaperDataListeners.put(className, wallpaperDataListener);
    }

    /**
     * Detect registered listener or not
     *
     * @param className class name where listener was added
     * @return true if listener registered
     */
    public boolean containWallpaperDataListener(@NonNull String className) {
        for (Map.Entry<String, IWallpaperDataListener> entry : wallpaperDataListeners.entrySet())
            if (entry.getKey().equals(className))
                return true;
        return false;
    }


    /**
     * Detect registered listener or not
     *
     * @param className class name where listener was added
     * @return true if listener registered
     */
    public boolean containWallpaperListener(@NonNull String className) {
        for (Map.Entry<String, IWallpaperListener> entry : wallpaperChangedListeners.entrySet())
            if (entry.getKey().equals(className))
                return true;
        return false;
    }


    /**
     * Unsubscribe from wallpaper changes
     *
     * @param wallpaper interface
     */
    public void removeWallpaperListener(@NonNull String className, @NonNull IWallpaperListener wallpaper) {
        if (!containWallpaperListener(className))
            return;
        wallpaperChangedListeners.remove(className);
        if (wallpaperChangedListeners.isEmpty() && wallpaperReceiverRegistered) {
            context.unregisterReceiver(wallpaperChangedReceiver);
            wallpaperReceiverRegistered = false;
        }
    }

    /**
     * Unsubscribe from color changes
     *
     * @param wallpaperDataListener interface
     */
    public void removeWallpaperDataListener(@NonNull String className, @NonNull IWallpaperDataListener wallpaperDataListener) {
        if (!containWallpaperDataListener(className))
            return;
        wallpaperDataListeners.remove(className);
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
    @RequiresPermission(anyOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    public Bitmap getWallpaper() {
        return Utils.drawableToBitmap(isWallpaperLive() ? wallpaperManager.getWallpaperInfo().loadThumbnail(packageManager) : wallpaperManager.getDrawable());
    }

    public static void apply(@NonNull Button button, @NonNull WallpaperData wallpaperData) {
        button.setBackgroundTintList(ColorStateList.valueOf(wallpaperData.primaryColor));
        button.setTextColor(wallpaperData.textOnPrimaryColor);
    }

}
