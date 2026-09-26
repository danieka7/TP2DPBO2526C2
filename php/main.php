<?php
require_once 'Tayangan.php';
require_once 'Film.php';
require_once 'Film3D.php';

// Folder untuk menyimpan poster yang diupload user
define('FOLDER_UPLOAD', __DIR__ . '/uploads/');

// ============================================================
// 1. INISIALISASI DATA AWAL
//    (dieksekusi ulang SETIAP KALI halaman ini diakses, jadi
//    daftar film selalu mulai dari 5 data ini)
// ============================================================
$daftarTayangan = [
    new Film3D("FD001", "avatar.jpg", "Avatar", 162, "18:30", 75000,
        "Sci-Fi", "James Cameron", "13+", "Inggris", 2009,
        "RealD 3D", 15000, 200),
    new Film3D("FD002", "avengers_endgame.jpg", "Avengers: Endgame", 181, "13:00", 65000,
        "Action/Superhero", "Anthony & Joe Russo", "13+", "Inggris", 2019,
        "IMAX 3D", 10000, 150),
    new Film3D("FD003", "doctor_strange.jpg", "Doctor Strange", 115, "16:00", 60000,
        "Action/Fantasi", "Scott Derrickson", "13+", "Inggris", 2016,
        "Dolby 3D", 20000, 100),
    new Film3D("FD004", "spider-man_atsv.jpg", "Spider-Man: Across the Spider-Verse", 170, "20:00", 70000,
        "Animasi/Superhero", "Joaquim Dos Santos", "13+", "Inggris", 2023,
        "IMAX 3D", 15000, 180),
    new Film3D("FD005", "oppenheimer.jpg", "Oppenheimer", 140, "21:30", 80000,
        "Biografi", "Christopher Nolan", "17+", "Inggris", 2023,
        "IMAX 3D", 12000, 220),
];

/** Fungsi bantu: cek apakah ID sudah dipakai objek lain di daftar. */
function idSudahAda($daftarTayangan, $id) {
    foreach ($daftarTayangan as $t) {
        if (strcasecmp($t->getId(), $id) === 0) {
            return true;
        }
    }
    return false;
}

/** Fungsi bantu: format angka jadi "Rp 75.000". */
function formatRupiah($angka) {
    return "Rp " . number_format((float) $angka, 0, ',', '.');
}

