package com.mycompany.pembelajarandigital.Student;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class WithdrawCourse extends JFrame implements ActionListener {
    JLabel title, courseCbLbl;
    JComboBox courseCb;
    JButton withdrawBtn;
    JPanel middlePanel;

    public WithdrawCourse() {
        super("Keluar Pembelajaran");
        setLayout(new BorderLayout());

        title = new JLabel("Keluar Pembelajaran", JLabel.CENTER);
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
        courseCbLbl.setBounds(80, 30, 120, 28);
        courseCbLbl.setHorizontalAlignment(JLabel.CENTER);
        middlePanel.add(courseCbLbl);

        courseCb = new JComboBox(this.getCourses());
        courseCb.setSelectedIndex(-1);
        courseCb.setBounds(200, 30, 140, 28);
        courseCb.addActionListener(this);
        middlePanel.add(courseCb);

        withdrawBtn = new JButton("Keluar");
        withdrawBtn.setFont(new Font(Font.SERIF, Font.BOLD, 15));
        withdrawBtn.setHorizontalAlignment(JButton.CENTER);
        withdrawBtn.addActionListener(this);
        add(withdrawBtn, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(500, 200);
        setLocation(420, 320);
        setVisible(true);
    }

    private String[] getCourses() {
        List<String> courseList = new ArrayList<>();
        try {
            DBConnection c1 = new DBConnection();

            String q = "SELECT C.Name FROM Courses AS C " +
                    "INNER JOIN Enrollments AS E ON E.course_ID = C.course_ID " +
                    "WHERE E.stdID = ?";

            PreparedStatement ps = c1.c.prepareStatement(q);
            ps.setInt(1, StudentLogin.currentStudentID); // ✅ correct usage
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
        if (ae.getSource() == withdrawBtn) {
            Object selected = courseCb.getSelectedItem();
            String course_Name = selected.toString();
            int input = JOptionPane.showConfirmDialog(null, "Apakah anda yakin hendak keluar dari pembelajaran?",
                    "Pilih opsi...",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

            if (input == 0) {
                try {
                    DBConnection c1 = new DBConnection();

                    String q = "Delete From Enrollments Where course_ID = (Select course_ID From Courses Where Name = '"
                            + course_Name + "')"
                            + " And stdID = '" + StudentLogin.currentStudentID + "'";
                    int x = c1.s.executeUpdate(q);
                    if (x == 0) {
                        JOptionPane.showMessageDialog(null, "Terjadi kesalahan!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Berhasil keluar dari pembelajaran!");
                        dispose();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new WithdrawCourse();
    }

}