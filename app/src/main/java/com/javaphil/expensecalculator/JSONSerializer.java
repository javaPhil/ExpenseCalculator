package com.javaphil.expensecalculator;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Philip on 6/24/2015.
 */
public class JSONSerializer {
    private static final String TAG = "JSONSerializer";
    private Context mContext;
    private String mFilename;
    private static final String JSON_PERSON_LIST = "personList";

    public JSONSerializer(Context c, String f){
        mContext = c;
        mFilename = f;
    }

    //Saves a list of Person objects
    public void savePersons(ArrayList<Person> personList) throws JSONException, IOException{
        JSONObject json = new JSONObject();

        JSONArray array = new JSONArray();
        for(Person p : personList){
            array.put(p.toJSON());
        }
        json.put(JSON_PERSON_LIST, array);
        Writer writer = null;
        try{
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(json.toString());
        }finally{
            if(writer != null) writer.close();
        }
    }

    //Loads a list of Person objects for the list page
    public ArrayList<Person> loadPersons() throws IOException, JSONException{
        ArrayList<Person> personList = new ArrayList<>();

        BufferedReader reader = null;
        InputStream in = null;
        try{
            in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null){
                jsonString.append(line);
                Log.i(TAG, "readLine(): " + line);

            }

            JSONArray array = (JSONArray)new JSONTokener(jsonString.toString()).nextValue();
            int count = 0;
            int arrayLength = array.length();
            for(int i = 0; i < array.length(); i++){
                JSONObject json_data = array.getJSONObject(i);
                personList.add(new Person(json_data));
                Log.i(TAG, "json_data: " + json_data);
                Log.i(TAG, "count: " + count);
                Log.i(TAG, "arrayLength: " + arrayLength);
                count++;
            }

        }catch(Exception ex){
//            Log.i(TAG, "Error loading Person list from JSON: ", ex);
        }finally{
            if(reader != null) reader.close();
            if(in != null) in.close();
        }
        return personList;
    }

}
