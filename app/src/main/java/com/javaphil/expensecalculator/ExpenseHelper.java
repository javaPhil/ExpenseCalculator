package com.javaphil.expensecalculator;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Philip on 6/25/2015.
 */
public class ExpenseHelper {
    private static final String TAG = "ExpenseHelper";

    /**
     * This method takes the the personList as an argument and if it is larger than one person, it
     * will loop through each person in the list, grab their total expense number, and calculate then
     * output the average.
     * @param personList
     * @return BigDecimal average
     */
    public static BigDecimal calculateAverage(ArrayList<Person> personList){
        BigDecimal average = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        if(personList.size() > 1) {
            for (Person _p : personList) {
                totalAmount = totalAmount.add(_p.getTotalExpenseAmount());
            }
            average = totalAmount.divide(BigDecimal.valueOf(personList.size()), 2, RoundingMode.HALF_UP);
        }
        return average;
    }

    /**
     * This method takes the personList and the average and if the list is larger than one person,
     * it sets each person's Deviation BigDecimal to the average MINUS their total expense amount
     * and stores that deviation BigDecimal in each Person object.
     * @param personList
     * @param average
     *
     */
    public static void calculateDeviation(ArrayList<Person> personList, BigDecimal average){
        if(personList.size() > 1) {
            for (Person _p : personList) {
                _p.setDeviation(average.subtract(_p.getTotalExpenseAmount()));
            }
        }
    }

    /**
     * This method sorts the personList (if the list is larger than 1) based on each person's deviation
     * from highest to lowest.  Example deviation output: 30 15 -10 -40
     * @param personList
     */
    public static void sortByDeviation(ArrayList<Person> personList){
        if(personList.size() > 1) {
            Collections.sort(personList, new Comparator<Person>() {
                @Override
                public int compare(Person p1, Person p2) {
                    return p2.getDeviation().compareTo(p1.getDeviation());
                }
            });
        }
    }

    /**
     * This method does all of the heavy lifting.  It builds a string, which gets added to a list
     * for the final screen's output.
     *
     * It creates two variables i and j to keep track of the list of Person object's
     * deviation.  It loops through the personList and finds the first negative deviation number's address in
     * the list; then it creates a temp double based on the first positive deviation and the first
     * negative deviation.
     *
     * If the temp double is greater than 0, the first positive person owes
     * the negative person some amount.  Then it sets the first negative person's deviation to 0,
     * increments the array tracking variable j++, checks to see if it is done, then either
     * loops again or stops.
     *
     * Else (if the temp double is NOT greater than 0) it will calculate that person at i will owe
     * person at j an amount then increment i++ and either stop or loop again.
     * @param personList
     * @return ArrayList<String>
     */
    public static ArrayList<String> calculateExchange(ArrayList<Person> personList){
        StringBuilder sb = new StringBuilder();

        ArrayList<String> stringList = new ArrayList<>();
        if(!personList.isEmpty()) {
            int i = 0; // first positive deviation
            int j; //first negative deviation
            boolean done = false;

            for (Person _p : personList) {
//                Log.e(TAG, "Person deviation: " + _p.getDeviation().doubleValue());
            }

            for (j = 0; j < personList.size(); j++) {
                if (personList.get(j).getDeviation().doubleValue() < 0) {
//                    Log.e(TAG, "deviation: " + personList.get(j).getDeviation().doubleValue());
                    break;
                }
            }
            do {
                double temp = personList.get(i).getDeviation().doubleValue() + personList.get(j).getDeviation().doubleValue();
//                Log.e(TAG, "i: " + i + " j: " + j);
                if (temp > 0) {
                    sb.append(personList.get(i).getName());
                    sb.append(" owes ");
                    sb.append(personList.get(j).getName());
                    sb.append(" ");
                    sb.append(String.format("%.2f", personList.get(j).getDeviation().abs().doubleValue()));
                    sb.append(" dollars.");
                    personList.get(j).setDeviation(BigDecimal.ZERO);
                    personList.get(i).setDeviation(BigDecimal.valueOf(temp));
                    j++;

                    if (j >= personList.size()) {
//                        Log.e(TAG, "end loop personList.size(): " + personList.size());
                        done = true;
                    }

                } else {
                    sb.append(personList.get(i).getName());
                    sb.append(" owes ");
                    sb.append(personList.get(j).getName());
                    sb.append(" ");
                    sb.append(String.format("%.2f", personList.get(i).getDeviation().abs().doubleValue()));
                    sb.append(" dollars.");
                    personList.get(i).setDeviation(BigDecimal.ZERO);
                    personList.get(j).setDeviation(BigDecimal.valueOf(temp));
                    i++;

                    if (personList.get(i).getDeviation().doubleValue() < 0.02) {
//                        Log.e(TAG, "end loop deviation2: " + personList.get(i).getDeviation().doubleValue());
                        done = true;
                    }

                }
                stringList.add(sb.toString());
                sb = new StringBuilder();

            } while (!done);
        }

        return stringList;
    }

}
