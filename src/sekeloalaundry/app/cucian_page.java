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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author yodie
 */
public class cucian_page extends javax.swing.JFrame {

    /**
     * Creates new form cucian_page
     */
    
    private DefaultTableModel model;
    public static int statusSearching = 0;
    
    Connection con;
    Statement st;
    
    int xy;
    int xx;
    
    public cucian_page() {
        initComponents();
        setLocationRelativeTo(this);
        
        koneksi data = new koneksi();
        data.config();
        con = data.con;
        st = data.st;
        model = (DefaultTableModel)tabel_jc.getModel();
        
        tampil_tabel();
        
        btn_simpan.setEnabled(false);
        btn_batal.setEnabled(false);
        btn_ubah.setEnabled(false);
        btn_hapus.setEnabled(false);
        
        nonaktifkan_teks();
    }
    
    public void nonaktifkan_teks(){
        id_jc.setEnabled(false);
        jenis_cucian.setEnabled(false);
        harga.setEnabled(false);
    }
    
    public void aktifkan_teks(){
        id_jc.setEnabled(true);
        jenis_cucian.setEnabled(true);
        harga.setEnabled(true);
    }
    
    public void membersihkan_teks(){
        id_jc.setText("");
        jenis_cucian.setText("");
        harga.setText("");
    }
    
    private void tambah_data() {
        try {
            st = con.createStatement();
            String sql = "SELECT * FROM jenis_cucian ORDER BY kd_jenis DESC";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                String lastKdJenis = rs.getString("kd_jenis");
                // Ekstrak angka dari kode terakhir
                int num = Integer.parseInt(lastKdJenis.substring(2)) + 1;
                // Format kode baru dengan prefix "JC"
                String newKdJenis = "JC" + num;
                id_jc.setText(newKdJenis);
            } else {
                id_jc.setText("JC1"); // Kode pertama jika belum ada data
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace(); // Penanganan masalah
        }
    }
    
