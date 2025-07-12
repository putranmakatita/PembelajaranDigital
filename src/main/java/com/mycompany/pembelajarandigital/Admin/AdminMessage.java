package com.mycompany.pembelajarandigital.Admin;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.mycompany.pembelajarandigital.DBConnection;
import javax.swing.JOptionPane;

public class AdminMessage {

    public static void sendNotif(String messageStr, String to_Type, int to_ID) {
        java.util.Date dt = new java.util.Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time_Stamp = dateFormat.format(dt);
        try {
            DBConnection c1 = new DBConnection();

            String q = "INSERT INTO Messages (message, time_Stamp, User_ID, User_Type, toUser_ID, toUser_Type) "
                    + "Values ('" + messageStr + "', '" + time_Stamp + "', '" + AdminLogin.currentAdminID
                    + "', 'admin', '" + to_ID + "', '" + to_Type + "')";
            int x = c1.s.executeUpdate(q);
            String q1 = "select Max(Message_ID) As Message_ID From Messages";
            ResultSet rs1 = c1.s.executeQuery(q1);
            rs1.next();
            int Message_ID = rs1.getInt("Message_ID");
            String q2 = "INSERT INTO MessageUsers (User_ID, User_Type,Message_ID) "
                    + "Values ('" + AdminLogin.currentAdminID + "', 'admin', '" + Message_ID + "' )";
            int x2 = c1.s.executeUpdate(q2);
            String q3 = "INSERT INTO MessageUsers (User_ID, User_Type,Message_ID) "
                    + "Values ('" + to_ID + "', '" + to_Type + "', '" + Message_ID + "' )";
            int x3 = c1.s.executeUpdate(q3);

            if (x == 0 || x2 == 0 || x3 == 0) {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan!");
            } else {
                JOptionPane.showMessageDialog(null, "Pesan Password terkirim!");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}