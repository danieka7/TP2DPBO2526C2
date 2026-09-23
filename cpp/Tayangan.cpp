#include <iostream>
#include <string>
using namespace std;

class Tayangan{
private:
    // atribut
    string id;
    string judul;
    int durasi;
    string jamTayang;
    double hargaTiket;

public:
    // konstruktor kosong
    Tayangan(){
    }

    // konstruktor
    Tayangan(string id, string judul, int durasi, string jamTayang, double hargaTiket){
        this->id = id;
        this->judul = judul;
        this->durasi = durasi;
        this->jamTayang = jamTayang;
        this->hargaTiket = hargaTiket;
    }

    // getter
    string getId(){
        return id;
    }
    string getJudul(){
        return judul;
    }
    int getDurasi(){
        return durasi;
    }
    string getJamTayang(){
        return jamTayang;
    }
    double getHargaTiket(){
        return hargaTiket;
    }
    
    // setter
    void setId(string id){
        this->id = id;
    }
    void setJudul(string judul){
        this->judul = judul;
    }
    void setDurasi(int durasi){
        this->durasi = durasi;
    }
    void setJamTayang(string jamTayang){
        this->jamTayang = jamTayang;
    }
    void setHargaTiket(double hargaTiket){
        this->hargaTiket = hargaTiket;
    }

    // destruktor
    ~Tayangan(){
    }
};