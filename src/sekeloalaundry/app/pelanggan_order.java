/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sekeloalaundry.app;

import static java.awt.image.ImageObserver.WIDTH;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author yodie
 */
public class pelanggan_order extends javax.swing.JFrame {

    /**
     * Creates new form pelanggan_page
     */
    private DefaultTableModel model;
    public static int statusSearching = 0;
    
    Connection con;
    Statement st;
    private int xx;
    private int xy;


    public pelanggan_order() {
        initComponents();
        setLocationRelativeTo(this);
        
        koneksi data = new koneksi();
        data.config();
        con = data.con;
        st = data.st;
        model = (DefaultTableModel)tabel_pel.getModel();
        
        tampil_tabel();
        
        btn_simpan.setEnabled(false);
        btn_batal.setEnabled(false);
        btn_order.setEnabled(false);
        btn_hapus.setEnabled(false);
        btn_ubah.setEnabled(false);
        nonaktifkan_teks();
    }
    
    public void nonaktifkan_teks(){
        id_pel.setEnabled(false);
        nama_pel.setEnabled(false);
        alamat_pel.setEnabled(false);
        telp_pel.setEnabled(false);
    }
    
    public void aktifkan_teks(){
        id_pel.setEnabled(true);
        nama_pel.setEnabled(true);
        alamat_pel.setEnabled(true);
        telp_pel.setEnabled(true);
    }
    
    public void membersihkan_teks(){
        id_pel.setText("");
        nama_pel.setText("");
        alamat_pel.setText("");
        telp_pel.setText("");
    }
    
