#include <iostream>
#include <string>
#include "Film.cpp"
using namespace std;

class Film3D : public Film{
private:
    // atribut
    string formatLayar;
    double biayaKacamata;
    int jumlahKacamataTersedia;

public:
    // konstruktor kosong
    Film3D(){
    }
    // konstruktor
    Film3D(string id, string judul, int durasi, string jamTayang, double hargaTiket, 
        string genre, string sutradara, string ratingUsia, string bahasa, int tahunRilis, 
        string formatLayar, double biayaKacamata, int jumlahKacamataTersedia) : 
        Film(id, judul, durasi, jamTayang, hargaTiket, genre, sutradara, ratingUsia, bahasa, tahunRilis){
        this->formatLayar = formatLayar;
        this->biayaKacamata = biayaKacamata;
        this->jumlahKacamataTersedia = jumlahKacamataTersedia;
    }

    // getter
    string getFormatLayar(){
        return formatLayar;
    }
    double getBiayaKacamata(){
        return biayaKacamata;
    }
    int getJumlahKacamataTersedia(){
        return jumlahKacamataTersedia;
    }

    // setter
    void setFormatLayar(string formatLayar){
        this->formatLayar = formatLayar;
    }
    void setBiayaKacamata(double biayaKacamata){
        this->biayaKacamata = biayaKacamata;
    }
    void setJumlahKacamataTersedia(int jumlahKacamataTersedia){
        this->jumlahKacamataTersedia = jumlahKacamataTersedia;
    }

    // destruktor
    ~Film3D(){
    }
};