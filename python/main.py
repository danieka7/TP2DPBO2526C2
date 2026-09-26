import re
from Tayangan import Tayangan
from Film import Film
from Film3D import Film3D


# Header gabungan dari SELURUH atribut Tayangan, Film, dan Film3D.
# Kolom yang tidak dimiliki suatu objek (misal Tayangan biasa tidak
# punya "Genre") akan diisi tanda "-"
HEADER = [
    "No", "ID", "Judul", "Durasi", "Jam Tayang", "Harga Tiket",
    "Genre", "Sutradara", "Rating Usia", "Bahasa", "Tahun Rilis",
    "Format Layar", "Biaya Kacamata", "Jml Kacamata"
]


def id_sudah_ada(daftar_tayangan, id_): 
    """
    Fungsi bantu untuk mengecek apakah suatu ID sudah dipakai
    oleh objek lain di dalam daftar (baik itu Tayangan, Film, atau Film3D).
    """
    for t in daftar_tayangan:
        if t.get_id().lower() == id_.lower():
            return True
    return False


def parse_baris_film3d(baris, daftar_tayangan):
    """
    Mem-parsing satu baris input (string) menjadi objek Film3D.
    Karena JUDUL, SUTRADARA, dan FORMAT_LAYAR bisa lebih dari satu kata,
    posisi field-field "jangkar" (angka & pola tetap) dicari dulu untuk
    menentukan batas tiap field yang panjangnya bisa berubah-ubah.
    Melempar ValueError dengan pesan yang jelas jika formatnya tidak sesuai.
    """
    token = baris.strip().split()
    if len(token) < 11:
        raise ValueError("Jumlah data terlalu sedikit, cek kembali urutannya.")

    # --- ID (token pertama) ---
    id_ = token[0]
    if not re.fullmatch(r"FD\d{3}", id_):
        raise ValueError("ID harus berformat FD diikuti 3 digit angka (contoh: FD001).")
    if id_sudah_ada(daftar_tayangan, id_):
        raise ValueError(f'ID "{id_}" sudah digunakan.')

    # --- Cari token DURASI: token angka murni pertama setelah ID ---
    idx_durasi = -1
    for i in range(1, len(token)):
        if re.fullmatch(r"\d+", token[i]):
            idx_durasi = i
            break
    if idx_durasi == -1 or idx_durasi == 1:
        raise ValueError("Durasi (angka) tidak ditemukan setelah judul.")

    # --- JUDUL: semua token antara ID dan DURASI ---
    judul = " ".join(token[1:idx_durasi])
    durasi = int(token[idx_durasi])

    # --- JAM_TAYANG & HARGA_TIKET: dua token tepat setelah DURASI ---
    if idx_durasi + 2 >= len(token):
        raise ValueError("Jam tayang / harga tiket tidak ditemukan.")
    jam_tayang = token[idx_durasi + 1]
    try:
        harga_tiket = float(token[idx_durasi + 2])
    except ValueError:
        raise ValueError("Harga tiket harus berupa angka.")

    # --- GENRE: satu token tepat setelah HARGA_TIKET ---
    idx_genre = idx_durasi + 3
    if idx_genre >= len(token):
        raise ValueError("Genre tidak ditemukan.")
    genre = token[idx_genre]

    # --- Cari token RATING_USIA: pola "13+", "17+", "SU", dst ---
    idx_rating = -1
    for i in range(idx_genre + 1, len(token)):
        if re.fullmatch(r"SU|\d+\+", token[i]):
            idx_rating = i
            break
    if idx_rating == -1 or idx_rating == idx_genre + 1:
        raise ValueError("Rating usia (contoh: 13+, 17+, SU) tidak ditemukan setelah sutradara.")

    # --- SUTRADARA: semua token antara GENRE dan RATING_USIA ---
    sutradara = " ".join(token[idx_genre + 1:idx_rating])
    rating_usia = token[idx_rating]

    # --- BAHASA: satu token tepat setelah RATING_USIA ---
    idx_bahasa = idx_rating + 1
    if idx_bahasa >= len(token):
        raise ValueError("Bahasa tidak ditemukan.")
    bahasa = token[idx_bahasa]

    # --- TAHUN_RILIS: satu token angka 4 digit setelah BAHASA ---
    idx_tahun = idx_bahasa + 1
    if idx_tahun >= len(token) or not re.fullmatch(r"(19|20)\d{2}", token[idx_tahun]):
        raise ValueError("Tahun rilis (4 digit, contoh 2024) tidak ditemukan.")
    tahun_rilis = int(token[idx_tahun])

    # --- FORMAT_LAYAR: semua token antara TAHUN_RILIS dan 2 token terakhir ---
    idx_akhir_format = len(token) - 2  # eksklusif, 2 token terakhir milik BIAYA & JUMLAH
    if idx_tahun + 1 >= idx_akhir_format:
        raise ValueError("Format layar tidak ditemukan.")
    format_layar = " ".join(token[idx_tahun + 1:idx_akhir_format])

    # --- BIAYA_KACAMATA & JUMLAH_KACAMATA: 2 token terakhir ---
    try:
        biaya_kacamata = float(token[-2])
        jumlah_kacamata_tersedia = int(token[-1])
    except ValueError:
        raise ValueError("Biaya kacamata / jumlah kacamata harus berupa angka.")

    return Film3D(id_, judul, durasi, jam_tayang, harga_tiket,
                  genre, sutradara, rating_usia, bahasa, tahun_rilis,
                  format_layar, biaya_kacamata, jumlah_kacamata_tersedia)


