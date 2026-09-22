public class Tayangan {
    // atribut
    private String id;
    private String judul;
    private int durasi;
    private String jamTayang;
    private double hargaTiket;

    // konstruktor kosong
    public Tayangan (){
    }

    // konstruktor
    public Tayangan (String id, String judul, int durasi, String jamTayang, double hargaTiket){
        this.id = id;
        this.judul = judul;
        this.durasi = durasi;
        this.jamTayang = jamTayang;
        this.hargaTiket = hargaTiket;
    }

    // getter
    public String getId(){
        return id;
    }
    public String getJudul(){
        return judul;
    }
    public int getDurasi(){
        return durasi;
    }
    public String getJamTayang(){
        return jamTayang;
    }
    public double getHargaTiket(){
        return hargaTiket;
    }

    // setter
    public void setId(String id){
        this.id = id;
    }
    public void setJudul(String judul){
        this.judul = judul;
    }
    public void setDurasi(int durasi){
        this.durasi = durasi;
    }
    public void setJamTayang(String jamTayang){
        this.jamTayang = jamTayang;
    }
    public void setHargaTiket(double hargaTiket){
        this.hargaTiket = hargaTiket;
    }
}