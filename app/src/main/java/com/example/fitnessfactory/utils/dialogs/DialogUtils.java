package com.example.fitnessfactory.utils.dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.CurrentUserType;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.activities.BaseActivity;
import com.example.fitnessfactory.ui.components.filters.ValueCheckers.EmailValueChecker;
import com.example.fitnessfactory.ui.components.filters.ValueCheckers.NoConditionChecker;
import com.example.fitnessfactory.ui.components.filters.ValueCheckers.ValueChecker;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.example.fitnessfactory.utils.dialogs.exceptions.DialogCancelledException;
import com.google.android.material.textfield.TextInputEditText;
import com.tiromansev.prefswrapper.typedprefs.BooleanPreference;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public class DialogUtils {

    public static <ItemType> Single<ItemType> showOptionPickerDialog(BaseActivity context,
                                                                     String title,
                                                                     List<ItemType> items) {
        return Single.create(emitter -> {
            ArrayAdapter<ItemType> adapter =
                    new ArrayAdapter<ItemType>(context, R.layout.support_simple_spinner_dropdown_item, items);

            new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setSingleChoiceItems(
                            adapter,
                            0,
                            (dialog, position) -> emitter.onSuccess(items.get(position)))
                    .show();
        });
    }

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

    public static Single<Integer> showAskOwnerDialog(BaseActivity context,
                                                     List<AppUser> gymOwners) {
        return Single.create(emitter -> {
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
                                                SingleEmitter<Integer> emitter) {
        String ownerId = gymOwners.get(option).getId();
        AppPrefs.gymOwnerId().setValue(ownerId);

        boolean isOptionMyOwnGym = option == 0;

        if (!emitter.isDisposed()) {
            emitter.onSuccess(
                    isOptionMyOwnGym ?
                            CurrentUserType.CURRENT_USER_OWNER :
                            CurrentUserType.CURRENT_USER_STAFF);
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
                ResUtils.getString(R.string.caption_cancel),
                EmailValueChecker.getInstance());
    }

    public static Single<String> showOneLineEditDialog(BaseActivity context,
                                                       String title,
                                                       String hint,
                                                       String okCaption,
                                                       String cancelCaption) {
        return showOneLineEditDialog(
                context,
                title,
                hint,
                okCaption,
                cancelCaption,
                NoConditionChecker.getInstance());
    }

    private static Single<String> showOneLineEditDialog(BaseActivity context,
                                                        String title,
                                                        String hint,
                                                        String okCaption,
                                                        String cancelCaption,
                                                        ValueChecker valueChecker) {
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
                if (!valueChecker.isValueValid(value)) {
                    GuiUtils.showMessage(valueChecker.getErrorMessage());
                } else {
                    handleInput(value.trim(), alertDialog, emitter);
                }
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

    public static Single<Date> showDateSelectDialog(BaseActivity context,
                                                    Date date) {
        return Single.create(emitter -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());

            DatePickerDialog dialog = new DatePickerDialog(context,
                    (view, year, month, dayOfMonth) -> {
                        if (!emitter.isDisposed()) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            emitter.onSuccess(calendar.getTime());
                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });
    }

    public static Single<Date> showTimePickerDialog(BaseActivity context, Date date) {
        return Single.create(emitter -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            new TimePickerDialog(
                    context,
                    ((view, hourOfDay, minute) -> {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(calendar.getTime());
                        }
                    }),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true)
                    .show();
        });
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
