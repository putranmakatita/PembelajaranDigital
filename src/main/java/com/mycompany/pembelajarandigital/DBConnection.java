package com.mycompany.pembelajarandigital;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
    public Connection c;
    public Statement s;

    public DBConnection() {
        try {
            // Optional: Explicitly load the MySQL 8+ driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Updated JDBC URL
            String url = "jdbc:mysql://localhost:3306/ELearningSystem?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = "";

            c = DriverManager.getConnection(url, user, password);
            s = c.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }

    public void Close() {
        try {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
