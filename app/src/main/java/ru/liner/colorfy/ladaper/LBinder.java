package ru.liner.colorfy.ladaper;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import ru.liner.colorfy.core.WallpaperData;

@SuppressWarnings("ALL")
public abstract class LBinder<T>{
    private View itemView;
    private final SparseArray<View> viewSparseArray = new SparseArray<>();

    void setItemView(@NonNull View itemView) {
        this.itemView = itemView;
    }

    protected <V> V find(@IdRes int id) {
        View view = viewSparseArray.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            viewSparseArray.put(id, view);
        }
        return (V) view;
    }

    public Context getContext(){
        return itemView.getContext();
    }
    public View getItemView() {
        return itemView;
    }
    public abstract void init();
    public abstract void bind(@NonNull T model);
    public abstract int getDragDirections();
    public abstract int getSwipeDirections();
    public abstract void onChanged(@NonNull WallpaperData wallpaperData);
}
