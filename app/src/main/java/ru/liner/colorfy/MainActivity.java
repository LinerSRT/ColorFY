package ru.liner.colorfy;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.UUID;

import ru.liner.colorfy.core.Colorfy;
import ru.liner.colorfy.core.ColorfyActivity;
import ru.liner.colorfy.dialogs.ColorfyDialog;
import ru.liner.colorfy.example.TestBinder;
import ru.liner.colorfy.ladaper.LAdapter;
import ru.liner.colorfy.views.ColorfyButton;
import ru.liner.colorfy.views.ColorfyToast;

public class MainActivity extends ColorfyActivity {
    private RecyclerView recyclerView;
    private LAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter = new LAdapter();
        adapter.register(R.layout.test_binder, String.class, TestBinder.class);
        recyclerView.setAdapter(adapter);
        for (int i = 0; i < 50; i++)
            adapter.add(UUID.randomUUID().toString());
    }
}