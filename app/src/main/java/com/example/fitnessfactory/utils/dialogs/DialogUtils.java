package com.example.fitnessfactory.utils.dialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.CurrentUserType;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.activities.BaseActivity;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.example.fitnessfactory.utils.dialogs.exceptions.DialogCancelledException;
import com.google.android.material.textfield.TextInputEditText;
import com.tiromansev.prefswrapper.typedprefs.BooleanPreference;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public class DialogUtils {

    public static Single<Boolean> showAskDialog(BaseActivity context,
                                                String message,
                                                String okCaption,
                                                String cancelCaption) {
        return Single.create(emitter -> {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.caption_warning_title)
                    .setMessage(message)
                    .setPositiveButton(okCaption, ((dialog, which) -> {
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(true);
                        }
                    }))
                    .setNegativeButton(cancelCaption, ((dialog, which) -> {
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(false);
                        }
                    }))
                    .show();
        });
    }

    public static Completable showAskOwnerDialog(BaseActivity context,
                                                 List<AppUser> gymOwners) {
        return Completable.create(emitter -> {
            String[] userTypes = new String[gymOwners.size()];
            int myOwnGymIdx = 0;
            userTypes[myOwnGymIdx] = ResUtils.getString(R.string.caption_my_own_gym);
            int firstOtherGymOwner = 1;
            for (int i = firstOtherGymOwner; i < gymOwners.size(); i++) {
                userTypes[i] = gymOwners.get(i).getName();
            }

            new AlertDialog.Builder(context)
                    .setTitle(ResUtils.getString(R.string.title_ask_gym_owner))
                    .setItems(userTypes, ((dialog, which) -> {
                        handlePickedOwnerOption(which, gymOwners, emitter);
                    }))
                    .setOnCancelListener(dialog -> emitter.onError(new Exception(ResUtils.getString(R.string.caption_wrong_auth))))
                    .show();
        });
    }

    private static void handlePickedOwnerOption(int option,
                                                List<AppUser> gymOwners,
                                                CompletableEmitter emitter) {
        String ownerId = gymOwners.get(option).getId();
        AppPrefs.gymOwnerId().setValue(ownerId);

        boolean isOptionMyOwnGym = option == 0;
        AppPrefs.currentUserType().setValue(
                isOptionMyOwnGym ?
                        CurrentUserType.CURRENT_USER_OWNER :
                        CurrentUserType.CURRENT_USER_STAFF);

        if (!emitter.isDisposed()) {
            emitter.onComplete();
        }
    }

    public static Single<Boolean> showAskNoMoreDialog(BaseActivity context,
                                                      String message,
                                                      BooleanPreference doAskPreference) {
        return Single.create(emitter -> {
            LinearLayout dialogView =
                    (LinearLayout) context.getLayoutInflater().inflate(R.layout.ask_no_more_dialog_view, null);
            CheckBox cbAskNoMore = dialogView.findViewById(R.id.cbAskNoMore);

            new AlertDialog.Builder(context)
                    .setTitle(R.string.title_question)
                    .setMessage(message)
                    .setView(dialogView)
                    .setPositiveButton(R.string.caption_yes_button, (dialog, which) -> {
                        doAskPreference.setValue(!cbAskNoMore.isChecked());
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(true);
                        }
                    })
                    .setNegativeButton(R.string.caption_no_button, (dialog, which) -> {
                        doAskPreference.setValue(!cbAskNoMore.isChecked());
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(false);
                        }
                    })
                    .show();
        });
    }

    public static Single<String> getAskEmailDialog(BaseActivity context) {
        return DialogUtils.showOneLineEditDialog(
                context,
                ResUtils.getString(R.string.title_invite_personnel),
                ResUtils.getString(R.string.caption_email),
                ResUtils.getString(R.string.caption_send),
                ResUtils.getString(R.string.caption_cancel));
    }

    public static Single<String> showOneLineEditDialog(BaseActivity context,
                                                       String title,
                                                       String hint,
                                                       String okCaption,
                                                       String cancelCaption) {
        return Single.create(emitter -> {
            RelativeLayout dialogView =
                    getOneLineDialogViewBuilder()
                    .setHint(hint)
                    .build(context);

            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setView(dialogView)
                    .setPositiveButton(okCaption, ((dialog, which) -> {

                    }))
                    .setNegativeButton(cancelCaption, ((dialog, which) -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(new DialogCancelledException());
                        }
                    }))
                    .create();
            alertDialog.show();

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((view) -> {
                TextInputEditText edtField = dialogView.findViewById(R.id.edtField);
                String value = edtField.getText() != null ? edtField.getText().toString() : "";
                handleInput(value.trim(), alertDialog, emitter);
            });
        });
    }

    private static void handleInput(String value, DialogInterface dialog, SingleEmitter<String> emitter) {
        if (!StringUtils.isEmpty(value)) {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(value);
                dialog.dismiss();
            }
        } else {
            GuiUtils.showMessage(ResUtils.getString(R.string.caption_blank_fields));
        }
    }

    private static OneLineDialogViewBuilder getOneLineDialogViewBuilder() {
        return new OneLineDialogViewBuilder();
    }

    private static class OneLineDialogViewBuilder {

        private String hint;

        public OneLineDialogViewBuilder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        public RelativeLayout build(BaseActivity context) {
            RelativeLayout dialogView = (RelativeLayout)
                    context.getLayoutInflater().inflate(R.layout.one_line_edit__dialog_view, null);
            TextInputEditText edtField = dialogView.findViewById(R.id.edtField);
            edtField.setHint(hint);

            return dialogView;
        }
    }
}
