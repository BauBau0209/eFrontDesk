/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package efrontdesk;
import Main.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;

public class DatabaseConnection {
    
    public static void readTable(String tableName){
        String url = "jdbc:mysql://localhost:3307/efrontdesk";
        String username = "root";
        String password = "020904";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database!");
            Statement stmt = connection.createStatement() ;
            String query = "select * from " + tableName ;
            ResultSet rs = stmt.executeQuery(query) ;
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }
        } catch (SQLException e) {
            System.out.println("Connection failed!" + e);
        }
    }
    
    public static void createData(){
        String url = "jdbc:mysql://localhost:3307/efrontdesk";
        String username = "root";
        String password = "020904";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database!");
            String[] data = new String[]{
                "0000000002",
                "0000000002", 
                "2", 
                "Samantha",
                "Kuya JJ Bautista",
                "1998-04-23",
                "1b",
                "Pasay",
                "2"
            };

            PreparedStatement myStmt = connection.prepareStatement(
                "insert into insurance values(?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            
            myStmt.setString(1, data[0]);
            myStmt.setString(2, data[1]);
            myStmt.setInt(3, Integer.parseInt(data[2]));
            myStmt.setString(4, data[3]);
            myStmt.setString(5, data[4]);
            myStmt.setDate(6, java.sql.Date.valueOf(data[5]));
            myStmt.setString(7, data[6]);
            myStmt.setString(8, data[7]);
            myStmt.setInt(9, Integer.parseInt(data[8]));
            
            myStmt.execute();
        } catch (SQLException e) {
            System.out.println("Connection failed!" + e);
        }
    }
    
    public static void updateTable(String insuranceID, String newGroupNumber){
        String url = "jdbc:mysql://localhost:3307/efrontdesk";
        String username = "root";
        String password = "020904";
        
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement myStmt = connection.prepareStatement(
                "UPDATE Insurance SET GroupNumber = ? WHERE insuranceId = ?"
            );
                    
            myStmt.setString(1, newGroupNumber);
            myStmt.setString(2, insuranceID);
            
            myStmt.execute();
            
        } catch (SQLException e) {
            System.out.println("Connection failed!" + e);
        }
    }
    
    public static void deleteData(String insuranceID){
        String url = "jdbc:mysql://localhost:3307/efrontdesk";
        String username = "root";
        String password = "020904";
        
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement myStmt = connection.prepareStatement(
                "DELETE FROM insurance WHERE insuranceID = ?"
            );
                    
            myStmt.setString(1, insuranceID);
            
            myStmt.execute();
            
        } catch (SQLException e) {
            System.out.println("Connection failed!" + e);
        }
    }
    
    public static void main(String[] args) {
        JFrame f = new JFrame("eFrontDesk Login");    
        login loginFrame = new login(f);
        loginFrame.setVisible(true);
    }
}
