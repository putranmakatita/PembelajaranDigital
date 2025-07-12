package com.mycompany.pembelajarandigital.Student;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.pembelajarandigital.DBConnection;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class EnrollCourse extends JFrame implements ActionListener {
    JLabel title, subjectCbLbl, courseCbLbl, courseDescriptionLbl;
    JTextArea courseDescription;
    JComboBox subjectsCb, coursesCb;
    JButton enrollBtn;
    JPanel middlePanel;
    JScrollPane scroll;

    public EnrollCourse() {
        super("Ambil pembelajaran");
        setLayout(new BorderLayout());

        title = new JLabel("Ambil pembelajaran", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(22.0f));
        title.setBackground(Color.LIGHT_GRAY);
        title.setForeground(Color.BLACK);
        title.setOpaque(true);
        add(title, BorderLayout.NORTH);

        middlePanel = new JPanel();
        middlePanel.setLayout(null);
        add(middlePanel, BorderLayout.CENTER);

        subjectCbLbl = new JLabel("Pilih Mata Pelajaran");
        subjectCbLbl.setFont(new Font(Font.SERIF, Font.BOLD, 10));
        subjectCbLbl.setBounds(80, 30, 120, 28);
        subjectCbLbl.setHorizontalAlignment(JLabel.CENTER);
        middlePanel.add(subjectCbLbl);

        subjectsCb = new JComboBox(this.getSubjects());
        subjectsCb.setSelectedIndex(-1);
        subjectsCb.setBounds(200, 30, 140, 28);
        subjectsCb.addActionListener(this);
        middlePanel.add(subjectsCb);

        courseCbLbl = new JLabel("Pilih Pembelajaran");
        courseCbLbl.setFont(new Font(Font.SERIF, Font.BOLD, 10));
        courseCbLbl.setBounds(80, 80, 120, 28);
        courseCbLbl.setHorizontalAlignment(JLabel.CENTER);
        middlePanel.add(courseCbLbl);

        coursesCb = new JComboBox();
        coursesCb.setSelectedIndex(-1);
        coursesCb.setBounds(200, 80, 140, 28);
        coursesCb.addActionListener(this);
        middlePanel.add(coursesCb);

        courseDescriptionLbl = new JLabel("Deskripsi Pembelajaran");
        courseDescriptionLbl.setFont(new Font(Font.SERIF, Font.BOLD, 10));
        courseDescriptionLbl.setBounds(80, 130, 140, 28);
        courseDescriptionLbl.setHorizontalAlignment(JLabel.CENTER);
        middlePanel.add(courseDescriptionLbl);

        courseDescription = new JTextArea();
        courseDescription.setLineWrap(true);
        courseDescription.setWrapStyleWord(true);
        scroll = new JScrollPane(courseDescription);
        scroll.setBounds(150, 160, 230, 100);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        courseDescription.setEditable(false);
        middlePanel.add(scroll);

        enrollBtn = new JButton("Ambil");
        enrollBtn.setFont(new Font(Font.SERIF, Font.BOLD, 15));
        enrollBtn.setHorizontalAlignment(JButton.CENTER);
        enrollBtn.addActionListener(this);
        add(enrollBtn, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(500, 400);
        setLocation(420, 320);
        setVisible(true);
    }

    private String[] getSubjects() {
        List<String> subjectList = new ArrayList<>();
        try {
            DBConnection c1 = new DBConnection();

            String q = "SELECT Name FROM Subjects";
            ResultSet rs = c1.s.executeQuery(q);

            while (rs.next()) {
                subjectList.add(rs.getString("Name"));
            }
            c1.Close(); // Don't forget to close the connection
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subjectList.toArray(new String[0]);
    }

    private void getSetCourses(int subjectID) {
        try {
            DBConnection c1 = new DBConnection();
            String q = "SELECT Name FROM Courses WHERE Subject_ID = '" + subjectID + "'";

            ResultSet rs = c1.s.executeQuery(q);
            List<String> coursesData = new ArrayList<>();

            while (rs.next()) {
                coursesData.add(rs.getString("Name"));
            }

            // Populate the ComboBox
            for (String courseName : coursesData) {
                coursesCb.addItem(courseName);
            }

            c1.Close(); // Don't forget to close the connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == subjectsCb) {
            coursesCb.removeAllItems();
            courseDescription.setText(null);
            Object selected = subjectsCb.getSelectedItem();
            String subjectName = selected.toString();
            try {
                DBConnection c1 = new DBConnection();
                String q1 = "Select Subject_ID From Subjects Where Name = '" + subjectName + "'";
                ResultSet rs = c1.s.executeQuery(q1);
                int subjectID;
                rs.next();
                subjectID = rs.getInt("Subject_ID");
                this.getSetCourses(subjectID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == coursesCb) {
            Object selectedMain = coursesCb.getSelectedItem();
            if (selectedMain == null) {
                System.out.println("Null Value");
            } else {
                Object selected = coursesCb.getSelectedItem();
                String courseName = selected.toString();
                try {
                    DBConnection c1 = new DBConnection();
                    String q1 = "Select Description From Courses Where Name = '" + courseName + "'";
                    ResultSet rs = c1.s.executeQuery(q1);
                    String course_Description;
                    rs.next();
                    course_Description = rs.getString("Description");
                    // courseDescriptionLbl.setVisible(true);
                    // scroll.setVisible(true);
                    courseDescription.setText(course_Description);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (ae.getSource() == enrollBtn) {
            String courseNameStr = coursesCb.getSelectedItem().toString();
            java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
            try {
                DBConnection c1 = new DBConnection();
                String q1 = "Select course_ID From Courses Where Name = '" + courseNameStr + "'";
                ResultSet rs = c1.s.executeQuery(q1);
                int course_ID;
                rs.next();
                course_ID = rs.getInt("course_ID");

                String q = "INSERT INTO Enrollments (Enrollment_Date, course_ID, stdID) "
                        + "Values ('" + sqlDate + "', '" + course_ID + "', '" + StudentLogin.currentStudentID + "')";

                int x = c1.s.executeUpdate(q);
                if (x == 0) {
                    JOptionPane.showMessageDialog(null, "Terjadi Kesalahan!");
                } else {
                    JOptionPane.showMessageDialog(null, "Berhasil mengambil pembelajaran!");
                    dispose();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Terjadi Kesalahan!");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new EnrollCourse();
    }

}