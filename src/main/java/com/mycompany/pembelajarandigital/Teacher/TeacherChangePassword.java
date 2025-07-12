package com.mycompany.pembelajarandigital.Teacher;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mycompany.pembelajarandigital.DBConnection;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class TeacherChangePassword extends JFrame implements ActionListener {
    JLabel newPasswordlbl;
    JPasswordField newPassword;
    JButton updatePassbtn;

    public TeacherChangePassword() {
        newPasswordlbl = new JLabel("Password Baru");
        newPasswordlbl.setFont(new Font("Tahoma", Font.PLAIN, 15));
        newPasswordlbl.setBounds(10, 10, 150, 30);
        add(newPasswordlbl);

        newPassword = new JPasswordField();
        newPassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
        newPassword.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        newPassword.setBounds(150, 10, 150, 40);
        add(newPassword);
        newPassword.setColumns(20);

        updatePassbtn = new JButton("Sunting");
        updatePassbtn.setBounds(80, 90, 150, 30);
        updatePassbtn.addActionListener((ActionListener) this);
        add(updatePassbtn);

        // ChangePassFrame.setResizable(false);
        setLayout(null);
        setSize(320, 220);
        setLocation(500, 320);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updatePassbtn) {
            String password = String.valueOf(newPassword.getPassword());
            try {
                DBConnection c1 = new DBConnection();

                String q = "update Teacher SET password = '" + password + "'"
                        + "Where teacherID ='" + TeacherLogin.currentTeacherID + "'";

                int x = c1.s.executeUpdate(q);
                if (x == 0) {
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan!");
                } else {
                    JOptionPane.showMessageDialog(null, "Password berhasil disunting!");
                    setVisible(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new TeacherChangePassword();
    }
}
