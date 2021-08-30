package com.example.fitnessfactory.system;

import android.util.Log;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.concurrent.atomic.AtomicReference;

public class SafeReference<T> extends AtomicReference<T> {

    public T getValue() throws Exception {
        if (super.get() == null) {
            Log.d(AppConsts.DEBUG_TAG, "Atomic reference is null");
            throw new Exception(ResUtils.getString(R.string.message_error_data_operate));
        }

        return super.get();
    }
}
