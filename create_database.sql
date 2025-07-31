-- Script untuk membuat database baru di phpMyAdmin
CREATE DATABASE IF NOT EXISTS philosofit_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE philosofit_db;

-- Membuat tabel tasks
CREATE TABLE IF NOT EXISTS tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    deadline DATE NOT NULL,
    motivation TEXT,
    priority ENUM('DO_FIRST', 'SCHEDULE', 'DELEGATE', 'ELIMINATE') NOT NULL,
    completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_title (title),
    INDEX idx_deadline (deadline),
    INDEX idx_priority (priority),
    INDEX idx_completed (completed)
);

-- Data contoh (opsional)
INSERT INTO tasks (title, deadline, motivation, priority, completed) VALUES
('Menyelesaikan Laporan Bulanan', '2025-01-15', 'Penting untuk evaluasi kinerja tim dan perencanaan bulan depan', 'DO_FIRST', FALSE),
('Belajar Framework Baru', '2025-02-01', 'Meningkatkan skill programming untuk karir yang lebih baik', 'SCHEDULE', FALSE),
('Meeting dengan Client', '2025-01-12', 'Membahas requirements project baru yang akan dimulai', 'DO_FIRST', FALSE),
('Organisir File Komputer', '2025-01-30', 'Membersihkan dan mengatur file agar lebih terorganisir', 'ELIMINATE', FALSE);
