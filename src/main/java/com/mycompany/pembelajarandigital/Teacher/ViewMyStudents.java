package com.mycompany.pembelajarandigital.Teacher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.pembelajarandigital.DBConnection;
import com.mycompany.pembelajarandigital.Student.StudentLogin;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ViewMyStudents extends JFrame implements ActionListener {
    JLabel title, courseCbLbl;
    JComboBox coursesCb;
    JPanel middlePanel;
    JScrollPane scroll;
    JTable table;
    DefaultTableModel model;
    JButton contactBtn;

    public ViewMyStudents() {
        super("Lihat Siswa");
        setLayout(new BorderLayout());

        title = new JLabel("Lihat Siswa", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(22.0f));
        title.setBackground(Color.LIGHT_GRAY);
        title.setForeground(Color.BLACK);
        title.setOpaque(true);
        add(title, BorderLayout.NORTH);

        middlePanel = new JPanel();
        middlePanel.setLayout(null);
        add(middlePanel, BorderLayout.CENTER);

        courseCbLbl = new JLabel("Pilih Pembelajaran");
        courseCbLbl.setFont(new Font(Font.SERIF, Font.BOLD, 16));
        courseCbLbl.setBounds(80, 50, 120, 28);
        courseCbLbl.setHorizontalAlignment(JLabel.LEFT);
        middlePanel.add(courseCbLbl);

        coursesCb = new JComboBox(this.getMyCourses());
        coursesCb.setSelectedIndex(-1);
        coursesCb.setBounds(200, 50, 140, 28);
        coursesCb.addActionListener(this);
        middlePanel.add(coursesCb);

        model = new DefaultTableModel();
        table = new JTable(model);
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.LIGHT_GRAY);
        JScrollPane pane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                String role = table.getValueAt(table.getSelectedRow(), 4).toString();
                // if (role == "Guru") {
                // contactBtn.setEnabled(false);

                // } else {
                // contactBtn.setEnabled(true);
                // }
                contactBtn.setEnabled(true);
            }
        });

        pane.setBounds(20, 150, 550, 200);
        middlePanel.add(pane);

        contactBtn = new JButton("Kontak");
        contactBtn.setToolTipText("Klik untuk mengirim pesan.");
        contactBtn.setPreferredSize(new Dimension(50, 40));
        contactBtn.addActionListener(this);
        add(contactBtn, BorderLayout.SOUTH);

        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 450);
        setLocation(420, 260);
        setVisible(true);
    }

    private String[] getMyCourses() {
        List<String> courseList = new ArrayList<>();
        try {
            DBConnection c1 = new DBConnection();
            String q1 = "SELECT Name FROM Courses WHERE teacherID = ?";
            PreparedStatement ps = c1.c.prepareStatement(q1);
            ps.setInt(1, TeacherLogin.currentTeacherID); // assuming it's int
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                courseList.add(rs.getString("Name"));
            }

            rs.close();
            ps.close();
            c1.Close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return courseList.toArray(new String[0]);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == coursesCb) {
            // Clear table data
            model.setRowCount(0);
            model.setColumnCount(0);

            Object selected = coursesCb.getSelectedItem();
            String courseName = selected.toString();

            try {
                DBConnection c1 = new DBConnection();
                String q = "SELECT S.stdID, S.fname, S.lname, S.Email_ID, S.Registration_Date, S.Gender "
                        + "FROM Student AS S "
                        + "INNER JOIN Enrollments AS E ON E.stdID = S.stdID "
                        + "WHERE E.course_ID = (SELECT course_ID FROM Courses WHERE Name = ?)";

                PreparedStatement ps = c1.c.prepareStatement(q);
                ps.setString(1, courseName);
                ResultSet rs = ps.executeQuery();

                // Get column names
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                String[] columnNames = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    columnNames[i] = rsmd.getColumnName(i + 1);
                    model.addColumn(columnNames[i]); // add column to JTable
                }

                // Read data into list
                List<String[]> dataList = new ArrayList<>();
                while (rs.next()) {
                    String[] row = new String[columnCount];
                    for (int c = 0; c < columnCount; c++) {
                        row[c] = rs.getString(columnNames[c]);
                    }
                    dataList.add(row);
                }

                // Add rows to table
                for (String[] rowData : dataList) {
                    model.addRow(rowData);
                }

                rs.close();
                ps.close();
                c1.Close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == table) {
            // Handle table click if needed
        } else if (ae.getSource() == contactBtn) {
            if (table.getSelectedRow() != -1) {
                int rowNum = table.getSelectedRow();
                int to_id = Integer.parseInt(model.getValueAt(rowNum, 0).toString());
                String to_role = model.getValueAt(rowNum, 4).toString();
                String toName = model.getValueAt(rowNum, 1).toString();
                new Message(StudentLogin.currentStudentID, toName, to_id, to_role);
            }
        }
    }

    public static void main(String[] args) {
        new ViewMyStudents();
    }
}
