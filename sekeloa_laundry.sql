-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 04, 2024 at 06:12 AM
-- Server version: 10.4.21-MariaDB
-- PHP Version: 8.0.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sekeloa_laundry`
--

-- --------------------------------------------------------

--
-- Table structure for table `history_transaksi`
--

CREATE TABLE `history_transaksi` (
  `id_history` varchar(10) NOT NULL,
  `id_transaksi` varchar(10) NOT NULL,
  `no_order` varchar(10) NOT NULL,
  `id_pelanggan` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `history_transaksi`
--

INSERT INTO `history_transaksi` (`id_history`, `id_transaksi`, `no_order`, `id_pelanggan`) VALUES
('HIS001', 'TRK001', 'ORD001', 'PLG002'),
('HIS002', 'TRK002', 'ORD002', 'PLG001');

-- --------------------------------------------------------

--
-- Table structure for table `jenis_cucian`
--

CREATE TABLE `jenis_cucian` (
  `kd_jenis` varchar(10) NOT NULL,
  `jenis_cucian` varchar(30) NOT NULL,
  `harga` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `jenis_cucian`
--

INSERT INTO `jenis_cucian` (`kd_jenis`, `jenis_cucian`, `harga`) VALUES
('JC2', 'Cuci Setrika', 10000),
('JC3', 'Cuci', 7000);

-- --------------------------------------------------------

--
-- Table structure for table `pelanggan`
--

CREATE TABLE `pelanggan` (
  `id_pelanggan` varchar(10) NOT NULL,
  `nama_pelanggan` varchar(50) NOT NULL,
  `alamat` varchar(100) NOT NULL,
  `telp` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `pelanggan`
--

INSERT INTO `pelanggan` (`id_pelanggan`, `nama_pelanggan`, `alamat`, `telp`) VALUES
('PLG001', 'Nazriel', 'B', '0895'),
('PLG002', 'Yodi', 'Kuningan', '0822');

-- --------------------------------------------------------

--
-- Table structure for table `pengguna`
--

CREATE TABLE `pengguna` (
  `id_pengguna` varchar(10) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `nama_pengguna` varchar(50) NOT NULL,
  `telp` varchar(50) NOT NULL,
  `alamat` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `pengguna`
--

INSERT INTO `pengguna` (`id_pengguna`, `username`, `password`, `nama_pengguna`, `telp`, `alamat`) VALUES
('USR1', 'admin', 'admin', 'Admin', '0897', 'B'),
('USR2', 'nazriel', 'nazriel', 'Muhammad Nazriel', '0895', '-');

-- --------------------------------------------------------

--
-- Table structure for table `pesanan`
--

CREATE TABLE `pesanan` (
  `no_order` varchar(10) NOT NULL,
  `id_pelanggan` varchar(10) NOT NULL,
  `tanggal_order` date NOT NULL,
  `tanggal_selesai` date NOT NULL,
  `total_bayar` int(10) NOT NULL,
  `bayar` int(10) NOT NULL,
  `sisa` int(10) NOT NULL,
  `status` varchar(20) NOT NULL,
  `kd_jenis` varchar(10) NOT NULL,
  `berat` int(10) NOT NULL,
  `pengerjaan` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `pesanan`
--

INSERT INTO `pesanan` (`no_order`, `id_pelanggan`, `tanggal_order`, `tanggal_selesai`, `total_bayar`, `bayar`, `sisa`, `status`, `kd_jenis`, `berat`, `pengerjaan`) VALUES
('ORD001', 'PLG002', '2024-08-04', '2024-08-05', 73000, 80000, 0, 'Lunas', 'JC3', 9, 'Next Day'),
('ORD002', 'PLG001', '2024-08-04', '2024-08-05', 100000, 100000, 0, 'Lunas', 'JC2', 9, 'Next Day');

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id_transaksi` varchar(10) NOT NULL,
  `no_order` varchar(10) NOT NULL,
  `id_pelanggan` varchar(10) NOT NULL,
  `tanggal_transaksi` date NOT NULL,
  `dibayar` int(10) NOT NULL,
  `kembalian` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transaksi`
--

INSERT INTO `transaksi` (`id_transaksi`, `no_order`, `id_pelanggan`, `tanggal_transaksi`, `dibayar`, `kembalian`) VALUES
('TRK001', 'ORD001', 'PLG002', '2024-08-04', 80000, 7000),
('TRK002', 'ORD002', 'PLG001', '2024-08-04', 100000, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `history_transaksi`
--
ALTER TABLE `history_transaksi`
  ADD PRIMARY KEY (`id_history`),
  ADD KEY `id_transaksi` (`id_transaksi`),
  ADD KEY `no_order` (`no_order`),
  ADD KEY `id_pelanggan` (`id_pelanggan`);

--
-- Indexes for table `jenis_cucian`
--
ALTER TABLE `jenis_cucian`
  ADD PRIMARY KEY (`kd_jenis`);

--
-- Indexes for table `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`id_pelanggan`);

--
-- Indexes for table `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`id_pengguna`);

--
-- Indexes for table `pesanan`
--
ALTER TABLE `pesanan`
  ADD PRIMARY KEY (`no_order`),
  ADD KEY `id_pelanggan` (`id_pelanggan`),
  ADD KEY `kd_jenis` (`kd_jenis`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `no_order` (`no_order`),
  ADD KEY `id_pelanggan` (`id_pelanggan`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
