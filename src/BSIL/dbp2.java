
package BSIL;

import java.sql.*;
import java.util.Scanner;
import java.time.*;

/**
 * <h1> CSCI X370 Databases Project 2 </h1>
 * <h2> dbp2 class = databases project 2 class </h2>
 * <p> This is a front end UI to help manage the sample SQL employees data base. The goal is to find 1st and 2nd degrees of separation between coworkers that worked in the same department at the same time. </p>
 *
 * @author Spencer King, Daniel Abramow, and Shreenal Patel
 * @version 2.0
 */

public class dbp2
{

    /**
     * <h1>Main Method</h1>
     * <p>The program open with a welcome statement and prompting. </p>
     * <h2>User Connection</h2>
     * <p> To interact with the database the user must first make a connection with a databases.
     * The program asks the user to enter the url of the server they are trying to connect to, the username, and the password.
     * The prompts provide examples of what these should look like.
     * The user will be asked to enter url, username, and password until a successful connection is made then the program will progress.</p>
     * <h2>User Entry</h2>
     * <p>The user will be prompted to enter values between [10001, 499999] for the employee number.  </p>
     * <h2>Calculate First Degree</h2>
     * <p>The program calls a function calls a method called oneDegree and passes it both of the employee numbers and the connection so that it can see if any
     *  first degree connections exist. </p>
     * <h2>Calculate Second Degree</h2>
     * <p>The program calls a function calls a method called twoDegrees and passes it both of the employee numbers and the connection so that it can see if any
     *  second degree connections exist.</p>
     * <h2>Termination</h2>
     * <p> After the calculation of the second degree of separation, the program will allow the user to rerun
     *  the program with other inputs or to terminate the program.</p>
     */

    public static void main(String [] args)
    {

        System.out.println();
        System.out.println("WELCOME TO THE DEGREES OF SEPARATION CALCULATOR!");
        System.out.println();
        System.out.println("Please follow the prompts below.");

        Scanner s = new Scanner(System.in);

        Connection c;

        do {

            System.out.println();
            System.out.println("This program requires a connection to the local SQL server on your machine.");
            System.out.println("The server and database address should look something like this: jdbc:mysql://localhost:3306/employees ");
            System.out.print("Please enter the server address and database here: ");
            String url = s.nextLine();

            System.out.println();
            System.out.println("This program requires a username for the connection.");
            System.out.println("It will most likely be: root");
            System.out.print("Please the username here: ");
            String username = s.nextLine();

            System.out.println();
            System.out.println("If the connection has a password, the program will also require this.");
            System.out.print("Please the password here: ");
            String password = s.nextLine();

            c = connect(url, username, password);

            if(c == null) {
                System.out.println();
                System.out.println("Could not make a connection! Please try again.");
            }

        }while(c == null);

        int qval;

        do
        {

            int e1Num;
            int e2Num;

            try
            {
                System.out.println();
                System.out.println("Employee numbers start at 10001 and end at 499999. Please enter an integer value in this range below.");
                System.out.println();
                System.out.print("First employee number: ");
                e1Num = s.nextInt();
                System.out.print("Second employee number: ");
                e2Num = s.nextInt();

                if(e2Num != e1Num)
                {
                    System.out.println();
                    System.out.println("Calculating first degree of separation...");
                    oneDegree(e1Num, e2Num, c);
                    System.out.println();
                    System.out.println("Calculating second degree of separation...");
                    twoDegrees(e1Num, e2Num, c);
                }
                else
                {
                    System.out.println();
                    System.out.println("Please enter two different employee numbers.");
                }

            }
            catch (Exception e)
            {
                System.out.println();
                System.out.println("Invalid input!!! Please make sure your entry is an integer value in the range of [10001, 499999].");
            }

            System.out.println();
            System.out.println("If you wish to exit the program enter 0, otherwise enter 5 to rerun.");
            System.out.print("Enter selection here: ");

            qval = s.nextInt();

        } while (qval != 0);

        System.out.println();
        System.out.println("*** PROGRAM TERMINATED ***");

    }

