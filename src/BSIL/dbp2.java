/**
 * this is an example of a javadoc style quote
 */

package BSIL;

import java.sql.*;
import java.util.Scanner;

public class dbp2
{

    public static void main(String [] args) throws SQLException
    {

        System.out.println();
        System.out.println("Hello welcome to the degrees of separation calculator!");
        System.out.println();
        System.out.println("Please follow the prompts below.");


        Scanner s = new Scanner(System.in);


        System.out.println();
        System.out.println("This program requires a connection to the local SQL server on your machine.");
        System.out.println("The server and database address should look something like this: jdbc:mysql://localhost:3306/employees ");
        System.out.print("Please enter the server address and database here: ");
        String url = s.nextLine();

        System.out.println();
        System.out.println("This program requires a username for the connection");
        System.out.println("It will most likely be: root");
        System.out.print("Please the username here: ");
        String username = s.nextLine();

        System.out.println();
        System.out.println("If the connection has a password, the program will also require this");
        System.out.print("Please the password here: ");
        String password = s.nextLine();

        Connection c = connect(url, username, password);

        int qval = -1;

        do
        {


            int e1Num;
            int e2Num;

            try
            {
                System.out.println();
                System.out.print("First employee number: ");
                e1Num = s.nextInt();
                System.out.print("Second employee number: ");
                e2Num = s.nextInt();

                if(e2Num != e1Num)
                {
                    System.out.println();
                    System.out.println("Calculating first and second degrees of separation");
                    Thread.sleep(1000);
                    oneDegree(e1Num, e2Num, c);
                    twoDegrees(e1Num, e2Num, c);
                }
                else
                {
                    System.out.println("Please enter two different employee numbers.");
                    main(null);
                }

            }
            catch (Exception e)
            {
                System.out.println("Invalid input. Please enter an integer.");
            }

            System.out.println();
            System.out.println("If you wish to exit the program enter 0, otherwise enter 5 to rerun");
            System.out.print("Enter selection here: ");

            qval = s.nextInt();

        } while (qval != 0);

        System.out.println();
        System.out.println("*** PROGRAM TERMINATED ***");

    }


    public static void oneDegree(int e1Num, int e2Num, Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT e1.dept_no FROM dept_emp e1, dept_emp e2 " +
                "WHERE e1.emp_no = " + e1Num +
                " AND e2.emp_no = " + e2Num +
                " AND e1.dept_no = e2.dept_no"
                + " LIMIT 100;");
        if(!rs.isBeforeFirst()){
        	System.out.println();
            System.out.println(String.format("There is no first degree relationship between employee %s and employee %s", e1Num, e2Num));
        }else{
        	System.out.println();
        	System.out.println(String.format("The following department link employee %s and employee %s via first degrees of separation", e1Num, e2Num));
            while(rs.next()){
                System.out.println(e1Num + " --> " + rs.getString(1) + " <-- " + e2Num);
            }
        }
    }


    public static void twoDegrees(int e1Num, int e2Num, Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * " +
                "FROM (SELECT e1.emp_no, e1.dept_no " +
                "FROM employees.dept_emp e1 " +
                "WHERE e1.dept_no IN (SELECT dept_no FROM employees.dept_emp WHERE emp_no = "+ e1Num + ")) a1 " +
                "INNER JOIN (SELECT e1.emp_no, e1.dept_no " +
                "FROM employees.dept_emp e1 " +
                "WHERE e1.dept_no IN (SELECT dept_no FROM employees.dept_emp WHERE emp_no = "+ e2Num + ")) a2 " +
                "ON a1.emp_no = a2.emp_no"
                + " LIMIT 100;");
        if(!rs.isBeforeFirst()){
            System.out.println();
            System.out.println(String.format("There is no second degree relationship between employee %s and employee %s", e1Num, e2Num));
        }else{
            System.out.println();
            System.out.println(String.format("The following employees link employee %s and employee %s via two degrees of separation", e1Num, e2Num));
            System.out.println();
            while(rs.next()){
            	System.out.println(e1Num + " --> " + rs.getString(2) + " <-- " + rs.getString(1) + " --> " + rs.getString(4) + " <-- " + e2Num);
            }
        }
    }


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
