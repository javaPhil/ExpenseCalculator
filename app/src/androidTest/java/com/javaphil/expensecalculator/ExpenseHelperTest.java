package com.javaphil.expensecalculator;


import junit.framework.TestCase;

import org.junit.Test;
import org.junit.*;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Philip on 6/26/2015.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 22)
public class ExpenseHelperTest{

    @Test
    public void testCalculateAverage() throws Exception{
        ArrayList<Person> personList = new ArrayList<>();
        //setup person a
        Person a = new Person();
        a.setName("a");
        Expense expenseA = new Expense();
        expenseA.setTitle("Tacos");
        expenseA.setTotal(BigDecimal.valueOf(10));
        a.getExpenseList().add(expenseA);
        personList.add(a);

        //setup person a
        Person b = new Person();
        b.setName("b");
        Expense expenseB = new Expense();
        expenseB.setTitle("McDonalds");
        expenseB.setTotal(BigDecimal.valueOf(15));
        b.getExpenseList().add(expenseB);
        personList.add(b);

        //setup person a
        Person c = new Person();
        c.setName("c");
        Expense expenseC = new Expense();
        expenseC.setTitle("Joe's Crab Shack");
        expenseC.setTotal(BigDecimal.valueOf(25));
        c.getExpenseList().add(expenseC);
        personList.add(c);

        BigDecimal expectedAverage = BigDecimal.valueOf(16.67);

        assert expectedAverage == ExpenseHelper.calculateAverage(personList);

    }

    @Test
    public void testCalculateDerivation(){

    }

    @Test
    public void testDerivationSort(){

    }

    @Test
    public void testCalculateExchange(){

    }

}
