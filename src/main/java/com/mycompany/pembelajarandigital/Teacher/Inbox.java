package com.mycompany.pembelajarandigital.Teacher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.pembelajarandigital.DBConnection;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Inbox extends JFrame implements ActionListener, ListSelectionListener {
    JList messagesList;
    DefaultListModel listModel;
    JLabel title, messageLbl;
    JPanel mainPanel;
    JTextArea messageTxt;
    JButton replyBtn, deleteBtn;
    JScrollPane scroll1, scroll2;
    String[][] messagesListData;
    int currentFromUserID, currentMessageID;
    String currentFromUserName, currentFromType;

    public Inbox() {
        super("Inbox");
        setLayout(new BorderLayout());

        title = new JLabel("Inbox");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font(Font.SERIF, Font.BOLD, 23));
        title.setBackground(Color.LIGHT_GRAY);
        title.setForeground(Color.BLACK);
        title.setOpaque(true);
        add(title, BorderLayout.NORTH);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        add(mainPanel, BorderLayout.CENTER);

        listModel = new DefaultListModel();
        messagesList = new JList(listModel);
        messagesList.setFixedCellHeight(40);
        messagesList.setFixedCellWidth(150);
        messagesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        messagesList.addListSelectionListener(this);
        scroll1 = new JScrollPane(messagesList);
        scroll1.setBounds(50, 50, 500, 220);
        mainPanel.add(scroll1);
        this.loadMessages();

        messageLbl = new JLabel("Pesan");
        messageLbl.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        messageLbl.setBounds(30, 270, 70, 50);
        mainPanel.add(messageLbl);

        messageTxt = new JTextArea();
        messageTxt.setLineWrap(true);
        messageTxt.setWrapStyleWord(true);
        messageTxt.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
        messageTxt.setEditable(false);
        scroll2 = new JScrollPane(messageTxt);
        scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll2.setBounds(30, 310, 550, 210);
        mainPanel.add(scroll2);

        replyBtn = new JButton("Balas");
        replyBtn.addActionListener(this);
        replyBtn.setEnabled(false);
        replyBtn.setBounds(455, 530, 80, 30);
        mainPanel.add(replyBtn);

        deleteBtn = new JButton("Hapus");
        deleteBtn.addActionListener(this);
        deleteBtn.setEnabled(false);
        deleteBtn.setBounds(70, 530, 80, 30);
        mainPanel.add(deleteBtn);

        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 640);
        setLocation(385, 100);
        setVisible(true);
    }

    private void loadMessages() {
        try {
            DBConnection c1 = new DBConnection();
            String q = "SELECT M.Message_ID, M.time_Stamp, M.User_ID, M.message, M.User_Type " +
                    "FROM Messages AS M " +
                    "INNER JOIN MessageUsers AS MU ON MU.Message_ID = M.Message_ID " +
                    "WHERE MU.User_ID = ? AND MU.User_Type = 'guru' AND M.User_ID <> ?";
            PreparedStatement ps = c1.c.prepareStatement(q);
            ps.setInt(1, TeacherLogin.currentTeacherID);
            ps.setInt(2, TeacherLogin.currentTeacherID);

            ResultSet rs = ps.executeQuery();
            List<String[]> messagesList = new ArrayList<>();

            DBConnection c = new DBConnection(); // For name lookup

            while (rs.next()) {
                String[] row = new String[7];
                row[0] = rs.getString("Message_ID");
                row[1] = rs.getString("time_Stamp");
                row[2] = rs.getString("User_ID");
                row[3] = rs.getString("message");

                String q2 = "SELECT fname, lname FROM Teacher WHERE teacherID = ?";
                PreparedStatement ps2 = c.c.prepareStatement(q2);
                ps2.setInt(1, rs.getInt("User_ID"));

                ResultSet rs2 = ps2.executeQuery();
                String fname = "Akun";
                String lname = "Terhapus";
                if (rs2.next()) {
                    fname = rs2.getString("fname");
                    lname = rs2.getString("lname");
                }
                row[4] = fname + " " + lname;
                row[5] = rs.getString("User_Type");

                messagesList.add(row);

                rs2.close();
                ps2.close();
            }

            rs.close();
            ps.close();
            c1.Close();
            c.Close();

            // Convert to array and fill listModel
            messagesListData = new String[messagesList.size()][7];
            int modelIndex = 0;
            for (int i = messagesList.size() - 1; i >= 0; i--) {
                String[] msg = messagesList.get(i);
                messagesListData[modelIndex] = msg;
                StringBuilder elementStr = new StringBuilder();
                elementStr.append("<html><pre><b>");
                elementStr.append(String.format("%s \t\t\t %s", "Dari: " + msg[4], "Pada: " + msg[1]));
                elementStr.append("</b></pre></html>");
                listModel.addElement(elementStr);
                messagesListData[modelIndex][6] = String.valueOf(modelIndex);
                modelIndex++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == replyBtn) {
            new Message(TeacherLogin.currentTeacherID, currentFromUserName, currentFromUserID, currentFromType);
        } else if (e.getSource() == deleteBtn) {
            try {
                DBConnection c1 = new DBConnection();

                String q = "Delete From MessageUsers Where User_ID ='" + TeacherLogin.currentTeacherID + "' And"
                        + " Message_ID = '" + currentMessageID + "'";

                int x = c1.s.executeUpdate(q);
                if (x == 0) {
                    JOptionPane.showMessageDialog(null, "Terjadi Kesalahan");
                } else {
                    JOptionPane.showMessageDialog(null, "Pesan berhasil dihapus!");
                    dispose();
                    new Inbox();
                }
                String q2 = "Select * From MessageUsers Where Message_ID ='" + currentMessageID + "'";
                ResultSet rs = c1.s.executeQuery(q2);
                if (rs.next() == false) {
                    String q3 = "Delete From Messages Where Message_ID ='" + currentMessageID + "'";
                    c1.s.executeUpdate(q3);
                }

            } catch (HeadlessException | SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int index = messagesList.getSelectedIndex();
        for (int i = 0; i < messagesListData.length; i++) {
            if (index == Integer.parseInt(messagesListData[i][6])) {
                messageTxt.setText(messagesListData[i][3]);
                currentFromUserID = Integer.parseInt(messagesListData[i][2]);
                currentFromUserName = messagesListData[i][4];
                currentFromType = messagesListData[i][5];
                currentMessageID = Integer.parseInt(messagesListData[i][0]);
                replyBtn.setEnabled(true);
                deleteBtn.setEnabled(true);
            }
        }
    }

    public static void main(String[] args) {
        new Inbox();
    }
}