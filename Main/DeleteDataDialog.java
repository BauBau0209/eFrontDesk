/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;
import javax.swing.*;

public class DeleteDataDialog {
    public static void showDeleteDataDialog(String patientId) {
        // Create a panel to hold checkboxes
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create checkboxes
        JCheckBox patientCheckbox = new JCheckBox("Patient Data");
        JCheckBox emergencyCheckbox = new JCheckBox("Emergency Contacts");
        JCheckBox insuranceCheckbox = new JCheckBox("Insurance Data");
        JCheckBox deleteAllCheckbox = new JCheckBox("All Data");

        // Add checkboxes to the panel
        panel.add(patientCheckbox);
        panel.add(emergencyCheckbox);
        panel.add(insuranceCheckbox);
        panel.add(deleteAllCheckbox);

        // Create a message
        String message = "Which data do you want to delete?";
        
        // Show the dialog
        int option = JOptionPane.showConfirmDialog(null, panel, message, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Process user's choice
        if (option == JOptionPane.OK_OPTION) {
            boolean patientData = false;
            boolean emergencyContacts = false;
            boolean insuranceData = false;
            boolean allData = false;

            if (patientCheckbox.isSelected()) {
                patientData = true;
            }
            if (emergencyCheckbox.isSelected()) {
                emergencyContacts = true;
            }
            if (insuranceCheckbox.isSelected()) {
                insuranceData = true;
            }
            if (deleteAllCheckbox.isSelected()) {
                allData = true;
            }
        } else {
            System.out.println("User canceled the operation.");
        }

    }
}

