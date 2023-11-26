-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 26, 2023 at 09:20 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dcoms_test`
--

-- --------------------------------------------------------

--
-- Table structure for table `carts`
--

CREATE TABLE `carts` (
  `cart_id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `product_quantity` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `order_date_time` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `carts`
--

INSERT INTO `carts` (`cart_id`, `product_id`, `product_quantity`, `user_id`, `order_date_time`) VALUES
(12, 8, 24, 5, '2023-11-26 07:21:04'),
(13, 9, 18, 5, '2023-11-26 07:22:06'),
(14, 2, 14, 5, '2023-11-26 07:34:25'),
(15, 4, 3, 5, '2023-11-26 07:48:26'),
(16, 7, 6, 5, '2023-11-26 07:49:43'),
(17, 6, 3, 5, '2023-11-26 07:50:09'),
(18, 2, 2, 8, '2023-11-26 08:17:35'),
(19, 7, 4, 8, '2023-11-26 08:17:47');

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `category_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`category_id`, `category_name`) VALUES
(1, 'Technology'),
(2, 'Clothes'),
(3, 'Foods');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `quantity_available` int(11) NOT NULL,
  `category_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `product_name`, `description`, `price`, `quantity_available`, `category_id`) VALUES
(1, 'Smartphone', 'Latest model with advanced features', 599.99, 20, 1),
(2, 'T-shirt', 'Comfortable and stylish', 29.99, 50, 2),
(3, 'Chocolate Bar', 'Delicious milk chocolate', 2.99, 100, 3),
(4, 'Laptop', 'Powerful laptop for work and play', 999.99, 10, 1),
(5, 'Headphones', 'High-quality noise-canceling headphones', 149.99, 30, 1),
(6, 'Jeans', 'Classic blue jeans', 39.99, 40, 2),
(7, 'Dress Shirt', 'Formal dress shirt for any occasion', 49.99, 25, 2),
(8, 'Granola Bars', 'Healthy and tasty snack bars', 3.99, 75, 3),
(9, 'Canned Soup', 'Assorted flavors for a quick meal', 2.49, 50, 3);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `first_name`, `last_name`, `email`, `phone`, `address`, `username`, `password`) VALUES
(1, 'g', 'p', 'g@p.com', '98477', 'pokhara', 'gokul', 'test'),
(2, 'gp', 'pg', 'pg@gp.com', '888888', 'np', 'pathak', 'api'),
(3, 'gokul', 'pathak', 'gokul@gmail.com', '98777777', 'baglung', 'bgl_gokul', 'testy'),
(4, 'gokul', 'pathak', 'goku@gmail.com', '984680000', 'pokhara', 'pathak1', 'test123'),
(5, 'Suraj', 'Adhikari', 'sssadhikari222@gmail.com', '9867808207', 'Rupalake', 'suraj', 'suraj123'),
(6, 'suraj', 'adhikari', 'surajadhikari0018@gmail.com', '9806797075', 'Rupalake', 'sura', 'suraj123'),
(7, 'suraj', 'adhikari', 'surajadhikari2102it.infomaxcollege@edu.np', '9804188295', 'Rupalake', 'sur', 'suraj123'),
(8, 'suraj', 'adhikari', 'sss@gmail.com', '9800000000', 'rupalake', 'su', 'suraj123');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `carts`
--
ALTER TABLE `carts`
  ADD PRIMARY KEY (`cart_id`),
  ADD KEY `product_id` (`product_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `carts`
--
ALTER TABLE `carts`
  MODIFY `cart_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `carts`
--
ALTER TABLE `carts`
  ADD CONSTRAINT `carts_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  ADD CONSTRAINT `carts_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