    /**
     * <h1>Method oneDegree</h1>
     * <h4>In general this method takes 3 inputs of employee 1 ID, employee 2 ID, and a connection and finds out if they have one degree of separation between them.
     *  In this case one degree of separation means that they have worked together in the same department for some overlapping amount of time. </h4>
     * <p>More specifically, this method queries the employees database and pulls the tuple from the dept_emp table associated with employee 1 and employee 2.
     *  It then extracts each column from the tuple as its own variable. An if then statement then compares these values to see if employee 1 and employee 2 worked
     *  in the same department. If that is true, it then compare the dates that each employee has worked in that department to make sure they overlap by at least
     *  one day. If that is also true, the method prints the a first degree relationship does exist and also the department relating them. If not, then the method
     *  returns that there is no relationship between the two employees.</p>
     *
     * @param e1Num First employee ID number as an integer in the range [10001, 499999]
     * @param e2Num Second employee ID number as an integer in the range [10001, 499999]
     * @param con Connection to the database
     * @throws SQLException If incorrect values or value types are entered for first or second employee, error will be thrown
     */

    public static void oneDegree(int e1Num, int e2Num, Connection con) throws SQLException
    {

        Statement stmt = con.createStatement();

        ResultSet e1_row = stmt.executeQuery("SELECT * FROM dept_emp WHERE emp_no = " + e1Num);

        String e1_dept = "";
        String e1_from_S;
        LocalDate e1_from = null;
        String e1_to_S;
        LocalDate e1_to = null;

        while (e1_row.next())
        {
            e1_dept = e1_row.getString("dept_no");
            e1_from_S = e1_row.getString("from_date");
            e1_from = LocalDate.parse(e1_from_S);
            e1_to_S = e1_row.getString("to_date");
            e1_to = LocalDate.parse(e1_to_S);
        }

        ResultSet e2_row = stmt.executeQuery("SELECT * FROM dept_emp WHERE emp_no = " + e2Num);

        String e2_dept = "";
        String e2_from_S;
        LocalDate e2_from = null;
        String e2_to_S;
        LocalDate e2_to = null;

        while (e2_row.next())
        {
            e2_dept = e2_row.getString("dept_no");
            e2_from_S = e2_row.getString("from_date");
            e2_from = LocalDate.parse(e2_from_S);
            e2_to_S = e2_row.getString("to_date");
            e2_to = LocalDate.parse(e2_to_S);
        }

        if (e1_dept.equals(e2_dept))
        {
            if (e1_to.isAfter(e2_from))
            {
                System.out.println();
                System.out.println("FIRST DEGREE:");
                System.out.printf("The following two employees,%s & %s, have a first degree relationship. %n", e1Num, e2Num);
                System.out.printf("The two employees both worked for department %s at overlapping times. %n", e1_dept);
            }
            else if(e1_from.isBefore(e2_to))
            {
                System.out.println();
                System.out.println("FIRST DEGREE:");
                System.out.printf("The following two employees,%s & %s, have a first degree relationship. %n", e1Num, e2Num);
                System.out.printf("The two employees both worked for department %s at overlapping times. %n", e1_dept);
            }
            else if(e1_from.equals(e2_from) && e1_to.equals(e2_from)){
                System.out.println();
                System.out.println("FIRST DEGREE:");
                System.out.printf("The following two employees,%s & %s, have a first degree relationship. %n", e1Num, e2Num);
                System.out.printf("The two employees both worked for department %s at overlapping times. %n", e1_dept);
            }

        }
        else
        {
            System.out.println();
            System.out.println("FIRST DEGREE:");
            System.out.printf("There is no first degree relationship between employee %s and employee %s. %n", e1Num, e2Num);
        }

    }