def input_film3d(daftar_tayangan):
    """
    Meminta input data Film3D dari user lewat terminal.
    Selama baris yang diketik user tidak valid (format salah,
    field kurang, ID salah format/duplikat, dsb), user akan terus
    diminta memasukkan ulang datanya (error handling).
    """
    print("--- Input Data Film3D ---")
    print("Format : ID JUDUL DURASI JAM_TAYANG HARGA_TIKET GENRE SUTRADARA "
          "RATING_USIA BAHASA TAHUN_RILIS FORMAT_LAYAR BIAYA_KACAMATA JUMLAH_KACAMATA")
    
    while True:
        baris = input("Input : ")
        try:
            return parse_baris_film3d(baris, daftar_tayangan)
        except ValueError as e:
            print(f"Gagal! {e}\n")
        except Exception:
            # Menangkap error tak terduga lain (misal token kurang / index habis)
            print("Gagal! Format input tidak lengkap atau tidak sesuai urutan.\n")


def buat_baris(t, nomor):
    """
    Mengubah satu objek Tayangan/Film/Film3D menjadi list of string
    sesuai urutan kolom pada HEADER.
    Menggunakan isinstance() untuk mendeteksi tipe ASLI objek saat runtime.
    """
    genre = sutradara = rating_usia = bahasa = tahun_rilis = "-"
    format_layar = biaya_kacamata = jumlah_kacamata = "-"

    if isinstance(t, Film3D):
        genre = t.get_genre()
        sutradara = t.get_sutradara()
        rating_usia = t.get_ratingUsia()
        bahasa = t.get_bahasa()
        tahun_rilis = str(t.get_tahunRilis())
        format_layar = t.get_formatLayar()
        biaya_kacamata = str(t.get_biayaKacamata())
        jumlah_kacamata = str(t.get_jumlahKacamataTersedia())
    elif isinstance(t, Film):
        genre = t.get_genre()
        sutradara = t.get_sutradara()
        rating_usia = t.get_ratingUsia()
        bahasa = t.get_bahasa()
        tahun_rilis = str(t.get_tahunRilis())
    # Jika objeknya murni Tayangan, semua variabel di atas tetap "-"

    return [
        str(nomor), t.get_id(), t.get_judul(),
        str(t.get_durasi()), t.get_jamTayang(), str(t.get_hargaTiket()),
        genre, sutradara, rating_usia, bahasa, tahun_rilis,
        format_layar, biaya_kacamata, jumlah_kacamata
    ]


def pad_right(teks, panjang):
    """Menambahkan spasi di kanan teks hingga mencapai panjang tertentu (rata kiri)."""
    return teks.ljust(panjang)


