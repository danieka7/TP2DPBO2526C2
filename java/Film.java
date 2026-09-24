public class Film extends Tayangan {
    // atribut
    private String genre;
    private String sutradara;
    private String ratingUsia;
    private String bahasa;
    private int tahunRilis;


    // konstruktor kosong
    public Film(){
    }

    // konstruktor
    public Film(String id, String judul, int durasi, String jamTayang, double hargaTiket, 
        String genre, String sutradara, String ratingUsia, String bahasa, int tahunRilis){
        super(id, judul, durasi, jamTayang, hargaTiket); 
        this.genre = genre;
        this.sutradara = sutradara;
        this.ratingUsia = ratingUsia;
        this.bahasa = bahasa;
        this.tahunRilis = tahunRilis;
    }

    // getter
    public String getGenre(){
        return genre;
    }
    public String getSutradara(){
        return sutradara;
    }
    public String getRatingUsia(){
        return ratingUsia;
    }
    public String getBahasa(){
        return bahasa;
    }
    public int getTahunRilis(){
        return tahunRilis;
    }

    // setter
    public void setGenre(String genre){
        this.genre = genre;
    }
    public void setSutradara(String sutradara){
        this.sutradara = sutradara;
    }
    public void setRatingUsia(String ratingUsia){
        this.ratingUsia = ratingUsia;
    }
    public void setBahasa(String bahasa){
        this.bahasa = bahasa;
    }
    public void setTahunRilis(int tahunRilis){
        this.tahunRilis = tahunRilis;
    }
}