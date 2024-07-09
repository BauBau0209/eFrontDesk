package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class dbUpdate {
    public dbUpdate(String[] patient, String[] contact, String[] insurance, int emerconCount, int insuranceCount) {
        Connection con = null;
        PreparedStatement patientStmt = null;
        PreparedStatement emergencyStmt = null;
        PreparedStatement insuranceStmt = null;
        PreparedStatement insertEmerconStmt = null;
        PreparedStatement insertInsuranceStmt = null;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3307/efrontdesk", "root", "020904");
            con.setAutoCommit(false); // Start transaction

            // Update patient data
            String patientQuery = "UPDATE efrontdesk.patient SET patientName = ?, patientDateOfBirth = ?, patientAge = ?, " +
                                  "patientAddress = ?, patientContactNo = ?, patientGender = ?, patientEmail = ?, " +
                                  "patientMaritalStatus = ?, patientOccupation = ?, ReferredBy = ?, " +
                                  "GuarantorName = ?, RelationToPatient = ?, " +
                                  "GuarantorAddress = ?, GuarantorContactNo = ?, GuarantorAge = ?, " +
                                  "GuarantorGender = ? WHERE patientID = ?";
            patientStmt = con.prepareStatement(patientQuery);
            java.sql.Date pDate = java.sql.Date.valueOf(patient[2]);
            patientStmt.setString(1, patient[1]); // patientName
            patientStmt.setDate(2, pDate); // patientDateOfBirth
            patientStmt.setInt(3, Integer.parseInt(patient[3])); // patientAge
            patientStmt.setString(4, patient[4]); // patientAddress
            patientStmt.setString(5, patient[5]); // patientContact
            patientStmt.setString(6, patient[6]); // patientGender
            patientStmt.setString(7, patient[7]); // patientEmail
            patientStmt.setString(8, patient[8]); // patientMaritalStatus
            patientStmt.setString(9, patient[9]); // patientOccupation
            patientStmt.setString(10, patient[10]); // patientReferral
            patientStmt.setString(11, patient[11]); // patientGuardianName
            patientStmt.setString(12, patient[12]); // patientGuardianRelationship
            patientStmt.setString(13, patient[13]); // patientGuardianAddress
            patientStmt.setString(14, patient[14]); // patientGuardianContact
            patientStmt.setString(15, patient[15]); // patientGuardianAge
            patientStmt.setString(16, patient[16]); // patientGuardianGender
            patientStmt.setString(17, patient[0]); // patientID (WHERE condition)
            patientStmt.executeUpdate();
            
            // Update emergency contact data
            String emergencyQuery = "UPDATE efrontdesk.emercon SET emergencyName = ?, emergencyContactNo = ? "
                    + "WHERE PatientID = ? AND EmergencyContactID = ?";
            emergencyStmt = con.prepareStatement(emergencyQuery);
            emergencyStmt.setString(1, contact[2]); // emergencyName
            emergencyStmt.setString(2, contact[3]); // emergencyContactNo
            emergencyStmt.setString(3, contact[0]); // PatientID (WHERE condition)
            emergencyStmt.setString(4, contact[1]); // emergencyContactId (WHERE condition)
            emergencyStmt.executeUpdate();

            // Insert or update additional emergency contacts
            if (contact.length > 5) {
                if (emerconCount == 1) {
                    String insertEmerconQuery = "INSERT INTO efrontdesk.emercon VALUES(?, ?, ?, ?)";
                    insertEmerconStmt = con.prepareStatement(insertEmerconQuery);
                    insertEmerconStmt.setString(1, contact[4]);
                    insertEmerconStmt.setString(2, contact[5]);
                    insertEmerconStmt.setString(3, contact[6]);
                    insertEmerconStmt.setString(4, contact[7]);
                    insertEmerconStmt.execute();
                } else {
                    emergencyStmt.setString(1, contact[6]); // emergencyName
                    emergencyStmt.setString(2, contact[7]); // emergencyContactNo
                    emergencyStmt.setString(3, contact[4]); // PatientID (WHERE condition)
                    emergencyStmt.setString(4, contact[5]); // PatientID (WHERE condition)
                    emergencyStmt.executeUpdate();
                }
            }

            if(insurance != null){
                // Update insurance data
                if(insuranceCount == 0){
                    System.out.print("hi");
                    String insertInsuranceQuery = "INSERT INTO efrontdesk.insurance VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    insuranceStmt = con.prepareStatement(insertInsuranceQuery);
                    java.sql.Date iDate1 = java.sql.Date.valueOf(insurance[5]);
                    insuranceStmt.setString(1, insurance[0]);      // PatientId
                    insuranceStmt.setString(2, insurance[1]);      // insuranceId
                    insuranceStmt.setString(3, insurance[2]);      // PolicyNumber
                    insuranceStmt.setString(4, insurance[3]);      // PlanName
                    insuranceStmt.setString(5, insurance[4]);      // InsuredName
                    insuranceStmt.setDate(6, iDate1);              // InsuredDateOfBirth
                    insuranceStmt.setString(7, insurance[6]);      // GroupNumber
                    insuranceStmt.setString(8, insurance[7]);      // InsuredAddress
                    insuranceStmt.setString(9, insurance[8]);      // InsuredContactNo
                    insuranceStmt.executeUpdate();
                } else {
                    String insuranceQuery = "UPDATE efrontdesk.insurance SET policyNumber = ?, planName = ?, insuredName = ?, " +
                                        "insuredDateOfBirth = ?, groupNumber = ?, insuredAddress = ?, insuredContactNo = ? " +
                                        "WHERE PatientID = ? AND insuranceID = ?";
                    insuranceStmt = con.prepareStatement(insuranceQuery);
                    java.sql.Date iDate1 = java.sql.Date.valueOf(insurance[5]);
                    insuranceStmt.setString(1, insurance[2]);      // PolicyNumber
                    insuranceStmt.setString(2, insurance[3]);      // PlanName
                    insuranceStmt.setString(3, insurance[4]);      // InsuredName
                    insuranceStmt.setDate(4, iDate1);              // InsuredDateOfBirth
                    insuranceStmt.setString(5, insurance[6]);      // GroupNumber
                    insuranceStmt.setString(6, insurance[7]);      // InsuredAddress
                    insuranceStmt.setString(7, insurance[8]);      // InsuredContactNo
                    insuranceStmt.setString(8, insurance[0]);      // PatientId
                    insuranceStmt.setString(9, insurance[1]);      // insuranceId
                    insuranceStmt.executeUpdate();
                }
                
                // Insert or update additional insurance data
                if (insurance.length > 10) {
                    java.sql.Date iDate2 = java.sql.Date.valueOf(insurance[14]);
                    if (insuranceCount == 1) {
                        String insertInsuranceQuery = "INSERT INTO efrontdesk.insurance VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        insertInsuranceStmt = con.prepareStatement(insertInsuranceQuery);
                        insertInsuranceStmt.setString(1, insurance[9]);
                        insertInsuranceStmt.setString(2, insurance[10]);
                        insertInsuranceStmt.setString(3, insurance[11]);
                        insertInsuranceStmt.setString(4, insurance[12]);
                        insertInsuranceStmt.setString(5, insurance[13]);
                        insertInsuranceStmt.setDate(6, iDate2);
                        insertInsuranceStmt.setString(7, insurance[15]);
                        insertInsuranceStmt.setString(8, insurance[16]);
                        insertInsuranceStmt.setString(9, insurance[17]);
                        insertInsuranceStmt.execute();
                    } else {
                        insuranceStmt.setString(1, insurance[11]); // PolicyNumber
                        insuranceStmt.setString(2, insurance[12]); // PlanName
                        insuranceStmt.setString(3, insurance[13]); // InsuredName
                        insuranceStmt.setDate(4, iDate2);          // InsuredDateOfBirth
                        insuranceStmt.setString(5, insurance[15]); // GroupNumber
                        insuranceStmt.setString(6, insurance[16]); // InsuredAddress
                        insuranceStmt.setString(7, insurance[17]); // InsuredContactNo
                        insuranceStmt.setString(8, insurance[9]);  // PatientId
                        insuranceStmt.setString(9, insurance[10]); // insuranceId
                        insuranceStmt.executeUpdate();
                    }
                }
            }

            con.commit(); // Commit transaction

        } catch (SQLException e) {
            System.out.println("Error updating data: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback(); // Rollback transaction on error
                } catch (SQLException ex) {
                    System.out.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
        } finally {
            // Close resources
            try {
                if (patientStmt != null) patientStmt.close();
                if (emergencyStmt != null) emergencyStmt.close();
                if (insertEmerconStmt != null) insertEmerconStmt.close();
                if (insuranceStmt != null) insuranceStmt.close();
                if (insertInsuranceStmt != null) insertInsuranceStmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
