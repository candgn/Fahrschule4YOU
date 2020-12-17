package com.projekt.fahrschule4you.Classes;

public class KategoriIstatistik {

    String isim,toplam,cozulen,dogru,yanlis,cozme_orani,basari_orani;

    public KategoriIstatistik(String isim,String toplam,String cozulen,String dogru,String yanlis,String cozme_orani,
                    String basari_orani){
        this.isim=isim;
        this.toplam=toplam;
        this.cozulen=cozulen;
        this.dogru=dogru;
        this.yanlis=yanlis;
        this.cozme_orani=cozme_orani;
        this.basari_orani=basari_orani;

    }

    public String getBasari_orani() {
        return basari_orani;
    }

    public String getCozme_orani() {
        return cozme_orani;
    }

    public String getIsim() {
        return isim;
    }

    public String getToplam() {
        return toplam;
    }

    public String getCozulen() {
        return cozulen;
    }

    public String getDogru() {
        return dogru;
    }

    public String getYanlis() {
        return yanlis;
    }
}
