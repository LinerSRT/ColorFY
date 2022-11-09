package ru.liner.colorfy.core;

import androidx.annotation.ColorInt;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 09.11.2022, среда
 **/
public class ColorVariant {
    public int index;
    @ColorInt
    public int color;

    public ColorVariant(int index, int color) {
        this.index = index;
        this.color = color;
    }
}
