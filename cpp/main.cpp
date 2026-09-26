#include <iostream>
#include <vector>
#include <string>
#include <sstream>
#include "Film3D.cpp"
using namespace std;

// DEKLARASI FUNGSI 
Film3D inputFilm3D(vector<Film3D>& daftarTayangan);
bool parseBarisFilm3D(const string& baris, vector<Film3D>& daftarTayangan, Film3D& hasil, string& pesanError);
bool idSudahAda(vector<Film3D>& daftarTayangan, const string& id);
void tampilkanTabel(vector<Film3D>& daftar);
vector<string> buatBaris(Film3D& f, int nomor);
void cetakBaris(const vector<string>& data, const vector<size_t>& lebarKolom);
void cetakGaris(const vector<size_t>& lebarKolom);
string padRight(const string& teks, size_t panjang);
string gabungToken(const vector<string>& token, int awal, int akhirEksklusif);
string formatAngka(double d);
bool semuaAngka(const string& s);
bool angkaDesimalValid(const string& s);
bool formatIdValid(const string& id);
bool formatRatingValid(const string& s);
bool formatTahunValid(const string& s);

// Header tabel: gabungan seluruh atribut Tayangan + Film + Film3D
const vector<string> HEADER = {
    "No", "ID", "Judul", "Durasi", "Jam Tayang", "Harga Tiket",
    "Genre", "Sutradara", "Rating Usia", "Bahasa", "Tahun Rilis",
    "Format Layar", "Biaya Kacamata", "Jml Kacamata"
};

int main() {
    // Karena semua objek yang ada (awal maupun hasil tambah) pasti Film3D,
    // cukup pakai vector<Film3D> biasa (tanpa pointer/polimorfisme)
    vector<Film3D> daftarTayangan;

    // INISIALISASI 5 OBJEK AWAL (WAJIB ADA SEBELUM INPUT USER)
    daftarTayangan.push_back(Film3D("FD001", "Avatar", 162, "18:30", 75000,
                "Sci-Fi", "James Cameron", "13+", "Inggris", 2009,
                "RealD 3D", 15000, 200));

    daftarTayangan.push_back(Film3D("FD002", "Avengers: Endgame", 181, "13:00", 65000,
                "Action/Superhero", "Anthony & Joe Russo", "13+", "Inggris", 2019,
                "IMAX 3D", 10000, 150));

    daftarTayangan.push_back(Film3D("FD003", "Doctor Strange", 115, "16:00", 60000,
                "Action/Fantasi", "Scott Derrickson", "13+", "Inggris", 2016,
                "Dolby 3D", 20000, 100));

    daftarTayangan.push_back(Film3D("FD004", "Spider-Man: Across the Spider-Verse", 170, "20:00", 70000,
                "Animasi/Superhero", "Joaquim Dos Santos", "13+", "Inggris", 2023,
                "IMAX 3D", 15000, 180));

    daftarTayangan.push_back(Film3D("FD005", "Oppenheimer", 140, "21:30", 80000,
                "Biografi", "Christopher Nolan", "17+", "Inggris", 2023,
                "IMAX 3D", 12000, 220));


    // tampilan menu
    bool berjalan = true;
    while (berjalan) {
        cout << "========================================\n";
        cout << "|| ----- BIOSKOP SINEFIL ABIEZ ------ ||\n";
        cout << "========================================\n";
        cout << "|| 1. Tambah Film                     ||\n";
        cout << "|| 2. Tampilkan Semua Film            ||\n";
        cout << "|| 3. Keluar                          ||\n";
        cout << "========================================\n";
        cout << ">> Pilih menu: ";

        string pilihan;
        getline(cin, pilihan);

        if (pilihan == "1") {
            daftarTayangan.push_back(inputFilm3D(daftarTayangan));
            cout << "Data Film3D berhasil ditambahkan.\n\n";
        } else if (pilihan == "2") {
            tampilkanTabel(daftarTayangan);
        } else if (pilihan == "3") {
            berjalan = false;
            cout << "Program selesai. Sampai jumpa!\n";
        } else {
            cout << "Pilihan tidak valid, coba lagi.\n\n";
        }
    }

    return 0;
}


