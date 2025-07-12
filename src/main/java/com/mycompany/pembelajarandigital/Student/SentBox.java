package com.mycompany.pembelajarandigital.Student;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class SentBox extends JFrame implements ActionListener, ListSelectionListener {
    JList messagesList;
    DefaultListModel listModel;
    JLabel title, messageLbl;
    JPanel mainPanel;
    JTextArea messageTxt;
    JButton messageBtn, deleteBtn;
    JScrollPane scroll1, scroll2;
    String[][] messagesListData;
    int currentToUserID, currentMessageID;
    String currentToUserName, currentToType;

    public SentBox() {
        super("SentBox");
        setLayout(new BorderLayout());

        title = new JLabel("SentBox");
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

        messageBtn = new JButton("Text");
        messageBtn.addActionListener(this);
        messageBtn.setEnabled(false);
        messageBtn.setBounds(455, 530, 80, 30);
        mainPanel.add(messageBtn);

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
            String q = "SELECT M.Message_ID, M.time_Stamp, M.User_ID, M.message, M.toUser_ID, M.toUser_Type "
                    + "FROM Messages AS M "
                    + "INNER JOIN MessageUsers AS MU ON MU.Message_ID = M.Message_ID "
                    + "WHERE MU.User_ID = '" + StudentLogin.currentStudentID + "' "
                    + "AND MU.User_Type = 'siswa' "
                    + "AND M.User_ID = '" + StudentLogin.currentStudentID + "'";

            ResultSet rs = c1.s.executeQuery(q);
            List<String[]> tempMessages = new ArrayList<>();
            DBConnection c = new DBConnection();

            while (rs.next()) {
                String[] row = new String[8];
                row[0] = rs.getString("Message_ID");
                row[1] = rs.getString("time_Stamp");
                row[2] = rs.getString("User_ID");
                row[3] = rs.getString("message");
                row[4] = rs.getString("toUser_ID");

                // Lookup recipient name
                String q2 = "SELECT fname, lname FROM Student WHERE stdID = '" + row[4] + "'";
                ResultSet rs2 = c.s.executeQuery(q2);

                String fname = "Akun";
                String lname = "Terhapus";
                if (rs2.next()) {
                    fname = rs2.getString("fname");
                    lname = rs2.getString("lname");
                }
                row[5] = fname + " " + lname;

                row[6] = rs.getString("toUser_Type");

                tempMessages.add(row);
            }

            messagesListData = new String[tempMessages.size()][8];

            int modelIndex = 0;
            for (int n = tempMessages.size() - 1; n >= 0; n--) {
                String[] row = tempMessages.get(n);
                row[7] = String.valueOf(modelIndex);
                messagesListData[tempMessages.size() - 1 - n] = row;

                StringBuilder elementStr = new StringBuilder();
                elementStr.append("<html><pre><b>");
                elementStr.append(String.format("%s \t\t\t %s", "Untuk: " + row[5], "Pada:  " + row[1]));
                elementStr.append("</b></pre></html>");
                listModel.addElement(elementStr);

                modelIndex++;
            }

            c1.Close();
            c.Close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == messageBtn) {
            new Message(StudentLogin.currentStudentID, currentToUserName, currentToUserID, currentToType);
        } else if (e.getSource() == deleteBtn) {
            try {
                DBConnection c1 = new DBConnection();

                String q = "Delete From MessageUsers Where User_ID ='" + StudentLogin.currentStudentID + "' And"
                        + " Message_ID = '" + currentMessageID + "'";

                int x = c1.s.executeUpdate(q);
                if (x == 0) {
                    JOptionPane.showMessageDialog(null, "Terjadi Kesalahan!");
                } else {
                    JOptionPane.showMessageDialog(null, "Pesan berhasil dihapus!");
                    dispose();
                    new SentBox();
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
            if (index == Integer.parseInt(messagesListData[i][7])) {
                messageTxt.setText(messagesListData[i][3]);
                currentToUserID = Integer.parseInt(messagesListData[i][4]);
                currentToUserName = messagesListData[i][5];
                currentToType = messagesListData[i][6];
                currentMessageID = Integer.parseInt(messagesListData[i][0]);
                messageBtn.setEnabled(true);
                deleteBtn.setEnabled(true);
            }
        }
    }

    public static void main(String[] args) {
        new SentBox();
    }
}