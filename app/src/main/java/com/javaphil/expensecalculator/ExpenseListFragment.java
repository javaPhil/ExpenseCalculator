package com.javaphil.expensecalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Philip on 6/25/2015.
 */
public class ExpenseListFragment extends ListFragment{

    public static final String EXTRA_PERSON_ID = "com.javaphil.expensecalculator.person_id";
    private static final String TAG = "ExpenseListFragment";

    private Person mPerson;
    private ArrayList<Expense> mExpenseList;

    public static ExpenseListFragment newInstance(UUID personID) {
        ExpenseListFragment fragment = new ExpenseListFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_PERSON_ID, personID);
        fragment.setArguments(args);
        return fragment;
    }

    public ExpenseListFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        UUID personID = (UUID)getArguments().getSerializable(EXTRA_PERSON_ID);
        mPerson = PersonHelper.get(getActivity()).getPerson(personID);
        mExpenseList = mPerson.getExpenseList();
        getActivity().setTitle(mPerson.getName() + "'s Expense List" );

        ExpenseAdapter adapter = new ExpenseAdapter(mExpenseList);
        setListAdapter(adapter);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI(){
        ((ExpenseAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_expense_list, container, false);
        ListView listView = (ListView)v.findViewById(android.R.id.list);
//        registerForContextMenu(listView);
        FloatingActionButton fab_add = (FloatingActionButton)v.findViewById(R.id.fab_expense_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Expense expense = new Expense();

                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(R.string.list_item_expense_title);
                alert.setMessage(R.string.alert_expense_message);
                final EditText titleInput = new EditText(view.getContext());
                final EditText amountInput = new EditText(view.getContext());
                titleInput.setHint(R.string.list_item_expense_title);

                amountInput.setHint(R.string.list_item_expense_amount);
                amountInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                amountInput.addTextChangedListener(new DecimalInputTextWatcher(amountInput, 2));

                linearLayout.addView(titleInput);
                linearLayout.addView(amountInput);
                alert.setView(linearLayout);

                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (titleInput.getText().toString() == null || titleInput.getText().toString().trim().isEmpty())
                            return;

                        expense.setTitle(titleInput.getText().toString());
                        if (amountInput.getText() == null || amountInput.getText().toString().trim().isEmpty()) {
                            expense.setTotal(BigDecimal.ZERO);
                        } else {
                            expense.setTotal(new BigDecimal(amountInput.getText().toString()));
                        }

                        mPerson.getExpenseList().add(expense);
                        updateUI();
                    }
                });
                alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });
                alert.show();

            }
        });

        return v;
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        final Expense e = ((ExpenseAdapter)getListAdapter()).getItem(position);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(R.string.list_item_expense_title);
        alert.setMessage(R.string.alert_expense_message);
        final EditText titleInput = new EditText(v.getContext());
        final EditText amountInput = new EditText(v.getContext());
        amountInput.addTextChangedListener(new DecimalInputTextWatcher(amountInput, 2));
        titleInput.setText(e.getTitle());
        amountInput.setText(e.getTotal().toPlainString());
        amountInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        linearLayout.addView(titleInput);
        linearLayout.addView(amountInput);
        alert.setView(linearLayout);

        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(titleInput.getText().toString() == null || titleInput.getText().toString().trim().isEmpty()) return;

                e.setTitle(titleInput.getText().toString());
                e.setTotal(new BigDecimal(amountInput.getText().toString()));
                updateUI();
            }
        });
        alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing
            }
        });
        alert.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_expense, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                if (NavUtils.getParentActivityIntent(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            case R.id.expense_fragment_menu_delete_person:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.alert_delete_person_title);
                builder.setMessage(R.string.alert_delete_person_message);
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PersonHelper.get(getActivity()).deletePerson(mPerson);
                        Intent intent = new Intent(getActivity(), PersonListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Toast.makeText(getActivity(), R.string.toast_delete_person_message, Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing and go back
                    }
                });
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                AlertDialog alert = builder.create();
                alert.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ExpenseAdapter extends ArrayAdapter<Expense> {

        public ExpenseAdapter(ArrayList<Expense> expenses){
            super(getActivity(), 0, expenses);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_expense, null);
            }
            Expense e = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.expense_list_item_titleTextView);
            titleTextView.setText(e.getTitle());

            TextView dateTextView = (TextView)convertView.findViewById(R.id.expense_list_item_dateTextView);
            SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
            String newDate = format.format(e.getDate());
            dateTextView.setText(newDate);

            TextView expenseTextView = (TextView)convertView.findViewById(R.id.expense_list_item_totalTextView);
            expenseTextView.setText(e.getTotal().toPlainString());

            return convertView;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        PersonHelper.get(getActivity()).savePersonList();
    }
}
