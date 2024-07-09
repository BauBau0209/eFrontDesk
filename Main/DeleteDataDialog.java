package Main;

import javax.swing.*;

public class DeleteDataDialog {
    public static boolean[] showDeleteDataDialog(String patientId) {
        // Create a panel to hold checkboxes
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create checkboxes
        JCheckBox deleteAllCheckbox = new JCheckBox("All Data");
        JCheckBox emergency1Checkbox = new JCheckBox("Emergency Contact 1");
        JCheckBox emergency2Checkbox = new JCheckBox("Emergency Contact 2");
        JCheckBox insurance1Checkbox = new JCheckBox("Insurance Data 1");
        JCheckBox insurance2Checkbox = new JCheckBox("Insurance Data 2");

        // Add checkboxes to the panel
        panel.add(deleteAllCheckbox);
        panel.add(emergency1Checkbox);
        panel.add(emergency2Checkbox);
        panel.add(insurance1Checkbox);
        panel.add(insurance2Checkbox);

        // Create a message
        String message = "Which data do you want to delete?";

        // Show the dialog
        int option = JOptionPane.showConfirmDialog(null, panel, message, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Process user's choice
        if (option == JOptionPane.OK_OPTION) {
            boolean[] selections = new boolean[5];
            selections[0] = deleteAllCheckbox.isSelected();
            selections[1] = emergency1Checkbox.isSelected();
            selections[2] = emergency2Checkbox.isSelected();
            selections[3] = insurance1Checkbox.isSelected();
            selections[4] = insurance2Checkbox.isSelected();
            return selections;
        } else {
            System.out.println("User canceled the operation.");
            return null;
        }
    }
}
