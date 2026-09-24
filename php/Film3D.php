<?php
class Film3D extends Film{
    // atribut
    private $formatLayar;
    private $biayaKacamata;
    private $jumlahKacamataTersedia;

    // konstruktor
    public function __construct($id, $judul, $durasi, $jamTayang, $hargaTiket,
                    $genre, $sutradara, $ratingUsia, $bahasa, $tahunRilis,
                    $formatLayar, $biayaKacamata, $jumlahKacamataTersedia){
        parent:: __construct($id, $judul, $durasi, $jamTayang, $hargaTiket,
                    $genre, $sutradara, $ratingUsia, $bahasa, $tahunRilis);
        $this->formatLayar = $formatLayar;
        $this->biayaKacamata = $biayaKacamata;
        $this->jumlahKacamataTersedia = $jumlahKacamataTersedia;
    }

    // getter
    public function getFormatLayar(){
        return $this->formatLayar;
    }
    public function getBiayaKacamata(){
        return $this->biayaKacamata;
    }
    public function getJumlahKacamataTersedia(){
        return $this->jumlahKacamataTersedia;
    }

    // setter
    public function setFormatLayar($formatLayar){
        $this->formatLayar = $formatLayar;
    }
    public function setBiayaKacamata($biayaKacamata){
        $this->biayaKacamata = $biayaKacamata;
    }
    public function setJumlahKacamataTersedia($jumlahKacamataTersedia){
        $this->jumlahKacamataTersedia = $jumlahKacamataTersedia;
    }
}


?>