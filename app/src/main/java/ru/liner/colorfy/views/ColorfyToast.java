package ru.liner.colorfy.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

import ru.liner.colorfy.R;
import ru.liner.colorfy.core.Colorfy;
import ru.liner.colorfy.core.WallpaperData;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 09.11.2022, среда
 **/
@SuppressLint("InflateParams")
public class ColorfyToast extends Toast {
    private final LinearLayout toastView;
    private final ColorfyTextView toastMessageView;


    private ColorfyToast(Context context) {
        super(context);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        toastView = (LinearLayout) layoutInflater.inflate(R.layout.transient_notification, null);
        toastMessageView = toastView.findViewById(R.id.toastMessage);
        setView(toastView);
    }

    public static void show(Context context, String message, int duration, WallpaperData wallpaperData) {
        ColorfyToast colorfyToast = new ColorfyToast(context);
        if (wallpaperData != null) {
            colorfyToast.toastView.setBackgroundTintList(ColorStateList.valueOf(wallpaperData.primaryColor));
            colorfyToast.toastMessageView.setTextColor(wallpaperData.textOnPrimaryColor);
        }
        colorfyToast.setDuration(duration);
        colorfyToast.toastMessageView.setText(message);
        colorfyToast.show();
    }

    public static void show(Context context, String message, int duration) {
        show(context, message, duration, Colorfy.getInstance(context).getLastWallpaperData());
    }
}