// ============================================================
// 2. MEMPROSES FORM "TAMBAH FILM" (kalau ada submit POST)
// ============================================================
$errors = [];          // menyimpan pesan error per field, untuk ditampilkan di form
$old = $_POST;         // menyimpan input lama supaya form tidak kosong lagi kalau ada error
$flash = null;         // pesan notifikasi (sukses) yang tampil sekali di atas halaman

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['aksi']) && $_POST['aksi'] === 'tambah') {

    // --- Ambil & bersihkan input teks ---
    $id          = trim($_POST['id'] ?? '');
    $judul       = trim($_POST['judul'] ?? '');
    $durasi      = trim($_POST['durasi'] ?? '');
    $jamTayang   = trim($_POST['jamTayang'] ?? '');
    $hargaTiket  = trim($_POST['hargaTiket'] ?? '');
    $genre       = trim($_POST['genre'] ?? '');
    $sutradara   = trim($_POST['sutradara'] ?? '');
    $ratingUsia  = trim($_POST['ratingUsia'] ?? '');
    $bahasa      = trim($_POST['bahasa'] ?? '');
    $tahunRilis  = trim($_POST['tahunRilis'] ?? '');
    $formatLayar = trim($_POST['formatLayar'] ?? '');
    $biayaKacamata = trim($_POST['biayaKacamata'] ?? '');
    $jumlahKacamata = trim($_POST['jumlahKacamata'] ?? '');

    // --- Validasi tiap field (mirror dari validasi versi CLI) ---
    if (!preg_match('/^FD\d{3}$/', $id)) {
        $errors['id'] = "ID harus berformat FD + 3 digit angka, contoh FD006.";
    } elseif (idSudahAda($daftarTayangan, $id)) {
        $errors['id'] = "ID \"$id\" sudah digunakan film lain.";
    }

    if ($judul === '') {
        $errors['judul'] = "Judul tidak boleh kosong.";
    }

    if (!preg_match('/^\d+$/', $durasi) || (int) $durasi <= 0) {
        $errors['durasi'] = "Durasi harus berupa angka menit lebih dari 0.";
    }

    if (!preg_match('/^([01]\d|2[0-3]):[0-5]\d$/', $jamTayang)) {
        $errors['jamTayang'] = "Jam tayang harus berformat HH:MM, contoh 19:30.";
    }

    if (!is_numeric($hargaTiket) || (float) $hargaTiket < 0) {
        $errors['hargaTiket'] = "Harga tiket harus berupa angka.";
    }

    if ($genre === '') {
        $errors['genre'] = "Genre tidak boleh kosong.";
    }

    if ($sutradara === '') {
        $errors['sutradara'] = "Sutradara tidak boleh kosong.";
    }

    if (!preg_match('/^(SU|\d+\+)$/', $ratingUsia)) {
        $errors['ratingUsia'] = "Rating usia contoh: SU, 13+, 17+.";
    }

    if ($bahasa === '') {
        $errors['bahasa'] = "Bahasa tidak boleh kosong.";
    }

    if (!preg_match('/^(19|20)\d{2}$/', $tahunRilis)) {
        $errors['tahunRilis'] = "Tahun rilis harus 4 digit, contoh 2024.";
    }

    if ($formatLayar === '') {
        $errors['formatLayar'] = "Format layar tidak boleh kosong.";
    }

    if (!is_numeric($biayaKacamata) || (float) $biayaKacamata < 0) {
        $errors['biayaKacamata'] = "Biaya kacamata harus berupa angka.";
    }

    if (!preg_match('/^\d+$/', $jumlahKacamata)) {
        $errors['jumlahKacamata'] = "Jumlah kacamata harus berupa angka bulat.";
    }

    // --- Validasi & pemrosesan upload poster (opsional) ---
    $namaFileGambar = '';
    if (isset($_FILES['gambar']) && $_FILES['gambar']['error'] !== UPLOAD_ERR_NO_FILE) {
        $file = $_FILES['gambar'];
        $extensiOk = ['jpg', 'jpeg', 'png', 'webp'];
        $ekstensi = strtolower(pathinfo($file['name'], PATHINFO_EXTENSION));

        if ($file['error'] !== UPLOAD_ERR_OK) {
            $errors['gambar'] = "Upload poster gagal, coba lagi.";
        } elseif (!in_array($ekstensi, $extensiOk)) {
            $errors['gambar'] = "Poster harus berformat JPG, PNG, atau WEBP.";
        } elseif ($file['size'] > 2 * 1024 * 1024) { // maksimal 2MB
            $errors['gambar'] = "Ukuran poster maksimal 2MB.";
        } else {
            // Nama file dibuat unik memakai ID film + timestamp
            $namaFileGambar = strtolower($id !== '' ? $id : 'poster') . '-' . time() . '.' . $ekstensi;
            if (!is_dir(FOLDER_UPLOAD)) {
                mkdir(FOLDER_UPLOAD, 0755, true);
            }
            if (!move_uploaded_file($file['tmp_name'], FOLDER_UPLOAD . $namaFileGambar)) {
                $errors['gambar'] = "Poster gagal disimpan ke server.";
                $namaFileGambar = '';
            }
        }
    }

    // --- Jika semua validasi lolos, buat objek Film3D baru ---
    if (empty($errors)) {
        $film3DBaru = new Film3D(
            $id, $namaFileGambar, $judul, (int) $durasi, $jamTayang, (float) $hargaTiket,
            $genre, $sutradara, $ratingUsia, $bahasa, (int) $tahunRilis,
            $formatLayar, (float) $biayaKacamata, (int) $jumlahKacamata
        );

        // Ditambahkan ke array LOKAL (bukan session) supaya langsung
        // tampil di tabel pada halaman hasil submit ini. TIDAK di-redirect,
        // karena kalau di-redirect, data ini justru akan langsung hilang
        // lagi (sesuai keinginan: tidak ada data yang tersimpan permanen).
        $daftarTayangan[] = $film3DBaru;
        $flash = ['tipe' => 'sukses', 'pesan' =>
            "Film \"$judul\" berhasil ditambahkan."];
    }
}
?>
<!DOCTYPE html>
<html lang="id">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Bioskop Sinefil Abiez</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@600;700&family=Work+Sans:wght@400;500;600&display=swap" rel="stylesheet">
<style>
/*
  style.css
  Tema visual: "papan jadwal bioskop" - latar gelap ala ruang studio,
  aksen emas seperti lampu marquee, dan kartu poster untuk tiap film.
  Token warna & tipografi didaftarkan sebagai CSS variable di :root
  supaya mudah dipakai ulang di seluruh halaman.
*/

