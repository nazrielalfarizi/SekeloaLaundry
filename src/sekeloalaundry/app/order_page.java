/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sekeloalaundry.app;

import com.mysql.jdbc.PreparedStatement;
import static java.awt.image.ImageObserver.WIDTH;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import static sekeloalaundry.app.pelanggan_order.statusSearching;

/**
 *
 * @author yodie
 */
public class order_page extends javax.swing.JFrame {
    private DefaultTableModel model;
    private static final DateTimeFormatter smpdtfmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDateTime tglsekarang = LocalDateTime.now();
    private final String ltanggal = smpdtfmt.format(tglsekarang);
    
    
    

    
    private LocalDate tanggalOrder;
    private LocalDate tanggalSelesai;
    
    
    String selectedItemStr,KDID,jcnya,selectedItemStrPengerjaan,ID_Pel;
    int harganya;
    int hargahitung,total,kg,totalfix,bayar,totalhitung,kembalian;

    Connection con;
    Statement st;
    private int xx;
    private int xy;
    /**
     * Creates new form order_page
     */
    public order_page() {
        
        initComponents();
        setLocationRelativeTo(this);
        
        koneksi data = new koneksi();
        data.config();
        con = data.con;
        st = data.st;
        model = (DefaultTableModel)tabel_pes.getModel();
        
        tampil_tabel();
        btn_hapus.setEnabled(false);
        btn_lanjut_transaksi.setEnabled(false);
        
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
    
    public void jenis_cucian_pen() {
        try {
                st = con.createStatement();
                String sql = "SELECT * FROM sekeloa_laundry.jenis_cucian";
                ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        idjc_pen.addItem((rs.getString(2)));
                    }

                    rs.close();
                    st.close();
                        } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex, ex.getMessage(), WIDTH, null);
                        }
       }
    
    private void cari_data_pel() {
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        statusSearching = 1;

        String cari = cari_order.getText();

        if(cari.isEmpty()) {
            statusSearching = 0;
        } else if(statusSearching == 1) {
            try {
                String sql = "SELECT " +
                             "p.no_order, " +
                             "pel.id_pelanggan, " +
                             "pel.nama_pelanggan, " +
                             "p.tanggal_order, " +
                             "p.tanggal_selesai, " +
                             "p.total_bayar, " +
                             "p.bayar, " +
                             "p.sisa, " +
                             "p.status, " +
                             "jc.jenis_cucian, " +
                             "p.berat, " +
                             "p.pengerjaan " +
                             "FROM pesanan p " +
                             "JOIN pelanggan pel ON p.id_pelanggan = pel.id_pelanggan " +
                             "JOIN jenis_cucian jc ON p.kd_jenis = jc.kd_jenis " +
                             "WHERE " +
                             "(p.no_order LIKE ? " +
                             "OR pel.id_pelanggan LIKE ? " +
                             "OR pel.nama_pelanggan LIKE ?)";

                PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
                pst.setString(1, "%" + cari + "%");
                pst.setString(2, "%" + cari + "%");
                pst.setString(3, "%" + cari + "%");

                ResultSet rs = pst.executeQuery();

                while(rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("no_order"),
                        rs.getString("id_pelanggan"),
                        rs.getString("nama_pelanggan"),
                        rs.getString("tanggal_order"),
                        rs.getString("tanggal_selesai"),
                        rs.getString("jenis_cucian"),
                        rs.getString("berat"),
                        rs.getString("pengerjaan"),
                        rs.getString("total_bayar"),
                        rs.getString("bayar"),
                        rs.getString("sisa"),
                        rs.getString("status")
                    });
                }

                tabel_pes.setModel(model);

                rs.close();
                pst.close();
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(rootPane, "Data yang dicari tidak ditemukan!");
            }
        }
    }
    
    private void tampil_tabel() {
        try {
            st = con.createStatement();
            String sql = "SELECT " +
                         "p.no_order, " +
                         "pel.id_pelanggan, " +
                         "pel.nama_pelanggan, " +
                         "p.tanggal_order, " +
                         "p.tanggal_selesai, " +
                         "p.total_bayar, " +
                         "p.bayar, " +
                         "p.sisa, " +
                         "p.status, " +
                         "jc.jenis_cucian, " +
                         "p.berat, " +
                         "p.pengerjaan " +
                         "FROM pesanan p " +
                         "JOIN pelanggan pel ON p.id_pelanggan = pel.id_pelanggan " +
                         "JOIN jenis_cucian jc ON p.kd_jenis = jc.kd_jenis " +
                         "WHERE p.status = 'Belum Lunas'";

            ResultSet rs = st.executeQuery(sql);

            DefaultTableModel tm = (DefaultTableModel) tabel_pes.getModel();
            tm.setColumnIdentifiers(new Object[] {
                "No Order", "ID Pelanggan", "Nama Pelanggan", "Tanggal Order", "Tanggal Selelsai", 
                "Jenis Cucian", "Berat", "Pengerjaan", "Total Bayar", "Bayar", "Sisa", "Status"
            });

            tm.setRowCount(0);
            while (rs.next()) {
                Object[] row = new Object[12];
                row[0] = rs.getString("no_order");
                row[1] = rs.getString("id_pelanggan");
                row[2] = rs.getString("nama_pelanggan");
                row[3] = rs.getString("tanggal_order");
                row[4] = rs.getString("tanggal_selesai");
                row[5] = rs.getString("jenis_cucian");
                row[6] = rs.getString("berat");
                row[7] = rs.getString("pengerjaan");
                row[8] = rs.getString("total_bayar");
                row[9] = rs.getString("bayar");
                row[10] = rs.getString("sisa");
                row[11] = rs.getString("status");

                tm.addRow(row);
            }

            tm.fireTableDataChanged();
            rs.close();
            st.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex, ex.getMessage(), WIDTH, null);
        }
    }
    
        private void tampil_seluruh() {
        try {
            st = con.createStatement();
            String sql = "SELECT " +
                         "p.no_order, " +
                         "pel.id_pelanggan, " +
                         "pel.nama_pelanggan, " +
                         "p.tanggal_order, " +
                         "p.tanggal_selesai, " +
                         "p.total_bayar, " +
                         "p.bayar, " +
                         "p.sisa, " +
                         "p.status, " +
                         "jc.jenis_cucian, " +
                         "p.berat, " +
                         "p.pengerjaan " +
                         "FROM pesanan p " +
                         "JOIN pelanggan pel ON p.id_pelanggan = pel.id_pelanggan " +
                         "JOIN jenis_cucian jc ON p.kd_jenis = jc.kd_jenis ";
                         

            ResultSet rs = st.executeQuery(sql);

            DefaultTableModel tm = (DefaultTableModel) tabel_pes.getModel();
            tm.setColumnIdentifiers(new Object[] {
                "No Order", "ID Pelanggan", "Nama Pelanggan", "Tanggal Order", "Tanggal Selelsai", 
                "Jenis Cucian", "Berat", "Pengerjaan", "Total Bayar", "Bayar", "Sisa", "Status"
            });

            tm.setRowCount(0);
            while (rs.next()) {
                Object[] row = new Object[12];
                row[0] = rs.getString("no_order");
                row[1] = rs.getString("id_pelanggan");
                row[2] = rs.getString("nama_pelanggan");
                row[3] = rs.getString("tanggal_order");
                row[4] = rs.getString("tanggal_selesai");
                row[5] = rs.getString("jenis_cucian");
                row[6] = rs.getString("berat");
                row[7] = rs.getString("pengerjaan");
                row[8] = rs.getString("total_bayar");
                row[9] = rs.getString("bayar");
                row[10] = rs.getString("sisa");
                row[11] = rs.getString("status");

                tm.addRow(row);
            }

            tm.fireTableDataChanged();
            rs.close();
            st.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex, ex.getMessage(), WIDTH, null);
        }
    }


    private void click_data() {
        String NoOrder = tabel_pes.getValueAt(tabel_pes.getSelectedRow(), 0).toString();
        no_order.setText(NoOrder);
        nama.setText("");
        berat.setText("");
        bayar_pen.setText("");
        total_pen.setText("");
        sisa_pen.setText("");
        status_pen.setText("");
    }
    
    private void hapus_data() {
            String[] options = {"Yes", "No"};
            JOptionPane.showOptionDialog(null, "Yakin Hapus Data Ini?", "Delete Confirm", 
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

            String id = no_order.getText();

            try {

            st = con.createStatement();
            String sqlUpdate = "DELETE FROM pesanan WHERE no_order='"+id+"'";
            st.executeUpdate(sqlUpdate);
            tampil_tabel();
           
            

        JOptionPane.showMessageDialog(null, "Data Dihapus dari Database");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Dihapus " + e.getMessage());
            }
        
    }
    
        private void cari_data_urut() {
            model.getDataVector().removeAllElements();
            model.fireTableDataChanged();
            statusSearching = 1;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(pilih_tanggal.getDate());
            String status = combo_status.getSelectedItem().toString();

            if (date.isEmpty() || status.equals("-")) {
                statusSearching = 0;
            } else if (statusSearching == 1) {
                try {
                    String sql = "SELECT " +
                                 "p.no_order, " +
                                 "pel.id_pelanggan, " +
                                 "pel.nama_pelanggan, " +
                                 "p.tanggal_order, " +
                                 "p.tanggal_selesai, " +
                                 "p.total_bayar, " +
                                 "p.bayar, " +
                                 "p.sisa, " +
                                 "p.status, " +
                                 "jc.jenis_cucian, " +
                                 "p.berat, " +
                                 "p.pengerjaan " +
                                 "FROM pesanan p " +
                                 "JOIN pelanggan pel ON p.id_pelanggan = pel.id_pelanggan " +
                                 "JOIN jenis_cucian jc ON p.kd_jenis = jc.kd_jenis " +
                                 "WHERE p.tanggal_order = ? " +
                                 "AND (p.status = ? OR ? = '-')";

                    PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
                    pst.setString(1, date);
                    pst.setString(2, status);
                    pst.setString(3, status);

                    ResultSet rs = pst.executeQuery();

                    while (rs.next()) {
                        model.addRow(new Object[]{
                            rs.getString("no_order"),
                            rs.getString("id_pelanggan"),
                            rs.getString("nama_pelanggan"),
                            rs.getString("tanggal_order"),
                            rs.getString("tanggal_selesai"),
                            rs.getString("jenis_cucian"),
                            rs.getString("berat"),
                            rs.getString("pengerjaan"),
                            rs.getString("total_bayar"),
                            rs.getString("bayar"),
                            rs.getString("sisa"),
                            rs.getString("status")
                        });
                    }

                    tabel_pes.setModel(model);
                    

                    rs.close();
                    pst.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(rootPane, "Data yang dicari tidak ditemukan!");
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_pes = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        harga_pen = new javax.swing.JLabel();
        nama = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        idjc_pen = new javax.swing.JComboBox<>();
        tanggal_order = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        berat = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        total_pen = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        bayar_pen = new javax.swing.JTextField();
        sisa_pen = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        status_pen = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        no_order = new javax.swing.JTextField();
        pengerjaan = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        tanggal_selesai = new javax.swing.JLabel();
        kembaili_pen = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        btn_buatorder = new javax.swing.JButton();
        btn_simpan = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_lanjut_transaksi = new javax.swing.JButton();
        btn_hapus2 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cari_order = new javax.swing.JTextField();
        combo_status = new javax.swing.JComboBox<>();
        pilih_tanggal = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        btn_cari = new javax.swing.JButton();
        btn_generate = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btn_hapus1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Data Order");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "TABEL ORDER"));

        tabel_pes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No Order", "ID Pelanggan", "Nama", "Tanggal Order", "Tanggal Selesai", "Pengerjaan", "Jenis Cucian", "Berat", "Total Bayar", "Bayar", "Status"
            }
        ));
        tabel_pes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_pesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel_pes);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2), "FORM ORDER"));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-numeric-20.png"))); // NOI18N
        jLabel2.setText("No Order");
        jLabel2.setToolTipText("");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-administrator-male-25.png"))); // NOI18N
        jLabel3.setText("Pelanggan");
        jLabel3.setToolTipText("");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-hair-dryer-20.png"))); // NOI18N
        jLabel4.setText("Jenis Cucian");
        jLabel4.setToolTipText("");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-weight-kg-20.png"))); // NOI18N
        jLabel5.setText("Berat");
        jLabel5.setToolTipText("");

        harga_pen.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        harga_pen.setText("Harga");

        nama.setEditable(false);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("/ Kg");

        idjc_pen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Jenis Cucian" }));
        idjc_pen.setToolTipText("");
        idjc_pen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idjc_penActionPerformed(evt);
            }
        });

        tanggal_order.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tanggal_order.setText("Tanggal");
        tanggal_order.setToolTipText("");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-express-20.png"))); // NOI18N
        jLabel10.setText("Pengerjaan");
        jLabel10.setToolTipText("");

        berat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                beratKeyReleased(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-bill-20.png"))); // NOI18N
        jLabel11.setText("Total");

        total_pen.setEditable(false);
        total_pen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                total_penActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-cash-in-hand-20.png"))); // NOI18N
        jLabel12.setText("Bayar / DP");

        bayar_pen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayar_penActionPerformed(evt);
            }
        });
        bayar_pen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bayar_penKeyReleased(evt);
            }
        });

        sisa_pen.setEditable(false);
        sisa_pen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sisa_penActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-cash-hand-20.png"))); // NOI18N
        jLabel14.setText("Sisa");

        status_pen.setEditable(false);
        status_pen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                status_penActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-data-pending-20.png"))); // NOI18N
        jLabel15.setText("Status");

        no_order.setEditable(false);

        pengerjaan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Jenis Pengerjaan", "Next Day", "Reguler" }));
        pengerjaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pengerjaanActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-calendar-27-20.png"))); // NOI18N
        jLabel9.setText("Tanggal order");
        jLabel9.setToolTipText("");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-submit-progress-20.png"))); // NOI18N
        jLabel13.setText("Tanggal Selesai");
        jLabel13.setToolTipText("");

        tanggal_selesai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tanggal_selesai.setText("Tanggal");
        tanggal_selesai.setToolTipText("");
        tanggal_selesai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tanggal_selesaiKeyReleased(evt);
            }
        });

        kembaili_pen.setEditable(false);
        kembaili_pen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kembaili_penActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-request-money-20.png"))); // NOI18N
        jLabel16.setText("Kembalian");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nama))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(harga_pen, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(idjc_pen, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(no_order, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(128, 128, 128)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(berat, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(37, 37, 37)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tanggal_order, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pengerjaan, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tanggal_selesai, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 2, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(total_pen, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bayar_pen, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sisa_pen, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(status_pen, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kembaili_pen, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(no_order, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nama, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idjc_pen, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(harga_pen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(berat, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(total_pen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tanggal_order, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bayar_pen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(pengerjaan, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sisa_pen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tanggal_selesai, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(status_pen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kembaili_pen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_buatorder.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_buatorder.setText("BUAT ORDER");
        btn_buatorder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buatorderActionPerformed(evt);
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

        btn_lanjut_transaksi.setBackground(new java.awt.Color(51, 102, 255));
        btn_lanjut_transaksi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_lanjut_transaksi.setForeground(new java.awt.Color(255, 255, 255));
        btn_lanjut_transaksi.setText("LANJUT TRANSAKSI");
        btn_lanjut_transaksi.setBorderPainted(false);
        btn_lanjut_transaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_lanjut_transaksiActionPerformed(evt);
            }
        });

        btn_hapus2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_hapus2.setText("BATAL");
        btn_hapus2.setBorderPainted(false);
        btn_hapus2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapus2ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("CARI ORDERAN");

        combo_status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Belum Lunas", "Lunas" }));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("URUTKAN");

        btn_cari.setText("CARI");
        btn_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cariActionPerformed(evt);
            }
        });

        btn_generate.setText("GENERATE");
        btn_generate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_generateActionPerformed(evt);
            }
        });

        jButton3.setText("Tampilkan Seluruh Data");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(btn_simpan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_hapus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_hapus2)
                .addGap(149, 149, 149)
                .addComponent(btn_buatorder)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_lanjut_transaksi)
                .addGap(18, 18, 18))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cari_order, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pilih_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(combo_status, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_generate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(combo_status, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pilih_tanggal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cari_order, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_cari))
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_generate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_simpan)
                        .addComponent(btn_hapus)
                        .addComponent(btn_hapus2)
                        .addComponent(btn_buatorder))
                    .addComponent(btn_lanjut_transaksi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sekeloalaundry/icon/icons8-purchase-order-40.png"))); // NOI18N
        jLabel1.setText("ORDER MASUK");

        btn_hapus1.setBackground(new java.awt.Color(255, 0, 51));
        btn_hapus1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_hapus1.setForeground(new java.awt.Color(255, 255, 255));
        btn_hapus1.setText("TUTUP");
        btn_hapus1.setBorderPainted(false);
        btn_hapus1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapus1ActionPerformed(evt);
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
                        .addGap(8, 8, 8)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_hapus1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_hapus1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void total_penActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_total_penActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_total_penActionPerformed

    private void bayar_penActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayar_penActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bayar_penActionPerformed

    private void sisa_penActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sisa_penActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sisa_penActionPerformed

    private void status_penActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_status_penActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_status_penActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        tanggal_order.setText(ltanggal);
        jenis_cucian_pen();
    }//GEN-LAST:event_formWindowOpened

    private void idjc_penActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idjc_penActionPerformed
        // TODO add your handling code here:
        Object selectedItem = idjc_pen.getSelectedItem();
        if (selectedItem != null) {
            String selectedItemStr = selectedItem.toString();

            if (selectedItemStr.equals("Pilih Jenis Cucian")) {
                harga_pen.setText("0");
                total_pen.setText("");
                berat.setText("");
                bayar_pen.setText("");
            } else {
                try {
                    st = con.createStatement();
                    String sql = "SELECT * FROM jenis_cucian WHERE jenis_cucian='" + selectedItemStr + "'";
                    ResultSet rs = st.executeQuery(sql);
                    while (rs.next()) {
                        KDID = rs.getString(1);
                        jcnya = rs.getString(2);
                        harga_pen.setText(rs.getString(3));
                        total_pen.setText(harga_pen.getText());
                        berat.setText("");
                        bayar_pen.setText("");
                    }
                    rs.close();
                    st.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex, ex.getMessage(), WIDTH, null);
                }
            }
        }
    }//GEN-LAST:event_idjc_penActionPerformed

    private void beratKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_beratKeyReleased
        // TODO add your handling code here:
        kg = Integer.parseInt(berat.getText());
        hargahitung = Integer.parseInt(harga_pen.getText());
        total = kg * hargahitung;
        total_pen.setText(String.valueOf(total));
    }//GEN-LAST:event_beratKeyReleased

    private void bayar_penKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bayar_penKeyReleased
        // TODO add your handling code here:
        bayar = Integer.parseInt(bayar_pen.getText());
        totalhitung = Integer.parseInt(total_pen.getText());
        kembalian = bayar - totalhitung;
        
        int kembali = 0;
        if (bayar > totalhitung) {
            sisa_pen.setText("0");
            kembali = bayar - totalhitung;
            kembaili_pen.setText(String.valueOf(kembali));
        } else {
            sisa_pen.setText(String.valueOf(kembalian));
            kembaili_pen.setText("0");
        }
        
        int Sisanya = Integer.parseInt(sisa_pen.getText());
        if (Sisanya > -1) {
            status_pen.setText("Lunas");
        } else if(Sisanya < 0) {
        status_pen.setText("Belum Lunas");
        }
    }//GEN-LAST:event_bayar_penKeyReleased

    private void tanggal_selesaiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tanggal_selesaiKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tanggal_selesaiKeyReleased

    private void pengerjaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pengerjaanActionPerformed
        // TODO add your handling code here:
         Object selectedItem = pengerjaan.getSelectedItem();
        if (selectedItem != null) {
            selectedItemStrPengerjaan = selectedItem.toString();
            tanggalOrder = LocalDate.now(); // Ambil tanggal order hari ini
            if (selectedItemStrPengerjaan.equals("Next Day")) {
                tanggalSelesai = tanggalOrder.plusDays(1);
                total_pen.setText(String.valueOf(total+10000));
                bayar_pen.setText("");
                // Tambah 1 hari untuk layanan Next Day
            } else if (selectedItemStrPengerjaan.equals("Reguler")) {
                tanggalSelesai = tanggalOrder.plusDays(3);
                total_pen.setText(String.valueOf(total));
                bayar_pen.setText("");
                // Tambah 3 hari untuk layanan Reguler
            } else if (selectedItemStrPengerjaan.equals("Pilih Jenis Pengerjaan")) {
                tanggalSelesai = tanggalOrder.plusDays(0);
                total_pen.setText(String.valueOf(total));
                bayar_pen.setText("");
                // Tambah 3 hari untuk layanan Reguler
            }
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedTanggalSelesai = tanggalSelesai.format(formatter);
            tanggal_selesai.setText(formattedTanggalSelesai);
        }
    }//GEN-LAST:event_pengerjaanActionPerformed

    private void btn_buatorderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buatorderActionPerformed
        // TODO add your handling code here:
        pelanggan_order po = new pelanggan_order();
        po.setVisible(true);
        
        this.setVisible(false);
    }//GEN-LAST:event_btn_buatorderActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
                    // TODO add your handling code here:

             String NoOrder = no_order.getText();
             String TBayar = total_pen.getText();
             String Bayarr = bayar_pen.getText();
             String Sisaa = sisa_pen.getText();
             String Statuss = status_pen.getText();
             String Beratt = berat.getText();
             String Tkeluar = tanggal_selesai.getText();
             selectedItemStr = idjc_pen.getSelectedItem().toString();
             selectedItemStrPengerjaan = pengerjaan.getSelectedItem().toString();

             // Validasi input
             if (selectedItemStr.equals("") || selectedItemStr.equals("Pilih Jenis Cucian")) {
                 JOptionPane.showMessageDialog(null, "Jenis Cucian harus dipilih.");
                 berat.setText("");
                 bayar_pen.setText("");
                 total_pen.setText("");
             } else if (Beratt.equals("")) {
                 JOptionPane.showMessageDialog(null, "Berat harus diisi.");
             } else if (!Beratt.matches("\\d+")) {
                 JOptionPane.showMessageDialog(null, "Berat harus berupa angka.");
             } else if (selectedItemStrPengerjaan.equals("") || selectedItemStrPengerjaan.equals("Pilih Jenis Pengerjaan")) {
                 JOptionPane.showMessageDialog(null, "Pengerjaan harus dipilih.");
                 berat.setText("");
                 bayar_pen.setText("");
                 total_pen.setText("");
             } else if (Bayarr.equals("")) {
                 JOptionPane.showMessageDialog(null, "Harap Bayar harus diisi.");
             } else if (!Bayarr.matches("\\d+")) {
                 JOptionPane.showMessageDialog(null, "Bayar harus berupa angka.");
             } else if (Integer.parseInt(Bayarr) <= 0) {
                 JOptionPane.showMessageDialog(null, "Harap Bayar Uang Muka.");
             } else if (Integer.parseInt(TBayar) > 30000 && Integer.parseInt(Bayarr) < 20000) {
                 JOptionPane.showMessageDialog(null, "Jika Total Bayar lebih dari 30.000 maka Bayar harus lebih dari atau sama dengan 20.000.");
             } else {
                 try {
                     st = con.createStatement();
                     String sqlSimpan = "INSERT INTO pesanan (no_order, id_pelanggan, kd_jenis, tanggal_order, tanggal_selesai, total_bayar, bayar, sisa, status, berat, pengerjaan) " +
                                        "VALUES ('"+NoOrder+"','"+ID_Pel+"', '"+KDID+"', '"+smpdtfmt.format(tglsekarang).toString()+"', '"+Tkeluar+"', '"+TBayar+"', '"+Bayarr+"', '"+Sisaa+"', '"+Statuss+"', '"+Beratt+"', '"+selectedItemStrPengerjaan+"')";
                     st.executeUpdate(sqlSimpan);

                     JOptionPane.showMessageDialog(null, "Data Berhasil Masuk!");

                     if (Statuss.equalsIgnoreCase("Lunas")) {
                         try {
                             // Mendapatkan id_transaksi baru
                             String sql = "SELECT * FROM transaksi ORDER BY id_transaksi DESC";
                             ResultSet rs = st.executeQuery(sql);
                             String newOrder;
                             if (rs.next()) {
                                 String lastOrder = rs.getString("id_transaksi");
                                 int numericPart = Integer.parseInt(lastOrder.replace("TRK", "")) + 1;
                                 newOrder = "TRK" + String.format("%03d", numericPart);
                             } else {
                                 newOrder = "TRK001";
                             }
                             rs.close();

                             // Menyimpan transaksi baru
                             String tanggalTransaksi = smpdtfmt.format(tglsekarang).toString();
                             String kembalian = kembaili_pen.getText();
                             String sqlTransaksi = "INSERT INTO transaksi (id_transaksi, no_order, id_pelanggan, tanggal_transaksi, dibayar, kembalian) " +
                                                   "VALUES ('"+newOrder+"', '"+NoOrder+"', '"+ID_Pel+"', '"+tanggalTransaksi+"', '"+Bayarr+"', '"+kembalian+"')";
                             st.executeUpdate(sqlTransaksi);

                             // Mendapatkan id_history baru
                             sql = "SELECT * FROM history_transaksi ORDER BY id_history DESC";
                             rs = st.executeQuery(sql);
                             String newHistory;
                             if (rs.next()) {
                                 String lastHistory = rs.getString("id_history");
                                 int numericPart = Integer.parseInt(lastHistory.replace("HIS", "")) + 1;
                                 newHistory = "HIS" + String.format("%03d", numericPart);
                             } else {
                                 newHistory = "HIS001";
                             }
                             rs.close();

                             // Menyimpan history transaksi baru
                             String sqlHistory = "INSERT INTO history_transaksi (id_history, id_transaksi, no_order, id_pelanggan) " +
                                                 "VALUES ('"+newHistory+"', '"+newOrder+"', '"+NoOrder+"', '"+ID_Pel+"')";
                             st.executeUpdate(sqlHistory);

                             JOptionPane.showMessageDialog(null, "Data dengan status 'Lunas' langsung masuk ke data transaksi dan history transaksi!");

                             st.close();
                         } catch (Exception e) {
                             e.printStackTrace();
                         }
                     }
                 } catch (Exception e) {
                     JOptionPane.showMessageDialog(null, "Data Tak Masuk " + e.getMessage());
                 }
                 tampil_tabel();
                 btn_simpan.setEnabled(false);
             }
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void tabel_pesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_pesMouseClicked
        // TODO add your handling code here:
        click_data();
        btn_simpan.setEnabled(false);
        btn_hapus.setEnabled(true);
        btn_lanjut_transaksi.setEnabled(true);
    }//GEN-LAST:event_tabel_pesMouseClicked

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        // TODO add your handling code here:
        hapus_data();
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void btn_lanjut_transaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_lanjut_transaksiActionPerformed
        // TODO add your handling code here:
            int row = 0;
            transaksi_page tp = new transaksi_page();
            row = tabel_pes.getSelectedRow();
            TableModel tm = tabel_pes.getModel();

            // Periksa apakah ada baris yang dipilih
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Harap pilih data dari tabel pelanggan terlebih dahulu!");
                return; // Keluar dari metode jika tidak ada baris yang dipilih
            }

            // Ambil status dari baris yang dipilih
            String status = tm.getValueAt(row, 11).toString();

            // Periksa apakah statusnya sudah "Lunas"
            if (status.equals("Lunas")) {
                JOptionPane.showMessageDialog(null, "Data ini sudah lunas dan tidak dapat melakukan transaksi lanjutan!");
                return; // Keluar dari metode jika statusnya sudah "Lunas"
            }

            try {
                st = con.createStatement();
                String sql = "SELECT * FROM transaksi ORDER BY id_transaksi DESC";
                ResultSet rs = st.executeQuery(sql);
                if (rs.next()) {
                    String lastOrder = rs.getString("id_transaksi");
                    int numericPart = Integer.parseInt(lastOrder.replace("TRK", "")) + 1;
                    String newOrder = "TRK" + String.format("%03d", numericPart);
                    tp.no_transaksi.setText(newOrder);
                } else {
                    tp.no_transaksi.setText("TRK001");
                }
                rs.close();
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String order = tm.getValueAt(row, 0).toString();
            String id_pel = tm.getValueAt(row, 1).toString();
            String nama = tm.getValueAt(row, 2).toString();
            String tb = tm.getValueAt(row, 8).toString();
            String b = tm.getValueAt(row, 9).toString();
            String sisa = tm.getValueAt(row, 10).toString();

            tp.no_order.setText(order);
            tp.nama_pelanggan.setText(nama);
            tp.total_bayar.setText(tb);
            tp.bayar.setText(b);
            tp.status.setText(status);
            tp.sisa.setText(sisa);

            tp.setVisible(true);
            this.setVisible(false);
            tp.dibayar.requestFocus();
            tp.setIDPel(id_pel);
            tp.btn_simpan.setEnabled(true);
    }//GEN-LAST:event_btn_lanjut_transaksiActionPerformed

    private void btn_hapus1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapus1ActionPerformed
        // TODO add your handling code here:
        home_page hp = new home_page();
        hp.setVisible(true);
        
        this.setVisible(false);
    }//GEN-LAST:event_btn_hapus1ActionPerformed

    private void btn_hapus2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapus2ActionPerformed
        // TODO add your handling code here:
        btn_hapus.setEnabled(false);
        btn_lanjut_transaksi.setEnabled(false);
    }//GEN-LAST:event_btn_hapus2ActionPerformed

    private void btn_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cariActionPerformed
        // TODO add your handling code here:
        cari_data_pel();
    }//GEN-LAST:event_btn_cariActionPerformed

    private void btn_generateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_generateActionPerformed
        // TODO add your handling code here:
        cari_data_urut();
    }//GEN-LAST:event_btn_generateActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        tampil_seluruh();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void kembaili_penActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kembaili_penActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kembaili_penActionPerformed

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
            java.util.logging.Logger.getLogger(order_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(order_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(order_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(order_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new order_page().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField bayar_pen;
    private javax.swing.JTextField berat;
    private javax.swing.JButton btn_buatorder;
    private javax.swing.JButton btn_cari;
    private javax.swing.JButton btn_generate;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_hapus1;
    private javax.swing.JButton btn_hapus2;
    private javax.swing.JButton btn_lanjut_transaksi;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JTextField cari_order;
    private javax.swing.JComboBox<String> combo_status;
    private javax.swing.JLabel harga_pen;
    private javax.swing.JComboBox<String> idjc_pen;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JTextField kembaili_pen;
    public javax.swing.JTextField nama;
    public javax.swing.JTextField no_order;
    private javax.swing.JComboBox<String> pengerjaan;
    private com.toedter.calendar.JDateChooser pilih_tanggal;
    private javax.swing.JTextField sisa_pen;
    private javax.swing.JTextField status_pen;
    private javax.swing.JTable tabel_pes;
    private javax.swing.JLabel tanggal_order;
    private javax.swing.JLabel tanggal_selesai;
    private javax.swing.JTextField total_pen;
    // End of variables declaration//GEN-END:variables
}
