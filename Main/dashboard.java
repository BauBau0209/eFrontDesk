/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.proteanit.sql.DbUtils;
import javax.swing.table.TableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
/**
 *
 * @author tessa
 */
public final class dashboard extends javax.swing.JFrame {
    private String[] patientData;
    private String[] contactData;
    private String[] insuranceData;  
    private String selectedRowId = "";
    
    public dashboard() {
        initComponents();
        getSelectedRow();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        fetchTable();
        fetchPatientCount();
        fetchInsuranceCount();
        showBarChart();
        showInsurancesPerGenderPieChart();
        showInsurancePerAgePieChart();
        fetchPatientCountByGender();
        fetchPatientsWithInsurance();
        fetchPatientsWithMultipleInsurance();
    }
    
    public dashboard(String username) {
        this();
        welcomeText.setText("Welcome Back, " + username);
    }
    
    public void showBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            Connection connection = dbConnect.Connect();

            // SQL query to get planName and count of patients
            String sql = "SELECT planName, COUNT(patientId) AS PatientCount "
                    + "FROM efrontdesk.insurance "
                    + "GROUP BY planName";

            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String planName = rs.getString("planName");
                int patientCount = rs.getInt("PatientCount");
                dataset.setValue(patientCount, "Amount", planName);
            }

            rs.close();
            pst.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }

        JFreeChart chart = ChartFactory.createBarChart(
            "PLAN NAME DISTRIBUTION",
            "Plan Name",
            "Number of Patients",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );
        
        TextTitle title = chart.getTitle();
        title.setPaint(new Color(27,15,119)); // Set title color

        chart.setBackgroundPaint(Color.white);

        CategoryPlot categoryPlot = chart.getCategoryPlot();
        categoryPlot.setBackgroundPaint(new Color(208,227,240));
        BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
        Color barColor = new Color(0, 102, 153);
        renderer.setSeriesPaint(0, barColor);

        // Set font color for axis labels
        CategoryAxis domainAxis = categoryPlot.getDomainAxis();
        domainAxis.setTickLabelPaint(new Color(27,15,119)); // Set font color for domain axis labels
        domainAxis.setLabelPaint(new Color(27,15,119));
        
        ValueAxis rangeAxis = categoryPlot.getRangeAxis();
        rangeAxis.setTickLabelPaint(new Color(27,15,119)); // Set font color for range axis labels
        rangeAxis.setLabelPaint(new Color(27,15,119));
        
        // Create chart panel
        ChartPanel barChartPanel = new ChartPanel(chart);
        barChartPanel1.removeAll();
        barChartPanel1.add(barChartPanel, BorderLayout.CENTER);
        barChartPanel1.validate();
    }
    
    public void showInsurancesPerGenderPieChart() {
        // Create dataset
        DefaultPieDataset pieDataset = new DefaultPieDataset();

        try (Connection connection = dbConnect.Connect();
             PreparedStatement pst = connection.prepareStatement(
                     "SELECT p.PatientGender, COUNT(i.InsuranceId) AS InsuranceCount " +
                     "FROM efrontdesk.patient p, efrontdesk.insurance i " +
                     "WHERE p.PatientId = i.PatientId " +
                     "GROUP BY p.PatientGender"
             );
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String gender = rs.getString("PatientGender");
                int insuranceCount = rs.getInt("InsuranceCount");
                pieDataset.setValue(gender, insuranceCount); // Set gender as the category label
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching data: " + e.getMessage());
            return;
        }

        // Create chart
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Insurances Per Gender",
                pieDataset,
                true,
                true,
                false
        );

        // Customize chart
        pieChart.getTitle().setPaint(new Color(27, 15, 119)); // Set title color

        PiePlot piePlot = (PiePlot) pieChart.getPlot();
        // Set colors for each section
        piePlot.setSectionPaint("Male", new Color(0, 153, 204));   // Medium blue for Male
        piePlot.setSectionPaint("Female", new Color(153, 0, 153)); // Purple for Female
        piePlot.setSectionPaint("Other", new Color(153, 153, 153));  // Gray for Other genders

        // Customize the plot (optional)
        piePlot.setBackgroundPaint(Color.white);
        // Add more customization as needed

        // Create chartPanel to display chart
        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setPreferredSize(new Dimension(420, 310));

        // Update pieChartPanel2 on the EDT
        SwingUtilities.invokeLater(() -> {
            pieChartPanel2.removeAll();
            pieChartPanel2.add(chartPanel, BorderLayout.CENTER);
            pieChartPanel2.revalidate();
            pieChartPanel2.repaint(); // Ensure repaint to reflect changes
        });
    }

    public void showInsurancePerAgePieChart() {
        // Create dataset
        DefaultPieDataset pieDataset = new DefaultPieDataset();

        try (Connection connection = dbConnect.Connect();
             PreparedStatement pst = connection.prepareStatement(
                     "SELECT CASE " +
                             "    WHEN p.PatientAge < 18 THEN 'Below 18' " +
                             "    WHEN p.PatientAge BETWEEN 18 AND 30 THEN '18-30' " +
                             "    WHEN p.PatientAge BETWEEN 31 AND 59 THEN '31-59' " +
                             "    ELSE '60 and above' " +
                             "END AS AgeGroup, " +
                             "COUNT(i.insuranceId) AS InsuranceCount " +
                             "FROM efrontdesk.patient AS p, efrontdesk.insurance AS i " +
                             "WHERE p.patientId = i.patientId " +
                             "GROUP BY AgeGroup"
             );
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String ageGroup = rs.getString("AgeGroup");
                int insuranceCount = rs.getInt("InsuranceCount");
                pieDataset.setValue(ageGroup, insuranceCount); // Set age group as the category label
            }

        } catch (SQLException e) {
            System.err.println("Error fetching data: " + e.getMessage());
            // Handle or log the exception appropriately
        }

        // Create chart
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Insurance Policies by Age Group",
                pieDataset,
                true,
                true,
                false
        );

        // Customize chart
        pieChart.getTitle().setPaint(new Color(27, 15, 119)); // Set title color

        PiePlot piePlot = (PiePlot) pieChart.getPlot();

        // Set colors for each section
        piePlot.setSectionPaint("Below 18", new Color(102, 204, 255)); // Light blue
        piePlot.setSectionPaint("18-30", new Color(0, 153, 204));      // Medium blue
        piePlot.setSectionPaint("31-59", new Color(0, 102, 153));      // Dark blue
        piePlot.setSectionPaint("60 and above", new Color(0, 51, 102));    // Navy blue

        // Customize the plot (optional)
        piePlot.setBackgroundPaint(Color.white);
        // Add more customization as needed

        // Create chartPanel to display chart
        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setPreferredSize(new Dimension(420, 310));

        // Update pieChartPanel1 on the EDT
        SwingUtilities.invokeLater(() -> {
            pieChartPanel1.removeAll();
            pieChartPanel1.add(chartPanel, BorderLayout.CENTER);
            pieChartPanel1.revalidate();
            pieChartPanel1.repaint(); // Ensure repaint to reflect changes
        });
    }

    private void getSelectedRow() {
        ListSelectionModel model = dashboardTable.getSelectionModel();
        model.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Ensure the event is processed only once
                if (!e.getValueIsAdjusting() && !model.isSelectionEmpty()) {
                    int selectedRow = dashboardTable.getSelectedRow();
                    if (selectedRow != -1) {
                        TableModel tableModel = dashboardTable.getModel();
                        selectedRowId = tableModel.getValueAt(selectedRow, 0).toString();
                        saveDataToString classSaveData = new saveDataToString();
                        patientData = classSaveData.getPatientData(selectedRowId);
                        contactData = classSaveData.getContactData(selectedRowId);
                        insuranceData = classSaveData.getInsuranceData(selectedRowId);

                        deleteButton.setEnabled(true);
                        updateButton.setEnabled(true);
                        showRowInfo(selectedRowId);
                    }
                }
            }
        });
    }
    
    public void showRowInfo(String patientId){
         try (Connection connection = dbConnect.Connect();
             PreparedStatement pst = connection.prepareStatement(
                     "SELECT PatientName, PatientDateOfBirth, PatientContactNo, PatientEmail, PatientAddress " +
                     "FROM efrontdesk.patient WHERE PatientId = ?")) {

            pst.setString(1, patientId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("PatientName");
                    String dob = rs.getString("PatientDateOfBirth");
                    String contactNo = rs.getString("PatientContactNo");
                    String email = rs.getString("PatientEmail");
                    String address = rs.getString("PatientAddress");

                    // Display or use the retrieved data as needed
                    nameLabel.setText(name);
                    patientIdLabel.setText(patientId);
                    dateOfBirthLabel.setText(dob);
                    contactNumberLabel.setText(contactNo);
                    emailLabel.setText(email);
                    addressLabel.setText(address);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void fetchTable() {
        try {
            Connection connection = dbConnect.Connect();
            ResultSet rs;
            PreparedStatement pst;

            String sql = "SELECT p.PatientID, p.PatientName, p.PatientGender, p.PatientAge, COALESCE(COUNT(i.insuranceId), 0) AS InsuranceCount " +
                         "FROM efrontdesk.patient AS p " +
                         "LEFT JOIN efrontdesk.insurance AS i ON p.patientId = i.patientId " +
                         "GROUP BY p.PatientID, p.PatientName, p.PatientGender, p.PatientAge";

            pst = connection.prepareStatement(sql);
            rs = pst.executeQuery();

            dashboardTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }   
    
    public void fetchPatientCount(){
        try{
            Connection connection = dbConnect.Connect();
            ResultSet rs;
            PreparedStatement pst;
            pst = connection.prepareStatement("SELECT COUNT(*) AS 'total' FROM efrontdesk.patient");
            rs = pst.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt("total");
                totalPatients.setText(String.valueOf(count));
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void fetchPatientsWithInsurance() {
        try {
            Connection connection = dbConnect.Connect();
            ResultSet rs;
            PreparedStatement pst;
            pst = connection.prepareStatement("SELECT COUNT(DISTINCT p.PatientId) AS totalPatientsWithInsurance " +
                                              "FROM efrontdesk.patient p, efrontdesk.insurance i " +
                                              "WHERE p.PatientId = i.PatientId");
            rs = pst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("totalPatientsWithInsurance");
                patientsWithInsurance.setText(String.valueOf(count)); // Display the total count of patients with insurance
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching data: " + e.getMessage());
        }
    }
    
    public void fetchPatientsWithMultipleInsurance() {
        try {
            Connection connection = dbConnect.Connect();
            ResultSet rs;
            PreparedStatement pst;
            pst = connection.prepareStatement("SELECT COUNT(*) AS patientsWithMultipleInsurance " +
                                              "FROM (" +
                                              "    SELECT PatientId " +
                                              "    FROM efrontdesk.insurance " +
                                              "    GROUP BY PatientId " +
                                              "    HAVING COUNT(*) > 1" +
                                              ") AS multipleInsurancePatients");
            rs = pst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("patientsWithMultipleInsurance");
                multipleInsurance.setText(String.valueOf(count)); // Display the total count of patients with multiple insurance
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching data: " + e.getMessage());
        }
    }
    
    public void fetchInsuranceCount() {
    try {
        Connection connection = dbConnect.Connect();
        ResultSet rs;
        PreparedStatement pst;
        pst = connection.prepareStatement("SELECT COUNT(*) AS 'total' FROM efrontdesk.insurance");
        rs = pst.executeQuery();
        
        if (rs.next()) {
            int count = rs.getInt("total");
            totalInsurances.setText(String.valueOf(count));
        }
    } catch(SQLException e) {
        JOptionPane.showMessageDialog(null, e);
    }
}
    
    public void fetchPatientCountByGender() {
        try {
            Connection connection = dbConnect.Connect();
            ResultSet rs;
            PreparedStatement pst;
            pst = connection.prepareStatement("SELECT PatientGender, COUNT(*) AS total FROM efrontdesk.patient GROUP BY PatientGender");
            rs = pst.executeQuery();

            int maleCount = 0;
            int femaleCount = 0;

            while (rs.next()) {
                String gender = rs.getString("PatientGender");
                int count = rs.getInt("total");

                if ("Male".equalsIgnoreCase(gender)) {
                    maleCount = count;
                } else if ("Female".equalsIgnoreCase(gender)) {
                    femaleCount = count;
                }
            }
           
            malePatientsLabel.setText(String.format("%d", maleCount)); // Assuming malePatientsLabel is a JLabel
            femalePatientsLabel.setText(String.format("%d", femaleCount)); // Assuming femalePatientsLabel is a JLabel
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dashboardTable = new javax.swing.JTable();
        addButton2 = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();
        Refresh = new javax.swing.JButton();
        sidePanel1 = new javax.swing.JPanel();
        showMoreButton = new javax.swing.JButton();
        nameLabel = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        dateOfBirthLabel = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        contactNumberLabel = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        patientIdLabel = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        emailLabel = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        addressLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        totalPatients = new javax.swing.JLabel();
        lbl1 = new javax.swing.JLabel();
        femalePatientsLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        malePatientsLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        barChartPanel1 = new javax.swing.JPanel();
        pieChartPanel1 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        welcomeText = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        welcomeText1 = new javax.swing.JLabel();
        welcomeText2 = new javax.swing.JLabel();
        welcomeText3 = new javax.swing.JLabel();
        pieChartPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        totalInsurances = new javax.swing.JLabel();
        lbl = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        patientsWithInsurance = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        multipleInsurance = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        sidePanel = new javax.swing.JPanel();
        TableSearch = new javax.swing.JButton();
        Dashboard = new javax.swing.JButton();
        LogOut = new javax.swing.JButton();
        Information = new javax.swing.JButton();
        logo = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(0, 0));

        mainPanel.setBackground(new java.awt.Color(208, 227, 240));
        mainPanel.setLayout(new java.awt.BorderLayout());

        jPanel.setBackground(new java.awt.Color(208, 227, 240));
        jPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel2.setBackground(new java.awt.Color(46, 62, 154));

        dashboardTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        dashboardTable.setRowHeight(35);
        dashboardTable.setGridColor(new java.awt.Color(255, 255, 255));
        dashboardTable.setSelectionBackground(new java.awt.Color(0, 153, 204));
        dashboardTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(dashboardTable);

        addButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addButton2.setText("Add");
        addButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButton2ActionPerformed(evt);
            }
        });

        deleteButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        deleteButton.setText("Delete");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        updateButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        updateButton.setText("Update");
        updateButton.setEnabled(false);
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        Refresh.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Refresh.setForeground(new java.awt.Color(255, 255, 255));
        Refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/Icons/icons8-refresh-24.png"))); // NOI18N
        Refresh.setBorderPainted(false);
        Refresh.setContentAreaFilled(false);
        Refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Refresh))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1123, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Refresh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(addButton2)
                        .addComponent(deleteButton)
                        .addComponent(updateButton)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1)
                .addGap(26, 26, 26))
        );

        sidePanel1.setBackground(new java.awt.Color(46, 62, 154));

        showMoreButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        showMoreButton.setForeground(new java.awt.Color(255, 255, 255));
        showMoreButton.setText("Show More");
        showMoreButton.setBorderPainted(false);
        showMoreButton.setContentAreaFilled(false);
        showMoreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showMoreButtonActionPerformed(evt);
            }
        });

        nameLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        nameLabel.setForeground(new java.awt.Color(255, 255, 255));
        nameLabel.setText("PATIENT NAME");
        nameLabel.setToolTipText("");
        nameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Date of Birth");
        jLabel12.setToolTipText("");

        dateOfBirthLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        dateOfBirthLabel.setForeground(new java.awt.Color(255, 255, 255));
        dateOfBirthLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/Icons/iconsbirthday.png"))); // NOI18N
        dateOfBirthLabel.setText("DATE OF BIRTH");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Contact Number");
        jLabel13.setToolTipText("");

        contactNumberLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        contactNumberLabel.setForeground(new java.awt.Color(255, 255, 255));
        contactNumberLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/Icons/icons8-telephone-15.png"))); // NOI18N
        contactNumberLabel.setText("CONTACT NUMBER");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Patient ID");
        jLabel14.setToolTipText("");

        patientIdLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        patientIdLabel.setForeground(new java.awt.Color(255, 255, 255));
        patientIdLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/Icons/icons8-list-15.png"))); // NOI18N
        patientIdLabel.setText("PATIENT ID");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Email");
        jLabel15.setToolTipText("");

        emailLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        emailLabel.setForeground(new java.awt.Color(255, 255, 255));
        emailLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/Icons/icons8-email-15.png"))); // NOI18N
        emailLabel.setText("EMAIL ADDRESS");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Address");
        jLabel16.setToolTipText("");

        addressLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        addressLabel.setForeground(new java.awt.Color(255, 255, 255));
        addressLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/Icons/icons8-address-15.png"))); // NOI18N
        addressLabel.setText("ADDRESS");

        javax.swing.GroupLayout sidePanel1Layout = new javax.swing.GroupLayout(sidePanel1);
        sidePanel1.setLayout(sidePanel1Layout);
        sidePanel1Layout.setHorizontalGroup(
            sidePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidePanel1Layout.createSequentialGroup()
                .addGroup(sidePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidePanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(sidePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateOfBirthLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(sidePanel1Layout.createSequentialGroup()
                        .addGroup(sidePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(sidePanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(showMoreButton, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(sidePanel1Layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addGroup(sidePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(contactNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(emailLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(patientIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(sidePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(nameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        sidePanel1Layout.setVerticalGroup(
            sidePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidePanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(patientIdLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateOfBirthLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contactNumberLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(emailLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addGap(21, 21, 21)
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addressLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addGap(50, 50, 50)
                .addComponent(showMoreButton)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(46, 62, 154));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("TOTAL PATIENTS");

        totalPatients.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalPatients.setForeground(new java.awt.Color(255, 255, 255));
        totalPatients.setText("000");

        lbl1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbl1.setForeground(new java.awt.Color(255, 255, 255));
        lbl1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/Icons/icons8-group-30 (1).png"))); // NOI18N

        femalePatientsLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        femalePatientsLabel.setForeground(new java.awt.Color(255, 255, 255));
        femalePatientsLabel.setText("000");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("FEMALE PATIENTS");

        malePatientsLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        malePatientsLabel.setForeground(new java.awt.Color(255, 255, 255));
        malePatientsLabel.setText("000");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("MALE PATIENTS");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(femalePatientsLabel))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(100, 100, 100))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(malePatientsLabel)
                        .addGap(127, 127, 127)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(lbl1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalPatients))
                    .addComponent(jLabel2))
                .addGap(63, 63, 63))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(femalePatientsLabel)
                            .addComponent(malePatientsLabel))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGap(31, 31, 31)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(jLabel3))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totalPatients)
                            .addComponent(lbl1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)))
                .addGap(11, 11, 11))
        );

        barChartPanel1.setBackground(new java.awt.Color(74, 86, 154));
        barChartPanel1.setLayout(new java.awt.BorderLayout());

        pieChartPanel1.setBackground(new java.awt.Color(83, 92, 145));
        pieChartPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        welcomeText.setBackground(new java.awt.Color(27, 15, 119));
        welcomeText.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        welcomeText.setForeground(new java.awt.Color(27, 15, 119));
        welcomeText.setText("Dashboard");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/Icons/Nurse (150px).png"))); // NOI18N

        welcomeText1.setBackground(new java.awt.Color(27, 15, 119));
        welcomeText1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        welcomeText1.setForeground(new java.awt.Color(27, 15, 119));
        welcomeText1.setText("Our mission: 'To provide compassionate and");

        welcomeText2.setBackground(new java.awt.Color(27, 15, 119));
        welcomeText2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        welcomeText2.setForeground(new java.awt.Color(27, 15, 119));
        welcomeText2.setText("comprehensive healthcare to all patients");

        welcomeText3.setBackground(new java.awt.Color(27, 15, 119));
        welcomeText3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        welcomeText3.setForeground(new java.awt.Color(27, 15, 119));
        welcomeText3.setText("with respect and excellence.'");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(welcomeText)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(welcomeText1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(welcomeText2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(welcomeText3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(welcomeText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(welcomeText1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(welcomeText2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(welcomeText3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pieChartPanel2.setBackground(new java.awt.Color(153, 0, 153));
        pieChartPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(46, 62, 154));

        totalInsurances.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalInsurances.setForeground(new java.awt.Color(255, 255, 255));
        totalInsurances.setText("000");

        lbl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbl.setForeground(new java.awt.Color(255, 255, 255));
        lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Main/Icons/icons8-insurance-50 (1).png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("TOTAL INSURANCES");

        patientsWithInsurance.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        patientsWithInsurance.setForeground(new java.awt.Color(255, 255, 255));
        patientsWithInsurance.setText("000");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("PATIENTS WITH INSURANCE");

        multipleInsurance.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        multipleInsurance.setForeground(new java.awt.Color(255, 255, 255));
        multipleInsurance.setText("000");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("WITH MULTIPLE INSURANCE");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel6)
                        .addGap(42, 42, 42)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(patientsWithInsurance)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(multipleInsurance)
                        .addGap(120, 120, 120)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(lbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalInsurances))
                    .addComponent(jLabel4))
                .addGap(54, 54, 54))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totalInsurances, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lbl, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(patientsWithInsurance)
                            .addComponent(multipleInsurance))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
        jPanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(24, 24, 24)
                        .addComponent(barChartPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pieChartPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sidePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pieChartPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(barChartPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pieChartPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pieChartPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sidePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );

        mainPanel.add(jPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 656, Short.MAX_VALUE)
                .addComponent(LogOut)
                .addGap(25, 25, 25))
        );

        getContentPane().add(sidePanel, java.awt.BorderLayout.LINE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void showMoreButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showMoreButtonActionPerformed
        if(selectedRowId.equals("")){
            JOptionPane.showMessageDialog(null, "Please click a row from the table!");
        } else {
            patientProfile patientProfileFrame = new patientProfile(selectedRowId);
            patientProfileFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            patientProfileFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            patientProfileFrame.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_showMoreButtonActionPerformed

    private void RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshActionPerformed
        fetchTable();
    }//GEN-LAST:event_RefreshActionPerformed

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        patientInfo patientInfoFrame = new patientInfo(patientData, contactData, insuranceData);
        patientInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        patientInfoFrame.setLocationRelativeTo(null);
        patientInfoFrame.setResizable(false);
        patientInfoFrame.setVisible(true);
    }//GEN-LAST:event_updateButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        dbDelete delete = new dbDelete(selectedRowId);
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void addButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButton2ActionPerformed
        patientInfo patientInfoFrame = new patientInfo();
        patientInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        patientInfoFrame.setLocationRelativeTo(null);
        patientInfoFrame.setResizable(false);
        patientInfoFrame.setVisible(true);
    }//GEN-LAST:event_addButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new dashboard().setVisible(true);
        });
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Dashboard;
    private javax.swing.JButton Information;
    private javax.swing.JButton LogOut;
    private javax.swing.JButton Refresh;
    private javax.swing.JButton TableSearch;
    private javax.swing.JButton addButton2;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JPanel barChartPanel1;
    private javax.swing.JLabel contactNumberLabel;
    private javax.swing.JTable dashboardTable;
    private javax.swing.JLabel dateOfBirthLabel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JLabel femalePatientsLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lbl;
    private javax.swing.JLabel lbl1;
    private javax.swing.JButton logo;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel malePatientsLabel;
    private javax.swing.JLabel multipleInsurance;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel patientIdLabel;
    private javax.swing.JLabel patientsWithInsurance;
    private javax.swing.JPanel pieChartPanel1;
    private javax.swing.JPanel pieChartPanel2;
    private javax.swing.JButton showMoreButton;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JPanel sidePanel1;
    private javax.swing.JLabel totalInsurances;
    private javax.swing.JLabel totalPatients;
    private javax.swing.JButton updateButton;
    private javax.swing.JLabel welcomeText;
    private javax.swing.JLabel welcomeText1;
    private javax.swing.JLabel welcomeText2;
    private javax.swing.JLabel welcomeText3;
    // End of variables declaration//GEN-END:variables
}