:root {
    /* --- Palet warna --- */
    --bg: #15131c;              /* latar utama: hitam kebiruan seperti ruang studio */
    --bg-panel: #1f1c29;        /* latar panel/kartu, sedikit lebih terang dari bg */
    --bg-panel-alt: #262233;    /* latar baris tabel selang-seling */
    --border: #37324a;          /* garis pembatas halus */
    --gold: #e3b23c;            /* aksen emas ala lampu marquee */
    --gold-soft: #f2d998;
    --text: #ece6d6;            /* teks utama, putih gading */
    --text-muted: #a79fc0;      /* teks sekunder */
    --red: #c1483d;             /* aksen untuk error/rating dewasa */
    --green: #4caf7d;           /* aksen untuk status berhasil */

    /* --- Tipografi --- */
    --font-display: "Playfair Display", Georgia, serif; /* judul, terasa seperti judul film */
    --font-body: "Work Sans", "Segoe UI", sans-serif;    /* isi & form, mudah dibaca */
}

* {
    box-sizing: border-box;
}

body {
    margin: 0;
    background: var(--bg);
    background-image:
        radial-gradient(circle at 15% 0%, rgba(227, 178, 60, 0.08), transparent 45%),
        radial-gradient(circle at 85% 10%, rgba(193, 72, 61, 0.06), transparent 40%);
    color: var(--text);
    font-family: var(--font-body);
    line-height: 1.5;
}

.wrap {
    max-width: 1080px;
    margin: 0 auto;
    padding: 0 24px 64px;
}

/* ---------- Header / marquee ---------- */
.marquee {
    padding: 48px 0 28px;
    text-align: center;
    border-bottom: 1px solid var(--border);
    margin-bottom: 36px;
}

.marquee .kicker {
    color: var(--gold);
    letter-spacing: 0.04em;
    font-size: 0.95rem;
    margin: 0 0 6px;
}

.marquee h1 {
    font-family: var(--font-display);
    font-size: clamp(2.1rem, 4vw, 3rem);
    margin: 0;
    color: var(--gold-soft);
    text-shadow: 0 0 18px rgba(227, 178, 60, 0.25);
}

.marquee p {
    color: var(--text-muted);
    max-width: 46ch;
    margin: 10px auto 0;
}

/* ---------- Flash message ---------- */
.flash {
    border-radius: 10px;
    padding: 14px 18px;
    margin-bottom: 28px;
    border: 1px solid transparent;
    font-size: 0.95rem;
}

.flash.sukses {
    background: rgba(76, 175, 125, 0.12);
    border-color: rgba(76, 175, 125, 0.4);
    color: #9be0bd;
}

