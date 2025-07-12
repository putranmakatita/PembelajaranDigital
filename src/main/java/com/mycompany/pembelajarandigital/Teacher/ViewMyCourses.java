package com.mycompany.pembelajarandigital.Teacher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.pembelajarandigital.DBConnection;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ViewMyCourses extends JFrame implements ActionListener {
    JLabel title;
    JScrollPane scroll;
    JTable table;
    DefaultTableModel model;
    String[][] data;
    String[] columnNames;
    JButton deleteButton;

    public ViewMyCourses() {
        super("Lihat Pembelajaran");
        setLayout(new BorderLayout());

        title = new JLabel("Lihat Pembelajaran", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(22.0f));
        title.setBackground(Color.LIGHT_GRAY);
        title.setForeground(Color.BLACK);
        title.setOpaque(true);
        add(title, BorderLayout.NORTH);

        this.getMyCourses();
        model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.LIGHT_GRAY);
        JScrollPane pane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setRowHeight(30);
        pane.setBounds(20, 150, 550, 200);
        add(pane, BorderLayout.CENTER);

        deleteButton = new JButton("Hapus");
        deleteButton.setHorizontalAlignment(JButton.CENTER);
        deleteButton.addActionListener(this);
        deleteButton.setToolTipText("Klik untuk menghapus pembelajaran yang dipilih.");
        deleteButton.setPreferredSize(new Dimension(20, 50));
        add(deleteButton, BorderLayout.SOUTH);

        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 360);
        setLocation(420, 260);
        setVisible(true);
    }

    private void getMyCourses() {
        try {
            DBConnection c1 = new DBConnection();
            String q1 = "SELECT Course_ID, Name FROM Courses WHERE teacherID = ?";
            PreparedStatement ps = c1.c.prepareStatement(q1);
            ps.setInt(1, TeacherLogin.currentTeacherID);
            ResultSet rs = ps.executeQuery();

            // Get column names
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = rsmd.getColumnName(i + 1);
            }

            // Read rows dynamically
            List<String[]> rowList = new ArrayList<>();
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int c = 0; c < columnCount; c++) {
                    row[c] = rs.getString(columnNames[c]);
                }
                rowList.add(row);
            }

            // Convert to array
            data = new String[rowList.size()][columnCount];
            for (int i = 0; i < rowList.size(); i++) {
                data[i] = rowList.get(i);
            }

            rs.close();
            ps.close();
            c1.Close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteButton) {
            // check for selected row first
            if (table.getSelectedRow() != -1) {
                int rowNum = table.getSelectedRow();
                // 0 is the Column index which is ID
                String course_ID = model.getValueAt(rowNum, 0).toString();

                try {
                    DBConnection c1 = new DBConnection();
                    String q = "Delete From Courses Where course_ID ='" + course_ID + "'";
                    int x = c1.s.executeUpdate(q);
                    if (x == 0) {
                        JOptionPane.showMessageDialog(null, "Terjadi kesalahan!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Pembelajaran berhasil dihapus!");
                        dispose();
                        new ViewMyCourses();
                    }
                } catch (HeadlessException | SQLException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new ViewMyCourses();
    }
}
