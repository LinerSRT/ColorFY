package ru.liner.colorfy.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.listener.IWallpaperDataListener;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 24.05.2023, среда
 **/
public abstract class ColorfyDialog implements DialogInterface.OnShowListener, DialogInterface.OnDismissListener, IWallpaperDataListener {
    protected Context context;
    protected View contentView;
    protected Dialog dialog;
    protected Window window;

    public ColorfyDialog(Context context) {
        this.context = context;
        this.contentView = LayoutInflater.from(context).inflate(getContentViewLayout(), null, false);
        this.dialog = new Dialog(context, getDialogTheme());
        this.dialog.setContentView(contentView);
        this.window = dialog.getWindow();
        this.window.setBackgroundDrawableResource(android.R.color.transparent);
        this.window.requestFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * Provide layout resource id, for creating dialog window
     *
     * @return none
     */
    @LayoutRes
    public abstract int getContentViewLayout();

    /**
     * Provide theme resource id, for styling dialog window. Maybe 0
     *
     * @return none
     */
    @StyleRes
    public abstract int getDialogTheme();


    /**
     * Show dialog
     */
    public void show() {
        dialog.show();
    }

    /**
     * Hide dialog
     */
    public void hide() {
        dialog.hide();
    }

    /**
     * Dismiss dialog
     */
    public void dismiss() {
        dialog.dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }

    @Override
    @CallSuper
    public void onShow(DialogInterface dialogInterface) {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        window.getDecorView().setSystemUiVisibility(uiOptions);
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {

    }
}
