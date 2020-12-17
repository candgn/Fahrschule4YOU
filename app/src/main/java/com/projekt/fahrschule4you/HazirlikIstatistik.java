package com.projekt.fahrschule4you;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projekt.fahrschule4you.Classes.KategoriIstatistik;
import com.projekt.fahrschule4you.Web.Api;
import com.projekt.fahrschule4you.Web.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HazirlikIstatistik extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    ProgressBar progressBar,circle_progress;
    RelativeLayout prgslyt;

    int position;
    String kidst,test_id,kategori,geldigi_yer,gectiniz,kaldiniz,butun_sorulari_cozmediniz;
    ArrayList<String> idlist,idlistkategori;
    Button eski_test;

    List<KategoriIstatistik> heroList;
    ListView listView;

    TextView basari_orani,basari;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazirlik_istatistik);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        prgslyt = (RelativeLayout)findViewById(R.id.prgs);

        eski_test = (Button)findViewById(R.id.button6);

        basari_orani=(TextView)findViewById(R.id.textView17);
        basari=(TextView)findViewById(R.id.basari);


        idlist = new ArrayList<>();
        idlistkategori = new ArrayList<>();


        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            kidst = extras.getString("kid");
            idlist = extras.getStringArrayList("idlist");
            idlistkategori = extras.getStringArrayList("idlistkategori");
            position = extras.getInt("position");
            test_id=extras.getString("test_id");
            kategori=extras.getString("kategori");
            geldigi_yer=extras.getString("geldigi_yer");

        }

        listView = (ListView) findViewById(R.id.listView);

        heroList = new ArrayList<>();
       // readKategoriler();
        if(geldigi_yer.equals("kategori")){
            readKategoriPuan();

        }else if(geldigi_yer.equals("test")){
            readTestPuan();
            System.out.println("testtest");

        }
        //readHeroes();


        eski_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (geldigi_yer.equals("kategori")){
                    Intent intent = new Intent(getApplicationContext(), KategoriSoruEski.class);
                    intent.putExtra("kid", kidst);
                    intent.putExtra("geldigi_yer", "kategori_detay");
                    intent.putExtra("kategori", kategori);
                    intent.putStringArrayListExtra("idlist", idlistkategori);
                    intent.putExtra("position", 0);
                    startActivity(intent);
                }else if (geldigi_yer.equals("test")){
                    Intent intent =new Intent(getApplicationContext(),HazirlikEskiSoru.class);
                    intent.putExtra("kid",kidst);
                    intent.putStringArrayListExtra("idlist",idlist);
                    intent.putExtra("position",0);
                    intent.putExtra("test_id",test_id);
                    startActivity(intent);
                }

            }
        });

        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");

        if (dil.equals("tr")){
            basari_orani.setText(R.string.basari_orani_tr);
            eski_test.setText(R.string.sorular_gozden_gecir_tr);
            gectiniz=getResources().getString(R.string.gectiniz_tr);;
            kaldiniz=getResources().getString(R.string.kaldiniz_tr);;
            butun_sorulari_cozmediniz=getResources().getString(R.string.butun_sorulari_cozmediniz_tr);

        }else if(dil.equals("de")){
            basari_orani.setText(R.string.basari_orani_de);
            eski_test.setText(R.string.sorular_gozden_gecir_de);
            gectiniz=getResources().getString(R.string.gectiniz_de);;
            kaldiniz=getResources().getString(R.string.kaldiniz_de);;
            butun_sorulari_cozmediniz=getResources().getString(R.string.butun_sorulari_cozmediniz_de);
        }else if(dil.equals("en")){
            basari_orani.setText(R.string.basari_orani_en);
            eski_test.setText(R.string.sorular_gozden_gecir_en);
            gectiniz=getResources().getString(R.string.gectiniz_en);;
            kaldiniz=getResources().getString(R.string.kaldiniz_en);;
            butun_sorulari_cozmediniz=getResources().getString(R.string.butun_sorulari_cozmediniz_en);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                super.onBackPressed();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void readHeroes() {


        HashMap<String, String> params = new HashMap<>();
        System.out.println("kullanıcı id" + kidst);
        params.put("test_id",test_id);


        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");

        if(dil.equals("tr")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_TEST_ID_GORE_ISTATISTIK+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_TEST_ID_GORE_ISTATISTIK+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_TEST_ID_GORE_ISTATISTIK+"&dil=en", params, CODE_POST_REQUEST);
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

            System.out.println("JSON object dolu mu " + s);
            try {
                JSONObject object = new JSONObject(s);
                System.out.println("JSON object dolu mu " + s);
                if (!object.getBoolean("error")) {

                    //Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                   // refreshHeroList(object.getJSONObject("basari_orani"));

                    int i= object.getInt("basari_orani");

                    TextView pgtv=(TextView)findViewById(R.id.oran);
                    String oran = String.valueOf(i);
                    pgtv.setText(oran +"%");


                    circle_progress = (ProgressBar) findViewById(R.id.progressBarcircle);
                    circle_progress.setProgress(i);


                    if (i<=30){
                        int color = 0xFFFF0000;
                        circle_progress.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        circle_progress.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    }else if (30<i && i<=65){
                        int color = 0xFFE89623;
                        circle_progress.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        circle_progress.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    }else if (65<i && i<=100){
                        int color = 0xFF1DB512;
                        circle_progress.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        circle_progress.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
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

    private void readKategoriler() {
        HashMap<String, String> params = new HashMap<>();
        System.out.println("kullanıcı id" + kidst);
        params.put("kullanici_id",kidst);
        params.put("test_id",test_id);


        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        if(dil.equals("tr")){
            PerformNetworkRequestk request = new PerformNetworkRequestk(Api.URL_READ_TEST_ISTATISTIK+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequestk request = new PerformNetworkRequestk(Api.URL_READ_TEST_ISTATISTIK+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequestk request = new PerformNetworkRequestk(Api.URL_READ_TEST_ISTATISTIK+"&dil=en", params, CODE_POST_REQUEST);
            request.execute();

        }
    }
    private void refreshHeroList2(JSONArray heroes) throws JSONException {
        //clearing previous heroes
        heroList.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < heroes.length(); i++) {
            //getting each hero object
            JSONObject obj = heroes.getJSONObject(i);

            //adding the hero to the list
            heroList.add(new KategoriIstatistik(
                    obj.getString("katagori_ismi"),
                    obj.getString("soru_sayisi"),
                    obj.getString("cozulen_soru_sayisi"),
                    obj.getString("dogru_soru_sayisi"),
                    obj.getString("yanlis_soru_sayisi"),
                    obj.getString("cozme_orani"),
                    obj.getString("basari_orani")

            ));
        }

        //creating the adapter and setting it to the listview
        HeroAdapter adapter = new HeroAdapter(heroList);
        listView.setAdapter(adapter);
    }

    private class PerformNetworkRequestk extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequestk(String url, HashMap<String, String> params, int requestCode) {
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
                    System.out.println("Hazırlık istatistik kategori"+s);
                    //         Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshHeroList2(object.getJSONArray("katagori_istatistikleri"));
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
        System.out.println("kullanıcı id" + kidst);
        params.put("kullanici_id",kidst);
        params.put("test_id","0");

        params.put("kategori_adı",kategori);




        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        if(dil.equals("tr")){
            PerformNetworkRequest3 request = new PerformNetworkRequest3(Api.URL_READ_PUAN_KATEGORI+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest3 request = new PerformNetworkRequest3(Api.URL_READ_PUAN_KATEGORI+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest3 request = new PerformNetworkRequest3(Api.URL_READ_PUAN_KATEGORI+"&dil=en", params, CODE_POST_REQUEST);
            request.execute();

        }
    }
    private void refreshHeroList3(JSONArray heroes) throws JSONException {
        //clearing previous heroes
        heroList.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        System.out.println("denemeoran"+heroes);
        int basari_hesap=0;
        int soru_sayisi_kontrol=0;
        int yanlis_puan=0;
        for (int i = 0; i < heroes.length(); i++) {
            //getting each hero object
            JSONObject obj = heroes.getJSONObject(i);


            //adding the hero to the list
            System.out.println("puan: "+obj.getString("puan"));
            heroList.add(new KategoriIstatistik(
                    obj.getString("puan"),
                    obj.getString("puana_gore_soru_sayisi"),
                    obj.getString("puana_gore_cozulen_soru_sayisi"),
                    obj.getString("puana_gore_dogru_soru_sayisi"),
                    obj.getString("puana_gore_yanlis_soru_sayisi"),
                    obj.getString("cozme_orani"),
                    obj.getString("basari_orani")

            ));
            System.out.println("i: "+obj.getString("puana_gore_yanlis_soru_sayisi"));
          int hata_siniri=Integer.parseInt(obj.getString("hata_siniri"));
          int puana_gore_yanlis_soru_sayisi=Integer.parseInt(obj.getString("puana_gore_yanlis_soru_sayisi"));
          int puan=Integer.parseInt(obj.getString("puan"));
          yanlis_puan=yanlis_puan+(puan*puana_gore_yanlis_soru_sayisi);
            if (puana_gore_yanlis_soru_sayisi>=hata_siniri){
                basari_hesap++;
            }
            int puana_gore_soru_sayisi=Integer.parseInt(obj.getString("puana_gore_soru_sayisi"));
            int puana_gore_cozulen_soru_sayisi=Integer.parseInt(obj.getString("puana_gore_cozulen_soru_sayisi"));
            if (puana_gore_soru_sayisi!=puana_gore_cozulen_soru_sayisi){
                soru_sayisi_kontrol++;

            }

        }


        if (soru_sayisi_kontrol==0){
            if(basari_hesap>0){


                basari.setText(kaldiniz+yanlis_puan);
                basari.setTextColor(Color.parseColor("#ff0000"));

            }else{
                basari.setText(gectiniz);
                basari.setTextColor(Color.parseColor("#0B6623"));
            }
        }else{
            basari.setText(butun_sorulari_cozmediniz);
        }



        //creating the adapter and setting it to the listview
        HeroAdapter adapter = new HeroAdapter(heroList);
        listView.setAdapter(adapter);
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
                System.out.println("Hazırlık istatistik kategori"+s);
                if (!object.getBoolean("error")) {
                    System.out.println("Hazırlık istatistik kategori"+s);
                    //         Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshHeroList3(object.getJSONArray("puan_sistemi"));
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

    private void readTestPuan() {
        HashMap<String, String> params = new HashMap<>();
        System.out.println("kullanıcı id" + kidst+"test id: "+test_id);
        params.put("kullanici_id",kidst);
        params.put("test_id",test_id);





        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        if(dil.equals("tr")){
            PerformNetworkRequest4 request = new PerformNetworkRequest4(Api.URL_READ_PUAN_TEST+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest4 request = new PerformNetworkRequest4(Api.URL_READ_PUAN_TEST+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest4 request = new PerformNetworkRequest4(Api.URL_READ_PUAN_TEST+"&dil=en", params, CODE_POST_REQUEST);
            request.execute();

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
            System.out.println("Hazırlık istatistik test"+s);
            try {
                JSONObject object = new JSONObject(s);
                System.out.println("Hazırlık istatistik test"+s);
                if (!object.getBoolean("error")) {
                    System.out.println("Hazırlık istatistik test 2"+s);
                    //         Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshHeroList4(object.getJSONArray("puan_sistemi"));
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
        heroList.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        System.out.println("denemeoran"+heroes);
        int basari_hesap=0;
        int soru_sayisi_kontrol=0;
        for (int i = 0; i < heroes.length(); i++) {
            //getting each hero object
            JSONObject obj = heroes.getJSONObject(i);


            //adding the hero to the list
            System.out.println("puan: "+obj.getString("puan"));
            heroList.add(new KategoriIstatistik(
                    obj.getString("puan"),
                    obj.getString("teste_gore_soru_sayisi"),
                    obj.getString("teste_gore_cozulen_soru_sayisi"),
                    obj.getString("teste_gore_dogru_soru_sayisi"),
                    obj.getString("teste_gore_yanlis_soru_sayisi"),
                    obj.getString("cozme_orani"),
                    obj.getString("basari_orani")

            ));
            System.out.println("i: "+obj.getString("teste_gore_yanlis_soru_sayisi"));
            int hata_siniri=Integer.parseInt(obj.getString("hata_siniri"));
            int teste_gore_yanlis_soru_sayisi=Integer.parseInt(obj.getString("teste_gore_yanlis_soru_sayisi"));
            if (teste_gore_yanlis_soru_sayisi>=hata_siniri){
                basari_hesap++;
            }
            int teste_gore_soru_sayisi=Integer.parseInt(obj.getString("teste_gore_soru_sayisi"));
            int teste_gore_cozulen_soru_sayisi=Integer.parseInt(obj.getString("teste_gore_cozulen_soru_sayisi"));
            if (teste_gore_soru_sayisi!=teste_gore_cozulen_soru_sayisi){
                soru_sayisi_kontrol++;

            }

        }

        if (soru_sayisi_kontrol==0){
            if(basari_hesap>0){

                basari.setText(kaldiniz);
                basari.setTextColor(Color.parseColor("#ff0000"));

            }else{
                basari.setText(gectiniz);
                basari.setTextColor(Color.parseColor("#0B6623"));
            }
        }else{
            basari.setText(butun_sorulari_cozmediniz);
        }



        //creating the adapter and setting it to the listview
        HeroAdapter adapter = new HeroAdapter(heroList);
        listView.setAdapter(adapter);
    }

    class HeroAdapter extends ArrayAdapter<KategoriIstatistik> {
        List<KategoriIstatistik> heroList;

        public HeroAdapter(List<KategoriIstatistik> heroList) {
            super(HazirlikIstatistik.this, R.layout.istatistik_kategori_bar_view, heroList);
            this.heroList = heroList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            final View listViewItem = inflater.inflate(R.layout.istatistik_kategori_bar_view, null, true);

            TextView kategoriName = listViewItem.findViewById(R.id.textView20);
            TextView toplam = listViewItem.findViewById(R.id.textView12);
            TextView yanlis = listViewItem.findViewById(R.id.textView18);
            TextView dogru = listViewItem.findViewById(R.id.textView19);
            TextView oran = listViewItem.findViewById(R.id.textView21);
            TextView basarioran = listViewItem.findViewById(R.id.textView212);
            TextView cozulen_soru_baslik = listViewItem.findViewById(R.id.textView25);
            TextView basari_oran_baslik = listViewItem.findViewById(R.id.textView23);
            TextView toplam_soru_baslik = listViewItem.findViewById(R.id.textView12t);
            TextView dogru_baslik = listViewItem.findViewById(R.id.textView12d);
            TextView yanlis_baslik = listViewItem.findViewById(R.id.textView12y);
            ProgressBar progressBar = listViewItem.findViewById(R.id.progressBar);
            ProgressBar progressBarBasari = listViewItem.findViewById(R.id.progressBar2);
            LinearLayout istatistikbar= listViewItem.findViewById(R.id.istatistikLayout);
            final ImageView detay=listViewItem.findViewById(R.id.imageView2);

            Drawable drawTuruncu = getDrawable(R.drawable.progress_yatay_turuncu);
            Drawable draw = getDrawable(R.drawable.prgoress_yatay);

            progressBar.setProgressDrawable(drawTuruncu);
            progressBarBasari.setProgressDrawable(draw);

            SharedPreferences settings = getSharedPreferences("mysettings",
                    Context.MODE_PRIVATE);
            String dil = settings.getString("dil", "defaultvalue");

            final KategoriIstatistik hero = heroList.get(position);
            System.out.println("herooo"+hero.getIsim());


            if (dil.equals("tr")){
                cozulen_soru_baslik.setText(R.string.cozulen_soru_tr);
                basari_oran_baslik.setText(R.string.basari_orani_tr);
                toplam_soru_baslik.setText(R.string.toplam_soru_tr);
                dogru_baslik.setText(R.string.dogru_tr);
                yanlis_baslik.setText(R.string.yanlis_tr);
                kategoriName.setText(hero.getIsim()+" "+getString(R.string.puanliksorular_tr));

            }else if(dil.equals("de")){
                cozulen_soru_baslik.setText(R.string.cozulen_soru_de);
                basari_oran_baslik.setText(R.string.basari_orani_de);
                toplam_soru_baslik.setText(R.string.toplam_soru_de);
                dogru_baslik.setText(R.string.dogru_de);
                yanlis_baslik.setText(R.string.yanlis_de);
                kategoriName.setText(hero.getIsim()+" "+getString(R.string.puanliksorular_de));
            }else if(dil.equals("en")){
                cozulen_soru_baslik.setText(R.string.cozulen_soru_en);
                basari_oran_baslik.setText(R.string.basari_orani_en);
                toplam_soru_baslik.setText(R.string.toplam_soru_en);
                dogru_baslik.setText(R.string.dogru_en);
                yanlis_baslik.setText(R.string.yanlis_en);
                kategoriName.setText(hero.getIsim()+" "+getString(R.string.puanliksorular_en));
            }










            toplam.setText(hero.getToplam());
            yanlis.setText(hero.getDogru());
            dogru.setText(hero.getYanlis());
            oran.setText(hero.getCozme_orani()+"%");
            basarioran.setText(hero.getBasari_orani()+"%");
            System.out.println("oran"+hero.getCozme_orani());
            int x = Integer.parseInt(hero.getCozme_orani());
            int y = Integer.parseInt(hero.getBasari_orani());
            progressBar.setProgress(x);
            progressBarBasari.setProgress(y);


            final int[] kontrol = {0};
            detay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (kontrol[0] ==0){
                        LinearLayout detay_istatistik = listViewItem.findViewById(R.id.detay_istatistik);
                        detay_istatistik.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        detay.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                        kontrol[0] =1;

                    }else if (kontrol[0] ==1){
                        LinearLayout detay_istatistik = listViewItem.findViewById(R.id.detay_istatistik);
                        detay_istatistik.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0));
                        detay.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                        kontrol[0] =0;
                    }
                }

            });






            return listViewItem;
        }
    }
}
