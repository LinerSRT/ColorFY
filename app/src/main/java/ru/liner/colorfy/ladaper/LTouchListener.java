package ru.liner.colorfy.ladaper;


import android.view.DragEvent;

import androidx.annotation.NonNull;

public interface LTouchListener<R extends LBinder<T>, T> {
    void onTouch(@NonNull R binder, @NonNull T model);
    void onLongTouch(@NonNull R binder, @NonNull T model);
    void onDrag(@NonNull R binder, DragEvent event, @NonNull T model);
}
