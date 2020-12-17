package com.projekt.fahrschule4you.Classes;

public class EskiTest {

    private String id,dogru,yanlis,toplam;

    public EskiTest(String id, String dogru, String yanlis, String toplam){
        this.id=id;
        this.dogru=dogru;
        this.yanlis=yanlis;
        this.toplam=toplam;

    }

    public String getDogru() {
        return dogru;
    }

    public String getYanlis() {
        return yanlis;
    }

    public String getToplam() {
        return toplam;
    }



    public String getId() {
        return id;
    }
}
