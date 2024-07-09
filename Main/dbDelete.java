package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class dbDelete {
    private String patientId;

    public dbDelete(String patientId) {
        this.patientId = patientId;
        boolean[] selections = DeleteDataDialog.showDeleteDataDialog(patientId);
        if (selections != null) {
            int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this information?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                if (selections[0]) {
                    deleteAllData();
                } else {
                    if (selections[1]) {
                        deleteFirstEmergencyContact();
                    }
                    if (selections[2]) {
                        deleteSecondEmergencyContact();
                    }
                    if (selections[3]) {
                        deleteFirstInsurance();
                    }
                    if (selections[4]) {
                        deleteSecondInsurance();
                    }
                }
            }
        }
    }

    private void deleteAllData() {
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
    

    private void deleteFirstEmergencyContact() {
        if (emergencyContactCanBeDeleted()) {
            String sql = "DELETE FROM efrontdesk.emercon WHERE patientid = ? ORDER BY emergencycontactid LIMIT 1";

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/efrontdesk", "root", "020904");
                 PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setString(1, patientId);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Emergency Contact 1 was deleted", "Delete successfully", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No Emergency Contact 1 found", "Delete failed", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException e) {
                System.out.println("There was an error deleting Emergency Contact 1: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Cannot delete Emergency Contact 1: at least one emergency contact must be present", "Delete failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSecondEmergencyContact() {
        if (emergencyContactExists(2)) {
            if (emergencyContactCanBeDeleted()) {
                String sql = "DELETE FROM efrontdesk.emercon WHERE patientid = ? ORDER BY emergencycontactid LIMIT 1 OFFSET 1";

                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/efrontdesk", "root", "020904");
                     PreparedStatement stmt = con.prepareStatement(sql)) {

                    stmt.setString(1, patientId);
                    int rowsAffected = stmt.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Emergency Contact 2 was deleted", "Delete successfully", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No Emergency Contact 2 found", "Delete failed", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException e) {
                    System.out.println("There was an error deleting Emergency Contact 2: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Cannot delete Emergency Contact 2: at least one emergency contact must be present", "Delete failed", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Emergency Contact 2 does not exist", "Delete failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteFirstInsurance() {
        if (insuranceCanBeDeleted()) {
            String sql = "DELETE FROM efrontdesk.insurance WHERE patientid = ? ORDER BY insuranceid LIMIT 1";

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/efrontdesk", "root", "020904");
                 PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setString(1, patientId);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Insurance Data 1 was deleted", "Delete successfully", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No Insurance Data 1 found", "Delete failed", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException e) {
                System.out.println("There was an error deleting Insurance Data 1: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Cannot delete Insurance Data 1: at least one insurance must be present", "Delete failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSecondInsurance() {
        if (insuranceExists(2)) {
            if (insuranceCanBeDeleted()) {
                String sql = "DELETE FROM efrontdesk.insurance WHERE patientid = ? ORDER BY insuranceid LIMIT 1 OFFSET 1";

                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/efrontdesk", "root", "020904");
                     PreparedStatement stmt = con.prepareStatement(sql)) {

                    stmt.setString(1, patientId);
                    int rowsAffected = stmt.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Insurance Data 2 was deleted", "Delete successfully", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No Insurance Data 2 found", "Delete failed", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException e) {
                    System.out.println("There was an error deleting Insurance Data 2: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Cannot delete Insurance Data 2: at least one insurance must be present", "Delete failed", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Insurance Data 2 does not exist", "Delete failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean insuranceCanBeDeleted() {
        String insuranceCountSql = "SELECT COUNT(*) FROM efrontdesk.insurance WHERE patientid = ?";
        String guarantorSql = "SELECT guarantorName FROM efrontdesk.patient WHERE patientid = ?";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/efrontdesk", "root", "020904");
             PreparedStatement insuranceCountStmt = con.prepareStatement(insuranceCountSql);
             PreparedStatement guarantorStmt = con.prepareStatement(guarantorSql)) {

            insuranceCountStmt.setString(1, patientId);
            ResultSet rs1 = insuranceCountStmt.executeQuery();
            rs1.next();
            int insuranceCount = rs1.getInt(1);

            guarantorStmt.setString(1, patientId);
            ResultSet rs2 = guarantorStmt.executeQuery();
            rs2.next();
            String guarantor = rs2.getString(1);

            return (insuranceCount > 1 || (insuranceCount == 1 && !guarantor.equals("")));
        } catch (SQLException e) {
            System.out.println("There was an error checking insurance data: " + e.getMessage());
            return false;
        }
    }

    private boolean emergencyContactCanBeDeleted() {
        String contactCountSql = "SELECT COUNT(*) FROM efrontdesk.emercon WHERE patientid = ?";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/efrontdesk", "root", "020904");
             PreparedStatement contactCountStmt = con.prepareStatement(contactCountSql)) {

            contactCountStmt.setString(1, patientId);
            ResultSet rs = contactCountStmt.executeQuery();
            rs.next();
            int contactCount = rs.getInt(1);

            return contactCount > 1;
        } catch (SQLException e) {
            System.out.println("There was an error checking emergency contact data: " + e.getMessage());
            return false;
        }
    }

    private boolean emergencyContactExists(int contactNumber) {
        String sql = "SELECT emergencycontactid FROM efrontdesk.emercon WHERE patientid = ? ORDER BY emergencycontactid LIMIT 1 OFFSET ?";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/efrontdesk", "root", "020904");
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, patientId);
            stmt.setInt(2, contactNumber - 1);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            System.out.println("There was an error checking Emergency Contact existence: " + e.getMessage());
            return false;
        }
    }

    private boolean insuranceExists(int insuranceNumber) {
        String sql = "SELECT insuranceid FROM efrontdesk.insurance WHERE patientid = ? ORDER BY insuranceid LIMIT 1 OFFSET ?";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/efrontdesk", "root", "020904");
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, patientId);
            stmt.setInt(2, insuranceNumber - 1);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            System.out.println("There was an error checking Insurance Data existence: " + e.getMessage());
            return false;
        }
    }
}
