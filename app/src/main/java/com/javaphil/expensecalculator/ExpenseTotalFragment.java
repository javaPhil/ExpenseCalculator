package com.javaphil.expensecalculator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Philip on 6/25/2015.
 */
public class ExpenseTotalFragment extends Fragment {
    private static final String TAG = "ExpenseTotalFragment";

    private ArrayList<Person> mPersonList;
    private BigDecimal mTotalGroupAmount;
    private BigDecimal mAverage;

    private TextView mTotalGroupAmountTextView, mGroupAverage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mPersonList = PersonHelper.get(getActivity()).getPersonList();
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_totals, menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_expense_total, container, false);
        mTotalGroupAmount = BigDecimal.ZERO;
        mAverage = BigDecimal.ZERO;

        mTotalGroupAmountTextView = (TextView)v.findViewById(R.id.fragment_expense_total_amount);
        mGroupAverage = (TextView)v.findViewById(R.id.fragment_expense_average);
        if(mPersonList != null) {
            ArrayList<Person> clonePersonList = mPersonList;
            mAverage = ExpenseHelper.calculateAverage(clonePersonList);
            for (Person _p : clonePersonList) {
                mTotalGroupAmount = mTotalGroupAmount.add(_p.getTotalExpenseAmount());

            }

            mTotalGroupAmountTextView.setText("Total Group Amount: " + mTotalGroupAmount.toPlainString());
            mGroupAverage.setText("Average Amount: " + mAverage.toPlainString());

            if(clonePersonList.size() > 1) {
                ExpenseHelper.calculateDeviation(clonePersonList, mAverage);
                ExpenseHelper.sortByDeviation(clonePersonList);
                ArrayList<String> outputList = ExpenseHelper.calculateExchange(clonePersonList);
                LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.fragment_linear_layout);
                linearLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);
//                Log.e(TAG, "outputList length: " + outputList.size());
                for (String _str : outputList) {
                    TextView textView = new TextView(getActivity());
                    textView.setText(_str);
                    textView.setPadding(0,10,0,10);
                    linearLayout.addView(textView);
                }
            }
        }

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                if (NavUtils.getParentActivityIntent(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
               return super.onOptionsItemSelected(item);
        }
    }
}
