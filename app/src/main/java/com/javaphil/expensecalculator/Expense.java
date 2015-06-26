package com.javaphil.expensecalculator;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Philip on 6/24/2015.
 */
public class Expense {

    private UUID mId;
    private String mTitle;
    private BigDecimal mTotal;
    private Date mDate;

    private static final String JSON_TITLE = "expenseTitle";
    private static final String JSON_DATE = "expenseDate";
    private static final String JSON_TOTAL = "expenseTotal";
    private static final String JSON_ID = "expenseId";

    public static final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);

    public Expense(){
        if (mId == null){
            mId = UUID.randomUUID();
        }
        mDate = new Date();
    }

    /*
    Grabbing Expense objects from JSON
     */
    public Expense(JSONObject json)throws JSONException{
        mId = UUID.fromString(json.getString(JSON_ID));
        if(json.has(JSON_TITLE))mTitle = json.getString(JSON_TITLE);
        mDate = new Date(json.getLong(JSON_DATE));
        if(json.has(JSON_TOTAL)){
            setTotal(new BigDecimal(json.optString(JSON_TOTAL, "0")));
        }

    }

    /*
     Storing Expense Objects from JSON
     */
    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_DATE, mDate.getTime());
        json.put(JSON_TOTAL, getTotal().toPlainString());
        return json;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public BigDecimal getTotal() {
        return mTotal;
    }

    public void setTotal(BigDecimal mTotal) {
        this.mTotal = mTotal;
    }
}
