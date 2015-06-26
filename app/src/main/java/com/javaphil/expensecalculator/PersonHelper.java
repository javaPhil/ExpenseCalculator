package com.javaphil.expensecalculator;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Philip on 6/25/2015.
 */
public class PersonHelper {
    private static final String TAG = "PersonHelper";
    private static final String FILENAME = "personList.json";

    private JSONSerializer mSerializer;

    private static PersonHelper sPersonHelper;
    private Context mAppContext;
    private ArrayList<Person> mPersonList;

    private PersonHelper(Context appContext){
        mAppContext = appContext;
        mSerializer = new JSONSerializer(mAppContext, FILENAME);
        try{
            mPersonList = mSerializer.loadPersons();
        }catch(Exception e){
            Log.e(TAG, "Error loading person list: ", e);
        }
    }

    public void addPerson(Person p){
        mPersonList.add(p);
    }

    public void deletePerson(Person p){
        mPersonList.remove(p);
    }

    public void addExpense(Person p, Expense e){
        for(Person _p : mPersonList){
            if(_p.getId().equals(p.getId())){
                p.getExpenseList().add(e);
            }
        }
    }

    public void removeExpense(Person p, Expense e){
        for(Person _p : mPersonList){
            if(_p.getId().equals(p.getId())){
                p.getExpenseList().remove(e);
            }
        }
    }

    public boolean savePersonList(){
        try{
            mSerializer.savePersons(mPersonList);
            return true;
        }catch(Exception e){
            Log.e(TAG, "Error saving passwords: ", e);
            return false;
        }
    }

    public static PersonHelper get(Context c){
        if(sPersonHelper == null){
            sPersonHelper = new PersonHelper(c.getApplicationContext());
        }
        return sPersonHelper;
    }

    public ArrayList<Person> getPersonList(){
        return mPersonList;
    }

    public Person getPerson(UUID id){
        for(Person p : mPersonList){
            if(p.getId().equals(id))
                return p;
        }
        return null;
    }
}
