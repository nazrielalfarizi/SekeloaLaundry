/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sekeloalaundry.app;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author Nazriel
 */
public class koneksi {
    Connection con;
    Statement st;

public void config() {
    try {
        Class.forName("com.mysql.jdbc.Driver");
        con = (Connection) DriverManager.getConnection ("jdbc:mysql://localhost/sekeloa_laundry","root","");
        st = (Statement) con.createStatement();
    }catch(Exception e) {
        JOptionPane.showMessageDialog(null, "Koneksi Gagal masuk ke Database!\n" + e.getMessage());
    }
  }
}
