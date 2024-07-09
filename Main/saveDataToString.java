package Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class saveDataToString {
    private String[] patientData;
    private String[] contactData;
    private String[] insuranceData;
    
    public String[] getPatientData(String patientId){
        saveDataToString(patientId);
        return this.patientData;
    }
    
    public String[] getContactData(String patientId){
        saveDataToString(patientId);
        return this.contactData;
    }
    
    public String[] getInsuranceData(String patientId){
        saveDataToString(patientId);
        return this.insuranceData;
    }

    public void saveDataToString(String patientId) {
        try {
            dbConnect con = new dbConnect();
            Connection connection = con.Connect();

            // Retrieve patient data
            PreparedStatement patientStmt = connection.prepareStatement("SELECT * FROM efrontdesk.patient WHERE patientId = ?");
            patientStmt.setString(1, patientId);
            ResultSet patientRs = patientStmt.executeQuery();
            if (patientRs.next()) {
                patientData = extractDataFromResultSet(patientRs);
            } else {
                System.out.println("Patient with ID " + patientId + " not found.");
            }

            // Retrieve contact data
            PreparedStatement contactStmt = connection.prepareStatement("SELECT * FROM efrontdesk.emercon WHERE patientId = ?");
            contactStmt.setString(1, patientId);
            ResultSet contactRs = contactStmt.executeQuery();
            ArrayList<String> contactList = new ArrayList<>();
            while (contactRs.next()) {
                for (String value : extractDataFromResultSet(contactRs)) {
                    contactList.add(value);
                }
            }
            contactData = contactList.toArray(new String[0]);

            // Retrieve insurance data
            PreparedStatement insuranceStmt = connection.prepareStatement("SELECT * FROM efrontdesk.insurance WHERE patientId = ?");
            insuranceStmt.setString(1, patientId);
            ResultSet insuranceRs = insuranceStmt.executeQuery();
            ArrayList<String> insuranceList = new ArrayList<>();
            while (insuranceRs.next()) {
                for (String value : extractDataFromResultSet(insuranceRs)) {
                    insuranceList.add(value);
                }
            }
            insuranceData = insuranceList.toArray(new String[0]);
            
            // Close resources
            patientRs.close();
            contactRs.close();
            insuranceRs.close();
            patientStmt.close();
            contactStmt.close();
            insuranceStmt.close();
            connection.close();
        } catch (SQLException e) {
            System.out.print("There was an error creating the Patient Data: " + e);
        }
    }

    private String[] extractDataFromResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        String[] data = new String[columnsNumber];
        for (int i = 1; i <= columnsNumber; i++) {
            data[i - 1] = rs.getString(i);
        }
        return data;
    }

    public static void main(String[] args) {
        saveDataToString saver = new saveDataToString();
        saver.saveDataToString("2");
    }
}
