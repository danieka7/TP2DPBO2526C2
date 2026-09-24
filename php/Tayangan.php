<?php
class Tayangan{
    // atribut
    private $id;
    private $judul;
    private $durasi;
    private $jamTayang;
    private $hargaTiket;

    // konstruktor
    public function __construct($id, $judul, $durasi, $jamTayang, $hargaTiket){
        $this->id = $id;
        $this->judul = $judul;
        $this->durasi = $durasi;
        $this->jamTayang = $jamTayang;
        $this->hargaTiket = $hargaTiket;
    }

    // getter
    public function getId(){
        return $this->id;
    }
    public function getJudul(){
        return $this->judul;
    }
    public function getDurasi(){
        return $this->durasi;
    }
    public function getJamTayang(){
        return $this->jamTayang;
    }
    public function getHargaTiket(){
        return $this->hargaTiket;
    }

    // setter
    public function setId($id){
        $this->id = $id;
    }
    public function setJudul($judul){
        $this->judul = $judul;
    }
    public function setDurasi($durasi){
        $this->durasi = $durasi;
    }
    public function setJamTayang($jamTayang){
        $this->jamTayang = $jamTayang;
    }
    public function setGetHarga($hargaTiket){
        $this->hargaTiket = $hargaTiket;
    }

    
}

?>