// Mengambil input data Film3D SEKALIGUS dalam satu baris, dipisah spasi:
// ID JUDUL DURASI JAM_TAYANG HARGA_TIKET GENRE SUTRADARA RATING_USIA
// BAHASA TAHUN_RILIS FORMAT_LAYAR BIAYA_KACAMATA JUMLAH_KACAMATA

Film3D inputFilm3D(vector<Film3D>& daftarTayangan) {
    cout << "--- Input Data Film3D ---\n";
    cout << "Format : ID JUDUL DURASI JAM_TAYANG HARGA_TIKET GENRE SUTRADARA RATING_USIA BAHASA TAHUN_RILIS FORMAT_LAYAR BIAYA_KACAMATA JUMLAH_KACAMATA\n";

    // ERROR HANDLING: selama baris yang diketik user tidak valid, program
    // menampilkan pesan error dan meminta input ULANG (pakai nilai
    while (true) {
        cout << "Input : ";
        string baris;
        getline(cin, baris);

        Film3D hasil;
        string pesanError;
        if (parseBarisFilm3D(baris, daftarTayangan, hasil, pesanError)) {
            return hasil;
        }
        cout << "Gagal! " << pesanError << "\n\n";
    }
}


// Mem-parsing satu baris input menjadi objek Film3D. JUDUL, SUTRADARA,
// dan FORMAT_LAYAR bisa lebih dari satu kata, jadi batasnya dideteksi
// lewat field "jangkar" (angka, pola rating, dsb) di sekitarnya.
// Mengembalikan true kalau berhasil (hasil diisi), false kalau gagal
// (pesanError diisi alasannya).

