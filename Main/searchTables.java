/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author tessa
 */
public class searchTables extends javax.swing.JFrame {

    /**
     * Creates new form searchPatient
     */
    public searchTables() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        fetch();
    }
    
    public void fetch() {
        try{
            dbConnect con = new dbConnect();
            Connection connection = con.Connect();
            ResultSet rs;
            PreparedStatement pst;
            String q = "SELECT * FROM efrontdesk.`patient`";
            pst = connection.prepareStatement(q);
            rs = pst.executeQuery();
            
            patientInformationTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
        try{
            dbConnect con = new dbConnect();
            Connection connection = con.Connect();
            ResultSet rs;
            PreparedStatement pst;
            String q = "SELECT * FROM efrontdesk.`emercon`";
            pst = connection.prepareStatement(q);
            rs = pst.executeQuery();
            
            emergencyContactTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
        try{
            dbConnect con = new dbConnect();
            Connection connection = con.Connect();
            ResultSet rs;
            PreparedStatement pst;
            String q = "SELECT * FROM efrontdesk.`insurance`";
            pst = connection.prepareStatement(q);
            rs = pst.executeQuery();
            
            insuranceTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        patientInformationTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        emergencyContactTable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        insuranceTable = new javax.swing.JTable();
        searchField = new javax.swing.JTextField();
        backButton = new javax.swing.JButton();
        searchBtn = new javax.swing.JButton();
        sidePanel = new javax.swing.JPanel();
        TableSearch = new javax.swing.JButton();
        Dashboard = new javax.swing.JButton();
        LogOut = new javax.swing.JButton();
        Information = new javax.swing.JButton();
        logo = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(208, 227, 240));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setText("Search");

        jPanel2.setBackground(new java.awt.Color(46, 62, 154));
        jPanel2.setPreferredSize(new java.awt.Dimension(956, 499));

        patientInformationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Patient Id", "Name", "Date of Birth", "Age", "Email", "Occupation", "Marital Status", "Address", "Guarantor Name", "Date of Birth", "Age", "Gender", "Occupation", "Marital Status", "Address"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        patientInformationTable.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(patientInformationTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 896, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE)
                    .addGap(16, 16, 16)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 578, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
                    .addGap(15, 15, 15)))
        );

        jTabbedPane1.addTab("Patient Information", jPanel3);

        emergencyContactTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Patient Id", "Emergency Contact ID", "Emergency Contact Name", "Contact Number"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        emergencyContactTable.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportView(emergencyContactTable);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        jTabbedPane1.addTab("Emergency Contact", jPanel4);

        insuranceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Patient Id", "Insurance ID", "Policy Number", "Plan Name", "Insured Name", "Date of Birth", "Group Number", "Address", "Contact No."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        insuranceTable.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane3.setViewportView(insuranceTable);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        jTabbedPane1.addTab("Insurance", jPanel5);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        backButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        searchBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/Icons/icons8-search-24.png"))); // NOI18N
        searchBtn.setText("Search");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 908, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchBtn)
                        .addGap(87, 87, 87)
                        .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(searchField, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(searchBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(backButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
                .addGap(25, 25, 25))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        sidePanel.setBackground(new java.awt.Color(27, 15, 119));
        sidePanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        TableSearch.setBackground(new java.awt.Color(0, 0, 153));
        TableSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/Icons/icons8-search-35.png"))); // NOI18N
        TableSearch.setBorderPainted(false);
        TableSearch.setContentAreaFilled(false);
        TableSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TableSearchActionPerformed(evt);
            }
        });

        Dashboard.setBackground(new java.awt.Color(0, 0, 153));
        Dashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/Icons/icons8-dashboard-24.png"))); // NOI18N
        Dashboard.setBorderPainted(false);
        Dashboard.setContentAreaFilled(false);
        Dashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DashboardActionPerformed(evt);
            }
        });

        LogOut.setBackground(new java.awt.Color(0, 0, 153));
        LogOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/Icons/icons8-logout-24 (1).png"))); // NOI18N
        LogOut.setBorderPainted(false);
        LogOut.setContentAreaFilled(false);
        LogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogOutActionPerformed(evt);
            }
        });

        Information.setBackground(new java.awt.Color(0, 0, 153));
        Information.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/Icons/icons8-information-30.png"))); // NOI18N
        Information.setBorderPainted(false);
        Information.setContentAreaFilled(false);
        Information.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InformationActionPerformed(evt);
            }
        });

        logo.setBackground(new java.awt.Color(0, 0, 153));
        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/Icons/Medical Clinic Logo (3).png"))); // NOI18N
        logo.setBorderPainted(false);
        logo.setContentAreaFilled(false);

        jSeparator2.setForeground(new java.awt.Color(0, 0, 51));

        javax.swing.GroupLayout sidePanelLayout = new javax.swing.GroupLayout(sidePanel);
        sidePanel.setLayout(sidePanelLayout);
        sidePanelLayout.setHorizontalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logo, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                    .addComponent(LogOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Information, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TableSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                    .addComponent(Dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );
        sidePanelLayout.setVerticalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(Dashboard)
                .addGap(32, 32, 32)
                .addComponent(TableSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(Information)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 377, Short.MAX_VALUE)
                .addComponent(LogOut)
                .addGap(25, 25, 25))
        );

        getContentPane().add(sidePanel, java.awt.BorderLayout.LINE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        this.setVisible(false);
        dashboard d = new dashboard();
        d.setVisible(true);
    }//GEN-LAST:event_backButtonActionPerformed

    private void TableSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TableSearchActionPerformed
        searchTables tables = new searchTables();
        tables.setExtendedState(JFrame.MAXIMIZED_BOTH);
        tables.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_TableSearchActionPerformed

    private void DashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardActionPerformed
        dashboard dashboardFrame = new dashboard();
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        dashboardFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_DashboardActionPerformed

    private void LogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogOutActionPerformed
        int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            this.dispose();
        } else {
            return;
        }
    }//GEN-LAST:event_LogOutActionPerformed

    private void InformationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InformationActionPerformed
        Information informationFrame = new Information();
        informationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        informationFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_InformationActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        String searchText = searchField.getText();
        
        // Patient Information Table
        DefaultTableModel pTableOb = (DefaultTableModel) patientInformationTable.getModel();
        TableRowSorter<DefaultTableModel> pRowSorter = new TableRowSorter<>(pTableOb);
        patientInformationTable.setRowSorter(pRowSorter);
        pRowSorter.setRowFilter(RowFilter.regexFilter(searchText));

        // Emergency Contacts Table
        DefaultTableModel eTableOb = (DefaultTableModel) emergencyContactTable.getModel();
        TableRowSorter<DefaultTableModel> eRowSorter = new TableRowSorter<>(eTableOb);
        emergencyContactTable.setRowSorter(eRowSorter);
        eRowSorter.setRowFilter(RowFilter.regexFilter(searchText));

        // Insurance Table
        DefaultTableModel iTableOb = (DefaultTableModel) insuranceTable.getModel();
        TableRowSorter<DefaultTableModel> iRowSorter = new TableRowSorter<>(iTableOb);
        insuranceTable.setRowSorter(iRowSorter);
        iRowSorter.setRowFilter(RowFilter.regexFilter(searchText));    
    }//GEN-LAST:event_searchBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(searchPatient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(searchPatient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(searchPatient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(searchPatient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new searchTables().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Dashboard;
    private javax.swing.JButton Information;
    private javax.swing.JButton LogOut;
    private javax.swing.JButton TableSearch;
    private javax.swing.JButton backButton;
    private javax.swing.JTable emergencyContactTable;
    private javax.swing.JTable insuranceTable;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton logo;
    private javax.swing.JTable patientInformationTable;
    private javax.swing.JButton searchBtn;
    private javax.swing.JTextField searchField;
    private javax.swing.JPanel sidePanel;
    // End of variables declaration//GEN-END:variables
}