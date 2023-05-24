package ru.liner.colorfy;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.UUID;

import ru.liner.colorfy.core.ColorfyActivity;
import ru.liner.colorfy.dialogs.ColorfyAlertDialog;
import ru.liner.colorfy.example.TestBinder;
import ru.liner.colorfy.ladaper.LAdapter;

public class MainActivity extends ColorfyActivity {
    private RecyclerView recyclerView;
    private LAdapter adapter;
    private Button buttonDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonDialog = findViewById(R.id.buttonDialog);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter = new LAdapter();
        adapter.register(R.layout.test_binder, String.class, TestBinder.class);
        recyclerView.setAdapter(adapter);
        for (int i = 0; i < 50; i++)
            adapter.add(UUID.randomUUID().toString());
        buttonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ColorfyAlertDialog.Builder(MainActivity.this)
                        .setTitle("Test dialog")
                        .setSingleChoiceItems(new CharSequence[]{"Test1", "Test2", "Test2", "Test2", "Test2", "Test2", "Test2", "Test2", "Test2", "Test2", "Test2", "Test2", "Test2", "Test2", "Test2", "Test2", "Test2", "Test2", "Test2", "Test2"}, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
    }
}