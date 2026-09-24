from Tayangan import Tayangan

class Film(Tayangan):
    def __init__(self, id, judul, durasi, jamTayang, hargaTiket, 
                 genre, sutradara, ratingUsia, bahasa, tahunRilis):
        super().__init__(id, judul, durasi, jamTayang, hargaTiket)
        self.genre = genre
        self.sutradara = sutradara
        self.ratingUsia = ratingUsia
        self.bahasa = bahasa
        self.tahunRilis = tahunRilis

    # getter
    def get_genre(self):
        return self.genre
    def get_sutradara(self):
        return self.sutradara
    def get_ratingUsia(self):
        return self.ratingUsia
    def get_bahasa(self):
        return self.bahasa
    def get_tahunRilis(self):
        return self.tahunRilis

    # setter
    def set_genre(self, genre):
        self.genre = genre
    def set_sutradara(self, sutradara):
        self.sutradara = sutradara
    def set_ratingUsia(self, ratingUsia):
        self.ratingUsia = ratingUsia
    def set_bahasa(self, bahasa):
        self.bahasa = bahasa
    def set_tahunRilis(self, tahunRilis):
        self.tahunRilis = tahunRilis