# Philosofit - Task Management Application

Aplikasi manajemen tugas menggunakan Eisenhower Matrix dengan database MySQL dan arsitektur MVC.

## Struktur Project

\`\`\`
Philosofit/
├── Main.java                    # Entry point aplikasi
├── model/                       # Model layer (Data & Business Objects)
│   ├── Priority.java           # Enum untuk prioritas tugas
│   ├── Task.java               # Model untuk tugas
│   ├── WhyReason.java          # Model untuk alasan motivasi
│   └── Database.java           # Database connection & operations
├── controller/                  # Controller layer (Business Logic)
│   └── TaskController.java     # Controller untuk operasi tugas
└── view/                       # View layer (User Interface)
    ├── MainFrame.java          # Main window
    ├── TodoMatrixPanel.java    # Panel untuk Eisenhower Matrix
    ├── AddTaskDialog.java      # Dialog untuk menambah tugas
    └── WhyReasonDialog.java    # Dialog untuk menggali motivasi
\`\`\`

## Persiapan

### 1. Database MySQL
- Pastikan MySQL server sudah berjalan
- Buat database `philosofit_db` di phpMyAdmin
- Jalankan script SQL berikut:

\`\`\`sql
CREATE DATABASE IF NOT EXISTS philosofit_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE philosofit_db;

CREATE TABLE IF NOT EXISTS tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    deadline DATE NOT NULL,
    motivation TEXT,
    priority ENUM('DO_FIRST', 'SCHEDULE', 'DELEGATE', 'ELIMINATE') NOT NULL,
    completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
\`\`\`

### 2. Konfigurasi Database
Edit file `model/Database.java` sesuai pengaturan MySQL Anda:
\`\`\`java
private static final String DB_USERNAME = "root"; // Username MySQL
private static final String DB_PASSWORD = "";     // Password MySQL
\`\`\`

### 3. Dependencies
Tambahkan MySQL Connector/J ke project dependencies:
- Download dari: https://dev.mysql.com/downloads/connector/j/
- Atau jika menggunakan Maven, tambahkan ke pom.xml:
\`\`\`xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
\`\`\`

## Cara Menjalankan di IntelliJ IDEA

1. **Import Project:**
   - File → New → Project from Existing Sources
   - Pilih folder project
   - Import as Java project

2. **Setup Dependencies:**
   - File → Project Structure → Libraries
   - Tambahkan mysql-connector-java.jar

3. **Run Application:**
   - Klik kanan pada `Main.java`
   - Pilih "Run 'Main.main()'"

## Fitur Aplikasi

- ✅ **Tambah Tugas:** Dengan sistem "5 Why" untuk menggali motivasi mendalam
- ✅ **Eisenhower Matrix:** Kategorisasi otomatis berdasarkan urgency & importance
- ✅ **Pencarian:** Cari tugas berdasarkan judul atau motivasi
- ✅ **Theme Toggle:** Dark/Light mode
- ✅ **Context Menu:** Hapus atau tandai selesai tugas
- ✅ **Database MySQL:** Persistent storage dengan phpMyAdmin

## Arsitektur MVC

### Model Layer
- `Priority.java`: Enum untuk kategori prioritas
- `Task.java`: Representasi data tugas
- `WhyReason.java`: Model untuk motivasi berlapis
- `Database.java`: Singleton untuk koneksi dan operasi database

### Controller Layer
- `TaskController.java`: Business logic untuk operasi tugas dan kalkulasi prioritas

### View Layer
- `MainFrame.java`: Window utama aplikasi
- `TodoMatrixPanel.java`: Panel grid Eisenhower Matrix
- `AddTaskDialog.java`: Dialog untuk input tugas baru
- `WhyReasonDialog.java`: Dialog untuk menggali motivasi

## Troubleshooting

### Error "Driver not found"
- Pastikan mysql-connector-java.jar sudah ditambahkan ke project dependencies

### Error "Access denied"
- Periksa username dan password di `model/Database.java`

### Error "Unknown database"
- Pastikan database `philosofit_db` sudah dibuat di MySQL

### Error saat compile
- Pastikan semua package imports sudah benar
- Check Java version compatibility (minimal Java 8)
