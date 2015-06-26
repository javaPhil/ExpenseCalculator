package com.javaphil.expensecalculator;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Philip on 6/24/2015.
 */
public class Person {
    private static final String TAG = "Person";

    private UUID mId;
    private String mName;
    private ArrayList<Expense> mExpenseList;
    private BigDecimal mTotalExpenseAmount;
    private BigDecimal mDeviation;

    private static final String JSON_PERSON_ID = "personId";
    private static final String JSON_PERSON_NAME = "personName";
    private static final String JSON_EXPENSE_LIST = "expenseList";


    public Person(){
        if(getId() == null) setId(UUID.randomUUID());
        if(mExpenseList != null){
            for(Expense _expense : mExpenseList){
                mTotalExpenseAmount.add(_expense.getTotal());
            }
        }
    }

    public Person(JSONObject json) throws JSONException{
        setId(UUID.fromString(json.getString(JSON_PERSON_ID)));
        if(json.has(JSON_PERSON_NAME)) mName = json.getString(JSON_PERSON_NAME);
        if(json.has(JSON_EXPENSE_LIST)){
            JSONArray array = json.getJSONArray(JSON_EXPENSE_LIST);
            for(int i = 0; i < array.length(); i++){
                JSONObject json_data = array.getJSONObject(i);
                Expense expense = new Expense(json_data);
                if(mExpenseList == null) new ArrayList<Expense>();

                if(expense != null)
                mExpenseList.add(expense);

                Log.i(TAG, "JSON Expense found. Title: " + expense.getTitle());
            }
        }
        if(mExpenseList != null){
            for(Expense _expense : mExpenseList){
                mTotalExpenseAmount.add(_expense.getTotal());
            }
        }
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_PERSON_ID, getId().toString());
        json.put(JSON_PERSON_NAME, mName);
        JSONArray array = new JSONArray();
        if(mExpenseList != null) {
            for (Expense _e : mExpenseList) {
                array.put(_e.toJSON());
            }
            json.put(JSON_EXPENSE_LIST, array);
        }
        return json;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public ArrayList<Expense> getExpenseList() {
        if(mExpenseList == null) mExpenseList = new ArrayList<Expense>();
        return mExpenseList;
    }

    public void setExpenseList(ArrayList<Expense> mExpenseList) {
        this.mExpenseList = mExpenseList;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID mId) {
        this.mId = mId;
    }

    /**
     * Getting the BigDecimal total amount of expenses from the ArrayList<Expense> mExpenseList
     * @return
     */
    public BigDecimal getTotalExpenseAmount() {
        mTotalExpenseAmount = BigDecimal.ZERO;
        if(mExpenseList != null) {

            for (Expense _e : mExpenseList) {
                mTotalExpenseAmount = mTotalExpenseAmount.add(_e.getTotal());
//                Log.i(TAG, "_e.Total: " + _e.getTotal().toPlainString());
            }
        }
        return mTotalExpenseAmount;
    }

    public void setTotalExpenseAmount(BigDecimal mTotalExpenseAmount) {
        this.mTotalExpenseAmount = mTotalExpenseAmount;
    }

    public BigDecimal getDeviation() {
        return mDeviation;
    }

    public void setDeviation(BigDecimal mDeviation) {
        this.mDeviation = mDeviation;
    }
}