    /**
     * <h1>Method twoDegrees</h1>
     * <h4>In general this method takes 3 inputs of employee 1 ID, employee 2 ID, and a connection and finds out if they have two degrees of separation between them.
     *  In this case, two degrees of separation means that employee 1 and 2 have each worked with another employee in the same department for an overlapping amount
     *  of time, and each of those employees have worked with some another employee in the same department for an overlapping amount of time. In other words,
     *  E1 --> D1 <-- E3 --> D2 <-- E2 (E1 and E3 work at D1 at the same time; E3 and E2 work at D2 at the same time). </h4>
     * <p>More specifically, this method pulls from the database two result sets. One is made up of all employees who worked in the same department at an overlapping time
     *  as employee 1, and the other is the same thing just for employee 2. The method then compares each tuple in one table to each tuple in the other
     *  to see if 2 other employees are related by department and overlapping time. If that is the case, then there is a link between employee 1 and some other employee
     *  E1, a link between employee 2 and some other employee E2, and a link between E1 and E2, then we have found a second degree connection between the original
     *  employee 1 and original employee 2. If this is the case, the method prints that a second degree connection is found and also prints the two sub connections
     *  that make it up.</p>
     *
     * @param e1Num First employee ID number as an integer in the range [10001, 499999]
     * @param e2Num Second employee ID number as an integer in the range [10001, 499999]
     * @param con Connection to the database
     * @throws SQLException If incorrect values or value types are entered for first or second employee, error will be thrown
     */

    public static void twoDegrees(int e1Num, int e2Num, Connection con) throws SQLException
    {

        Statement stmt = con.createStatement();
        Statement stmt2 = con.createStatement();

        ResultSet e1_row = stmt.executeQuery("SELECT * FROM dept_emp WHERE emp_no = " + e1Num);

        String e1_dept = "";
        String e1_from_S = "";
        String e1_to_S = "";

        while (e1_row.next())
        {
            e1_dept = e1_row.getString("dept_no");
            e1_from_S = e1_row.getString("from_date");
            e1_to_S = e1_row.getString("to_date");

        }

        ResultSet e2_row = stmt.executeQuery("SELECT * FROM dept_emp WHERE emp_no = " + e2Num);

        String e2_dept = "";
        String e2_from_S = "";
        String e2_to_S = "";

        while (e2_row.next())
        {
            e2_dept = e2_row.getString("dept_no");
            e2_from_S = e2_row.getString("from_date");
            e2_to_S = e2_row.getString("to_date");
        }

        ResultSet e1_e_dept_table = stmt.executeQuery(

                "SELECT * FROM dept_emp WHERE dept_no = '" + e1_dept + "' AND (to_date >= '" + e1_from_S + "' AND from_date <= '" + e1_to_S + "')"

        );

        ResultSet e2_e_dept_table = stmt2.executeQuery(

                "SELECT * FROM dept_emp WHERE dept_no = '" + e2_dept + "' AND (to_date >= '" + e2_from_S + "' AND from_date <= '" + e2_to_S + "')"

        );

        String e1_e_dept;
        String e1_e_from_S;
        LocalDate e1_e_from;
        String e1_e_to_S;
        LocalDate e1_e_to;

        int e2_e_num;
        String e2_e_dept;
        String e2_e_from_S;
        LocalDate e2_e_from;
        String e2_e_to_S;
        LocalDate e2_e_to;

        boolean dep2nd = false;

        int e_num = 0;

        while (e1_e_dept_table.next())
        {

            e1_e_dept = e1_e_dept_table.getString("dept_no");
            e1_e_from_S = e1_e_dept_table.getString("from_date");
            e1_e_from = LocalDate.parse(e1_e_from_S);
            e1_e_to_S = e1_e_dept_table.getString("to_date");
            e1_e_to = LocalDate.parse(e1_e_to_S);

            while(e2_e_dept_table.next())
            {

                e2_e_num = e2_e_dept_table.getInt("emp_no");
                e2_e_dept = e2_e_dept_table.getString("dept_no");
                e2_e_from_S = e2_e_dept_table.getString("from_date");
                e2_e_from = LocalDate.parse(e2_e_from_S);
                e2_e_to_S = e2_e_dept_table.getString("to_date");
                e2_e_to = LocalDate.parse(e2_e_to_S);

                if (e1_e_dept.equals(e2_e_dept))
                {
                    if (e1_e_to.isAfter(e2_e_from))
                    {
                        dep2nd = true;
                        e_num = e2_e_num;
                        break;
                    }
                    else if(e1_e_from.isBefore(e2_e_to))
                    {
                        dep2nd = true;
                        e_num = e2_e_num;
                        break;
                    }
                    else if(e1_e_from.equals(e2_e_from) && e1_e_to.equals(e2_e_from))
                    {
                        dep2nd = true;
                        e_num = e2_e_num;
                        break;
                    }

                }

            }

        }

        if (dep2nd)
        {
            System.out.println();
            System.out.println("SECOND DEGREE:");
            System.out.printf("The following two employees,%s & %s, have a 2nd degree relationship. %n", e1Num, e2Num);
            System.out.printf("Employee %s and Employee %s work at %s at overlapping times. %n" , e1Num, e_num, e1_dept);
            System.out.printf("Employee %s and Employee %s work at %s at overlapping times. %n" , e2Num, e_num, e2_dept );
        }
        else
        {
            System.out.println();
            System.out.println("SECOND DEGREE:");
            System.out.printf("There is no second degree relationship between employee %s and employee %s. %n", e1Num, e2Num);
        }

    }

