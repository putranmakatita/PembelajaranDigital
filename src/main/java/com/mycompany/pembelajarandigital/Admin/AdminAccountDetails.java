package com.mycompany.pembelajarandigital.Admin;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mycompany.pembelajarandigital.DBConnection;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class AdminAccountDetails extends JFrame implements ActionListener {
    JPanel contentPane;
    JTextField firstname, lastname, email, username, passwordField, gender;
    JButton OkButton;

    public AdminAccountDetails() {
        super("Detail Admin");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1014, 550);
        setLocation(210, 110);

        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTeacherDetails = new JLabel("Detail Admin");
        lblTeacherDetails.setFont(new Font("Times New Roman", Font.PLAIN, 42));
        lblTeacherDetails.setBounds(362, 52, 325, 50);
        contentPane.add(lblTeacherDetails);

        JLabel lblName = new JLabel("Nama Awal");
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblName.setBounds(58, 152, 99, 43);
        contentPane.add(lblName);

        JLabel lblNewLabel = new JLabel("Nama Akhir");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel.setBounds(58, 243, 110, 29);
        contentPane.add(lblNewLabel);

        JLabel lblEmailAddress = new JLabel("Alamat Email");
        lblEmailAddress.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblEmailAddress.setBounds(58, 324, 124, 36);
        contentPane.add(lblEmailAddress);

        String firstName = null, lastName = null,
                emailId = null, userName = null,
                password = null, userGender = null;
        try {
            DBConnection c1 = new DBConnection();
            String q = "select * from Admin where Adminid = '" + AdminLogin.currentAdminID + "'";

            ResultSet rs = c1.s.executeQuery(q);
            if (rs.next()) {
                firstName = rs.getString("fname");
                lastName = rs.getString("lname");
                emailId = rs.getString("Email_ID");
                userName = rs.getString("username");
                password = rs.getString("password");
                userGender = rs.getString("Gender");
            } else {
                JOptionPane.showMessageDialog(null, "Tidak Ditemukan");
            }
        } catch (HeadlessException | NumberFormatException | SQLException e) {
            e.printStackTrace();
        }

        firstname = new JTextField();
        firstname.setFont(new Font("Tahoma", Font.PLAIN, 25));
        firstname.setBounds(214, 151, 228, 50);
        firstname.setEditable(false);
        firstname.setText(firstName);
        contentPane.add(firstname);

        lastname = new JTextField();
        lastname.setFont(new Font("Tahoma", Font.PLAIN, 25));
        lastname.setBounds(214, 235, 228, 50);
        lastname.setEditable(false);
        lastname.setText(lastName);
        contentPane.add(lastname);

        email = new JTextField();
        email.setFont(new Font("Tahoma", Font.PLAIN, 25));
        email.setBounds(214, 320, 320, 50);
        email.setEditable(false);
        email.setText(emailId);
        contentPane.add(email);

        username = new JTextField();
        username.setFont(new Font("Tahoma", Font.PLAIN, 25));
        username.setBounds(707, 151, 228, 50);
        username.setEditable(false);
        username.setText(userName);
        contentPane.add(username);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblUsername.setBounds(542, 159, 99, 29);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblPassword.setBounds(542, 245, 99, 24);
        contentPane.add(lblPassword);

        passwordField = new JTextField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 25));
        passwordField.setBounds(707, 235, 228, 50);
        passwordField.setEditable(false);
        passwordField.setText(password);
        contentPane.add(passwordField);

        JLabel lblGender = new JLabel("Gender");
        lblGender.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblGender.setBounds(542, 321, 99, 24);
        contentPane.add(lblGender);

        gender = new JTextField();
        gender.setFont(new Font("Tahoma", Font.PLAIN, 25));
        gender.setBounds(707, 311, 228, 50);
        gender.setEditable(false);
        gender.setText(userGender);
        contentPane.add(gender);

        OkButton = new JButton("Ok");
        OkButton.setBounds(410, 440, 228, 60);
        OkButton.addActionListener((ActionListener) this);
        contentPane.add(OkButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == OkButton) {
            dispose();
        }
    }

    public static void main(String[] args) {
        new AdminAccountDetails();
    }
}
