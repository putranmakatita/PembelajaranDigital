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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class StudyCourse extends JFrame implements ActionListener {
    JLabel title, subjectCbLbl, courseCbLbl, courseContentLbl;
    JTextArea courseContent;
    JComboBox coursesCb;
    JPanel middlePanel;
    JScrollPane scroll;

    public StudyCourse() {
        super("Belajar");
        setLayout(new BorderLayout());

        title = new JLabel("Belajar", JLabel.CENTER);
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
        courseCbLbl.setBounds(80, 80, 120, 28);
        courseCbLbl.setHorizontalAlignment(JLabel.LEFT);
        middlePanel.add(courseCbLbl);

        coursesCb = new JComboBox(this.getCourses());
        coursesCb.setSelectedIndex(-1);
        coursesCb.setBounds(200, 80, 140, 28);
        coursesCb.addActionListener(this);
        middlePanel.add(coursesCb);

        courseContentLbl = new JLabel("Konten Pembelajaran");
        courseContentLbl.setFont(new Font(Font.SERIF, Font.BOLD, 16));
        courseContentLbl.setBounds(80, 160, 120, 28);
        courseContentLbl.setHorizontalAlignment(JLabel.LEFT);
        middlePanel.add(courseContentLbl);

        courseContent = new JTextArea();
        courseContent.setLineWrap(true);
        courseContent.setWrapStyleWord(true);
        scroll = new JScrollPane(courseContent);
        scroll.setBounds(10, 190, 570, 180);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        courseContent.setEditable(false);
        middlePanel.add(scroll);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(600, 450);
        setLocation(420, 260);
        setVisible(true);
    }

    private String[] getCourses() {
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

            c1.Close(); // Don't forget to close the connection
        } catch (Exception e) {
            e.printStackTrace();
        }

        return courseList.toArray(new String[0]);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == coursesCb) {
            Object selected = coursesCb.getSelectedItem();
            String courseName = selected.toString();
            try {
                DBConnection c1 = new DBConnection();
                String q1 = "Select Content From Courses Where Name = '" + courseName + "'";
                ResultSet rs = c1.s.executeQuery(q1);
                rs.next();
                String course_Content;
                course_Content = rs.getString("Content");
                courseContent.setText(course_Content);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new StudyCourse();
    }

}