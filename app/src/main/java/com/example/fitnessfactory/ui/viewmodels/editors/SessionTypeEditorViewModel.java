package com.example.fitnessfactory.ui.viewmodels.editors;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.utils.ResUtils;

public class SessionTypeEditorViewModel extends EditorViewModel {



    @Override
    public SingleLiveEvent<Boolean> isModified() {
        return null;
    }

    @Override
    public SingleLiveEvent<Boolean> save() {
        return null;
    }

    @Override
    public SingleLiveEvent<Boolean> delete() {
        return null;
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak().concat(ResUtils.getString(R.string.message_session_type_null));
    }
}
