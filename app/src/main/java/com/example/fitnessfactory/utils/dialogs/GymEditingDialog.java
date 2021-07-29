package com.example.fitnessfactory.utils.dialogs;

import android.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.callbacks.GymEditCallback;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.activities.BaseActivity;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.material.textfield.TextInputEditText;

import io.reactivex.Single;

public class GymEditingDialog {

    public static Single<Gym> showGymEditingDialog(BaseActivity context,
                                                   String title,
                                                   GymEditCallback callback,
                                                   Gym gym) {
        return Single.create(emitter -> {
            RelativeLayout dialogView = getDialogView(context);
            setGymTextFields(dialogView, gym);

            new AlertDialog
                    .Builder(context)
                    .setTitle(title)
                    .setView(dialogView)
                    .setCancelable(true)
                    .setPositiveButton(ResUtils.getString(R.string.caption_ok),
                            ((dialog1, which) -> {
                                Gym changedGym = getChangedGym(dialogView);

                                boolean isAnyFiledBlank =
                                        StringUtils.isEmpty(changedGym.getName()) ||
                                                StringUtils.isEmpty(changedGym.getAddress());

                                if (isAnyFiledBlank) {
                                    GuiUtils.showMessage(ResUtils.getString(R.string.caption_blank_fields));
                                } else {
                                    callback.changeGym(changedGym);
                                }
                            }))
                    .setNegativeButton(ResUtils.getString(R.string.caption_cancel), ((dialog1, which) -> {
                    }))
                    .show();
        });
    }

    private static RelativeLayout getDialogView(BaseActivity context) {
        return (RelativeLayout) context.getLayoutInflater().inflate(R.layout.edit_gym_dialog_view, null);
    }

    private static void setGymTextFields(RelativeLayout dialogView, Gym gym) {
        TextInputEditText edtName = dialogView.findViewById(R.id.edtName);
        TextInputEditText edtAddress = dialogView.findViewById(R.id.edtAddress);
        edtName.setText(gym.getName());
        edtAddress.setText(gym.getAddress());
    }

    private static Gym getChangedGym(View dialogView) {
        TextInputEditText edtName = dialogView.findViewById(R.id.edtName);
        TextInputEditText edtAddress = dialogView.findViewById(R.id.edtAddress);

        Gym editedGym = new Gym();
        editedGym.setName(edtName.getText().toString());
        editedGym.setAddress(edtAddress.getText().toString());

        return editedGym;
    }
}
