class Tayangan:
    def __init__(self, id, judul, durasi, jamTayang, hargaTiket):
        self.id = id
        self.judul = judul
        self.durasi = durasi
        self.jamTayang = jamTayang
        self.hargaTiket = hargaTiket

    # getter
    def get_id(self):
        return self.id
    def get_judul(self):
        return self.judul
    def get_durasi(self):
        return self.durasi
    def get_jamTayang(self):
        return self.jamTayang
    def get_hargaTiket(self):
        return self.hargaTiket

    # setter
    def set_id(self, id):
        self.id = id
    def set_judul(self, judul):
        self.judul = judul
    def set_durasi(self, durasi):
        self.durasi = durasi
    def set_jamTayang(self, jamTayang):
        self.jamTayang = jamTayang
    def set_hargaTiket(self, hargaTiket):
        self.hargaTiket = hargaTiket