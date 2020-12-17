package com.projekt.fahrschule4you;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projekt.fahrschule4you.Web.Api;
import com.projekt.fahrschule4you.Web.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class KategoriDetay extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    ArrayList<String> idList,idList2,idList3;

    String kategori,kidst;
    TextView kategoritv,cozulen_sayisi,dogru_sayisi,yanlis_sayisi,toplam_soru_sayisi,istatistikleri_sifirla_text,gecme_durumu,gecme_durumu_puan_y;
    Button basla,gozdengecir,yanliscoz,istatistiksifirla,detayli_istatistik;

    ProgressBar progressBar;
    RelativeLayout prgslyt;

    String istatistikler_sifirlandi,bu_kategorideki_butun_sorulari_cozdunuz,cozulmus_soru_bulunmamakta,yanlis_soru_bulunmamakta,gectiniz,kaldiniz,butun_sorulari_cozmediniz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_detay);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        kategoritv=(TextView)findViewById(R.id.textView2);
        cozulen_sayisi=(TextView)findViewById(R.id.textView3);
        dogru_sayisi=(TextView)findViewById(R.id.textView4);
        yanlis_sayisi=(TextView)findViewById(R.id.textView5);
        toplam_soru_sayisi=(TextView)findViewById(R.id.textView6);
        istatistikleri_sifirla_text=(TextView)findViewById(R.id.istatistikleri_sifirla_text);
        gecme_durumu=(TextView)findViewById(R.id.gecme_durumu);
        gecme_durumu_puan_y=(TextView)findViewById(R.id.gecme_durumu_puan_y);
        basla=(Button)findViewById(R.id.button2);
        gozdengecir=(Button)findViewById(R.id.button3);
        yanliscoz=(Button)findViewById(R.id.button4);
        istatistiksifirla=(Button)findViewById(R.id.button5);
        detayli_istatistik=(Button)findViewById(R.id.detayli_istatistik);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        prgslyt = (RelativeLayout)findViewById(R.id.prgs);

        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            kategori = extras.getString("kategori");
            kidst = extras.getString("kid");
        }
        setTitle(kategori);

        kategoritv.setText(kategori);
        idList = new ArrayList<>();
        idList2 = new ArrayList<>();
        idList3 = new ArrayList<>();
        readHeroes();
        istatistik();
        readHeroesCozulmus();
        readHeroesYanlis();
        readKategoriPuan();
        basla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("idlist size"+idList.size());
                System.out.println("idlist"+idList);

                if (idList.size()>0){
                    Intent intent =new Intent(getApplicationContext(),KategoriSoru.class);
                    intent.putExtra("kid",kidst);
                    intent.putExtra("kategori",kategori);
                    intent.putStringArrayListExtra("idlist",idList);
                    intent.putExtra("position",0);
                    startActivity(intent);

                }else if (idList.size()==0){
                    Toast.makeText(getApplicationContext(),bu_kategorideki_butun_sorulari_cozdunuz, Toast.LENGTH_LONG).show();
                }

            }
        });

        gozdengecir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (idList2.size()>0) {
                    System.out.println("Çözülmüş Soruların ID'leri: " + idList2);
                    Intent intent = new Intent(getApplicationContext(), KategoriSoruEski.class);
                    intent.putExtra("kid", kidst);
                    intent.putExtra("geldigi_yer", "kategori_detay");
                    intent.putExtra("kategori", kategori);
                    intent.putStringArrayListExtra("idlist", idList2);
                    intent.putExtra("position", 0);
                    startActivity(intent);
                }else if (idList2.size()==0){
                    Toast.makeText(getApplicationContext(),cozulmus_soru_bulunmamakta, Toast.LENGTH_LONG).show();
                }

            }
        });

        yanliscoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idList3.size()>0) {
                Intent intent =new Intent(getApplicationContext(),KategoriYanlisSorular.class);
                intent.putExtra("kid",kidst);
                intent.putExtra("kategori",kategori);
                intent.putStringArrayListExtra("idlist",idList3);
                intent.putExtra("position",0);
                startActivity(intent);
                }else if (idList3.size()==0){
                    Toast.makeText(getApplicationContext(),yanlis_soru_bulunmamakta, Toast.LENGTH_LONG).show();
                }
            }
        });

        istatistiksifirla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                istatistikSifirla();
                Intent intent = new Intent(getApplicationContext(),KategoriDetay.class);
                intent.putExtra("kategori",kategori);
                intent.putExtra("kid",kidst);
                startActivity(intent);
            }
        });

        istatistikleri_sifirla_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                istatistikSifirla();
                Intent intent = new Intent(getApplicationContext(),KategoriDetay.class);
                intent.putExtra("kategori",kategori);
                intent.putExtra("kid",kidst);
                startActivity(intent);
            }
        });
        detayli_istatistik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HazirlikIstatistik.class);
                intent.putExtra("kategori",kategori);
                intent.putExtra("kid",kidst);
                intent.putExtra("geldigi_yer","kategori");
                intent.putStringArrayListExtra("idlistkategori",idList2);
                startActivity(intent);
            }
        });



        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");

        if (dil.equals("tr")){
            basla.setText(R.string.egitime_basla_tr);
            gozdengecir.setText(R.string.eski_cozdugun_soruları_gozden_gecir_tr);
            yanliscoz.setText(R.string.yanlis_sorular_tekrar_coz_tr);
            istatistikleri_sifirla_text.setText(R.string.istatistikleri_sifirla_tr);
            istatistikler_sifirlandi=getString(R.string.istatistikler_sifirlandi_tr);
            bu_kategorideki_butun_sorulari_cozdunuz=getString(R.string.bu_kategorideki_butun_sorulari_cozdunuz_tr);
            cozulmus_soru_bulunmamakta=getString(R.string.cozulmus_soru_bulunmamakta_tr);
            yanlis_soru_bulunmamakta=getString(R.string.yanlis_soru_bulunmamakta_tr);
            detayli_istatistik.setText(R.string.detayli_istatistik_tr);
            gectiniz= getResources().getString(R.string.gectiniz_tr);
            kaldiniz= getResources().getString(R.string.kaldiniz_tr);
            butun_sorulari_cozmediniz=getResources().getString(R.string.butun_sorulari_cozmediniz_tr);

        }else if(dil.equals("de")){
            basla.setText(R.string.egitime_basla_de);
            gozdengecir.setText(R.string.eski_cozdugun_soruları_gozden_gecir_de);
            yanliscoz.setText(R.string.yanlis_sorular_tekrar_coz_de);
            istatistikleri_sifirla_text.setText(R.string.istatistikleri_sifirla_de);
            istatistikler_sifirlandi=getString(R.string.istatistikler_sifirlandi_de);
            bu_kategorideki_butun_sorulari_cozdunuz=getString(R.string.bu_kategorideki_butun_sorulari_cozdunuz_de);
            cozulmus_soru_bulunmamakta=getString(R.string.cozulmus_soru_bulunmamakta_de);
            yanlis_soru_bulunmamakta=getString(R.string.yanlis_soru_bulunmamakta_de);
            detayli_istatistik.setText(R.string.detayli_istatistik_de);
            gectiniz= getResources().getString(R.string.gectiniz_de);
            kaldiniz= getResources().getString(R.string.kaldiniz_de);
            butun_sorulari_cozmediniz=getResources().getString(R.string.butun_sorulari_cozmediniz_de);
        }else if(dil.equals("en")){
            basla.setText(R.string.egitime_basla_tr);
            gozdengecir.setText(R.string.eski_cozdugun_soruları_gozden_gecir_en);
            yanliscoz.setText(R.string.yanlis_sorular_tekrar_coz_en);
            istatistikleri_sifirla_text.setText(R.string.istatistikleri_sifirla_en);
            istatistikler_sifirlandi=getString(R.string.istatistikler_sifirlandi_en);
            bu_kategorideki_butun_sorulari_cozdunuz=getString(R.string.bu_kategorideki_butun_sorulari_cozdunuz_en);
            cozulmus_soru_bulunmamakta=getString(R.string.cozulmus_soru_bulunmamakta_en);
            yanlis_soru_bulunmamakta=getString(R.string.yanlis_soru_bulunmamakta_en);
            detayli_istatistik.setText(R.string.detayli_istatistik_en);
            gectiniz= getResources().getString(R.string.gectiniz_en);
            kaldiniz= getResources().getString(R.string.kaldiniz_en);
            butun_sorulari_cozmediniz=getResources().getString(R.string.butun_sorulari_cozmediniz_en);
        }



    }

    @Override
    public void onBackPressed() {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(getApplicationContext(),Kategoriler.class);
                intent.putExtra("kid",kidst);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void readHeroes() {


        HashMap<String, String> params = new HashMap<>();
        params.put("katagori_ismi",kategori);
        params.put("kullanici_id",kidst);



        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        if(dil.equals("tr")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_KATEGORI_SORU+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_KATEGORI_SORU+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_KATEGORI_SORU+"&dil=en", params, CODE_POST_REQUEST);
            request.execute();

        }
    }

    private void readHeroesCozulmus() {


        HashMap<String, String> params = new HashMap<>();
        params.put("katagori_ismi",kategori);
        params.put("kullanici_id",kidst);



        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        if(dil.equals("tr")){
            PerformNetworkRequest3 request = new PerformNetworkRequest3(Api.URL_READ_COZULMUS_SORULAR_TR+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest3 request = new PerformNetworkRequest3(Api.URL_READ_COZULMUS_SORULAR_TR+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest3 request = new PerformNetworkRequest3(Api.URL_READ_COZULMUS_SORULAR_TR+"&dil=en", params, CODE_POST_REQUEST);
            request.execute();

        }
    }
    private void readHeroesYanlis() {


        System.out.println("parametreler: " + kategori + " --- "+ kidst);
        HashMap<String, String> params = new HashMap<>();
        params.put("katagori_ismi",kategori);
        params.put("kullanici_id",kidst);



        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        if(dil.equals("tr")){
            PerformNetworkRequest4 request = new PerformNetworkRequest4(Api.URL_READ_KATEGORI_YANLİS+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest4 request = new PerformNetworkRequest4(Api.URL_READ_KATEGORI_YANLİS+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest4 request = new PerformNetworkRequest4(Api.URL_READ_KATEGORI_YANLİS+"&dil=en", params, CODE_POST_REQUEST);
            request.execute();

        }
    }
    private void istatistikSifirla() {


        HashMap<String, String> params = new HashMap<>();
        params.put("katagori_ismi",kategori);
        params.put("kullanici_id",kidst);

        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");

        if(dil.equals("tr")){
            PerformNetworkRequest5 request = new PerformNetworkRequest5(Api.URL_READ_KATEGORI_ISTATISTIK_SIFIRLA+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest5 request = new PerformNetworkRequest5(Api.URL_READ_KATEGORI_ISTATISTIK_SIFIRLA+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest5 request = new PerformNetworkRequest5(Api.URL_READ_KATEGORI_ISTATISTIK_SIFIRLA+"&dil=en", params, CODE_POST_REQUEST);
            request.execute();

        }
    }

    private void istatistik(){
        HashMap<String, String> params = new HashMap<>();
        params.put("katagori_ismi",kategori);
        params.put("kullanici_id",kidst);

        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");

        if(dil.equals("tr")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_UPDATE_ISTATISTIK_KATEGORI+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_UPDATE_ISTATISTIK_KATEGORI+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_UPDATE_ISTATISTIK_KATEGORI+"&dil=en", params, CODE_POST_REQUEST);
            request.execute();

        }

    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            prgslyt.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            prgslyt.setVisibility(View.GONE);


            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    System.out.println("JSON object dolu mu " + s);
                    //Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshHeroList(object.getJSONArray("sorular"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    private void refreshHeroList(JSONArray heroes) throws JSONException {
        //clearing previous heroes
        idList.clear();

        for (int i = 0; i < heroes.length(); i++) {
            //getting each hero object
            JSONArray obj = heroes.getJSONArray(i);

            //adding the hero to the list
            idList.add(obj.get(0).toString());
        }
        System.out.println("JSONarray idlist" + idList);






    }

    private class PerformNetworkRequest2 extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest2(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            System.out.println("sssssssssssss"+s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    //Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();

                    System.out.println("toplaö"+object.getString("katagori_toplam_soru_sayisi"));
                    System.out.println("dsafsafa");



                    SharedPreferences settings = getSharedPreferences("mysettings",
                            Context.MODE_PRIVATE);
                    String dil = settings.getString("dil", "defaultvalue");
                    gecme_durumu_puan_y.setText(object.getString("toplam_yanlis_puan"));

                    if (dil.equals("tr")){
                        cozulen_sayisi.setText(getString(R.string.toplam_cozulen_soru_tr) + object.getString("cozulen_soru_sayisi"));
                        dogru_sayisi.setText(getString(R.string.dogru_tr) + object.getString("dogru_soru_sayisi"));
                        yanlis_sayisi.setText(getString(R.string.yanlis_tr )+ object.getString("yanlis_soru_sayisi"));
                        toplam_soru_sayisi.setText(getString(R.string.toplam_soru_tr) + object.getString("katagori_toplam_soru_sayisi"));




                    }else if(dil.equals("de")){
                        cozulen_sayisi.setText(getString(R.string.toplam_cozulen_soru_de) + object.getString("cozulen_soru_sayisi"));
                        dogru_sayisi.setText(getString(R.string.dogru_de) + object.getString("dogru_soru_sayisi"));
                        yanlis_sayisi.setText(getString(R.string.yanlis_de) + object.getString("yanlis_soru_sayisi"));
                        toplam_soru_sayisi.setText(getString(R.string.toplam_soru_de) + object.getString("katagori_toplam_soru_sayisi"));


                    }else if(dil.equals("en")){
                        cozulen_sayisi.setText(getString(R.string.toplam_cozulen_soru_en) + object.getString("cozulen_soru_sayisi"));
                        dogru_sayisi.setText(getString(R.string.dogru_en) + object.getString("dogru_soru_sayisi"));
                        yanlis_sayisi.setText(getString(R.string.yanlis_en) + object.getString("yanlis_soru_sayisi"));
                        toplam_soru_sayisi.setText(getString(R.string.toplam_soru_en) + object.getString("katagori_toplam_soru_sayisi"));


                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    private class PerformNetworkRequest3 extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest3(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    //Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshHeroList3(object.getJSONArray("sorular"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    private void refreshHeroList3(JSONArray heroes) throws JSONException {
        //clearing previous heroes
        idList2.clear();

        for (int i = 0; i < heroes.length(); i++) {
            //getting each hero object
            JSONArray obj = heroes.getJSONArray(i);

            //adding the hero to the list
            idList2.add(obj.get(0).toString());
        }






    }

    private class PerformNetworkRequest4 extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest4(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

                System.out.println("sssssss"+s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    //Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshHeroList4(object.getJSONArray("sorular"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    private void refreshHeroList4(JSONArray heroes) throws JSONException {
        //clearing previous heroes
        idList3.clear();

        for (int i = 0; i < heroes.length(); i++) {
            //getting each hero object
            JSONArray obj = heroes.getJSONArray(i);

            //adding the hero to the list
            idList3.add(obj.get(0).toString());
        }
        System.out.println("yanlışlar"+idList3);






    }

    private class PerformNetworkRequest5 extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest5(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), istatistikler_sifirlandi, Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    private void readKategoriPuan() {
        HashMap<String, String> params = new HashMap<>();
        System.out.println("kullanıcı id" + kidst+"kategori_adı: "+kategori);

        params.put("kullanici_id",kidst);
        params.put("kategori_adı",kategori);
        params.put("test_id","0");



        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        if(dil.equals("tr")){
            KategoriDetay.PerformNetworkRequestKategoriPuan request = new KategoriDetay.PerformNetworkRequestKategoriPuan(Api.URL_READ_PUAN_KATEGORI+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            KategoriDetay.PerformNetworkRequestKategoriPuan request = new KategoriDetay.PerformNetworkRequestKategoriPuan(Api.URL_READ_PUAN_KATEGORI+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            KategoriDetay.PerformNetworkRequestKategoriPuan request = new KategoriDetay.PerformNetworkRequestKategoriPuan(Api.URL_READ_PUAN_KATEGORI+"&dil=en", params, CODE_POST_REQUEST);
            request.execute();

        }
    }
    private void refreshHeroListKategoriPuan(JSONArray heroes) throws JSONException {



        //traversing through all the items in the json array
        //the json we got from the response
        System.out.println("denemekategoripuan"+heroes);
        int basari_hesap=0;
        int soru_sayisi_kontrol=0;
        for (int i = 0; i < heroes.length(); i++) {
            //getting each hero object
            JSONObject obj = heroes.getJSONObject(i);


            //adding the hero to the list
            System.out.println("puan: "+obj.getString("puan"));

            int hata_siniri=Integer.parseInt(obj.getString("hata_siniri"));
            int puana_gore_yanlis_soru_sayisi=Integer.parseInt(obj.getString("puana_gore_yanlis_soru_sayisi"));
            if (puana_gore_yanlis_soru_sayisi>=hata_siniri){
                basari_hesap++;
                System.out.println("kaldı");
            }
            int puana_gore_soru_sayisi=Integer.parseInt(obj.getString("puana_gore_soru_sayisi"));
            int puana_gore_cozulen_soru_sayisi=Integer.parseInt(obj.getString("puana_gore_cozulen_soru_sayisi"));
            System.out.println("puana_gore_soru_sayisi"+puana_gore_soru_sayisi+"puana_gore_cozulen_soru_sayisi"+puana_gore_cozulen_soru_sayisi);
            if (puana_gore_soru_sayisi!=puana_gore_cozulen_soru_sayisi){
                soru_sayisi_kontrol++;

            }

        }

        if (soru_sayisi_kontrol==0){
            if(basari_hesap>0){

                gecme_durumu.setText(kaldiniz);
                gecme_durumu.setTextColor(Color.parseColor("#ff0000"));
                String x = gecme_durumu_puan_y.getText().toString();
                gecme_durumu_puan_y.setText(x+"");

                SharedPreferences settings = getSharedPreferences("mysettings",
                        Context.MODE_PRIVATE);
                String dil = settings.getString("dil", "defaultvalue");


                if (dil.equals("tr")){

                    gecme_durumu_puan_y.setText(x+" Puan yanlış yaptığınızdan dolayı kaldınız!");


                }else if(dil.equals("de")){
                    gecme_durumu_puan_y.setText("Erfolglos wegen der "+x+" Punkte fehler!");


                }else if(dil.equals("en")){

                   gecme_durumu_puan_y.setText("You are unsuccessfull because of "+x+" points fail!");

                }




            }else{
                gecme_durumu.setText(gectiniz);
                gecme_durumu.setTextColor(Color.parseColor("#0B6623"));
                String x = gecme_durumu_puan_y.getText().toString();
                SharedPreferences settings = getSharedPreferences("mysettings",
                        Context.MODE_PRIVATE);
                String dil = settings.getString("dil", "defaultvalue");
                if (dil.equals("tr")){

                    gecme_durumu_puan_y.setText(x+" Puan yanlış yaptınız!");


                }else if(dil.equals("de")){
                    gecme_durumu_puan_y.setText(x+" Punkte fehler!");


                }else if(dil.equals("en")){

                    gecme_durumu_puan_y.setText(x+" points fail!");

                }
            }
        }else{
            gecme_durumu.setText(butun_sorulari_cozmediniz);
        }




    }
    private class PerformNetworkRequestKategoriPuan extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequestKategoriPuan(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(s);
                System.out.println("Hazırlık istatistik kategori"+s);
                if (!object.getBoolean("error")) {
                    System.out.println("Hazırlık istatistik kategori"+s);
                    //         Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshHeroListKategoriPuan(object.getJSONArray("puan_sistemi"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }
}
