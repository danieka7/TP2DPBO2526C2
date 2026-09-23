public class Film3D extends Film {
    // atribut
    private String formatLayar;
    private double biayaKacamata;
    private int jumlahKacamataTersedia;

    // konstruktor kosong
    public Film3D(){
    }

    // konstruktor
    public Film3D(String id, String judul, int durasi, String jamTayang, double hargaTiket, String genre, String sutradara, String ratingUsia, 
        String bahasa, int tahunRilis, String formatLayar, double biayaKacamata, int jumlahKacamataTersedia){
        super(id, judul, durasi, jamTayang, hargaTiket, genre, sutradara, ratingUsia, bahasa, tahunRilis); 
        this.formatLayar = formatLayar;
        this.biayaKacamata = biayaKacamata;
        this.jumlahKacamataTersedia = jumlahKacamataTersedia;
    }

    // getter
    public String getFormatLayar(){
        return formatLayar;
    }
    public double getBiayaKacamata(){
        return biayaKacamata;
    }
    public int getJumlahKacamataTersedia(){
        return jumlahKacamataTersedia;
    }

    // setter
    public void setFormatLayar(String formatLayar){
        this.formatLayar = formatLayar;
    }
    public void setBiayaKacamata(double biayaKacamata){
        this.biayaKacamata = biayaKacamata;
    }
    public void setJumlahKacamataTersedia(int jumlahKacamataTersedia){
        this.jumlahKacamataTersedia = jumlahKacamataTersedia;
    }
}