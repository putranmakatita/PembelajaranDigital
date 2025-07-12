package com.mycompany.pembelajarandigital.Teacher;

import com.mycompany.pembelajarandigital.DBConnection;
import javax.swing.JOptionPane;

public class TeacherDeleteAccount {
    int input;

    public TeacherDeleteAccount() {

        input = JOptionPane.showConfirmDialog(null, "Apakah anda yakin?", "Pilih opsi...",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (input == 0) {
            try {
                DBConnection c1 = new DBConnection();

                String q = "Delete From Teacher Where teacherID ='" + TeacherLogin.currentTeacherID + "'";

                int x = c1.s.executeUpdate(q);
                if (x == 0) {
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan!");
                } else {
                    JOptionPane.showMessageDialog(null, "Akun berhasil dihapus");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        new TeacherDeleteAccount();
    }
}
