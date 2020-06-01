package com.example.exkost.Model;

public class ModelHome {
    String namabarang, idbarang, namajenis, hargabarang, waktulelang;

    public ModelHome(){}

    public ModelHome(String namabarang, String idbarang, String namajenis, String hargabarang, String waktulelang) {
        this.namabarang = namabarang;
        this.idbarang = idbarang;
        this.hargabarang = hargabarang;
        this.namajenis = namajenis;
        this.waktulelang = waktulelang;
    }

    public String getNamaBarang() {
        return namabarang;
    }
    public void setNamaBarang(String namabarang) {
        this.namabarang = namabarang;
    }

    public String getIdBarang() {
        return idbarang;
    }
    public void setIdBarang(String idbarang) {
        this.idbarang = idbarang;
    }

    public String getNamaJenis() {
        return namajenis;
    }
    public void setNamaJenis(String namajenis) { this.namajenis = namajenis; }

    public String getHargaBarang() {
        return hargabarang;
    }
    public void setHargaBarang(String hargabarang) {
        this.hargabarang = hargabarang;
    }

    public String getWaktuLelang() {
        return waktulelang;
    }
    public void setWaktuLelang(String waktulelang) {
        this.waktulelang = waktulelang;
    }
}

