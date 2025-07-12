package com.mycompany.pembelajarandigital.Student;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.pembelajarandigital.DBConnection;
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

public class ViewParticipants extends JFrame implements ActionListener {
    JLabel title, courseCbLbl;
    JComboBox coursesCb;
    JPanel middlePanel;
    JScrollPane scroll;
    JTable table;
    DefaultTableModel model;
    JButton contactBtn;

    public ViewParticipants() {
        super("Lihat Kelas");
        setLayout(new BorderLayout());

        title = new JLabel("Kelas", JLabel.CENTER);
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
        pane.setBounds(20, 95, 550, 245);
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
            String q = "SELECT C.Name FROM Courses AS C "
                    + "INNER JOIN Enrollments AS E ON E.course_ID = C.course_ID "
                    + "WHERE E.stdID = '" + StudentLogin.currentStudentID + "'";

            ResultSet rs = c1.s.executeQuery(q);
            while (rs.next()) {
                courseList.add(rs.getString("Name"));
            }

            c1.Close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return courseList.toArray(new String[0]);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == coursesCb) {
            // Clear all data from JTable
            model.setRowCount(0);
            model.setColumnCount(0);

            Object selected = coursesCb.getSelectedItem();
            String courseName = selected.toString();

            try {
                DBConnection c1 = new DBConnection();

                // Fetch all students (except current) in this course
                String q = "SELECT S.stdID, S.fname, S.lname, S.Email_ID, S.Last_Login "
                        + "FROM Student AS S "
                        + "INNER JOIN Enrollments AS E ON E.stdID = S.stdID "
                        + "WHERE E.course_ID = (SELECT course_ID FROM Courses WHERE Name = ?) "
                        + "AND S.stdID <> ?";
                PreparedStatement ps = c1.c.prepareStatement(q);
                ps.setString(1, courseName);
                ps.setInt(2, StudentLogin.currentStudentID);
                ResultSet rs = ps.executeQuery();

                List<String[]> studentList = new ArrayList<>();
                while (rs.next()) {
                    String[] row = new String[5];
                    row[0] = rs.getString("stdID");
                    row[1] = rs.getString("fname") + " " + rs.getString("lname");
                    row[2] = rs.getString("Email_ID");
                    row[3] = rs.getString("Last_Login");
                    row[4] = "Siswa";
                    studentList.add(row);
                }

                // Fetch teacher for the course
                String q2 = "SELECT T.teacherID, T.fname, T.lname, T.Email_ID, T.Last_Login "
                        + "FROM Teacher AS T "
                        + "INNER JOIN Courses AS C ON C.teacherID = T.teacherID "
                        + "WHERE C.course_ID = (SELECT course_ID FROM Courses WHERE Name = ?)";
                PreparedStatement ps2 = c1.c.prepareStatement(q2);
                ps2.setString(1, courseName);
                ResultSet rs2 = ps2.executeQuery();

                String[][] teacherData = new String[1][5];
                if (rs2.next()) {
                    teacherData[0][0] = rs2.getString("teacherID");
                    teacherData[0][1] = rs2.getString("fname") + " " + rs2.getString("lname");
                    teacherData[0][2] = rs2.getString("Email_ID");
                    teacherData[0][3] = rs2.getString("Last_Login");
                    teacherData[0][4] = "Guru";
                }

                // Add columns
                String[] columnNames = { "ID", "Nama", "Email", "Terakhir Login", "Sebagai" };
                for (String col : columnNames) {
                    model.addColumn(col);
                }

                // Add teacher row first
                model.addRow(teacherData[0]);

                // Add student rows
                for (String[] studentRow : studentList) {
                    model.addRow(studentRow);
                }

                // Close DB connection
                c1.Close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (ae.getSource() == table) {
            // Optional: Handle table row selection if needed

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
        new ViewParticipants();
    }
}