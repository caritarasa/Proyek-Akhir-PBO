PANDUAN MIGRASI KE MYSQL
========================

1. PERSIAPAN DATABASE
   - Pastikan MySQL server sudah terinstall dan berjalan
   - Buka phpMyAdmin di browser (biasanya http://localhost/phpmyadmin)
   - Login dengan username dan password MySQL Anda

2. MEMBUAT DATABASE
   - Jalankan script SQL berikut secara berurutan di phpMyAdmin:
     a. 01-create-database.sql (membuat database)
     b. 02-create-tables.sql (membuat tabel)
     c. 03-sample-data.sql (data contoh, opsional)

3. KONFIGURASI KONEKSI
   - Edit file database.properties sesuai dengan pengaturan MySQL Anda:
     * db.username = username MySQL Anda (default: root)
     * db.password = password MySQL Anda
     * db.host = host MySQL (default: localhost)
     * db.port = port MySQL (default: 3306)

4. DEPENDENCY YANG DIPERLUKAN
   - Tambahkan MySQL Connector/J ke classpath project Anda
   - Download dari: https://dev.mysql.com/downloads/connector/j/
   - Atau jika menggunakan Maven, tambahkan dependency:
     <dependency>
         <groupId>mysql</groupId>
         <artifactId>mysql-connector-java</artifactId>
         <version>8.0.33</version>
     </dependency>

5. PERUBAHAN UTAMA DARI SQLITE KE MYSQL
   - Driver berubah dari org.sqlite.JDBC ke com.mysql.cj.jdbc.Driver
   - URL koneksi berubah format
   - Sintaks SQL sedikit berbeda (BOOLEAN vs INTEGER, AUTO_INCREMENT vs AUTOINCREMENT)
   - Ditambahkan konfigurasi timezone dan encoding
   - Ditambahkan fitur ON DUPLICATE KEY UPDATE untuk upsert

6. FITUR BARU YANG DITAMBAHKAN
   - DatabaseConfig class untuk manajemen konfigurasi
   - DatabaseStats untuk statistik database
   - Method getCompletedTasks() untuk melihat tugas yang sudah selesai
   - Improved error handling dan logging
   - Index pada tabel untuk performa yang lebih baik

7. TESTING
   - Jalankan aplikasi dan pastikan koneksi berhasil
   - Coba tambah, edit, hapus, dan cari tugas
   - Periksa data di phpMyAdmin untuk memastikan perubahan tersimpan

8. TROUBLESHOOTING
   - Jika error "Access denied": periksa username/password di database.properties
   - Jika error "Unknown database": pastikan database sudah dibuat
   - Jika error "Driver not found": pastikan MySQL Connector/J sudah ditambahkan ke classpath
   - Jika error timezone: sesuaikan serverTimezone di database.properties

CATATAN PENTING:
- Backup data SQLite lama sebelum migrasi
- Test aplikasi secara menyeluruh setelah migrasi
- Sesuaikan konfigurasi database sesuai environment Anda
