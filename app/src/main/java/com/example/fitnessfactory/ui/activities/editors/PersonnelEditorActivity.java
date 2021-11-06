package com.example.fitnessfactory.ui.activities.editors;

import android.view.Menu;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.events.PersonnelEmailUpdateEvent;
import com.example.fitnessfactory.databinding.ActivityPersonnelEditorBinding;
import com.example.fitnessfactory.ui.viewmodels.editors.PersonnelEditorViewModel;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.material.tabs.TabLayoutMediator;

import org.greenrobot.eventbus.EventBus;

public abstract class PersonnelEditorActivity extends TabParentEditorActivity<PersonnelEmailUpdateEvent> {

    protected ActivityPersonnelEditorBinding binding;

    @Override
    protected abstract PersonnelEditorViewModel getViewModel();

    protected abstract FragmentStateAdapter getPageAdapter();

    @Override
    public Toolbar getToolbar() {
        return binding.toolbar;
    }

    @Override
    public void initActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personnel_editor);
        super.initActivity();
        binding.setModel(getViewModel());
        getViewModel().setPersonnelData(getIntent());
        subscribeForPersonnelEmailChangesForTabs();
        binding.container.vpGyms.setAdapter(getPageAdapter());
        new TabLayoutMediator(binding.container.tlGyms, binding.container.vpGyms,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(ResUtils.getString(R.string.title_gyms));
                            break;
                    }
                }
        ).attach();
        binding.container.vpGyms.setUserInputEnabled(false);
    }

    private void subscribeForPersonnelEmailChangesForTabs() {
        getViewModel().getPersonnelEmail()
                .observe(this, personnelEmail -> EventBus.getDefault().postSticky(new PersonnelEmailUpdateEvent(personnelEmail)));
    }

    @Override
    protected boolean isNewEntity() {
        return false;
    }

    @Override
    protected void initEntityKey() { }

    @Override
    protected boolean isDataValid() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean isCreated = super.onCreateOptionsMenu(menu);
        menu.removeItem(MENU_SAVE);

        return isCreated;
    }

    @Override
    protected Class<PersonnelEmailUpdateEvent> getEventType() {
        return PersonnelEmailUpdateEvent.class;
    }
}