bool parseBarisFilm3D(const string& baris, vector<Film3D>& daftarTayangan, Film3D& hasil, string& pesanError) {
    istringstream iss(baris);
    vector<string> token;
    string kata;
    while (iss >> kata) {
        token.push_back(kata);
    }

    if (token.size() < 11) {
        pesanError = "Jumlah data terlalu sedikit, cek kembali urutannya.";
        return false;
    }

    // --- ID ---
    string id = token[0];
    if (!formatIdValid(id)) {
        pesanError = "ID harus berformat FD diikuti 3 digit angka (contoh: FD001).";
        return false;
    }
    if (idSudahAda(daftarTayangan, id)) {
        pesanError = "ID \"" + id + "\" sudah digunakan.";
        return false;
    }

    // --- Cari DURASI: token angka murni pertama setelah ID ---
    int idxDurasi = -1;
    for (size_t i = 1; i < token.size(); i++) {
        if (semuaAngka(token[i])) {
            idxDurasi = static_cast<int>(i);
            break;
        }
    }
    if (idxDurasi == -1 || idxDurasi == 1) {
        pesanError = "Durasi (angka) tidak ditemukan setelah judul.";
        return false;
    }

    string judul = gabungToken(token, 1, idxDurasi);
    int durasi = stoi(token[idxDurasi]);

    // --- JAM_TAYANG & HARGA_TIKET ---
    if (static_cast<size_t>(idxDurasi + 2) >= token.size()) {
        pesanError = "Jam tayang / harga tiket tidak ditemukan.";
        return false;
    }
    string jamTayang = token[idxDurasi + 1];
    if (!angkaDesimalValid(token[idxDurasi + 2])) {
        pesanError = "Harga tiket harus berupa angka.";
        return false;
    }
    double hargaTiket = stod(token[idxDurasi + 2]);

    // --- GENRE ---
    int idxGenre = idxDurasi + 3;
    if (static_cast<size_t>(idxGenre) >= token.size()) {
        pesanError = "Genre tidak ditemukan.";
        return false;
    }
    string genre = token[idxGenre];

    // --- Cari RATING_USIA: pola "13+", "17+", "SU", dst ---
    int idxRating = -1;
    for (size_t i = idxGenre + 1; i < token.size(); i++) {
        if (formatRatingValid(token[i])) {
            idxRating = static_cast<int>(i);
            break;
        }
    }
    if (idxRating == -1 || idxRating == idxGenre + 1) {
        pesanError = "Rating usia (contoh: 13+, 17+, SU) tidak ditemukan setelah sutradara.";
        return false;
    }

    string sutradara = gabungToken(token, idxGenre + 1, idxRating);
    string ratingUsia = token[idxRating];

    // --- BAHASA ---
    int idxBahasa = idxRating + 1;
    if (static_cast<size_t>(idxBahasa) >= token.size()) {
        pesanError = "Bahasa tidak ditemukan.";
        return false;
    }
    string bahasa = token[idxBahasa];

    // --- TAHUN_RILIS: angka 4 digit ---
    int idxTahun = idxBahasa + 1;
    if (static_cast<size_t>(idxTahun) >= token.size() || !formatTahunValid(token[idxTahun])) {
        pesanError = "Tahun rilis (4 digit, contoh 2024) tidak ditemukan.";
        return false;
    }
    int tahunRilis = stoi(token[idxTahun]);

    // --- FORMAT_LAYAR: sisa token sebelum 2 token terakhir ---
    int idxAkhirFormat = static_cast<int>(token.size()) - 2;
    if (idxTahun + 1 >= idxAkhirFormat) {
        pesanError = "Format layar tidak ditemukan.";
        return false;
    }
    string formatLayar = gabungToken(token, idxTahun + 1, idxAkhirFormat);

    // --- BIAYA_KACAMATA & JUMLAH_KACAMATA: 2 token terakhir ---
    if (!angkaDesimalValid(token[token.size() - 2]) || !semuaAngka(token[token.size() - 1])) {
        pesanError = "Biaya kacamata / jumlah kacamata harus berupa angka.";
        return false;
    }
    double biayaKacamata = stod(token[token.size() - 2]);
    int jumlahKacamataTersedia = stoi(token[token.size() - 1]);

    hasil = Film3D(id, judul, durasi, jamTayang, hargaTiket,
            genre, sutradara, ratingUsia, bahasa, tahunRilis,
            formatLayar, biayaKacamata, jumlahKacamataTersedia);
    return true;
}


// Mengecek apakah semua karakter dalam string adalah digit (0-9)
bool semuaAngka(const string& s) {
    if (s.empty()) return false;
    for (char c : s) {
        if (c < '0' || c > '9') return false;
    }
    return true;
}

// Mengecek apakah string valid sebagai angka desimal (digit + maksimal satu titik)
bool angkaDesimalValid(const string& s) {
    if (s.empty()) return false;
    bool sudahAdaTitik = false;
    bool adaDigit = false;
    for (char c : s) {
        if (c == '.') {
            if (sudahAdaTitik) return false;
            sudahAdaTitik = true;
        } else if (c >= '0' && c <= '9') {
            adaDigit = true;
        } else {
            return false;
        }
    }
    return adaDigit;
}

// Mengecek format ID: harus "FD" diikuti tepat 3 digit angka
bool formatIdValid(const string& id) {
    if (id.size() != 5) return false;
    if (id[0] != 'F' || id[1] != 'D') return false;
    return semuaAngka(id.substr(2));
}

// Mengecek format rating usia: "SU" atau angka diikuti "+" (contoh 13+, 17+)
bool formatRatingValid(const string& s) {
    if (s == "SU") return true;
    if (s.size() < 2 || s.back() != '+') return false;
    return semuaAngka(s.substr(0, s.size() - 1));
}

