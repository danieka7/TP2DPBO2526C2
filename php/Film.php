<?php
class Film extends Tayangan{
    // atribut
    private $genre;
    private $sutradara;
    private $ratingUsia;
    private $bahasa;
    private $tahunRilis;

    // konstruktor
    public function __construct($id, $judul, $durasi, $jamTayang, $hargaTiket, 
                                $genre, $sutradara, $ratingUsia, $bahasa, $tahunRilis){
        parent::__construct($id, judul, $durasi, $jamTayang, $hargaTiket);
        $this->genre = $genre;
        $this->sutradara = $sutradara;
        $this->ratingUsia = $ratingUsia;
        $this->bahasa = $bahasa;
        $this->tahunRilis = $tahunRilis;
    }

    // getter
    public function getGenre(){
        return $this->genre;
    }
    public function getSutradara(){
        return $this->sutradara;
    }
    public function getRatingUsia(){
        return $this->ratingUsia;
    }
    public function getBahasa(){
        return $this->bahasa;
    }
    public function getTahunRilis(){
        return $this->tahunRilis;
    }

    // setter
    public function setGenre($genre){
        $this->genre = $genre;
    }
    public function setSutradara($sutradara){
        $this->sutradara = $sutradara;
    }
    public function setRatingUsia($ratingUsia){
        $this->ratingUsia = $ratingUsia;
    }
    public function setBahasa($bahasa){
        $this->bahasa = $bahasa;
    }
    public function setTahunRilis($tahunRilis){
        $this->tahunRilis = $tahunRilis;
    }
}
?>