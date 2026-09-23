#include <iostream>
#include <string>
#include "Tayangan.cpp"
using namespace std;

class Film : public Tayangan{
private:
    // atribut
    string genre;
    string sutradara;
    string ratingUsia;
    string bahasa;
    int tahunRilis;

public:
    // konstruktor kosong
    Film(){
    }

    // konstruktor
    Film(string id, string judul, int durasi, string jamTayang, double hargaTiket, 
        string genre, string sutradara, string ratingUsia, string bahasa, int tahunRilis) : 
        Tayangan (id, judul, durasi, jamTayang, hargaTiket){
        this->genre = genre;
        this->sutradara = sutradara;
        this->ratingUsia = ratingUsia;
        this->bahasa = bahasa;
        this->tahunRilis = tahunRilis;
    }

    // getter 
    string getGenre(){
        return genre;
    }
    string getSutradara(){
        return sutradara;
    }
    string getRatingUsia(){
        return ratingUsia;
    }
    string getBahasa(){
        return bahasa;
    }
    int getTahunRilis(){
        return tahunRilis;
    }
    // setter
    void setGenre(string genre){
        this->genre = genre;
    }
    void setSutradara(string sutradara){
        this->sutradara = sutradara;
    }
    void setRatingUsia(string ratingUsia){
        this->ratingUsia = ratingUsia;
    }
    void setBahasa(string bahasa){
        this->bahasa = bahasa;
    }
    void setTahunRilis(int tahunRilis){
        this->tahunRilis = tahunRilis;
    }

    // destruktor
    ~Film(){
    }
};