from Film import Film

class Film3D(Film):
    def __init__(self, id, judul, durasi, jamTayang, hargaTiket, 
                 genre, sutradara, ratingUsia, bahasa, tahunRilis, 
                 formatLayar, biayaKacamata, jumlahKacamataTersedia):
        super().__init__(id, judul, durasi, jamTayang, hargaTiket, 
                         genre, sutradara, ratingUsia, bahasa, tahunRilis)
        self.formatLayar = formatLayar
        self.biayaKacamata = biayaKacamata
        self.jumlahKacamataTersedia = jumlahKacamataTersedia

    # getter
    def get_formatLayar(self):
        return self.formatLayar
    def get_biayaKacamata(self):
        return self.biayaKacamata
    def get_jumlahKacamataTersedia(self):
        return self.jumlahKacamataTersedia

    # setter
    def set_formatLayar(self, formatLayar):
        self.formatLayar = formatLayar
    def set_biayaKacamata(self, biayaKacamata):
        self.biayaKacamata = biayaKacamata
    def set_jumlahKacamataTersedia(self, jumlahKacamataTersedia):
        self.jumlahKacamataTersedia = jumlahKacamataTersedia