package com.javaphil.expensecalculator;

import android.support.v4.app.Fragment;

/**
 * Created by Philip on 6/25/2015.
 */
public class PersonListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new PersonListFragment();
    }
}
