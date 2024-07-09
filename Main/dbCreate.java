
package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class dbCreate {
    public dbCreate(String[] patient, String[] contact, String[] insurance){
        try {
            dbConnect con = new dbConnect();
            Connection connection = con.Connect();
            PreparedStatement myStmt = connection.prepareStatement(
                "INSERT INTO efrontdesk.patient VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            
            java.sql.Date pDate = java.sql.Date.valueOf(patient[2]);
            myStmt.setString(1, patient[0]);
            myStmt.setString(2, patient[1]);
            myStmt.setDate(3, pDate);
            myStmt.setInt(4, Integer.parseInt(patient[3]));
            myStmt.setString(5, patient[4]);
            myStmt.setString(6, patient[5]);
            myStmt.setString(7, patient[6]);
            myStmt.setString(8, patient[7]);
            myStmt.setString(9, patient[8]);
            myStmt.setString(10, patient[9]);
            myStmt.setString(11, patient[10]);
            myStmt.setString(12, patient[11]);
            myStmt.setString(13, patient[12]);
            myStmt.setString(14, patient[13]);
            myStmt.setString(15, patient[14]);
            myStmt.setString(16, patient[15]);
            myStmt.setString(17, patient[16]);
            myStmt.execute();
        } catch (SQLException e) {
            System.out.print("There was an error creating the Patient Data: " +e);
        }
        
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/efrontdesk", "root", "020904");
            PreparedStatement myStmt = con.prepareStatement(
                "INSERT INTO efrontdesk.emercon VALUES(?, ?, ?, ?)"
            );
            
            myStmt.setString(1, contact[0]);
            myStmt.setString(2, contact[1]);
            myStmt.setString(3, contact[2]);
            myStmt.setString(4, contact[3]);
            myStmt.execute();
            if (contact.length > 5) {
                myStmt.setString(1, contact[4]);
                myStmt.setString(2, contact[5]);
                myStmt.setString(3, contact[6]);
                myStmt.setString(4, contact[7]);
                myStmt.execute();
            }
        } catch (SQLException e) {
            System.out.print("There was an error creating the Emergency Contact Data: " + e);
        }
        
        if(insurance != null){
            try{
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/efrontdesk", "root", "020904");
                PreparedStatement myStmt = con.prepareStatement(
                    "INSERT INTO efrontdesk.insurance VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );

                java.sql.Date iDate1 = java.sql.Date.valueOf(insurance[5]);
                myStmt.setString(1, insurance[0]);
                myStmt.setString(2, insurance[1]);
                myStmt.setString(3, insurance[2]);
                myStmt.setString(4, insurance[3]);
                myStmt.setString(5, insurance[4]);
                myStmt.setDate(6, iDate1);
                myStmt.setString(7, insurance[6]);
                myStmt.setString(8, insurance[7]);
                myStmt.setString(9, insurance[8]);
                myStmt.execute();

                if (insurance.length > 10) {
                    java.sql.Date iDate2 = java.sql.Date.valueOf(insurance[14]);
                    myStmt.setString(1, insurance[9]);
                    myStmt.setString(2, insurance[10]);
                    myStmt.setString(3, insurance[11]);
                    myStmt.setString(4, insurance[12]);
                    myStmt.setString(5, insurance[13]);
                    myStmt.setDate(6, iDate2);
                    myStmt.setString(7, insurance[15]);
                    myStmt.setString(8, insurance[16]);
                    myStmt.setString(9, insurance[17]);
                    myStmt.execute();
                }
            } catch (SQLException e) {
                System.out.print("There was an error creating the Patient Datas: " + e);
            }
        }
    }
}
