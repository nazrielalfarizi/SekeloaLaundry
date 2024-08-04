/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sekeloalaundry.app;

import com.mysql.jdbc.PreparedStatement;
import static java.awt.image.ImageObserver.WIDTH;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author yodie
 */
public class history_transaksi_page extends javax.swing.JFrame {
    private static final DateTimeFormatter smpdtfmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDateTime tglsekarang = LocalDateTime.now();
    private final String ltanggal = smpdtfmt.format(tglsekarang);
    /**
     * Creates new form transaksi_page
     */
    String ID_Pel;
    String Statuss,Pelanggann,selectedItemStr;
    String Tbayarr,Bayarr;
    int hargahitung,totalfix,bayarnya,totalhitung,kembaliannya,sisanya,sisaNya;
    Connection con;
    Statement st;
    private DefaultTableModel model;
    
    public history_transaksi_page() {
        initComponents();
        setLocationRelativeTo(this);
        koneksi data = new koneksi();
        data.config();
        con = data.con;
        st = data.st;
        model = (DefaultTableModel)tabel_transaksi.getModel();
        
         // Buat objek Calendar untuk mendapatkan tanggal hari ini
        Calendar calendar = Calendar.getInstance();

        // Dapatkan tanggal hari ini
        Date today = calendar.getTime();

        // Set tanggal hari ini ke JDateChooser
        pilih_tanggal.setDate(today);
    }
    
    // Metode untuk menyimpan id_pel
    public void setIDPel(String id_pel) {
        this.ID_Pel = id_pel;
    }