    /**
     * <h1>Connect</h1>
     * <p>This method establishes a connection with the databases when passed the 3 parameters listed below.</p>
     *
     * @param url - Address of the server and database you wish to connect to | ex. jdbc:mysql://localhost:3306/employees
     * @param username - User name of the connection instance | ex. root
     * @param password - Password associate with the connection instance | unique for each instance and may not even be need in some cases
     * @return Returns a connection as the variable con
     */

    public static Connection connect(String url, String username, String password){

        Connection con = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
        }catch(Exception e){
            System.out.println(e);
        }
        return con;
    }

}



//    public static void oneDegree(int e1Num, int e2Num, Connection con) throws SQLException {
//
//        Statement stmt = con.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT e1.dept_no FROM dept_emp e1, dept_emp e2 " +
//                "WHERE e1.emp_no = " + e1Num +
//                " AND e2.emp_no = " + e2Num +
//                " AND e1.dept_no = e2.dept_no"
//                + " LIMIT 100;");
//        if(!rs.isBeforeFirst()){
//        	System.out.println();
//            System.out.println(String.format("There is no first degree relationship between employee %s and employee %s", e1Num, e2Num));
//        }else{
//        	System.out.println();
//        	System.out.println(String.format("The following department link employee %s and employee %s via first degrees of separation", e1Num, e2Num));
//            while(rs.next()){
//                System.out.println(e1Num + " --> " + rs.getString(1) + " <-- " + e2Num);
//            }
//        }
//    }
//
//
//    public static void twoDegrees(int e1Num, int e2Num, Connection con) throws SQLException {
//
//        Statement stmt = con.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT * " +
//                "FROM (SELECT e1.emp_no, e1.dept_no " +
//                "FROM employees.dept_emp e1 " +
//                "WHERE e1.dept_no IN (SELECT dept_no FROM employees.dept_emp WHERE emp_no = "+ e1Num + ")) a1 " +
//                "INNER JOIN (SELECT e1.emp_no, e1.dept_no " +
//                "FROM employees.dept_emp e1 " +
//                "WHERE e1.dept_no IN (SELECT dept_no FROM employees.dept_emp WHERE emp_no = "+ e2Num + ")) a2 " +
//                "ON a1.emp_no = a2.emp_no"
//                + " LIMIT 100;");
//        if(!rs.isBeforeFirst()){
//            System.out.println();
//            System.out.println(String.format("There is no second degree relationship between employee %s and employee %s", e1Num, e2Num));
//        }else{
//            System.out.println();
//            System.out.println(String.format("The following employees link employee %s and employee %s via two degrees of separation", e1Num, e2Num));
//            System.out.println();
//            while(rs.next()){
//            	System.out.println(e1Num + " --> " + rs.getString(2) + " <-- " + rs.getString(1) + " --> " + rs.getString(4) + " <-- " + e2Num);
//            }
//        }
//    }






