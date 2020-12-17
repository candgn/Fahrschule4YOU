package com.projekt.fahrschule4you.Web;

public class Api {




    private static final String ROOT_URL = "http://panel.4you-fahrschule.de/EhliyetApi/v1/Api.php?";

    public static final String URL_READ_LOGIN = ROOT_URL + "apicall=giris_kontrol";
    public static final String URL_READ_KATEGORILER = ROOT_URL + "apicall=getkategori";
    public static final String URL_READ_KULLANICI = ROOT_URL + "apicall=kullanici_bilgi";
    public static final String URL_READ_HAZIRTEST = ROOT_URL + "apicall=test_hazirla_tr";
    public static final String URL_UPDATE_SORUKAYIT = ROOT_URL + "apicall=soru_kayit_tr";
    public static final String URL_UPDATE_TESTKAYIT = ROOT_URL + "apicall=test_kayit_tr";
    public static final String URL_READ_SORU = ROOT_URL + "apicall=getsoru";
    public static final String URL_READ_KATEGORI_SORU = ROOT_URL + "apicall=katagori_hazirla_tr";
    public static final String URL_UPDATE_SIFRE = ROOT_URL + "apicall=sifre_degis";
    public static final String URL_UPDATE_ISTATISTIK_KATEGORI = ROOT_URL + "apicall=katagoriye_gore_istatistik";
    public static final String URL_READ_COZULMUS_SORULAR_TR = ROOT_URL + "apicall=cozulmus_sorular_tr";
    public static final String URL_READ_TEST_ID = ROOT_URL + "apicall=test_id_getir";
    public static final String URL_READ_TEST_ID_GORE_ISTATISTIK = ROOT_URL + "apicall=test_idye_gore_istatistik";
    public static final String URL_READ_TEST_ID_GORE_SORU_IDLERI = ROOT_URL + "apicall=test_idye_gore_sorular";
    public static final String URL_READ_TOPLAM_ISTATISTIK = ROOT_URL + "apicall=toplam_istatistik";
    public static final String URL_READ_TOPLAM_VERILEN_CEVAP = ROOT_URL + "apicall=verilen_cevap";
    public static final String URL_READ_KATEGORI_AYAR= ROOT_URL + "apicall=katagori_soru_ayari";
    public static final String URL_READ_KATEGORI_YANLİS= ROOT_URL + "apicall=katagori_hazirla_yanlis_tr";
    public static final String URL_READ_KATEGORI_ISTATISTIK_SIFIRLA= ROOT_URL + "apicall=katagori_sifirlama_tr";
    public static final String URL_UPDATE_YANLIS_SORU_KAYIT= ROOT_URL + "apicall=yanlis_soru_kayit_tr";
    public static final String URL_READ_TEST_ISTATISTIK= ROOT_URL + "apicall=teste_id_istatistik";
    public static final String URL_UPDATE_MARKIEREN = ROOT_URL + "apicall=soru_isaretle";
    public static final String URL_UPDATE_MARKIEREN_DELETE = ROOT_URL + "apicall=soru_isaret_kaldir";
    public static final String URL_READ_MARKIEREN = ROOT_URL + "apicall=isaretli_sorulari_getir";
    public static final String URL_READ_PUAN_KATEGORI = ROOT_URL + "apicall=puan_ve_kategori";
    public static final String URL_READ_PUAN_TEST = ROOT_URL + "apicall=puan_ve_id";
}
