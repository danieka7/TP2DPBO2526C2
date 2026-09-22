public class Film extends Tayangan {
    // atribut
    private String genre;
    private String sutradara;
    private String ratingUsia;

    // konstruktor kosong
    public Film(){
    }

    // konstruktor
    public Film(String id, String judul, int durasi, String jamTayang, double hargaTiket, String genre, String sutradara, String ratingUsia){
        super(id, judul, durasi, jamTayang, hargaTiket); 
        this.genre = genre;
        this.sutradara = sutradara;
        this.ratingUsia = ratingUsia;
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
}