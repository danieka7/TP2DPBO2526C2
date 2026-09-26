# Tugas Praktikum 2 - Desain Pemrograman Berorientasi Objek

## Janji
Saya **Dani Eka Saputra** dengan NIM **2501158** mengerjakan Tugas Praktikum 2 pada Mata Kuliah Desain Pemrograman Berorientasi Objek untuk keberkahan-Nya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin

## 🗂️ Struktur Projek
```
.
├── cpp
│   ├── Film.cpp
│   ├── Film3D.cpp
│   ├── main.cpp
│   ├── Tayangan.cpp
│   └── testcase.txt
├── dokumentasi
│   ├── cpp
│   ├── java
│   ├── php
│   └── python
├── java
│   ├── Film.java
│   ├── Film3D.java
│   ├── Main.java
│   ├── Tayangan.java
│   └── testcase.txt
├── php
│   ├── uploads
│   ├── Film.php
│   ├── Film3D.php
│   ├── main.php
│   └── Tayangan.php
├── python
│   ├── Film.py
│   ├── Film3D.py
│   ├── main.py
│   ├── Tayangan.py
│   └── testcase.txt
└── README.md
```

## Fitur Utama
### C++ / Java / Python (CLI / Terminal)
- **5 data awal otomatis:** program selalu memuat 5 objek `Film3D` contoh sebelum menerima input apa pun dari pengguna.
- **Tambah data dalam satu baris:** pengguna cukup mengetik seluruh atribut sekaligus, dipisah spasi.
- **Validasi & error handling:** ID wajib berformat `FDxxx` (FD + 3 digit angka) dan tidak boleh duplikat; jika data tidak lengkap atau salah format, program menampilkan pesan error spesifik dan meminta input ulang tanpa menghentikan program.
- **Tampilkan Data:** Menampilkan semua objek yang tersimpan.

### PHP (Web)
- **5 data awal otomatis:** program selalu memuat 5 objek `Film3D` contoh sebelum menerima input apa pun dari pengguna.
- **Tambah Data:** Menambah objek baru melalui form HTML, sekaligus menambahkan foto.
- **Validasi & pesan error/sukses di halaman:** ID tetap divalidasi format `FDxxx` dan keunikannya; pesan ditampilkan sebagai notifikasi berwarna di atas form tanpa reload penuh dari nol.
- **Tampilkan Data:** Menampilkan semua data film yang tersimpan di bagian bawah.
- **Data persisten selama sesi:** perubahan (penambahan data) langsung tersimpan di session dan tetap ada selama browser/tab tidak ditutup, tanpa database eksternal.

## Desain dan Alur Kerja

### Desain Diagram
  ![diagram](<dokumentasi/TP2_diagram.png>)


### Struktur Data Kelas 
- #### Tayangan (Base Class)
  | Atribut                   | Keterangan               |
  | ------------------------- | ------------------------ |
  | **ID** *(string)*         | Identifier unik tayangan |
  | **Judul** *(string)*      | Judul/nama dari tayangan |
  | **durasi** *(int)*        | Durasi tayangan          |
  | **jamTayang** *(int)*     | Jadwal jam tayang        |
  | **hargaTiket** *(double)* | Harga tiket tayangan     |
    
- #### Film (Intermediary Class)
  | Atribut                   | Keterangan                       |
  | ------------------------- | -------------------------------- |
  | **genre** *(string)*      | Genre film                       |
  | **sutradara** *(string)*  | Sutradara film                   |
  | **ratingUsia** *(string)* | Rating Usia                      |
  | **bahasa** *(string)*     | Bahasa yang digunakan dalam film |
  | **tahunRilis** *(int)*    | Tahun rilis film                 |

- #### Film3D (Derived Class)
  | Atribut                    | Keterangan                              |
  | -------------------------- | --------------------------------------- |
  | **formatLayar** *(string)* | Format layar yang digunakan oleh film   |
  | **biayaKacamata** *(int)*  | Biaya sewa untuk kacamata               |
  | **jamTayang** *(int)*      | Jadwal jam tayang                       |
  | **hargaTiket** *(double)*  | Jumlah stok kacamata tersedia tiap film |

### Alur Kerja
- #### C++ / Java / Python (CLI / Terminal)
  Program dimulai dengan memuat 5 objek Film3D awal ke dalam list di memori, lalu menampilkan menu berisi tiga pilihan: Tambah Data, Tampilkan Tabel, dan Keluar. Saat memilih Tambah Data, pengguna mengetik seluruh atribut sekaligus dalam satu baris dipisah spasi; baris ini diparsing dengan mendeteksi pola tiap field, divalidasi (format ID dan keunikannya), lalu ditambahkan ke list jika valid atau diminta diketik ulang jika tidak. Saat memilih Tampilkan Tabel, seluruh isi list dicetak sebagai tabel teks dengan lebar kolom otomatis. Proses ini berulang sampai pengguna memilih Keluar, dan karena data hanya tersimpan di memori, semuanya hilang begitu program berhenti.
- #### PHP (Web)
  Setiap kali main.php diakses, program mengecek apakah session sudah berisi data; jika belum (kunjungan pertama), 5 objek Film3D awal dibuat dan disimpan ke $_SESSION. Halaman kemudian merender form tambah data beserta tabel HTML dari data yang ada. Saat form disubmit (request POST ke halaman yang sama), data dibaca dan divalidasi di server; jika valid, objek Film3D baru ditambahkan ke session dan pesan sukses ditampilkan, jika tidak, pesan error ditampilkan tanpa mengubah data. Halaman lalu dirender ulang dengan data terbaru. Karena disimpan di session (bukan di memori proses seperti CLI), data tetap ada meski halaman di-refresh berkali-kali, selama browser/tab belum ditutup atau session belum kedaluwarsa.

## Dokumentasi
- ### Dokumentasi Program C++
  - #### Tambah Data Baru
    ![alt](<dokumentasi/cpp/cpp_add.png>)
  - #### Tampilkan Semua Data
    ![alt](<dokumentasi/cpp/cpp_view.png>)
- ### Dokumentasi Program Java
  - #### Tambah Data Baru
    ![alt](<dokumentasi/java/java_add.png>)
  - #### Tampilkan Semua Data
    ![alt](<dokumentasi/java/java_view.png>)
- ### Dokumentasi Program Python
  - #### Tambah Data Baru
    ![alt](<dokumentasi/python/python_add.png>)
  - #### Tampilkan Semua Data
    ![alt](<dokumentasi/python/python_view.png>)
- ### Dokumentasi Program PHP
  - #### Tambah Data Baru
    ![alt](<dokumentasi/php/php_add.png>)
  - #### Tampilkan Semua Data
    ![alt](<dokumentasi/php/php_view.png>)