package com.mycompany.pembelajarandigital;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public class AboutProject extends JFrame {
    JTextArea aboutText;
    JLabel title;

    public AboutProject() {
        super("Tentang Aplikasi");
        setSize(540, 380);
        setLocation(430, 280);
        setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon(
                ClassLoader.getSystemResource("icons/systemIcon.png"));
        setIconImage(icon.getImage());

        title = new JLabel("Aplikasi Pembelajaran Digital", JLabel.CENTER);
        title.setFont(new Font(Font.SERIF, Font.BOLD, 25));
        title.setBackground(Color.LIGHT_GRAY);
        title.setForeground(Color.BLUE);
        title.setOpaque(true);
        add(title, BorderLayout.NORTH);

        String txt = "\t-->  Aplikasi Pembelajaran Digital adalah sebuah aplikasi berbasis desktop "
                + "yang dikembangkan menggunakan bahasa Java dan memanfaatkan tools dari library Swing dan AWT. Aplikasi ini ditujukan untuk digunakan oleh siswa dan guru dalam "
                + "Pembelajaran berbasis Online / Daring. \n\n" +
                "\t-->  Admin dapat menambahkan mata pelajaran, Guru dapat menambahkan pembelajaran untuk suatu mata pelajaran dan "
                + "Siswa dapat mengambil pembelajaran dan mempelajarinya, serta siswa juga bisa bertukar pesan dengan siswa lain di dalam pembelajaran ";

        aboutText = new JTextArea(txt);
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);
        aboutText.setFont(new Font(Font.DIALOG, Font.ITALIC, 19));
        aboutText.setForeground(Color.BLACK);
        aboutText.setBackground(Color.ORANGE);
        aboutText.setBorder(new LineBorder(Color.BLUE, 2, true));
        aboutText.setEditable(false);
        add(aboutText, BorderLayout.CENTER);

        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new AboutProject();
    }
}