package ru.liner.colorfy;

import android.app.Application;

import ru.liner.colorfy.core.Colorfy;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 08.11.2022, вторник
 **/
public class ExampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Colorfy.getInstance(this);
    }
}
