-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 29, 2025 at 01:25 PM
-- Server version: 11.7.2-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `antiscamdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `blocked_numbers`
--

CREATE TABLE `blocked_numbers` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `phone_number` varchar(20) NOT NULL,
  `block_calls` tinyint(1) DEFAULT 0,
  `block_sms` tinyint(1) DEFAULT 0,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

--
-- Dumping data for table `blocked_numbers`
--

INSERT INTO `blocked_numbers` (`id`, `user_id`, `phone_number`, `block_calls`, `block_sms`, `created_at`) VALUES
(1, 3, '64646', 0, 1, '2025-03-29 09:04:39'),
(3, 3, '97979', 1, 0, '2025-03-29 09:59:54');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `token` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `full_name`, `username`, `email`, `password`, `token`) VALUES
(1, 'test1', 'jdhshs', 'jshshs', '$2y$10$zpQps6ID5weSpWMukvZrM.qVaSq6pvUjSteJCcXcxiCf6Rwh/Ysp6', NULL),
(2, 'test2', 'hdhshs', 'hdhdhz', '$2y$10$j8zPM1n2uFxJG2B9A94pOurfapp1.yQ8svCxAY1495/9qAbO..s1e', NULL),
(3, 'jdjdj', 'test1', 'bxbxhx', '$2y$10$ZrbpC.yQ7I1Z7bmErrYi6.hLAtXIz5QlwGI4LwrfKL3y9FFp7tU6C', 'dbe637ae2f0da4fdd972ede247157ba3bfcd067185d38a8cb4f90a5fb67b5fa5'),
(7, 'jdjdj', 'jdjsj', 'test2', '$2y$10$4B2AJu/p8g7nxuPXLpSfgODq9S7hNVDTTvY8XgtXlEdWoPKWcJxby', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `blocked_numbers`
--
ALTER TABLE `blocked_numbers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `blocked_numbers`
--
ALTER TABLE `blocked_numbers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `blocked_numbers`
--
ALTER TABLE `blocked_numbers`
  ADD CONSTRAINT `blocked_numbers_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