.flash.gagal {
    background: rgba(193, 72, 61, 0.12);
    border-color: rgba(193, 72, 61, 0.4);
    color: #eba69f;
}

/* ---------- Panel umum ---------- */
.panel {
    background: var(--bg-panel);
    border: 1px solid var(--border);
    border-radius: 14px;
    padding: 28px 30px;
    margin-bottom: 36px;
}

.panel h2 {
    font-family: var(--font-display);
    font-size: 1.5rem;
    margin: 0 0 4px;
    color: var(--gold-soft);
}

.panel .subtitel {
    color: var(--text-muted);
    margin: 0 0 22px;
    font-size: 0.92rem;
}

/* ---------- Form tambah film ---------- */
.grid-form {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 16px 20px;
}

.field {
    display: flex;
    flex-direction: column;
    gap: 6px;
}

.field.lebar {
    grid-column: 1 / -1;
}

.field label {
    font-size: 0.85rem;
    color: var(--text-muted);
}

.field input,
.field select {
    background: #100e17;
    border: 1px solid var(--border);
    border-radius: 8px;
    padding: 10px 12px;
    color: var(--text);
    font-family: var(--font-body);
    font-size: 0.95rem;
}

.field input:focus,
.field select:focus {
    outline: 2px solid var(--gold);
    outline-offset: 1px;
    border-color: var(--gold);
}

.field .pesan-error {
    color: #eba69f;
    font-size: 0.8rem;
}

.field.invalid input {
    border-color: var(--red);
}

.tombol-submit {
    margin-top: 22px;
    background: var(--gold);
    color: #1a1710;
    border: none;
    border-radius: 8px;
    padding: 12px 26px;
    font-family: var(--font-body);
    font-weight: 600;
    font-size: 0.95rem;
    cursor: pointer;
}

.tombol-submit:hover {
    background: var(--gold-soft);
}

/* ---------- Jadwal / tabel film ---------- */
.jadwal-kosong {
    color: var(--text-muted);
    text-align: center;
    padding: 32px 0;
}

.tabel-scroll {
    overflow-x: auto;
}

table.jadwal {
    width: 100%;
    border-collapse: collapse;
    min-width: 900px;
}

table.jadwal th,
table.jadwal td {
    padding: 12px 14px;
    text-align: left;
    border-bottom: 1px solid var(--border);
    font-size: 0.88rem;
    white-space: nowrap;
}

table.jadwal th {
    color: var(--gold);
    font-weight: 600;
    font-size: 0.8rem;
    letter-spacing: 0.02em;
}

table.jadwal tbody tr:nth-child(even) {
    background: var(--bg-panel-alt);
}

.poster-cell {
    display: flex;
    align-items: center;
    gap: 10px;
}

.poster-thumb {
    width: 42px;
    height: 60px;
    border-radius: 4px;
    object-fit: cover;
    border: 1px solid var(--border);
    flex-shrink: 0;
}

.poster-placeholder {
    width: 42px;
    height: 60px;
    border-radius: 4px;
    border: 1px dashed var(--border);
    display: flex;
    align-items: center;
    justify-content: center;
    color: var(--text-muted);
    font-size: 1.1rem;
    flex-shrink: 0;
}

.badge-rating {
    display: inline-block;
    padding: 2px 8px;
    border-radius: 999px;
    font-size: 0.75rem;
    border: 1px solid var(--red);
    color: #eba69f;
}

footer {
    text-align: center;
    color: var(--text-muted);
    font-size: 0.82rem;
    margin-top: 12px;
}

