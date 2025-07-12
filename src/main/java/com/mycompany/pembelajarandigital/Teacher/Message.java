package com.mycompany.pembelajarandigital.Teacher;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.mycompany.pembelajarandigital.DBConnection;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class Message extends JFrame implements ActionListener {
    JTextArea messageTextArea;
    JButton sendBtn;
    JLabel receiverNameLabel, writeMsgLbl;
    JScrollPane scroll;
    JPanel mainPanel;
    int to_ID;
    String to_Type;

    public Message(int from_ID, String toName, int to_ID, String to_Type) {
        super("Pesan");
        setLayout(new BorderLayout());
        this.to_ID = to_ID;
        this.to_Type = to_Type;
        receiverNameLabel = new JLabel(toName);
        receiverNameLabel.setHorizontalAlignment(JLabel.CENTER);
        receiverNameLabel.setFont(new Font(Font.SERIF, Font.BOLD, 23));
        add(receiverNameLabel, BorderLayout.NORTH);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        writeMsgLbl = new JLabel("Tulis Pesan");
        writeMsgLbl.setHorizontalAlignment(JLabel.LEFT);
        writeMsgLbl.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        mainPanel.add(writeMsgLbl, BorderLayout.NORTH);

        messageTextArea = new JTextArea();
        messageTextArea.setLineWrap(true);
        messageTextArea.setWrapStyleWord(true);
        messageTextArea.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
        messageTextArea.setToolTipText("Tulis Pesan disini.");
        scroll = new JScrollPane(messageTextArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        mainPanel.add(scroll, BorderLayout.CENTER);

        sendBtn = new JButton("Kirim");
        sendBtn.setPreferredSize(new Dimension(0, 50));
        sendBtn.addActionListener(this);
        add(sendBtn, BorderLayout.SOUTH);

        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 320);
        setLocation(515, 300);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendBtn) {
            String messageStr = messageTextArea.getText();
            java.util.Date dt = new java.util.Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String time_Stamp = dateFormat.format(dt);
            try {
                DBConnection c1 = new DBConnection();

                String q = "INSERT INTO Messages (message, time_Stamp, User_ID, User_Type,toUser_ID,toUser_Type) "
                        + "Values ('" + messageStr + "', '" + time_Stamp + "', '" + TeacherLogin.currentTeacherID
                        + "','guru', "
                        + " '" + to_ID + "', '" + to_Type + "')";
                int x = c1.s.executeUpdate(q);
                String q1 = "select Max(Message_ID) As Message_ID From Messages";
                ResultSet rs1 = c1.s.executeQuery(q1);
                rs1.next();
                int Message_ID = rs1.getInt("Message_ID");
                String q2 = "INSERT INTO MessageUsers (User_ID, User_Type,Message_ID) "
                        + "Values ('" + TeacherLogin.currentTeacherID + "', 'guru'," + Message_ID + "' )";
                int x2 = c1.s.executeUpdate(q2);
                String q3 = "INSERT INTO MessageUsers (User_ID, User_Type,Message_ID) "
                        + "Values ('" + to_ID + "', '" + to_Type + "', '" + Message_ID + "' )";
                int x3 = c1.s.executeUpdate(q3);

                if (x == 0 || x2 == 0 || x3 == 0) {
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan!");
                } else {
                    JOptionPane.showMessageDialog(null, "Pesan terkirim");
                    dispose();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}