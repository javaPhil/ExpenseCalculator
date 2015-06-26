package com.javaphil.expensecalculator;

import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by Philip on 6/25/2015.
 */
public class ExpenseListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        UUID personId = (UUID)getIntent().getSerializableExtra(ExpenseListFragment.EXTRA_PERSON_ID);
        return ExpenseListFragment.newInstance(personId);
    }
}
