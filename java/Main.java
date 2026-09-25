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

        // ===================================================================
        // 1. INISIALISASI 5 OBJEK AWAL (WAJIB ADA SEBELUM INPUT USER)
        //    Semua dibuat sebagai Film3D supaya SETIAP kolom pada tabel
        //    terisi penuh (tidak ada tanda "-"), karena Film3D adalah
        //    kelas paling lengkap (mewarisi field Tayangan + Film,
        //    ditambah field khusus Film3D)
        // ===================================================================

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
            System.out.println("(Ketik 'batal' pada input kapan saja untuk kembali ke menu ini)");
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
                case "3":
                    berjalan = false;
                    System.out.println("Program selesai. Sampai jumpa!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid, coba lagi.\n");
            }
        }

        scanner.close();
    }

    // =======================================================================
    // Method untuk mengambil input data Film3D dari user
    // (field Tayangan + field Film + field khusus Film3D)
    // =======================================================================
    private static Film3D inputFilm3D(Scanner scanner, List<Tayangan> daftarTayangan) {
        System.out.println("--- Input Data Film3D ---");

        // ERROR HANDLING:
        // 1. Format ID harus berawalan "FD" diikuti 3 digit angka, contoh: FD001
        // 2. ID tidak boleh sama dengan ID yang sudah ada (duplikat)
        // Program akan terus meminta ID baru selama salah satu syarat di atas
        // belum terpenuhi (looping validasi).
        String id;
        while (true) {
            System.out.print("ID (format FDxxx)        : ");
            id = scanner.nextLine();

            if (!id.matches("FD\\d{3}")) {
                System.out.println("Gagal! ID harus berformat FD diikuti 3 digit angka (contoh: FD001).\n");
            } else if (idSudahAda(daftarTayangan, id)) {
                System.out.println("Gagal! ID \"" + id + "\" sudah digunakan. Silakan masukkan ID lain.\n");
            } else {
                break; // ID valid dan belum dipakai, keluar dari loop
            }
        }

        System.out.print("Judul                    : ");
        String judul = scanner.nextLine();
        System.out.print("Durasi (mnt)             : ");
        int durasi = Integer.parseInt(scanner.nextLine());
        System.out.print("Jam Tayang               : ");
        String jamTayang = scanner.nextLine();
        System.out.print("Harga Tiket              : ");
        double hargaTiket = Double.parseDouble(scanner.nextLine());
        System.out.print("Genre                    : ");
        String genre = scanner.nextLine();
        System.out.print("Sutradara                : ");
        String sutradara = scanner.nextLine();
        System.out.print("Rating Usia              : ");
        String ratingUsia = scanner.nextLine();
        System.out.print("Bahasa                   : ");
        String bahasa = scanner.nextLine();
        System.out.print("Tahun Rilis              : ");
        int tahunRilis = Integer.parseInt(scanner.nextLine());
        System.out.print("Format Layar             : ");
        String formatLayar = scanner.nextLine();
        System.out.print("Biaya Kacamata           : ");
        double biayaKacamata = Double.parseDouble(scanner.nextLine());
        System.out.print("Jumlah Kacamata Tersedia : ");
        int jumlahKacamataTersedia = Integer.parseInt(scanner.nextLine());

        return new Film3D(id, judul, durasi, jamTayang, hargaTiket,
                genre, sutradara, ratingUsia, bahasa, tahunRilis,
                formatLayar, biayaKacamata, jumlahKacamataTersedia);
    }

    // =======================================================================
    // Method bantu untuk mengecek apakah suatu ID sudah dipakai
    // oleh objek lain di dalam daftar (baik itu Tayangan, Film, atau Film3D,
    // karena getId() ada di superclass Tayangan)
    // =======================================================================
    private static boolean idSudahAda(List<Tayangan> daftarTayangan, String id) {
        for (Tayangan t : daftarTayangan) {
            if (t.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    // =======================================================================
    // Menampilkan seluruh data dalam SATU tabel dinamis.
    // "Dinamis" di sini berarti lebar tiap kolom otomatis menyesuaikan
    // panjang data terpanjang di kolom tersebut, bukan lebar tetap (fixed).
    // =======================================================================
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

    // =======================================================================
    // Mengubah satu objek Tayangan/Film/Film3D menjadi array String
    // sesuai urutan kolom pada HEADER.
    // Menggunakan instanceof untuk mendeteksi tipe ASLI objek saat runtime
    // (ini contoh polimorfisme: List berisi Tayangan, tapi isinya bisa
    // sebenarnya Film atau Film3D)
    // =======================================================================
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