// Mengecek format tahun rilis: 4 digit angka, diawali "19" atau "20"
bool formatTahunValid(const string& s) {
    if (s.size() != 4 || !semuaAngka(s)) return false;
    string dua = s.substr(0, 2);
    return dua == "19" || dua == "20";
}

// Mengecek apakah suatu ID sudah dipakai objek lain di daftar
bool idSudahAda(vector<Film3D>& daftarTayangan, const string& id) {
    for (auto& t : daftarTayangan) {
        if (t.getId() == id) {
            return true;
        }
    }
    return false;
}

// Menampilkan seluruh data dalam SATU tabel dinamis (lebar kolom
// menyesuaikan panjang data terpanjang di kolom tersebut)
void tampilkanTabel(vector<Film3D>& daftar) {
    if (daftar.empty()) {
        cout << "Belum ada data untuk ditampilkan.\n\n";
        return;
    }

    vector<vector<string>> baris;
    for (size_t i = 0; i < daftar.size(); i++) {
        baris.push_back(buatBaris(daftar[i], static_cast<int>(i) + 1));
    }

    // Menghitung lebar kolom secara dinamis (pengganti std::max, bandingkan manual)
    vector<size_t> lebarKolom(HEADER.size());
    for (size_t j = 0; j < HEADER.size(); j++) {
        lebarKolom[j] = HEADER[j].size();
        for (const auto& b : baris) {
            if (b[j].size() > lebarKolom[j]) {
                lebarKolom[j] = b[j].size();
            }
        }
    }

    cetakGaris(lebarKolom);
    cetakBaris(HEADER, lebarKolom);
    cetakGaris(lebarKolom);
    for (const auto& b : baris) {
        cetakBaris(b, lebarKolom);
    }
    cetakGaris(lebarKolom);
    cout << "\n";
}

// Mengubah satu objek Film3D menjadi vector<string> sesuai kolom HEADER
vector<string> buatBaris(Film3D& f, int nomor) {
    return {
        to_string(nomor), f.getId(), f.getJudul(),
        to_string(f.getDurasi()), f.getJamTayang(), formatAngka(f.getHargaTiket()),
        f.getGenre(), f.getSutradara(), f.getRatingUsia(), f.getBahasa(), to_string(f.getTahunRilis()),
        f.getFormatLayar(), formatAngka(f.getBiayaKacamata()), to_string(f.getJumlahKacamataTersedia())
    };
}

// Mencetak satu baris tabel; tiap kolom diratakan kiri sesuai lebar dinamisnya
void cetakBaris(const vector<string>& data, const vector<size_t>& lebarKolom) {
    string baris = "|";
    for (size_t j = 0; j < data.size(); j++) {
        baris += " " + padRight(data[j], lebarKolom[j]) + " |";
    }
    cout << baris << "\n";
}

// Mencetak garis pembatas (+----+----+...) sesuai lebar tiap kolom
void cetakGaris(const vector<size_t>& lebarKolom) {
    string garis = "+";
    for (size_t lebar : lebarKolom) {
        garis += string(lebar + 2, '-') + "+";
    }
    cout << garis << "\n";
}

// Menambahkan spasi di kanan teks hingga mencapai panjang tertentu (rata kiri)
string padRight(const string& teks, size_t panjang) {
    string hasil = teks;
    if (hasil.size() < panjang) {
        hasil += string(panjang - hasil.size(), ' ');
    }
    return hasil;
}

// Menggabungkan token dari indeks [awal, akhirEksklusif) jadi satu string
string gabungToken(const vector<string>& token, int awal, int akhirEksklusif) {
    string hasil;
    for (int i = awal; i < akhirEksklusif; i++) {
        if (i > awal) hasil += " ";
        hasil += token[i];
    }
    return hasil;
}

// Mengubah double menjadi String untuk hargaTiket & biayaKacamata
string formatAngka(double d) {
    ostringstream oss;
    oss << d;
    return oss.str();
}