package com.mycompany.pembelajarandigital.Admin;

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

public class DeleteSubject extends JFrame implements ActionListener {
    JLabel title, subjectCbLbl;
    JComboBox subjectsCb, coursesCb;
    JButton deleteBtn;
    JPanel middlePanel;

    public DeleteSubject() {
        super("Hapus Mata Pelajaran");
        setLayout(new BorderLayout());

        title = new JLabel("Hapus Mata Pelajaran", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(22.0f));
        title.setBackground(Color.LIGHT_GRAY);
        title.setForeground(Color.BLACK);
        title.setOpaque(true);
        add(title, BorderLayout.NORTH);

        middlePanel = new JPanel();
        middlePanel.setLayout(null);
        add(middlePanel, BorderLayout.CENTER);

        subjectCbLbl = new JLabel("Pilih Mata Pelajaran");
        subjectCbLbl.setFont(new Font(Font.SERIF, Font.BOLD, 16));
        subjectCbLbl.setBounds(80, 30, 120, 28);
        subjectCbLbl.setHorizontalAlignment(JLabel.CENTER);
        middlePanel.add(subjectCbLbl);

        subjectsCb = new JComboBox(this.getSubjects());
        subjectsCb.setSelectedIndex(-1);
        subjectsCb.setBounds(200, 30, 140, 28);
        subjectsCb.addActionListener(this);
        middlePanel.add(subjectsCb);

        deleteBtn = new JButton("Hapus");
        deleteBtn.setFont(new Font(Font.SERIF, Font.BOLD, 15));
        deleteBtn.setHorizontalAlignment(JButton.CENTER);
        deleteBtn.addActionListener(this);
        add(deleteBtn, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(500, 200);
        setLocation(420, 320);
        setVisible(true);
    }

    private String[] getSubjects() {
        List<String> subjectsList = new ArrayList<>();
        try {
            DBConnection c1 = new DBConnection();

            String q = "SELECT Name FROM Subjects";
            ResultSet rs = c1.s.executeQuery(q);

            while (rs.next()) {
                subjectsList.add(rs.getString("Name"));
            }

            rs.close();
            c1.Close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subjectsList.toArray(new String[0]);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == deleteBtn) {
            Object selected = subjectsCb.getSelectedItem();
            String subjectName = selected.toString();
            int input = JOptionPane.showConfirmDialog(null,
                    "Menghapus mata pelajaran akan berdampak pada terhapusnya data pembelajaran terkait"
                            + ", apakah anda yakin?",
                    "Pilih opsi...",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

            if (input == 0) {
                DBConnection c1 = new DBConnection();
                try {
                    String q = "Delete From Subjects Where Name ='" + subjectName + "'";
                    int x = c1.s.executeUpdate(q);
                    if (x == 0) {
                        JOptionPane.showMessageDialog(null, "Terjadi Kesalahan!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Mata Pelajaran berhasil dihapus!");
                        dispose();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    c1.Close();
                }
            }
        }
    }

    public static void main(String[] args) {
        new DeleteSubject();
    }

}