    private void simpan() {
        String KdJenis = id_jc.getText(); // Ambil kode jenis cucian yang sudah di-generate
        String JenisJC = jenis_cucian.getText();
        String HargaJC = harga.getText();

        // Validasi input
        if (JenisJC.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Jenis Cucian harus diisi.");
            return; // Keluar dari metode jika validasi gagal
        }

        if (HargaJC.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Harga harus diisi.");
            return; // Keluar dari metode jika validasi gagal
        }

        try {
            Double.parseDouble(HargaJC); // Cek apakah harga bisa diubah menjadi angka
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Harga harus berupa angka.");
            return; // Keluar dari metode jika validasi gagal
        }

        try {
            st = con.createStatement();
            String sqlSimpan = "INSERT INTO jenis_cucian (kd_jenis, jenis_cucian, harga) VALUES ('" + KdJenis + "','" + JenisJC + "','" + HargaJC + "')";
            st.executeUpdate(sqlSimpan);
            JOptionPane.showMessageDialog(null, "Data Berhasil Masuk!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Tak Masuk " + e.getMessage());
        }
    }

    
    private void hapus_data() {
    // Tampilkan dialog konfirmasi
        int choice = JOptionPane.showOptionDialog(null, "Yakin Hapus Data Ini?", "Delete Confirm", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

        // Jika pengguna memilih "Yes" (JOptionPane.YES_OPTION), lanjutkan dengan penghapusan
        if (choice == JOptionPane.YES_OPTION) {
            String id = id_jc.getText();

            try {
                st = con.createStatement();
                String sqlUpdate = "DELETE FROM jenis_cucian WHERE kd_jenis = ?";
                PreparedStatement pst = (PreparedStatement) con.prepareStatement(sqlUpdate);
                pst.setString(1, id);
                pst.executeUpdate();

                // Bersihkan teks dan nonaktifkan komponen
                jenis_cucian.setText("");
                harga.setText("");
                membersihkan_teks();
                nonaktifkan_teks();

                JOptionPane.showMessageDialog(null, "Data Dihapus dari Database");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Data Gagal Dihapus: " + e.getMessage());
            }
        } else {
            // Jika pengguna memilih "No", tidak ada tindakan yang diambil
            JOptionPane.showMessageDialog(null, "Penghapusan dibatalkan.");
        }
    }

    
    private void click_data() {
        String JenisID = tabel_jc.getValueAt(tabel_jc.getSelectedRow(), 0).toString();
        String JenisJC = tabel_jc.getValueAt(tabel_jc.getSelectedRow(), 1).toString();
        String HargaJC = tabel_jc.getValueAt(tabel_jc.getSelectedRow(), 2).toString();
        
        id_jc.setText(JenisID);
        jenis_cucian.setText(JenisJC);
        harga.setText(HargaJC);
        id_jc.setEditable(false);
        btn_simpan.setEnabled(false);
        btn_ubah.setEnabled(true);
        btn_hapus.setEnabled(true);
        btn_batal.setEnabled(false);
        aktifkan_teks();
    }
    
    private void ubah_data() {
        int index = tabel_jc.getSelectedRow();
        String id = tabel_jc.getValueAt(index, 0).toString();
        
        String IDJC = id_jc.getText();
        String JenisJC = jenis_cucian.getText();
        String HargaJC = harga.getText();
        
        try {

            st = con.createStatement();
            String sqlUpdate = "UPDATE jenis_cucian SET kd_jenis='"+IDJC+"',jenis_cucian='"+JenisJC+"',harga='"+HargaJC+"' WHERE kd_jenis='"+id+"' ";
            st.executeUpdate(sqlUpdate);

        JOptionPane.showMessageDialog(null, "Data Berhasil di Ubah!");
        membersihkan_teks();
        nonaktifkan_teks();
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Data Gagal di Ubah " + e.getMessage());
            }
        }
    
    private void tampil_tabel() {
        try {
                st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM jenis_cucian");
                ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                DefaultTableModel tm = (DefaultTableModel) tabel_jc.getModel();
                tm.setColumnCount(3);
                    for (int i = 4; i <= columnCount; i++ ) {
                    tm.addColumn(rsmd.getColumnName(i));
                }
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
        tabel_jc = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        id_jc = new javax.swing.JTextField();
        jenis_cucian = new javax.swing.JTextField();
        harga = new javax.swing.JTextField();
        btn_tambah = new javax.swing.JButton();
        btn_simpan = new javax.swing.JButton();
        btn_ubah = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_batal = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Data Jenis Cucian");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "TABLE DATA CUCIAN"));

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        tabel_jc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Jenis Cucian", "Harga"
            }
        ));
        tabel_jc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_jcMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel_jc);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "FORM JENIS CUCIAN"));
        jPanel4.setForeground(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-touch-id-20.png"))); // NOI18N
        jLabel2.setText("ID");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-hair-dryer-20.png"))); // NOI18N
        jLabel3.setText("JENIS CUCIAN");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-expensive-20.png"))); // NOI18N
        jLabel4.setText("HARGA");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(id_jc, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jenis_cucian, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(harga, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(id_jc))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jenis_cucian))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(harga))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        btn_tambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_tambah.setText("TAMBAH");
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });

        btn_simpan.setBackground(new java.awt.Color(51, 204, 0));
        btn_simpan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_simpan.setForeground(new java.awt.Color(255, 255, 255));
        btn_simpan.setText("SIMPAN");
        btn_simpan.setBorderPainted(false);
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });

        btn_ubah.setBackground(new java.awt.Color(0, 102, 255));
        btn_ubah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_ubah.setForeground(new java.awt.Color(255, 255, 255));
        btn_ubah.setText("UBAH");
        btn_ubah.setBorderPainted(false);
        btn_ubah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ubahActionPerformed(evt);
            }
        });

        btn_hapus.setBackground(new java.awt.Color(255, 0, 0));
        btn_hapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_hapus.setForeground(new java.awt.Color(255, 255, 255));
        btn_hapus.setText("HAPUS");
        btn_hapus.setBorderPainted(false);
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });

        btn_batal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_batal.setText("BATAL");
        btn_batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_batalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btn_tambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_simpan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_hapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_ubah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_batal)
                        .addGap(45, 381, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_tambah)
                    .addComponent(btn_simpan)
                    .addComponent(btn_ubah)
                    .addComponent(btn_hapus)
                    .addComponent(btn_batal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-washing-machine-50.png"))); // NOI18N
        jLabel1.setText("JENIS CUCIAN");

        jButton5.setBackground(new java.awt.Color(255, 0, 0));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("TUTUP");
        jButton5.setBorderPainted(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
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
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton5)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        // TODO add your handling code here:
        simpan();
        tampil_tabel();
        membersihkan_teks();
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void btn_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_batalActionPerformed
        // TODO add your handling code here:
        id_jc.setText("");
        nonaktifkan_teks();
        membersihkan_teks();
        btn_ubah.setEnabled(false);
        btn_hapus.setEnabled(false);
        btn_simpan.setEnabled(false);
        
    }//GEN-LAST:event_btn_batalActionPerformed

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed
        // TODO add your handling code here:
        id_jc.setEditable(false);
        jenis_cucian.requestFocus();
        btn_ubah.setEnabled(false);
        btn_hapus.setEnabled(false);
        btn_simpan.setEnabled(true);
        btn_batal.setEnabled(true);
        aktifkan_teks();
        tambah_data();
    }//GEN-LAST:event_btn_tambahActionPerformed

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void tabel_jcMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_jcMouseClicked
        // TODO add your handling code here:
        click_data();
        aktifkan_teks();
        btn_batal.setEnabled(true);
    }//GEN-LAST:event_tabel_jcMouseClicked

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        // TODO add your handling code here:
        hapus_data();
        tampil_tabel();
        btn_hapus.setEnabled(false);
        btn_ubah.setEnabled(false);
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void btn_ubahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ubahActionPerformed
        // TODO add your handling code here:
        ubah_data();
        tampil_tabel();
    }//GEN-LAST:event_btn_ubahActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        home_page utama = new home_page();
        utama.setVisible(true);
        
        this.setVisible(false);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowOpened

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
            java.util.logging.Logger.getLogger(cucian_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(cucian_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(cucian_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(cucian_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new cucian_page().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_batal;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JButton btn_ubah;
    private javax.swing.JTextField harga;
    private javax.swing.JTextField id_jc;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jenis_cucian;
    private javax.swing.JTable tabel_jc;
    // End of variables declaration//GEN-END:variables
}
