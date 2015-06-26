/**
 * Created by Philip on 6/26/2015.
 */


import com.javaphil.expensecalculator.*;
import org.junit.Test;
import org.junit.*;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Philip on 6/26/2015.
 */

public class ExpenseHelperTest{

    public ArrayList<Person> setup(){
        ArrayList<Person> personList = new ArrayList<>();
        //setup person a
        Person a = new Person();
        a.setName("a");
        Expense expenseA = new Expense();
        expenseA.setTitle("Tacos");
        expenseA.setTotal(BigDecimal.valueOf(20));
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

        return personList;
    }

    @Test
    public void testCalculateAverage() throws Exception{
        ArrayList<Person> personList = setup();

        BigDecimal expectedAverage = BigDecimal.valueOf(20.00);

        Assert.assertEquals(expectedAverage.doubleValue(), ExpenseHelper.calculateAverage(personList).doubleValue(), 2);

    }

    @Test
    public void testCalculateDerivation(){

        ArrayList<Person> personList = setup();

        BigDecimal expectedAverage = BigDecimal.valueOf(20.00);

        BigDecimal expectedDeviationA = BigDecimal.valueOf(0);
        BigDecimal expectedDeviationB = BigDecimal.valueOf(5);
        BigDecimal expectedDeviationC = BigDecimal.valueOf(-5);

        ExpenseHelper.calculateDeviation(personList, expectedAverage);
        for(Person _p : personList){
            if(_p.getName().equals("a")){
                Assert.assertEquals(expectedDeviationA.doubleValue(), _p.getDeviation().doubleValue(), 2);
            }else if(_p.getName().equals("b")){
                Assert.assertEquals(expectedDeviationB.doubleValue(), _p.getDeviation().doubleValue(), 2);
            }else{
                Assert.assertEquals(expectedDeviationC.doubleValue(), _p.getDeviation().doubleValue(), 2);
            }
        }


    }

    @Test
    public void testDerivationSort(){

        ArrayList<Person> personList = setup();
        BigDecimal expectedAverage = BigDecimal.valueOf(20.00);
        ExpenseHelper.calculateDeviation(personList, expectedAverage);
        ExpenseHelper.sortByDeviation(personList);

        Assert.assertTrue(personList.get(0).getName().equals("b"));
        Assert.assertTrue(personList.get(1).getName().equals("a"));
        Assert.assertTrue(personList.get(2).getName().equals("c"));

    }

    @Test
    public void testCalculateExchange(){
        ArrayList<Person> personList = setup();
        BigDecimal expectedAverage = BigDecimal.valueOf(20.00);
        ExpenseHelper.calculateDeviation(personList, expectedAverage);
        ExpenseHelper.sortByDeviation(personList);

        //Based on the actors we're using, we know that b owes c 5.00 dollars
        Assert.assertTrue(ExpenseHelper.calculateExchange(personList).get(0).equals("b owes c 5.00 dollars."));

    }

}

