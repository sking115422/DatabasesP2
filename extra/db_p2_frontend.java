package BSIL;

import java.sql.*;
import java.util.Scanner;

public class db_p2_frontend
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
        System.out.print("Please the username here: ");
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
                " AND e1.dept_no = e2.dept_no");
        if(!rs.isBeforeFirst()){
            System.out.println();
            System.out.println(String.format("There is no relationship between employee %s and employee %s", e1Num, e2Num));
        }else{
            System.out.println();
            while(rs.next()){
                System.out.println("There is one degree of separation between employee" + Integer.toString(e1Num)
                        + " and employee" + Integer.toString(e2Num) +" via department "+ rs.getString(1));
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
                "ON a1.emp_no = a2.emp_no");
        if(!rs.isBeforeFirst()){
            System.out.println();
            System.out.println(String.format("There is no second degree relationship between employee %s and employee %s", e1Num, e2Num));
        }else{
            System.out.println();
            System.out.println(String.format("The following employees link employee %s and employee %s via two degrees of separation", e1Num, e2Num));
            System.out.println();
            while(rs.next()){
                System.out.println("Employee number : " + rs.getString(1) + "| Department number: "+ rs.getString(2));
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













//    public static void main (String[] args) {
//
//
//        String url = "jdbc:mysql://localhost:3306/namdb";
//        String username = "root";
//        String password = "2396";
//
//        try
//
//        {
//            Connection connection = DriverManager.getConnection(url, username, password);
//            System.out.println("Connected to the database!");
//
//            String sql = "SELECT * FROM customer";
//            Statement stmt = connection.createStatement();
//            ResultSet result= stmt.executeQuery(sql);
//
//            int count = 0;
//
//            while (result.next()){
//                String firstname = result.getString(1);
//                String lastname = result.getString("lastname");
//                count++;
//                System.out.println("Customer " + count + ": " + firstname + " " + lastname);
//            }
//
//            connection.close();
//
//
//
//            String sql = "INSERT INTO customer (firstname, lastname) VALUES (?, ?)";
//
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, "Nam");
//            statement.setString(2, "Spencer");
//
//            int rows = statement.executeUpdate();
