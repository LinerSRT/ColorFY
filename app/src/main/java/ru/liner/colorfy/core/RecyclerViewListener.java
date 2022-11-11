package ru.liner.colorfy.core;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 11.11.2022, пятница
 **/
public abstract class RecyclerViewListener extends RecyclerView.OnScrollListener {
    private final String hashCode;

    public RecyclerViewListener(String hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        requestColors(recyclerView);
    }

    public abstract void requestColors(RecyclerView recyclerView);

    public String getHashCode() {
        return hashCode;
    }

    public static RecyclerViewListener attach(RecyclerView recyclerView) {
        return new RecyclerViewListener(recyclerView.getClass().getSimpleName() + recyclerView.hashCode()) {
            @Override
            public void requestColors(RecyclerView recyclerView) {
                Context context = recyclerView.getContext();
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    return;
                Colorfy.getInstance(context).requestColors();
            }
        };
    }
}
