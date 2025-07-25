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
import javax.swing.JTextField;

public class TeacherChangeName extends JFrame implements ActionListener {
    JLabel newFNamelbl;
    JLabel newLNamelbl;
    JTextField newFName;
    JTextField newLName;
    JButton updateNameButton;

    public TeacherChangeName() {
        newFNamelbl = new JLabel("Nama Awal");
        newFNamelbl.setFont(new Font("Tahoma", Font.PLAIN, 15));
        newFNamelbl.setBounds(10, 10, 150, 30);
        add(newFNamelbl);

        newFName = new JTextField();
        newFName.setFont(new Font("Tahoma", Font.PLAIN, 20));
        newFName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        newFName.setBounds(150, 10, 150, 40);
        add(newFName);
        newFName.setColumns(20);

        newLNamelbl = new JLabel("Nama Akhir");
        newLNamelbl.setFont(new Font("Tahoma", Font.PLAIN, 15));
        newLNamelbl.setBounds(10, 80, 150, 30);
        add(newLNamelbl);

        newLName = new JTextField();
        newLName.setFont(new Font("Tahoma", Font.PLAIN, 20));
        newLName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        newLName.setBounds(150, 80, 150, 40);
        add(newLName);
        newLName.setColumns(20);

        updateNameButton = new JButton("Update");
        updateNameButton.setBounds(80, 140, 150, 30);
        updateNameButton.addActionListener((ActionListener) this);
        add(updateNameButton);

        // ChangePassFrame.setResizable(false);
        setLayout(null);
        setSize(320, 220);
        setLocation(500, 320);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateNameButton) {
            String FirstName = newFName.getText();
            String LastName = newLName.getText();
            try {
                DBConnection c1 = new DBConnection();

                String q = "update Teacher SET fname = '" + FirstName + "', lname = '" + LastName + "'"
                        + "Where teacherID ='" + TeacherLogin.currentTeacherID + "'";

                int x = c1.s.executeUpdate(q);
                if (x == 0) {
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan!");
                } else {
                    JOptionPane.showMessageDialog(null, "Nama berhasil disunting!");
                    setVisible(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new TeacherChangeName();
    }
}
