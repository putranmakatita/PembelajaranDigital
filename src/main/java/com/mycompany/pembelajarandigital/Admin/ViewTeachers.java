package com.mycompany.pembelajarandigital.Admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mycompany.pembelajarandigital.DBConnection;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ViewTeachers extends JFrame implements ActionListener {
    String columnNames[];
    Object[][] data;
    JTable table;
    JButton deleteButton;
    DefaultTableModel model;
    JLabel title;

    public ViewTeachers() {
        super("Lihat Guru");
        setLayout(new BorderLayout());

        title = new JLabel("Guru", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(25.0f));
        title.setBackground(Color.LIGHT_GRAY);
        title.setForeground(Color.BLACK);
        title.setOpaque(true);
        add(title, BorderLayout.NORTH);

        this.getTeachers();
        model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 9:
                        return ImageIcon.class;
                    default:
                        return Object.class;
                }
            }
        };
        table = new JTable(model);
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.LIGHT_GRAY);
        JScrollPane pane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setRowHeight(96);
        add(pane, BorderLayout.CENTER);

        deleteButton = new JButton("Hapus");
        deleteButton.setHorizontalAlignment(JButton.CENTER);
        deleteButton.addActionListener(this);
        deleteButton.setToolTipText("Klik untuk hapus guru.");
        deleteButton.setPreferredSize(new Dimension(20, 50));
        add(deleteButton, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 500);
        setLocation(250, 200);
        setVisible(true);
    }

    private void getTeachers() {
        DBConnection c1 = new DBConnection();
        try {
            String q = "SELECT * FROM Teacher";
            ResultSet rs = c1.s.executeQuery(q);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Column names
            columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = rsmd.getColumnName(i + 1);
            }

            // Use dynamic list to collect row data
            List<Object[]> rowList = new ArrayList<>();

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int c = 0; c < columnCount; c++) {
                    String colName = columnNames[c];
                    if (colName.equalsIgnoreCase("picture")) {
                        byte[] bytImage = rs.getBytes(colName);
                        if (bytImage != null && bytImage.length > 0) {
                            row[c] = getImageIcon(bytImage);
                        } else {
                            row[c] = new JLabel(new ImageIcon(
                                    ClassLoader.getSystemResource("icons/uploadPicIcon.png")));
                        }
                    } else {
                        row[c] = rs.getString(colName);
                    }
                }
                rowList.add(row);
            }

            // Convert List<Object[]> to Object[][]
            data = rowList.toArray(new Object[0][]);
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            c1.Close();
        }
    }

    private ImageIcon getImageIcon(byte[] byteImage) {
        InputStream is = new ByteArrayInputStream(byteImage);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(is);
        } catch (IOException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }

        ImageIcon icon = new ImageIcon(bufferedImage);
        Image i2 = icon.getImage().getScaledInstance(96, 96, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        return i3;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == deleteButton) {
            // check for selected row first
            if (table.getSelectedRow() != -1) {
                int rowNum = table.getSelectedRow();
                // 0 is the Column index which is ID
                String id = model.getValueAt(rowNum, 0).toString();

                DBConnection c1 = new DBConnection();
                try {
                    String q = "Delete From Teacher Where teacherID ='" + id + "'";
                    int x = c1.s.executeUpdate(q);
                    if (x == 0) {
                        JOptionPane.showMessageDialog(null, "Terjadi Kesalahan!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Guru berhasil dihapus!");
                        dispose();
                        new ViewTeachers();
                    }
                } catch (HeadlessException | SQLException exception) {
                    exception.printStackTrace();
                } finally {
                    c1.Close();
                }
            }
        }
    }

    public static void main(String[] args) {
        new ViewTeachers();
    }

}