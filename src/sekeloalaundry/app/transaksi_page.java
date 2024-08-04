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
public class transaksi_page extends javax.swing.JFrame {
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
    
    public transaksi_page() {
        initComponents();
        setLocationRelativeTo(this);
        koneksi data = new koneksi();
        data.config();
        con = data.con;
        st = data.st;
        model = (DefaultTableModel)tabel_transaksi.getModel();
        btn_simpan.setEnabled(false);
        
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
    
    private void membersihkan_teks(){
        no_transaksi.setText("");
        no_order.setText("");
        nama_pelanggan.setText("");
        total_bayar.setText("");
        bayar.setText("");
        status.setText("");
        sisa.setText("");
        dibayar.setText("");
        kembalian.setText("");
    }
    
    private void tampil_tabel() {
        try {
                st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT transaksi.id_transaksi,pesanan.no_order,pelanggan.id_pelanggan, pelanggan.nama_pelanggan,transaksi.tanggal_transaksi, pesanan.total_bayar, pesanan.sisa, transaksi.dibayar, transaksi.kembalian FROM transaksi JOIN pelanggan ON transaksi.id_pelanggan = pelanggan.id_pelanggan JOIN pesanan ON transaksi.no_order = pesanan.no_order");
                ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                DefaultTableModel tm = (DefaultTableModel) tabel_transaksi.getModel();
                tm.setColumnCount(9);
                    for (int i = 10; i <= columnCount; i++ ) {
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
    
    private void cari_transaksi() {
        String cari = cari_transaksi.getText(); // Ambil teks pencarian dari komponen teks

        try {
            st = con.createStatement();
            String sql = "SELECT transaksi.id_transaksi, pesanan.no_order, pelanggan.id_pelanggan, pelanggan.nama_pelanggan, transaksi.tanggal_transaksi, pesanan.total_bayar, pesanan.sisa, transaksi.dibayar, transaksi.kembalian " +
                         "FROM transaksi " +
                         "JOIN pelanggan ON transaksi.id_pelanggan = pelanggan.id_pelanggan " +
                         "JOIN pesanan ON transaksi.no_order = pesanan.no_order " +
                         "WHERE transaksi.id_transaksi LIKE ? " +
                         "OR pesanan.no_order LIKE ? " +
                         "OR pelanggan.id_pelanggan LIKE ? " +
                         "OR pelanggan.nama_pelanggan LIKE ?";

            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            String searchPattern = "%" + cari + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            pst.setString(3, searchPattern);
            pst.setString(4, searchPattern);

            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            DefaultTableModel tm = (DefaultTableModel) tabel_transaksi.getModel();
            tm.setColumnCount(9);

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
            String sql = "SELECT transaksi.id_transaksi, pesanan.no_order, pelanggan.id_pelanggan, pelanggan.nama_pelanggan, transaksi.tanggal_transaksi, pesanan.total_bayar, pesanan.sisa, transaksi.dibayar, transaksi.kembalian " +
                         "FROM transaksi " +
                         "JOIN pelanggan ON transaksi.id_pelanggan = pelanggan.id_pelanggan " +
                         "JOIN pesanan ON transaksi.no_order = pesanan.no_order " +
                         "WHERE transaksi.tanggal_transaksi = ?";

            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            // Set parameter untuk tanggal
            pst.setString(1, date);

            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            DefaultTableModel tm = (DefaultTableModel) tabel_transaksi.getModel();
            // Reset column count to avoid issues with previous searches
            tm.setColumnCount(columnCount);

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
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        no_transaksi = new javax.swing.JTextField();
        nama_pelanggan = new javax.swing.JTextField();
        total_bayar = new javax.swing.JTextField();
        bayar = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        kembalian = new javax.swing.JTextField();
        dibayar = new javax.swing.JTextField();
        sisa = new javax.swing.JTextField();
        status = new javax.swing.JTextField();
        tanggal = new javax.swing.JLabel();
        no_order = new javax.swing.JTextField();
        btn_simpan = new javax.swing.JButton();
        btn_history = new javax.swing.JButton();
        btn_kembali = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        cari_transaksi = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        pilih_tanggal = new com.toedter.calendar.JDateChooser();
        btn_generate = new javax.swing.JButton();
        btn_cari = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
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
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "TABLE TRANSAKSI"));

        tabel_transaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Transaksi", "No Order", "ID Pelanggan", "Nama Pelanggan", "Tanggal Transaksi", "Total Bayar", "Sisa", "Dibayar", "Kembalian"
            }
        ));
        jScrollPane1.setViewportView(tabel_transaksi);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "FORM TRANSAKSI"));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-numbers-20.png"))); // NOI18N
        jLabel2.setText("NO TRANSAKSI");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-numeric-20.png"))); // NOI18N
        jLabel3.setText("NO ORDER");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-customer-20.png"))); // NOI18N
        jLabel4.setText("PELANGGAN");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-bill-20.png"))); // NOI18N
        jLabel5.setText("TOTAL BAYAR");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-cash-in-hand-20.png"))); // NOI18N
        jLabel6.setText("BAYAR / DP");

        no_transaksi.setEditable(false);
        no_transaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                no_transaksiActionPerformed(evt);
            }
        });

        nama_pelanggan.setEditable(false);
        nama_pelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nama_pelangganActionPerformed(evt);
            }
        });

        total_bayar.setEditable(false);

        bayar.setEditable(false);
        bayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayarActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-data-pending-20.png"))); // NOI18N
        jLabel7.setText("STATUS");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-cash-in-hand-20.png"))); // NOI18N
        jLabel8.setText("SISA");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-paycheque-20.png"))); // NOI18N
        jLabel9.setText("DIBAYAR");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-request-money-20.png"))); // NOI18N
        jLabel10.setText("KEMBALIAN");

        kembalian.setEditable(false);

        dibayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dibayarKeyReleased(evt);
            }
        });

        sisa.setEditable(false);

        status.setEditable(false);

        tanggal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tanggal.setText("0000-00-00");

        no_order.setEditable(false);
        no_order.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                no_orderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(28, 28, 28)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nama_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(bayar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                        .addComponent(total_bayar, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(no_order, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(no_transaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9)
                                .addComponent(jLabel8))
                            .addGap(22, 22, 22)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(dibayar, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(sisa, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tanggal)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(no_transaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(tanggal))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(no_order, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nama_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(sisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dibayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(total_bayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 12, Short.MAX_VALUE))))
        );

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

        btn_history.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_history.setText("HISTORY");
        btn_history.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_historyActionPerformed(evt);
            }
        });

        btn_kembali.setBackground(new java.awt.Color(0, 102, 255));
        btn_kembali.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_kembali.setForeground(new java.awt.Color(255, 255, 255));
        btn_kembali.setText("BUAT TRANSAKSI");
        btn_kembali.setBorderPainted(false);
        btn_kembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_kembaliActionPerformed(evt);
            }
        });

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

        jLabel13.setText("Jika ingin melunasi Orderan");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cari_transaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cari)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pilih_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_generate, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_kembali)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_history, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel11)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pilih_tanggal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btn_generate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cari_transaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_simpan)
                    .addComponent(btn_kembali, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_history)
                    .addComponent(jLabel13))
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-bank-cards-40.png"))); // NOI18N
        jLabel1.setText("TRANSAKSI");

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
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_tutp, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_tutp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void no_transaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_no_transaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_no_transaksiActionPerformed

    private void no_orderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_no_orderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_no_orderActionPerformed

    private void nama_pelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nama_pelangganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nama_pelangganActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        tanggal.setText(ltanggal);
        tampil_tabel();
    }//GEN-LAST:event_formWindowOpened

    private void btn_kembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_kembaliActionPerformed
        // TODO add your handling code here:
        order_page op = new order_page();
        op.setVisible(true);
        
        this.setVisible(false);
    }//GEN-LAST:event_btn_kembaliActionPerformed

    private void btn_tutpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tutpActionPerformed
        // TODO add your handling code here:
        home_page hp = new home_page();
        hp.setVisible(true);
        
        this.setVisible(false); 
    }//GEN-LAST:event_btn_tutpActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
            // TODO add your handling code here:
             String ID_Transaksi = no_transaksi.getText();
             String NoOrder = no_order.getText();
             String DIbayar = dibayar.getText();
             String KembalianNya = kembalian.getText();
             String StatusNya = status.getText();
             String Sisaa = sisa.getText();

             double sisaValue = Math.abs(Double.parseDouble(Sisaa));

                // Validasi input dibayar harus lebih besar daripada sisa
                if (Double.parseDouble(DIbayar) < sisaValue) {
                    dibayar.requestFocus();
                    JOptionPane.showMessageDialog(null, "Jumlah yang dibayar harus lebih besar daripada sisa.");
                    return; // Keluar dari metode jika validasi gagal
                }

                try {
                    st = con.createStatement();

                    // Ambil nilai bayar/dp dari tabel pesanan
                    String sqlGetBayar = "SELECT bayar FROM pesanan WHERE no_order='" + NoOrder + "'";
                    ResultSet rsBayar = st.executeQuery(sqlGetBayar);
                    double bayarAwal = 0;
                    if (rsBayar.next()) {
                        bayarAwal = rsBayar.getDouble("bayar");
                    }
                    rsBayar.close();

                    // Tambahkan nilai dibayar input dengan bayar/dp dari tabel pesanan
                    double totalDibayar = bayarAwal + Double.parseDouble(DIbayar);

                    // Simpan transaksi baru
                    String sqlSimpan = "INSERT INTO transaksi (id_transaksi, no_order, id_pelanggan, tanggal_transaksi, dibayar, kembalian) VALUES ('"
                                       + ID_Transaksi + "', '"
                                       + NoOrder + "', '"
                                       + ID_Pel + "', '"
                                       + smpdtfmt.format(tglsekarang).toString() + "', '"
                                       + totalDibayar + "', '"
                                       + KembalianNya + "')";
                    st.executeUpdate(sqlSimpan);

                    // Update status dan sisa di tabel pesanan
                    String sqlUpdate = "UPDATE pesanan SET status='"
                                       + StatusNya + "', sisa='"
                                       + sisaNya + "', bayar='"
                                       + totalDibayar + "' WHERE no_order='"
                                       + NoOrder + "'";
                    st.executeUpdate(sqlUpdate);

                    // Menyimpan data ke tabel history_transaksi
                    String sqlHistoryId = "SELECT * FROM history_transaksi ORDER BY id_history DESC";
                    ResultSet rs = st.executeQuery(sqlHistoryId);
                    String newHistoryId;
                    if (rs.next()) {
                        String lastHistory = rs.getString("id_history");
                        int numericPart = Integer.parseInt(lastHistory.replace("HIS", "")) + 1;
                        newHistoryId = "HIS" + String.format("%03d", numericPart);
                    } else {
                        newHistoryId = "HIS001";
                    }
                    rs.close();

                    String sqlHistory = "INSERT INTO history_transaksi (id_history, id_transaksi, no_order, id_pelanggan) VALUES ('"
                                        + newHistoryId + "', '"
                                        + ID_Transaksi + "', '"
                                        + NoOrder + "', '"
                                        + ID_Pel + "')";
                    st.executeUpdate(sqlHistory);

                    JOptionPane.showMessageDialog(null, "Data Berhasil Masuk!");

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Data Tak Masuk " + e.getMessage());
                }
                tampil_tabel();
                btn_simpan.setEnabled(false);
                membersihkan_teks();

    }//GEN-LAST:event_btn_simpanActionPerformed

    private void dibayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dibayarKeyReleased
        // TODO add your handling code here:
         try {
            // Parsing input dari JTextField
            bayarnya = Integer.parseInt(dibayar.getText());
            sisanya = Integer.parseInt(sisa.getText());

            // Menghitung kembalian
            kembaliannya = bayarnya + sisanya;
            kembalian.setText(String.valueOf(kembaliannya));
            
            if (bayarnya >= -1*sisanya) {
                status.setText("Lunas");
                sisaNya = 0;
            } else {
                status.setText("Belum Lunas");
                sisaNya = sisanya + bayarnya;
                kembaliannya = 0;
                kembalian.setText(String.valueOf(kembaliannya));
            }

        } catch (NumberFormatException e) {
            // Menangani input yang bukan angka
            status.setText("Input tidak valid");
            kembalian.setText("");
        }
    }//GEN-LAST:event_dibayarKeyReleased

    private void bayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bayarActionPerformed

    private void btn_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cariActionPerformed
        // TODO add your handling code here:
        cari_transaksi();
    }//GEN-LAST:event_btn_cariActionPerformed

    private void btn_generateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_generateActionPerformed
        // TODO add your handling code here:
        cari_urut();
    }//GEN-LAST:event_btn_generateActionPerformed

    private void btn_historyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_historyActionPerformed
        // TODO add your handling code here:
        history_transaksi_page hp = new history_transaksi_page();
        hp.setVisible(true);
        
        this.setVisible(false);
    }//GEN-LAST:event_btn_historyActionPerformed

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
            java.util.logging.Logger.getLogger(transaksi_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(transaksi_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(transaksi_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(transaksi_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new transaksi_page().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextField bayar;
    private javax.swing.JButton btn_cari;
    private javax.swing.JButton btn_generate;
    private javax.swing.JButton btn_history;
    private javax.swing.JButton btn_kembali;
    public javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_tutp;
    private javax.swing.JTextField cari_transaksi;
    public javax.swing.JTextField dibayar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextField kembalian;
    public javax.swing.JTextField nama_pelanggan;
    public javax.swing.JTextField no_order;
    public javax.swing.JTextField no_transaksi;
    private com.toedter.calendar.JDateChooser pilih_tanggal;
    public javax.swing.JTextField sisa;
    public javax.swing.JTextField status;
    private javax.swing.JTable tabel_transaksi;
    private javax.swing.JLabel tanggal;
    public javax.swing.JTextField total_bayar;
    // End of variables declaration//GEN-END:variables
}
