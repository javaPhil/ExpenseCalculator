package com.javaphil.expensecalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by Philip on 6/25/2015.
 */
public class PersonListFragment extends ListFragment {
    private static final String TAG = "PersonListFragment";

    private ArrayList<Person> mPersonList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.app_name);

        mPersonList = PersonHelper.get(getActivity()).getPersonList();

        PersonAdapter adapter = new PersonAdapter(mPersonList);
        setListAdapter(adapter);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        PersonHelper.get(getActivity()).savePersonList();
    }

    public void updateUI(){
        ((PersonAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_person_list, container, false);
        ListView listView = (ListView)v.findViewById(android.R.id.list);
        FloatingActionButton fab_total = (FloatingActionButton)v.findViewById(R.id.fab_total);
        fab_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ExpenseTotalActivity.class);
                startActivity(i);
            }
        });
        FloatingActionButton fab_add = (FloatingActionButton)v.findViewById(R.id.fab);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Person person = new Person();
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(R.string.alert_person_name_title);
                alert.setMessage(R.string.alert_person_name_message);
                final EditText input = new EditText(view.getContext());
                alert.setView(input);
                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (input.getText().toString() == null || input.getText().toString().trim().isEmpty())
                            return;

                        person.setName(input.getText().toString());
                        PersonHelper.get(getActivity()).addPerson(person);
                        Intent intent = new Intent(getActivity(), ExpenseListActivity.class);
                        intent.putExtra(ExpenseListFragment.EXTRA_PERSON_ID, person.getId());
                        startActivityForResult(intent, 0);
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
        Person p = ((PersonAdapter)getListAdapter()).getItem(position);
        Intent i = new Intent(getActivity(), ExpenseListActivity.class);
        i.putExtra(ExpenseListFragment.EXTRA_PERSON_ID, p.getId());
        startActivity(i);
    }

    private class PersonAdapter extends ArrayAdapter<Person>{

        public PersonAdapter(ArrayList<Person> personList){
            super(getActivity(), 0, personList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_person, null);
            }
            updateUI();
            Person p = getItem(position);

            TextView nameTextView = (TextView)convertView.findViewById(R.id.person_list_item_nameTextView);
            nameTextView.setText(p.getName());

            TextView totalExpenseTextView = (TextView)convertView.findViewById(R.id.person_list_item_totalExpenseTextView);
            totalExpenseTextView.setText(p.getTotalExpenseAmount() != null ? p.getTotalExpenseAmount().toPlainString() : "");

            return convertView;
        }
    }
}
