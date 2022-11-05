package ru.liner.colorfy.listener;

import androidx.annotation.NonNull;

import ru.liner.colorfy.core.WallpaperData;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 04.11.2022, пятница
 **/
public interface IWallpaperDataListener {
    void onChanged(@NonNull WallpaperData wallpaperData);
}
