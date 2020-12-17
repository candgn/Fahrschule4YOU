package com.projekt.fahrschule4you.Classes;

public class Kategori {

    private int id;
    private String kategori,sayi,durum;

    public Kategori(int id,String kategori,String sayi,String durum){
        this.id=id;
        this.kategori=kategori;
        this.sayi=sayi;
        this.durum=durum;
    }

    public int getId() {
        return id;
    }

    public String getKategori() {
        return kategori;
    }

    public String getSayi() {
        return sayi;
    }

    public String getDurum() {
        return durum;
    }
}
