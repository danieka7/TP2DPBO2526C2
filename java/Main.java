import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // Header gabungan dari SELURUH atribut Tayangan, Film, dan Film3D.
    // Kolom yang tidak dimiliki suatu objek (misal Tayangan biasa tidak
    // punya "Genre") akan diisi tanda "-"
    private static final String[] HEADER = {
        "No", "ID", "Judul", "Durasi", "Jam Tayang", "Harga Tiket",
        "Genre", "Sutradara", "Rating Usia", "Bahasa", "Tahun Rilis",
        "Format Layar", "Biaya Kacamata", "Jml Kacamata"
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // List bertipe Tayangan (superclass) digunakan agar bisa menampung
        // objek Tayangan, Film, maupun Film3D sekaligus (konsep polimorfisme)
        List<Tayangan> daftarTayangan = new ArrayList<>();

        // INISIALISASI 5 OBJEK AWAL 

        daftarTayangan.add(new Film3D("FD001", "Avatar", 162, "18:30", 75000,
                "Sci-Fi", "James Cameron", "13+", "Inggris", 2009,
                "RealD 3D", 15000, 200));

        daftarTayangan.add(new Film3D("FD002", "Avengers: Endgame", 181, "13:00", 65000,
                "Action/Superhero", "Anthony & Joe Russo", "13+", "Inggris", 2019,
                "IMAX 3D", 10000, 150));

        daftarTayangan.add(new Film3D("FD003", "Doctor Strange", 115, "16:00", 60000,
                "Action/Fantasi", "Scott Derrickson", "13+", "Inggris", 2016,
                "Dolby 3D", 20000, 100));

        daftarTayangan.add(new Film3D("FD004", "Spider-Man: Across the Spider-Verse", 170, "20:00", 70000,
                "Animasi/Superhero", "Joaquim Dos Santos", "13+", "Inggris", 2023,
                "IMAX 3D", 15000, 180));

        daftarTayangan.add(new Film3D("FD005", "Oppenheimer", 140, "21:30", 80000,
                "Biografi", "Christopher Nolan", "17+", "Inggris", 2023,
                "IMAX 3D", 12000, 220));

        System.out.println("5 data awal berhasil dimuat ke sistem.\n");

        // ===================================================================
        // 2. MENU INTERAKTIF: user hanya bisa MENAMBAH data (sesuai ketentuan)
        //    dan menampilkan tabel seluruh data
        // ===================================================================
        boolean berjalan = true;
        while (berjalan) {
            System.out.println("========================================");
            System.out.println("|| ----- BIOSKOP SINEFIL ABIEZ ------ ||");
            System.out.println("========================================");
            System.out.println("|| 1. Tambah Film                     ||");
            System.out.println("|| 2. Tampilkan Semua Film            ||");
            System.out.println("|| 3. Keluar                          ||");
            System.out.println("========================================");
            System.out.print(">> Pilih menu: ");
            String pilihan = scanner.nextLine();

            switch (pilihan) {
                case "1":
                    daftarTayangan.add(inputFilm3D(scanner, daftarTayangan));
                    System.out.println("Data Film3D berhasil ditambahkan.\n");
                    break;
                case "2":
                    tampilkanTabel(daftarTayangan);
                    break;
                case "0":
                    berjalan = false;
                    System.out.println("Program selesai. Sampai jumpa!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid, coba lagi.\n");
            }
        }

        scanner.close();
    }

    // Method untuk mengambil input data Film3D dari user
    private static Film3D inputFilm3D(Scanner scanner, List<Tayangan> daftarTayangan) {
        System.out.println("--- Input Data Film3D (satu baris, dipisah spasi) ---");
        System.out.println("Format : ID JUDUL DURASI JAM_TAYANG HARGA_TIKET GENRE SUTRADARA RATING_USIA BAHASA TAHUN_RILIS FORMAT_LAYAR BIAYA_KACAMATA JUMLAH_KACAMATA");
        System.out.println("Contoh : FD007 Spider-Man: No Way Home 148 21:00 50000 Action/Superhero Jon Watts 13+ Inggris 2021 IMAX 3D 15000 200");

        // ERROR HANDLING: selama baris yang diketik user tidak valid
        // (format salah, field kurang, ID salah format/duplikat, dsb),
        while (true) {
            System.out.print("Input : ");
            String baris = scanner.nextLine();

            try {
                Film3D hasil = parseBarisFilm3D(baris, daftarTayangan);
                return hasil; // berhasil di-parse, keluar dari loop & method
            } catch (IllegalArgumentException e) {
                System.out.println("Gagal! " + e.getMessage() + "\n");
            } catch (Exception e) {
                // Menangkap error tak terduga lain (misal token kurang / index habis)
                System.out.println("Gagal! Format input tidak lengkap atau tidak sesuai urutan.\n");
            }
        }
    }

    // Mem-parsing satu baris input menjadi objek Film3D.
    // Karena JUDUL, SUTRADARA, dan FORMAT_LAYAR bisa lebih dari satu kata,
    // posisi field-field "jangkar" (angka & pola tetap) dicari dulu untuk
    // menentukan batas tiap field yang panjangnya bisa berubah-ubah.
    // Melempar IllegalArgumentException dengan pesan yang jelas jika
    // formatnya tidak sesuai.
    private static Film3D parseBarisFilm3D(String baris, List<Tayangan> daftarTayangan) {
        String[] token = baris.trim().split("\\s+");
        if (token.length < 11) {
            throw new IllegalArgumentException("Jumlah data terlalu sedikit, cek kembali urutannya.");
        }

        // --- ID (token pertama) ---
        String id = token[0];
        if (!id.matches("FD\\d{3}")) {
            throw new IllegalArgumentException("ID harus berformat FD diikuti 3 digit angka (contoh: FD001).");
        }
        if (idSudahAda(daftarTayangan, id)) {
            throw new IllegalArgumentException("ID \"" + id + "\" sudah digunakan.");
        }

        // --- Cari token DURASI: token angka murni pertama setelah ID ---
        int idxDurasi = -1;
        for (int i = 1; i < token.length; i++) {
            if (token[i].matches("\\d+")) {
                idxDurasi = i;
                break;
            }
        }
        if (idxDurasi == -1 || idxDurasi == 1) {
            throw new IllegalArgumentException("Durasi (angka) tidak ditemukan setelah judul.");
        }

        // --- JUDUL: semua token antara ID dan DURASI ---
        String judul = String.join(" ", java.util.Arrays.copyOfRange(token, 1, idxDurasi));
        int durasi = Integer.parseInt(token[idxDurasi]);

        // --- JAM_TAYANG & HARGA_TIKET: dua token tepat setelah DURASI ---
        if (idxDurasi + 2 >= token.length) {
            throw new IllegalArgumentException("Jam tayang / harga tiket tidak ditemukan.");
        }
        String jamTayang = token[idxDurasi + 1];
        double hargaTiket;
        try {
            hargaTiket = Double.parseDouble(token[idxDurasi + 2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Harga tiket harus berupa angka.");
        }

        // --- GENRE: satu token tepat setelah HARGA_TIKET ---
        int idxGenre = idxDurasi + 3;
        if (idxGenre >= token.length) {
            throw new IllegalArgumentException("Genre tidak ditemukan.");
        }
        String genre = token[idxGenre];

        // --- Cari token RATING_USIA: pola "13+", "17+", "SU", dst ---
        int idxRating = -1;
        for (int i = idxGenre + 1; i < token.length; i++) {
            if (token[i].matches("SU|\\d+\\+")) {
                idxRating = i;
                break;
            }
        }
        if (idxRating == -1 || idxRating == idxGenre + 1) {
            throw new IllegalArgumentException("Rating usia (contoh: 13+, 17+, SU) tidak ditemukan setelah sutradara.");
        }

        // --- SUTRADARA: semua token antara GENRE dan RATING_USIA ---
        String sutradara = String.join(" ", java.util.Arrays.copyOfRange(token, idxGenre + 1, idxRating));
        String ratingUsia = token[idxRating];

        // --- BAHASA: satu token tepat setelah RATING_USIA ---
        int idxBahasa = idxRating + 1;
        if (idxBahasa >= token.length) {
            throw new IllegalArgumentException("Bahasa tidak ditemukan.");
        }
        String bahasa = token[idxBahasa];

        // --- TAHUN_RILIS: satu token angka 4 digit setelah BAHASA ---
        int idxTahun = idxBahasa + 1;
        if (idxTahun >= token.length || !token[idxTahun].matches("(19|20)\\d{2}")) {
            throw new IllegalArgumentException("Tahun rilis (4 digit, contoh 2024) tidak ditemukan.");
        }
        int tahunRilis = Integer.parseInt(token[idxTahun]);

        // --- FORMAT_LAYAR: semua token antara TAHUN_RILIS dan 2 token terakhir ---
        int idxAkhirFormat = token.length - 2; // eksklusif, 2 token terakhir milik BIAYA & JUMLAH
        if (idxTahun + 1 >= idxAkhirFormat) {
            throw new IllegalArgumentException("Format layar tidak ditemukan.");
        }
        String formatLayar = String.join(" ", java.util.Arrays.copyOfRange(token, idxTahun + 1, idxAkhirFormat));

        // --- BIAYA_KACAMATA & JUMLAH_KACAMATA: 2 token terakhir ---
        double biayaKacamata;
        int jumlahKacamataTersedia;
        try {
            biayaKacamata = Double.parseDouble(token[token.length - 2]);
            jumlahKacamataTersedia = Integer.parseInt(token[token.length - 1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Biaya kacamata / jumlah kacamata harus berupa angka.");
        }

        return new Film3D(id, judul, durasi, jamTayang, hargaTiket,
                genre, sutradara, ratingUsia, bahasa, tahunRilis,
                formatLayar, biayaKacamata, jumlahKacamataTersedia);
    }

    // Method bantu untuk mengecek apakah suatu ID sudah dipakai
    // oleh objek lain di dalam daftar (baik itu Tayangan, Film, atau Film3D,
    private static boolean idSudahAda(List<Tayangan> daftarTayangan, String id) {
        for (Tayangan t : daftarTayangan) {
            if (t.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    // Menampilkan seluruh data dalam SATU tabel dinamis.
    private static void tampilkanTabel(List<Tayangan> daftar) {
        if (daftar.isEmpty()) {
            System.out.println("Belum ada data untuk ditampilkan.\n");
            return;
        }

        // Mengubah setiap objek (apapun tipenya) menjadi baris teks
        String[][] baris = new String[daftar.size()][HEADER.length];
        for (int i = 0; i < daftar.size(); i++) {
            baris[i] = buatBaris(daftar.get(i), i + 1);
        }

        // Menghitung lebar kolom secara dinamis:
        // lebar = panjang teks terpanjang antara header dan semua isi baris
        int[] lebarKolom = new int[HEADER.length];
        for (int j = 0; j < HEADER.length; j++) {
            lebarKolom[j] = HEADER[j].length();
            for (String[] b : baris) {
                lebarKolom[j] = Math.max(lebarKolom[j], b[j].length());
            }
        }

        // Mencetak tabel: garis atas, header, garis pemisah, isi data, garis bawah
        cetakGaris(lebarKolom);
        cetakBaris(HEADER, lebarKolom);
        cetakGaris(lebarKolom);
        for (String[] b : baris) {
            cetakBaris(b, lebarKolom);
        }
        cetakGaris(lebarKolom);
        System.out.println();
    }

    // Mengubah satu objek Tayangan/Film/Film3D menjadi array String
    // sesuai urutan kolom pada HEADER.
    // Menggunakan instanceof untuk mendeteksi tipe ASLI objek saat runtime
    private static String[] buatBaris(Tayangan t, int nomor) {
        String genre = "-", sutradara = "-", ratingUsia = "-", bahasa = "-", tahunRilis = "-";
        String formatLayar = "-", biayaKacamata = "-", jumlahKacamata = "-";

        // Cek Film3D LEBIH DULU karena Film3D adalah turunan dari Film.
        // Kalau dicek Film dulu, objek Film3D juga akan lolos sebagai Film
        // dan data khusus Film3D (formatLayar, dll) tidak akan tercetak.
        // (Tanda "-" akan tetap muncul jika suatu saat user menambah data
        // Tayangan/Film biasa lewat menu input, karena kolomnya tidak lengkap)
        if (t instanceof Film3D) {
            Film3D f3d = (Film3D) t;
            genre = f3d.getGenre();
            sutradara = f3d.getSutradara();
            ratingUsia = f3d.getRatingUsia();
            bahasa = f3d.getBahasa();
            tahunRilis = String.valueOf(f3d.getTahunRilis());
            formatLayar = f3d.getFormatLayar();
            biayaKacamata = String.valueOf(f3d.getBiayaKacamata());
            jumlahKacamata = String.valueOf(f3d.getJumlahKacamataTersedia());
        } else if (t instanceof Film) {
            Film f = (Film) t;
            genre = f.getGenre();
            sutradara = f.getSutradara();
            ratingUsia = f.getRatingUsia();
            bahasa = f.getBahasa();
            tahunRilis = String.valueOf(f.getTahunRilis());
        }
        // Jika objeknya murni Tayangan, semua variabel di atas tetap "-"

        return new String[] {
            String.valueOf(nomor), t.getId(), t.getJudul(),
            String.valueOf(t.getDurasi()), t.getJamTayang(), String.valueOf(t.getHargaTiket()),
            genre, sutradara, ratingUsia, bahasa, tahunRilis,
            formatLayar, biayaKacamata, jumlahKacamata
        };
    }

    // Mencetak satu baris tabel; tiap kolom diratakan kiri sesuai lebar dinamisnya
    private static void cetakBaris(String[] data, int[] lebarKolom) {
        StringBuilder sb = new StringBuilder("|");
        for (int j = 0; j < data.length; j++) {
            sb.append(" ").append(padRight(data[j], lebarKolom[j])).append(" |");
        }
        System.out.println(sb);
    }

    // Mencetak garis pembatas (+----+----+...) sesuai lebar tiap kolom
    private static void cetakGaris(int[] lebarKolom) {
        StringBuilder sb = new StringBuilder("+");
        for (int lebar : lebarKolom) {
            sb.append("-".repeat(lebar + 2)).append("+");
        }
        System.out.println(sb);
    }

    // Menambahkan spasi di kanan teks hingga mencapai panjang tertentu (rata kiri)
    private static String padRight(String teks, int panjang) {
        return String.format("%-" + panjang + "s", teks);
    }
}