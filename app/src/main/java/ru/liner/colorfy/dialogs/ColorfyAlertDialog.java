package ru.liner.colorfy.dialogs;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import ru.liner.colorfy.core.Colorfy;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 24.05.2023, среда
 **/
public class ColorfyAlertDialog extends AlertDialog {
    protected ColorfyAlertDialog(@NonNull Context context) {
        this(context, 0);
    }

    protected ColorfyAlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ColorfyAlertDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder extends AlertDialog.Builder {

        public Builder(@NonNull Context context) {
            super(context);
        }

        public Builder(@NonNull Context context, int themeResId) {
            super(context, themeResId);
        }

        public AlertDialog show() {
            final AlertDialog dialog = create();
            dialog.show();
            ColorfyDialogUtils.apply(dialog, Colorfy.getInstance(getContext()).getCurrentWallpaperData());
            return dialog;
        }
    }
}