    private void tampil_tabel() {
        try {
                st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM pelanggan");
                ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                DefaultTableModel tm = (DefaultTableModel) tabel_pel.getModel();
                tm.setColumnCount(4);
                    for (int i = 5; i <= columnCount; i++ ) {
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

    private void simpan() {
    String IdPel = id_pel.getText(); // Ambil ID pelanggan yang sudah di-generate
    String NamaPel = nama_pel.getText();
    String AlamatPel = alamat_pel.getText();
    String TelpPel = telp_pel.getText();
    
    // Validasi apakah semua field diisi
    if (IdPel.isEmpty() || NamaPel.isEmpty() || AlamatPel.isEmpty() || TelpPel.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Semua data harus diisi!");
        membersihkan_teks();
        return; // Hentikan eksekusi metode jika ada field yang kosong
    }
    
    // Validasi apakah nomor telepon hanya berisi angka
    if (!TelpPel.matches("\\d+")) {
        JOptionPane.showMessageDialog(null, "Nomor telepon harus berupa angka!");
        membersihkan_teks();
        return; // Hentikan eksekusi metode jika nomor telepon tidak valid
    }
    
   
    try {
        st = con.createStatement();
        String sqlSimpan = "INSERT INTO pelanggan (id_pelanggan, nama_pelanggan, alamat, telp) VALUES ('" + IdPel + "','" + NamaPel + "','" + AlamatPel + "','" + TelpPel + "')";
        st.executeUpdate(sqlSimpan);
        JOptionPane.showMessageDialog(null, "Data Berhasil Masuk!");
        membersihkan_teks();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Data Tak Masuk " + e.getMessage());
    }
}
    
private void hapus_data() {
    // Menampilkan dialog konfirmasi
    String[] options = {"Yes", "No"};
    int result = JOptionPane.showOptionDialog(
        null, 
        "Yakin Hapus Data Ini?", 
        "Delete Confirm", 
        JOptionPane.YES_NO_OPTION, 
        JOptionPane.QUESTION_MESSAGE, 
        null, 
        options, 
        options[1]
    );

    // Jika pengguna memilih "No", hentikan eksekusi metode
    if (result == JOptionPane.NO_OPTION) {
        btn_hapus.setEnabled(false);
        btn_ubah.setEnabled(false);
        return; // Menghentikan eksekusi metode
    }

    // Jika pengguna memilih "Yes", lanjutkan dengan proses penghapusan
    String id = id_pel.getText().trim();
    btn_hapus.setEnabled(false);
    btn_ubah.setEnabled(false);
    
    try {
        st = con.createStatement();
        String sqlUpdate = "DELETE FROM pelanggan WHERE id_pelanggan='" + id + "'";
        int rowsAffected = st.executeUpdate(sqlUpdate);

        // Jika penghapusan berhasil dan ada baris yang dihapus
        if (rowsAffected > 0) {
            id_pel.setText("");
            nama_pel.setText("");
            alamat_pel.setText("");
            telp_pel.setText("");

            JOptionPane.showMessageDialog(null, "Data Dihapus dari Database!");
        } else {
            JOptionPane.showMessageDialog(null, "Data Tidak Ditemukan!");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Data Gagal Dihapus " + e.getMessage());
    }
}
    
    private void click_data() {
        String PelID = tabel_pel.getValueAt(tabel_pel.getSelectedRow(), 0).toString();
        String PelNama = tabel_pel.getValueAt(tabel_pel.getSelectedRow(), 1).toString();
        String PelAlamat = tabel_pel.getValueAt(tabel_pel.getSelectedRow(), 2).toString();
        String PelTelp = tabel_pel.getValueAt(tabel_pel.getSelectedRow(), 3).toString();
        
        id_pel.setText(PelID);
        nama_pel.setText(PelNama);
        alamat_pel.setText(PelAlamat);
        telp_pel.setText(PelTelp);
        
        aktifkan_teks();
        btn_simpan.setEnabled(false);
        btn_batal.setEnabled(true);
        btn_order.setEnabled(true);
        btn_ubah.setEnabled(true);
        btn_hapus.setEnabled(true);
    }
    
    private void ubah_data() {
        
                // Menampilkan dialog konfirmasi
        String[] options = {"Yes", "No"};
        int result = JOptionPane.showOptionDialog(
            null, 
            "Yakin Ubah Data Ini?", 
            "Delete Confirm", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            options, 
            options[1]
        );

        // Jika pengguna memilih "No", hentikan eksekusi metode
        if (result == JOptionPane.NO_OPTION) {
            btn_hapus.setEnabled(false);
            btn_ubah.setEnabled(false);
            return; // Menghentikan eksekusi metode
        }

        // Jika pengguna memilih "Yes", lanjutkan dengan proses penghapusan
        btn_hapus.setEnabled(false);
        btn_ubah.setEnabled(false);
        int index = tabel_pel.getSelectedRow();
        String id = tabel_pel.getValueAt(index, 0).toString();
        
        String IDPel = id_pel.getText();
        String NamaPel = nama_pel.getText();
        String AlamatPel = alamat_pel.getText();
        String TelpPel = telp_pel.getText();
        
        // Validasi apakah nomor telepon hanya berisi angka
        if (!TelpPel.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Nomor telepon harus berupa angka!");
            return; // Hentikan eksekusi metode jika nomor telepon tidak valid
        }
        
        try {

            st = con.createStatement();
            String sqlUpdate = "UPDATE pelanggan SET id_pelanggan='"+IDPel+"',nama_pelanggan='"+NamaPel+"',alamat='"+AlamatPel+"',telp='"+TelpPel+"' WHERE id_pelanggan='"+id+"' ";
            st.executeUpdate(sqlUpdate);

        JOptionPane.showMessageDialog(null, "Data Berhasil di Ubah!");
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Data Gagal di Ubah " + e.getMessage());
            }
        }
    
    private void tambah_data() {
        try {
            st = con.createStatement();
        String sql = "SELECT * FROM pelanggan ORDER BY id_pelanggan DESC";
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
            String lastId = rs.getString("id_pelanggan");
            // Ekstrak angka dari ID terakhir
            int num = Integer.parseInt(lastId.substring(3)) + 1;
            // Format angka menjadi 3 digit dan tambahkan prefix "plg"
            String newId = "PLG" + String.format("%03d", num);
            id_pel.setText(newId);
        } else {
            id_pel.setText("PLG001"); // ID pertama jika belum ada data
        }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();//penanganan masalah
        }
        
        nama_pel.setText("");
        alamat_pel.setText("");
        telp_pel.setText("");
    }
    
    private void cari_data_pel() {
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        statusSearching = 1;
        if(cari_btn_pel.getText().isEmpty())
        { statusSearching = 0; }
        else if(statusSearching==1){
           
        
        String cari = cari_pel.getText();
        
        try{
            String sql = "Select * From pelanggan Where id_pelanggan LIKE '"+cari+"'"
                            + "OR nama_pelanggan LIKE '"+cari+"' OR alamat LIKE '"+cari+"'"
                            + "OR telp LIKE '"+cari+"' ORDER BY id_pelanggan";
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery(sql);
        while(rs.next()){
            model.addRow(new Object[]{
               rs.getString(1),
               rs.getString(2),
               rs.getString(3),
               rs.getString(4)
            
            });
        }
        
        tabel_pel.setModel(model);
                
                }catch(SQLException ex){
                    JOptionPane.showMessageDialog(rootPane,"Data yang dicari tidak di temukan!");
                }
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
        cari_pel = new javax.swing.JTextField();
        cari_btn_pel = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        JScrollPane = new javax.swing.JScrollPane();
        tabel_pel = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        nama_pel = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        id_pel = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        alamat_pel = new javax.swing.JTextField();
        telp_pel = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btn_order = new javax.swing.JButton();
        btn_tambah = new javax.swing.JButton();
        btn_simpan = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_ubah = new javax.swing.JButton();
        btn_batal = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btn_tutup = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Data Pelanggan");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        cari_pel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cari_pelActionPerformed(evt);
            }
        });

        cari_btn_pel.setText("CARI");
        cari_btn_pel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cari_btn_pelActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("CARI PELANGGAN");

        jButton2.setText("TAMPIL SELURUH DATA");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cari_pel, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cari_btn_pel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cari_pel)
                        .addComponent(cari_btn_pel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "TABLE PELANGGAN"));

        JScrollPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JScrollPaneMouseClicked(evt);
            }
        });

        tabel_pel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nama", "Alamat", "No Telepon"
            }
        ));
        tabel_pel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_pelMouseClicked(evt);
            }
        });
        JScrollPane.setViewportView(tabel_pel);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JScrollPane)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "TAMBAH DATA PELANGGAN"));

        nama_pel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nama_pelActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-name-tag-20.png"))); // NOI18N
        jLabel3.setText("NAMA");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-touch-id-20.png"))); // NOI18N
        jLabel4.setText("ID");

        id_pel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id_pelActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-phone-20.png"))); // NOI18N
        jLabel5.setText("NO TELP.");

        alamat_pel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alamat_pelActionPerformed(evt);
            }
        });

        telp_pel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                telp_pelActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-home-address-20.png"))); // NOI18N
        jLabel6.setText("ALAMAT");

        btn_order.setBackground(new java.awt.Color(255, 153, 51));
        btn_order.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_order.setText("ORDER");
        btn_order.setBorderPainted(false);
        btn_order.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_orderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(alamat_pel, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(telp_pel, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_order, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nama_pel, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(id_pel, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(id_pel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nama_pel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(alamat_pel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(telp_pel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_order))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_tambah.setBackground(new java.awt.Color(255, 255, 255));
        btn_tambah.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_tambah.setText("TAMBAH");
        btn_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambahActionPerformed(evt);
            }
        });

        btn_simpan.setBackground(new java.awt.Color(51, 204, 0));
        btn_simpan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_simpan.setForeground(new java.awt.Color(255, 255, 255));
        btn_simpan.setText("SIMPAN");
        btn_simpan.setBorderPainted(false);
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });

        btn_hapus.setBackground(new java.awt.Color(255, 0, 0));
        btn_hapus.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_hapus.setForeground(new java.awt.Color(255, 255, 255));
        btn_hapus.setText("HAPUS");
        btn_hapus.setBorderPainted(false);
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });

        btn_ubah.setBackground(new java.awt.Color(0, 102, 255));
        btn_ubah.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_ubah.setForeground(new java.awt.Color(255, 255, 255));
        btn_ubah.setText("EDIT");
        btn_ubah.setBorderPainted(false);
        btn_ubah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ubahActionPerformed(evt);
            }
        });

        btn_batal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_simpan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_hapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_ubah, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_batal)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_tambah)
                    .addComponent(btn_simpan)
                    .addComponent(btn_hapus)
                    .addComponent(btn_ubah)
                    .addComponent(btn_batal))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel5.getAccessibleContext().setAccessibleName("FORM PENGGUNA");
        jPanel5.getAccessibleContext().setAccessibleParent(jPanel5);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-administrator-male-40.png"))); // NOI18N
        jLabel1.setText("PELANGGAN");

        btn_tutup.setBackground(new java.awt.Color(255, 0, 51));
        btn_tutup.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_tutup.setForeground(new java.awt.Color(255, 255, 255));
        btn_tutup.setText("TUTUP");
        btn_tutup.setBorderPainted(false);
        btn_tutup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tutupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_tutup)
                        .addGap(9, 9, 9)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_tutup, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cari_pelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cari_pelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cari_pelActionPerformed

    private void nama_pelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nama_pelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nama_pelActionPerformed

    private void id_pelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id_pelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_id_pelActionPerformed

    private void alamat_pelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alamat_pelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_alamat_pelActionPerformed

    private void telp_pelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_telp_pelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_telp_pelActionPerformed

    private void btn_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambahActionPerformed
        // TODO add your handling code here:
        nama_pel.requestFocus();
        btn_ubah.setEnabled(false);
        btn_hapus.setEnabled(false);
        btn_simpan.setEnabled(true);
        btn_batal.setEnabled(true);
        aktifkan_teks();
        tambah_data();
        id_pel.setEditable(false);
    }//GEN-LAST:event_btn_tambahActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        // TODO add your handling code here:
        simpan();
        tampil_tabel();
        nonaktifkan_teks();
        btn_simpan.setEnabled(false);
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void btn_tutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tutupActionPerformed
        // TODO add your handling code here:
        home_page utama = new home_page();
        utama.setVisible(true);
        
        this.setVisible(false);
    }//GEN-LAST:event_btn_tutupActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        // TODO add your handling code here:
        hapus_data();
        membersihkan_teks();
        nonaktifkan_teks();
        tampil_tabel();
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void btn_ubahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ubahActionPerformed
        // TODO add your handling code here:
        ubah_data();
        membersihkan_teks();
        nonaktifkan_teks();
        tampil_tabel();
    }//GEN-LAST:event_btn_ubahActionPerformed

    private void btn_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_batalActionPerformed
        // TODO add your handling code here:
        id_pel.setText("");
        nonaktifkan_teks();
        membersihkan_teks();
        btn_ubah.setEnabled(false);
        btn_hapus.setEnabled(false);
        btn_simpan.setEnabled(false);
        btn_order.setEnabled(false);
    }//GEN-LAST:event_btn_batalActionPerformed

    private void JScrollPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JScrollPaneMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_JScrollPaneMouseClicked

    private void tabel_pelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_pelMouseClicked
        // TODO add your handling code here:
        click_data();
    }//GEN-LAST:event_tabel_pelMouseClicked

    private void cari_btn_pelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cari_btn_pelActionPerformed
        // TODO add your handling code here:
        cari_data_pel();
    }//GEN-LAST:event_cari_btn_pelActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        tampil_tabel();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btn_orderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_orderActionPerformed
        // TODO add your handling code here:
        int row = 0;
        order_page op = new order_page();
        row = tabel_pel.getSelectedRow();
        TableModel tm = tabel_pel.getModel();
        
        // Periksa apakah ada baris yang dipilih
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Harap pilih data dari tabel pelanggan terlebih dahulu!");
            return; // Keluar dari metode jika tidak ada baris yang dipilih
        }
        
        try {
            st = con.createStatement();
            String sql = "SELECT * FROM pesanan ORDER BY no_order DESC";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                String lastOrder = rs.getString("no_order");
                int numericPart = Integer.parseInt(lastOrder.replace("ORD", "")) + 1;
                String newOrder = "ORD" + String.format("%03d", numericPart);
                op.no_order.setText(newOrder);
            } else {
                op.no_order.setText("ORD001");
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
}

        String id_pel = tm.getValueAt(row, 0).toString();
        String nama = tm.getValueAt(row, 1).toString();
        
        op.setIDPel(id_pel);
        op.nama.setText(nama);
        
        
        
        op.setVisible(true);
        
        this.setVisible(false);
        
    }//GEN-LAST:event_btn_orderActionPerformed

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
            java.util.logging.Logger.getLogger(pelanggan_order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(pelanggan_order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(pelanggan_order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(pelanggan_order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
                new pelanggan_order().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane JScrollPane;
    private javax.swing.JTextField alamat_pel;
    private javax.swing.JButton btn_batal;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_order;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_tambah;
    private javax.swing.JButton btn_tutup;
    private javax.swing.JButton btn_ubah;
    private javax.swing.JButton cari_btn_pel;
    private javax.swing.JTextField cari_pel;
    public javax.swing.JTextField id_pel;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTextField nama_pel;
    private javax.swing.JTable tabel_pel;
    private javax.swing.JTextField telp_pel;
    // End of variables declaration//GEN-END:variables
}