    // Metode untuk mendapatkan id_pel jika diperlukan
    public String getIDPel() {
        return ID_Pel;
    }
    
    
    private void tampil_tabel() {
        try {
                st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT history_transaksi.id_history,transaksi.id_transaksi,pesanan.no_order,pelanggan.id_pelanggan, pelanggan.nama_pelanggan,pelanggan.telp,transaksi.tanggal_transaksi, pesanan.total_bayar, pesanan.sisa, transaksi.dibayar, transaksi.kembalian, pesanan.status FROM history_transaksi JOIN transaksi ON history_transaksi.id_transaksi = transaksi.id_transaksi JOIN pesanan ON history_transaksi.no_order = pesanan.no_order JOIN pelanggan ON history_transaksi.id_pelanggan = pelanggan.id_pelanggan;");
                ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                DefaultTableModel tm = (DefaultTableModel) tabel_transaksi.getModel();
                tm.setColumnCount(12);
                    
                    tm.setRowCount(0);
                    while (rs.next()) {
                    String[] a = new String[columnCount];
                        for(int i = 0; i < columnCount; i++) {
                        a[i] = rs.getString(i+1);
                    }
                    tm.addRow(a);
                }
                tm.fireTableDataChanged();
                rs.close();
                st.close();
                } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex, ex.getMessage(), WIDTH, null);
                }
       }
    
    private void cari_transaksi() {
            String cari = cari_transaksi.getText(); // Ambil teks pencarian dari komponen teks

            try {
                st = con.createStatement();
                // Update SQL query untuk mencakup history_transaksi dan join tabel lain
                String sql = "SELECT history_transaksi.id_history, transaksi.id_transaksi, pesanan.no_order, pelanggan.id_pelanggan, pelanggan.nama_pelanggan, pelanggan.telp, transaksi.tanggal_transaksi, pesanan.total_bayar, pesanan.sisa, transaksi.dibayar, transaksi.kembalian, pesanan.status " +
                             "FROM history_transaksi " +
                             "JOIN transaksi ON history_transaksi.id_transaksi = transaksi.id_transaksi " +
                             "JOIN pesanan ON history_transaksi.no_order = pesanan.no_order " +
                             "JOIN pelanggan ON history_transaksi.id_pelanggan = pelanggan.id_pelanggan " +
                             "WHERE history_transaksi.id_history LIKE ? " +
                             "OR transaksi.id_transaksi LIKE ? " +
                             "OR pesanan.no_order LIKE ? " +
                             "OR pelanggan.id_pelanggan LIKE ? " +
                             "OR pelanggan.nama_pelanggan LIKE ? " +
                             "OR pelanggan.telp LIKE ? " +
                             "OR pesanan.status LIKE ?";

                PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
                String searchPattern = "%" + cari + "%";
                pst.setString(1, searchPattern);
                pst.setString(2, searchPattern);
                pst.setString(3, searchPattern);
                pst.setString(4, searchPattern);
                pst.setString(5, searchPattern);
                pst.setString(6, searchPattern);
                pst.setString(7, searchPattern);

                ResultSet rs = pst.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();

                DefaultTableModel tm = (DefaultTableModel) tabel_transaksi.getModel();
                tm.setColumnCount(columnCount);

             
                tm.setRowCount(0);
                while (rs.next()) {
                    String[] row = new String[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        row[i] = rs.getString(i + 1);
                    }
                    tm.addRow(row);
                }

                tm.fireTableDataChanged();
                rs.close();
                pst.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex, ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }


        private void cari_urut() {
                // Ambil tanggal dari JDateChooser
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format(pilih_tanggal.getDate());

                try {
                    st = con.createStatement();
                    // Modifikasi query untuk mencari berdasarkan tanggal_transaksi
                    String sql = "SELECT history_transaksi.id_history, transaksi.id_transaksi, pesanan.no_order, pelanggan.id_pelanggan, pelanggan.nama_pelanggan, pelanggan.telp, transaksi.tanggal_transaksi, pesanan.total_bayar, pesanan.sisa, transaksi.dibayar, transaksi.kembalian, pesanan.status " +
                             "FROM history_transaksi " +
                             "JOIN transaksi ON history_transaksi.id_transaksi = transaksi.id_transaksi " +
                             "JOIN pesanan ON history_transaksi.no_order = pesanan.no_order " +
                             "JOIN pelanggan ON history_transaksi.id_pelanggan = pelanggan.id_pelanggan " +
                             "WHERE transaksi.tanggal_transaksi LIKE ? ";
                             

                    PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
                    // Set parameter untuk tanggal
                    pst.setString(1, date);

                    ResultSet rs = pst.executeQuery();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();

                    DefaultTableModel tm = (DefaultTableModel) tabel_transaksi.getModel();
                    // Reset column count to avoid issues with previous searches
                    tm.setColumnCount(12);

                    // Clear previous data
                    tm.setRowCount(0);

                    while (rs.next()) {
                        String[] row = new String[columnCount];
                        for (int i = 0; i < columnCount; i++) {
                            row[i] = rs.getString(i + 1);
                        }
                        tm.addRow(row);
                    }

                    tm.fireTableDataChanged();
                    rs.close();
                    pst.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
    }

    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_transaksi = new javax.swing.JTable();
        btn_history = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        cari_transaksi = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        pilih_tanggal = new com.toedter.calendar.JDateChooser();
        btn_generate = new javax.swing.JButton();
        btn_cari = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btn_tutp = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Data Transaksi");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "TABEL HISTORY"));

        tabel_transaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID History", "ID Transaksi", "No Order", "ID Pelanggan", "Nama Pelanggan", "Nomor Telepon", "Tanggal Transaksi", "Total Bayar", "Sisa", "Dibayar", "Kembalian", "Status"
            }
        ));
        jScrollPane1.setViewportView(tabel_transaksi);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
        );

        btn_history.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_history.setText("HISTORY");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("CARI TRANSAKSI");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("URUTKAN");

        btn_generate.setText("GENERATE");
        btn_generate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_generateActionPerformed(evt);
            }
        });

        btn_cari.setText("CARI");
        btn_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cariActionPerformed(evt);
            }
        });

        jButton1.setText("Tampil Seluruh Data");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cari_transaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cari)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 344, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pilih_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_generate, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_history, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pilih_tanggal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_generate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel11))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cari_transaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(202, 202, 202)
                .addComponent(btn_history)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-history-50.png"))); // NOI18N
        jLabel1.setText("HISTORY TRANSAKSI");

        btn_tutp.setBackground(new java.awt.Color(255, 0, 0));
        btn_tutp.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_tutp.setForeground(new java.awt.Color(255, 255, 255));
        btn_tutp.setText("TUTUP");
        btn_tutp.setBorderPainted(false);
        btn_tutp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tutpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_tutp, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_tutp)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        tampil_tabel();
    }//GEN-LAST:event_formWindowOpened

    private void btn_tutpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tutpActionPerformed
        // TODO add your handling code here:
        home_page hp = new home_page();
        hp.setVisible(true);
        
        this.setVisible(false); 
    }//GEN-LAST:event_btn_tutpActionPerformed

    private void btn_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cariActionPerformed
        // TODO add your handling code here:
        cari_transaksi();
    }//GEN-LAST:event_btn_cariActionPerformed

    private void btn_generateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_generateActionPerformed
        // TODO add your handling code here:
        cari_urut();
    }//GEN-LAST:event_btn_generateActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        tampil_tabel();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(history_transaksi_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(history_transaksi_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(history_transaksi_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(history_transaksi_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new history_transaksi_page().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cari;
    private javax.swing.JButton btn_generate;
    private javax.swing.JButton btn_history;
    private javax.swing.JButton btn_tutp;
    private javax.swing.JTextField cari_transaksi;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser pilih_tanggal;
    private javax.swing.JTable tabel_transaksi;
    // End of variables declaration//GEN-END:variables
}
