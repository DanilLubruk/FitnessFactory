package com.example.fitnessfactory.utils.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.CurrentUserType;
import com.example.fitnessfactory.data.callbacks.StringCallback;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.activities.BaseActivity;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
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
            userTypes[myOwnGymIdx] = ResUtils.getString(R.string.caption_organisation_owner);
            int firstOtherGymOwner = 1;
            for (int i = firstOtherGymOwner; i < gymOwners.size(); i++) {
                userTypes[i] = gymOwners.get(i).getName();
            }

            new AlertDialog.Builder(context)
                    .setTitle(ResUtils.getString(R.string.title_ask_gym_owner))
                    .setItems(userTypes, ((dialog, which) -> {
                        handlePickedOption(which, gymOwners, emitter);
                    }))
                    .show();
        });
    }

    private static void handlePickedOption(int option,
                                           List<AppUser> gymOwners,
                                           CompletableEmitter emitter) {
        String ownerId = gymOwners.get(option).getId();
        AppPrefs.gymOwnerId().setValue(ownerId);

        if (!emitter.isDisposed()) {
            emitter.onComplete();
        }
    }

    public static Single<String> showSendEmailInvitationDialog(BaseActivity context) {
        return Single.create(emitter -> {
            RelativeLayout dialogView = (RelativeLayout)
                    context.getLayoutInflater().inflate(R.layout.admin_invitation_dialog_view, null);
            TextInputEditText edtEmail = dialogView.findViewById(R.id.edtEmail);

            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle(ResUtils.getString(R.string.title_invite_admin))
                    .setView(dialogView)
                    .setPositiveButton(ResUtils.getString(R.string.caption_send), ((dialog, which) -> {

                    }))
                    .setNegativeButton(ResUtils.getString(R.string.caption_cancel), ((dialog, which) -> {
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess("");
                        }
                    }))
                    .create();
            alertDialog.show();

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((view) -> {
                String email = edtEmail.getText().toString();
                handleEmailInput(email, alertDialog, emitter);
            });
        });
    }

    private static void handleEmailInput(String email, DialogInterface dialog, SingleEmitter<String> emitter) {
        if (!StringUtils.isEmpty(email)) {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(email);
                dialog.dismiss();
            }
        } else {
            GuiUtils.showMessage(ResUtils.getString(R.string.caption_blank_fields));
        }
    }
}