</style>
</head>
<body>
<div class="wrap">

    <header class="marquee">
        <h1>Bioskop Sinefil Abiez</h1>
    </header>

    <?php if ($flash): ?>
        <div class="flash <?= $flash['tipe'] === 'sukses' ? 'sukses' : 'gagal' ?>">
            <?= htmlspecialchars($flash['pesan']) ?>
        </div>
    <?php endif; ?>

    <?php if (!empty($errors)): ?>
        <div class="flash gagal">
            Data belum tersimpan, ada <?= count($errors) ?> isian yang perlu diperbaiki di bawah.
        </div>
    <?php endif; ?>

    <!-- ============ FORM TAMBAH FILM3D ============ -->
    <section class="panel">
        <h2>Tambah Film Baru</h2>
        <p class="subtitel">Semua field wajib diisi kecuali poster.</p>

        <form action="main.php" method="post" enctype="multipart/form-data" novalidate>
            <input type="hidden" name="aksi" value="tambah">
            <div class="grid-form">

                <div class="field <?= isset($errors['id']) ? 'invalid' : '' ?>">
                    <label for="id">ID Film (FDxxx)</label>
                    <input type="text" id="id" name="id" placeholder="FD006"
                           value="<?= htmlspecialchars($old['id'] ?? '') ?>">
                    <?php if (isset($errors['id'])): ?><span class="pesan-error"><?= $errors['id'] ?></span><?php endif; ?>
                </div>

                <div class="field lebar <?= isset($errors['judul']) ? 'invalid' : '' ?>">
                    <label for="judul">Judul Film</label>
                    <input type="text" id="judul" name="judul" placeholder="Contoh: Dune: Part Two"
                           value="<?= htmlspecialchars($old['judul'] ?? '') ?>">
                    <?php if (isset($errors['judul'])): ?><span class="pesan-error"><?= $errors['judul'] ?></span><?php endif; ?>
                </div>

                <div class="field <?= isset($errors['durasi']) ? 'invalid' : '' ?>">
                    <label for="durasi">Durasi (menit)</label>
                    <input type="text" id="durasi" name="durasi" placeholder="150"
                           value="<?= htmlspecialchars($old['durasi'] ?? '') ?>">
                    <?php if (isset($errors['durasi'])): ?><span class="pesan-error"><?= $errors['durasi'] ?></span><?php endif; ?>
                </div>

                <div class="field <?= isset($errors['jamTayang']) ? 'invalid' : '' ?>">
                    <label for="jamTayang">Jam Tayang</label>
                    <input type="text" id="jamTayang" name="jamTayang" placeholder="19:30"
                           value="<?= htmlspecialchars($old['jamTayang'] ?? '') ?>">
                    <?php if (isset($errors['jamTayang'])): ?><span class="pesan-error"><?= $errors['jamTayang'] ?></span><?php endif; ?>
                </div>

                <div class="field <?= isset($errors['hargaTiket']) ? 'invalid' : '' ?>">
                    <label for="hargaTiket">Harga Tiket (Rp)</label>
                    <input type="text" id="hargaTiket" name="hargaTiket" placeholder="65000"
                           value="<?= htmlspecialchars($old['hargaTiket'] ?? '') ?>">
                    <?php if (isset($errors['hargaTiket'])): ?><span class="pesan-error"><?= $errors['hargaTiket'] ?></span><?php endif; ?>
                </div>

                <div class="field <?= isset($errors['genre']) ? 'invalid' : '' ?>">
                    <label for="genre">Genre</label>
                    <input type="text" id="genre" name="genre" placeholder="Sci-Fi"
                           value="<?= htmlspecialchars($old['genre'] ?? '') ?>">
                    <?php if (isset($errors['genre'])): ?><span class="pesan-error"><?= $errors['genre'] ?></span><?php endif; ?>
                </div>

                <div class="field lebar <?= isset($errors['sutradara']) ? 'invalid' : '' ?>">
                    <label for="sutradara">Sutradara</label>
                    <input type="text" id="sutradara" name="sutradara" placeholder="Denis Villeneuve"
                           value="<?= htmlspecialchars($old['sutradara'] ?? '') ?>">
                    <?php if (isset($errors['sutradara'])): ?><span class="pesan-error"><?= $errors['sutradara'] ?></span><?php endif; ?>
                </div>

                <div class="field <?= isset($errors['ratingUsia']) ? 'invalid' : '' ?>">
                    <label for="ratingUsia">Rating Usia</label>
                    <input type="text" id="ratingUsia" name="ratingUsia" placeholder="13+ / SU"
                           value="<?= htmlspecialchars($old['ratingUsia'] ?? '') ?>">
                    <?php if (isset($errors['ratingUsia'])): ?><span class="pesan-error"><?= $errors['ratingUsia'] ?></span><?php endif; ?>
                </div>

                <div class="field <?= isset($errors['bahasa']) ? 'invalid' : '' ?>">
                    <label for="bahasa">Bahasa</label>
                    <input type="text" id="bahasa" name="bahasa" placeholder="Inggris"
                           value="<?= htmlspecialchars($old['bahasa'] ?? '') ?>">
                    <?php if (isset($errors['bahasa'])): ?><span class="pesan-error"><?= $errors['bahasa'] ?></span><?php endif; ?>
                </div>

                <div class="field <?= isset($errors['tahunRilis']) ? 'invalid' : '' ?>">
                    <label for="tahunRilis">Tahun Rilis</label>
                    <input type="text" id="tahunRilis" name="tahunRilis" placeholder="2024"
                           value="<?= htmlspecialchars($old['tahunRilis'] ?? '') ?>">
                    <?php if (isset($errors['tahunRilis'])): ?><span class="pesan-error"><?= $errors['tahunRilis'] ?></span><?php endif; ?>
                </div>

                <div class="field <?= isset($errors['formatLayar']) ? 'invalid' : '' ?>">
                    <label for="formatLayar">Format Layar</label>
                    <input type="text" id="formatLayar" name="formatLayar" placeholder="IMAX 3D"
                           value="<?= htmlspecialchars($old['formatLayar'] ?? '') ?>">
                    <?php if (isset($errors['formatLayar'])): ?><span class="pesan-error"><?= $errors['formatLayar'] ?></span><?php endif; ?>
                </div>

                <div class="field <?= isset($errors['biayaKacamata']) ? 'invalid' : '' ?>">
                    <label for="biayaKacamata">Biaya Kacamata (Rp)</label>
                    <input type="text" id="biayaKacamata" name="biayaKacamata" placeholder="15000"
                           value="<?= htmlspecialchars($old['biayaKacamata'] ?? '') ?>">
                    <?php if (isset($errors['biayaKacamata'])): ?><span class="pesan-error"><?= $errors['biayaKacamata'] ?></span><?php endif; ?>
                </div>

                <div class="field <?= isset($errors['jumlahKacamata']) ? 'invalid' : '' ?>">
                    <label for="jumlahKacamata">Stok Kacamata 3D</label>
                    <input type="text" id="jumlahKacamata" name="jumlahKacamata" placeholder="200"
                           value="<?= htmlspecialchars($old['jumlahKacamata'] ?? '') ?>">
                    <?php if (isset($errors['jumlahKacamata'])): ?><span class="pesan-error"><?= $errors['jumlahKacamata'] ?></span><?php endif; ?>
                </div>

                <div class="field lebar <?= isset($errors['gambar']) ? 'invalid' : '' ?>">
                    <label for="gambar">Poster (opsional, JPG/PNG/WEBP, maks 2MB)</label>
                    <input type="file" id="gambar" name="gambar" accept=".jpg,.jpeg,.png,.webp">
                    <?php if (isset($errors['gambar'])): ?><span class="pesan-error"><?= $errors['gambar'] ?></span><?php endif; ?>
                </div>

            </div>
            <button type="submit" class="tombol-submit">Tambahkan ke Jadwal</button>
        </form>
    </section>

    <!-- ============ TABEL / JADWAL SEMUA FILM ============ -->
    <section class="panel">
        <h2>Jadwal Tayang</h2>
        <p class="subtitel"><?= count($daftarTayangan) ?> film terjadwal saat ini.</p>

        <?php if (empty($daftarTayangan)): ?>
            <p class="jadwal-kosong">Belum ada film yang dijadwalkan.</p>
        <?php else: ?>
            <div class="tabel-scroll">
            <table class="jadwal">
                <thead>
                    <tr>
                        <th>No</th>
                        <th>Poster &amp; Judul</th>
                        <th>Durasi</th>
                        <th>Jam</th>
                        <th>Harga Tiket</th>
                        <th>Genre</th>
                        <th>Sutradara</th>
                        <th>Rating</th>
                        <th>Bahasa</th>
                        <th>Tahun</th>
                        <th>Format Layar</th>
                        <th>Kacamata</th>
                    </tr>
                </thead>
                <tbody>
                <?php foreach ($daftarTayangan as $nomor => $t): ?>
                    <?php
                        // Menggunakan instanceof untuk mendeteksi tipe ASLI objek saat
                        // runtime, sama seperti pada versi CLI (Main.java / Main.php).
                        $genre = $sutradara = $ratingUsia = $bahasa = $tahunRilis = "-";
                        $formatLayar = $infoKacamata = "-";

                        if ($t instanceof Film3D) {
                            $genre = $t->getGenre();
                            $sutradara = $t->getSutradara();
                            $ratingUsia = $t->getRatingUsia();
                            $bahasa = $t->getBahasa();
                            $tahunRilis = $t->getTahunRilis();
                            $formatLayar = $t->getFormatLayar();
                            $infoKacamata = formatRupiah($t->getBiayaKacamata())
                                . " &middot; stok " . $t->getJumlahKacamataTersedia();
                        } elseif ($t instanceof Film) {
                            $genre = $t->getGenre();
                            $sutradara = $t->getSutradara();
                            $ratingUsia = $t->getRatingUsia();
                            $bahasa = $t->getBahasa();
                            $tahunRilis = $t->getTahunRilis();
                        }

                        $lokasiPoster = FOLDER_UPLOAD . $t->getGambar();
                        $adaPoster = $t->getGambar() !== '' && file_exists($lokasiPoster);
                    ?>
                    <tr>
                        <td><?= $nomor + 1 ?></td>
                        <td>
                            <div class="poster-cell">
                                <?php if ($adaPoster): ?>
                                    <img class="poster-thumb" src="uploads/<?= htmlspecialchars($t->getGambar()) ?>" alt="Poster <?= htmlspecialchars($t->getJudul()) ?>">
                                <?php else: ?>
                                    <span class="poster-placeholder" aria-hidden="true">&#127916;</span>
                                <?php endif; ?>
                                <div>
                                    <strong><?= htmlspecialchars($t->getJudul()) ?></strong><br>
                                    <span style="color:var(--text-muted); font-size:0.8rem;"><?= htmlspecialchars($t->getId()) ?></span>
                                </div>
                            </div>
                        </td>
                        <td><?= (int) $t->getDurasi() ?> mnt</td>
                        <td><?= htmlspecialchars($t->getJamTayang()) ?></td>
                        <td><?= formatRupiah($t->getHargaTiket()) ?></td>
                        <td><?= htmlspecialchars($genre) ?></td>
                        <td><?= htmlspecialchars($sutradara) ?></td>
                        <td><span class="badge-rating"><?= htmlspecialchars($ratingUsia) ?></span></td>
                        <td><?= htmlspecialchars($bahasa) ?></td>
                        <td><?= htmlspecialchars((string) $tahunRilis) ?></td>
                        <td><?= htmlspecialchars($formatLayar) ?></td>
                        <td><?= $infoKacamata ?></td>
                    </tr>
                <?php endforeach; ?>
                </tbody>
            </table>
            </div>
        <?php endif; ?>
    </section>

    <footer>Data tidak disimpan permanen &mdash; setiap halaman ini dibuka/dimuat ulang, jadwal akan kembali ke 5 data awal.</footer>
</div>
</body>
</html>