def cetak_garis(lebar_kolom):
    """Mencetak garis pembatas (+----+----+...) sesuai lebar tiap kolom."""
    sb = "+"
    for lebar in lebar_kolom:
        sb += "-" * (lebar + 2) + "+"
    print(sb)


def cetak_baris(data, lebar_kolom):
    """Mencetak satu baris tabel; tiap kolom diratakan kiri sesuai lebar dinamisnya."""
    sb = "|"
    for j in range(len(data)):
        sb += " " + pad_right(data[j], lebar_kolom[j]) + " |"
    print(sb)


def tampilkan_tabel(daftar):
    """
    Menampilkan seluruh data dalam SATU tabel dinamis.
    Lebar tiap kolom dihitung otomatis dari isi terpanjang di kolom tsb.
    """
    if not daftar:
        print("Belum ada data untuk ditampilkan.\n")
        return

    # Mengubah setiap objek (apapun tipenya) menjadi baris teks
    baris = [buat_baris(t, i + 1) for i, t in enumerate(daftar)]

    # Menghitung lebar kolom secara dinamis:
    # lebar = panjang teks terpanjang antara header dan semua isi baris
    lebar_kolom = [len(h) for h in HEADER]
    for b in baris:
        for j in range(len(HEADER)):
            lebar_kolom[j] = max(lebar_kolom[j], len(b[j]))

    # Mencetak tabel: garis atas, header, garis pemisah, isi data, garis bawah
    cetak_garis(lebar_kolom)
    cetak_baris(HEADER, lebar_kolom)
    cetak_garis(lebar_kolom)
    for b in baris:
        cetak_baris(b, lebar_kolom)
    cetak_garis(lebar_kolom)
    print()


def main():
    # List bertipe Tayangan (superclass) digunakan agar bisa menampung
    # objek Tayangan, Film, maupun Film3D sekaligus (konsep polimorfisme)
    daftar_tayangan = []

    # INISIALISASI 5 OBJEK AWAL
    daftar_tayangan.append(Film3D("FD001", "Avatar", 162, "18:30", 75000,
                                   "Sci-Fi", "James Cameron", "13+", "Inggris", 2009,
                                   "RealD 3D", 15000, 200))

    daftar_tayangan.append(Film3D("FD002", "Avengers: Endgame", 181, "13:00", 65000,
                                   "Action/Superhero", "Anthony & Joe Russo", "13+", "Inggris", 2019,
                                   "IMAX 3D", 10000, 150))

    daftar_tayangan.append(Film3D("FD003", "Doctor Strange", 115, "16:00", 60000,
                                   "Action/Fantasi", "Scott Derrickson", "13+", "Inggris", 2016,
                                   "Dolby 3D", 20000, 100))

    daftar_tayangan.append(Film3D("FD004", "Spider-Man: Across the Spider-Verse", 170, "20:00", 70000,
                                   "Animasi/Superhero", "Joaquim Dos Santos", "13+", "Inggris", 2023,
                                   "IMAX 3D", 15000, 180))

    daftar_tayangan.append(Film3D("FD005", "Oppenheimer", 140, "21:30", 80000,
                                   "Biografi", "Christopher Nolan", "17+", "Inggris", 2023,
                                   "IMAX 3D", 12000, 220))

    # MENU INTERAKTIF: user hanya bisa MENAMBAH data (sesuai ketentuan)
    # dan menampilkan tabel seluruh data
    berjalan = True
    while berjalan:
        print("========================================")
        print("|| ----- BIOSKOP SINEFIL ABIEZ ------ ||")
        print("========================================")
        print("|| 1. Tambah Film                     ||")
        print("|| 2. Tampilkan Semua Film            ||")
        print("|| 3. Keluar                          ||")
        print("========================================")
        pilihan = input(">> Pilih menu: ")

        if pilihan == "1":
            daftar_tayangan.append(input_film3d(daftar_tayangan))
            print("Data Film3D berhasil ditambahkan.\n")
        elif pilihan == "2":
            tampilkan_tabel(daftar_tayangan)
        elif pilihan == "3":
            berjalan = False
            print("Program selesai. Sampai jumpa!")
        else:
            print("Pilihan tidak valid, coba lagi.\n")


# Titik masuk program (equivalent dengan public static void main di Java)
if __name__ == "__main__":
    main()