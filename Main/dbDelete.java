package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class dbDelete {

    public dbDelete(String patientId) {
        String deleteEmerconSql = "DELETE FROM efrontdesk.emercon WHERE patientid = ?";
        String deleteInsuranceSql = "DELETE FROM efrontdesk.insurance WHERE patientid = ?";
        String deletePatientSql = "DELETE FROM efrontdesk.patient WHERE patientid = ?";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/efrontdesk", "root", "020904")) {
            con.setAutoCommit(false); // Start transaction

            try (PreparedStatement emerconStmt = con.prepareStatement(deleteEmerconSql);
                 PreparedStatement insuranceStmt = con.prepareStatement(deleteInsuranceSql);
                 PreparedStatement patientStmt = con.prepareStatement(deletePatientSql)) {

                // Delete from emercon table
                emerconStmt.setString(1, patientId);
                emerconStmt.executeUpdate();

                // Delete from insurance table
                insuranceStmt.setString(1, patientId);
                insuranceStmt.executeUpdate();

                // Delete from patient table
                patientStmt.setString(1, patientId);
                patientStmt.executeUpdate();

                con.commit(); // Commit transaction if all deletes were successful
                JOptionPane.showMessageDialog(null, "The information was deleted", "Delete successfully", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                con.rollback(); // Rollback transaction if there was an error
                System.out.println("There was an error deleting the records: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("There was an error connecting to the database: " + e.getMessage());
        }
    }
}
