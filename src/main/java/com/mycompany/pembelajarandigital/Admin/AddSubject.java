package com.mycompany.pembelajarandigital.Admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mycompany.pembelajarandigital.DBConnection;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddSubject extends JFrame implements ActionListener {
    JLabel title, subjectCbLbl;
    JButton addBtn;
    JPanel middlePanel;
    JTextField subjectName;

    public AddSubject() {
        super("Tambah Mata Pelajaran");
        setLayout(new BorderLayout());

        title = new JLabel("Tambah Mata Pelajaran", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(22.0f));
        title.setBackground(Color.LIGHT_GRAY);
        title.setForeground(Color.BLACK);
        title.setOpaque(true);
        add(title, BorderLayout.NORTH);

        middlePanel = new JPanel();
        middlePanel.setLayout(null);
        add(middlePanel, BorderLayout.CENTER);

        subjectCbLbl = new JLabel("Nama Mata Pelajaran");
        subjectCbLbl.setFont(new Font(Font.SERIF, Font.BOLD, 10));
        subjectCbLbl.setBounds(80, 50, 120, 28);
        subjectCbLbl.setHorizontalAlignment(JLabel.CENTER);
        middlePanel.add(subjectCbLbl);

        subjectName = new JTextField();
        subjectName.setBounds(200, 50, 140, 28);
        middlePanel.add(subjectName);

        addBtn = new JButton("Tambah");
        addBtn.setFont(new Font(Font.SERIF, Font.BOLD, 15));
        addBtn.setHorizontalAlignment(JButton.CENTER);
        addBtn.addActionListener(this);
        add(addBtn, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(500, 250);
        setLocation(420, 320);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addBtn) {
            String subject_Name = subjectName.getText();
            try {
                DBConnection c1 = new DBConnection();

                String q = "INSERT INTO Subjects (Name, Adminid) "
                        + "Values ('" + subject_Name + "', '" + AdminLogin.currentAdminID + "')";

                int x = c1.s.executeUpdate(q);
                if (x == 0) {
                    JOptionPane.showMessageDialog(null, "Mata Pelajaran sudah tersedia!");
                } else {
                    JOptionPane.showMessageDialog(null, "Mata Pelajaran berhasil ditambahkan!");
                    dispose();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan!");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new AddSubject();
    }

}
