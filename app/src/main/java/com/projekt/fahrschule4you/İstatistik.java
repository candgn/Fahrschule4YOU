package com.projekt.fahrschule4you;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class İstatistik extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    ProgressBar circle_progress;
    String kidst;

    List<KategoriIstatistik> heroList;
    ListView listView;

    ProgressBar progressBar;
    RelativeLayout prgslyt;

    TextView toplam_cozulen_soru;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istatistik);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // get menu from navigationView
        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change
        MenuItem profil = menu.findItem(R.id.profil);
        MenuItem bilgilerim = menu.findItem(R.id.bilgilerim);
        MenuItem istatistikler = menu.findItem(R.id.istatistikler);
        MenuItem kendinidene = menu.findItem(R.id.dene);
        MenuItem kategoriler = menu.findItem(R.id.test1);
        MenuItem sınav = menu.findItem(R.id.test2);
        MenuItem iletişim = menu.findItem(R.id.iletisim);
        MenuItem çıkış = menu.findItem(R.id.cikis);
        MenuItem isaretlisorular = menu.findItem(R.id.test3);

        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");

        if (dil.equals("tr")){
            profil.setTitle(R.string.profil_tr);
            bilgilerim.setTitle(R.string.bilgilerim_tr);
            istatistikler.setTitle(R.string.istatistikler_tr);
            kendinidene.setTitle(R.string.kendinizi_deneyin_tr);
            kategoriler.setTitle(R.string.kategori_tr);
            sınav.setTitle(R.string.sinav_tr);
            iletişim.setTitle(R.string.letisim_tr);
            çıkış.setTitle(R.string.cikis_yap_tr);
            isaretlisorular.setTitle(R.string.title_isaretli_sorular_tr);



        }else if(dil.equals("de")){
            profil.setTitle(R.string.profil_de);
            bilgilerim.setTitle(R.string.bilgilerim_de);
            istatistikler.setTitle(R.string.istatistikler_de);
            kendinidene.setTitle(R.string.kendinizi_deneyin_de);
            kategoriler.setTitle(R.string.kategori_de);
            sınav.setTitle(R.string.sinav_de);
            iletişim.setTitle(R.string.letisim_de);
            çıkış.setTitle(R.string.cikis_yap_de);
            isaretlisorular.setTitle(R.string.title_isaretli_sorular_de);
        }else if(dil.equals("en")){
            profil.setTitle(R.string.profil_en);
            bilgilerim.setTitle(R.string.bilgilerim_en);
            istatistikler.setTitle(R.string.istatistikler_en);
            kendinidene.setTitle(R.string.kendinizi_deneyin_en);
            kategoriler.setTitle(R.string.kategori_en);
            sınav.setTitle(R.string.sinav_en);
            iletişim.setTitle(R.string.letisim_en);
            çıkış.setTitle(R.string.cikis_yap_en);
            isaretlisorular.setTitle(R.string.title_isaretli_sorular_en);
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        prgslyt = (RelativeLayout)findViewById(R.id.prgs);

        toplam_cozulen_soru=(TextView)findViewById(R.id.textView17);

        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            kidst = extras.getString("kid");
        }

        readHeroes();

        listView = (ListView) findViewById(R.id.listView);

        heroList = new ArrayList<>();
        readKategoriler();



        if (dil.equals("tr")){
            toplam_cozulen_soru.setText(R.string.toplam_cozulen_soru_tr);

        }else if(dil.equals("de")){
            toplam_cozulen_soru.setText(R.string.toplam_cozulen_soru_de);
        }else if(dil.equals("en")){
            toplam_cozulen_soru.setText(R.string.toplam_cozulen_soru_en);
        }





    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(getApplicationContext(),Profil.class);
            intent.putExtra("kid",kidst);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.istatistik, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.bilgilerim){
            Intent intent = new Intent(getApplicationContext(),Profil.class);
            intent.putExtra("kid",kidst);
            startActivity(intent);
        }
        else if (id == R.id.test1) {
            Intent intent = new Intent(getApplicationContext(),Kategoriler.class);
            intent.putExtra("kid",kidst);
            startActivity(intent);
        }else if (id == R.id.test2) {
            Intent intent = new Intent(getApplicationContext(),Hazirlik.class);
            intent.putExtra("kid",kidst);
            startActivity(intent);
        }else if (id == R.id.test3) {
            Intent intent = new Intent(getApplicationContext(),IsaretliSorular.class);
            intent.putExtra("kid",kidst);
            startActivity(intent);
        }else if (id == R.id.iletisim) {
            Intent intent = new Intent(getApplicationContext(),İletisim.class);
            intent.putExtra("kid",kidst);
            startActivity(intent);
        }else if (id == R.id.cikis) {

            SharedPreferences settings = getSharedPreferences("mysettings",
                    Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("loggedin", "defaultvalue");
            editor.commit();
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void readHeroes() {


        HashMap<String, String> params = new HashMap<>();
        System.out.println("kullanıcı id" + kidst);
        params.put("kullanici_id",kidst);


        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        if(dil.equals("tr")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_TOPLAM_ISTATISTIK+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_TOPLAM_ISTATISTIK+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_TOPLAM_ISTATISTIK+"&dil=en", params, CODE_POST_REQUEST);
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
                    refreshHeroList(object.getJSONObject("toplam_istatistik"));
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

    private void refreshHeroList(JSONObject heroes) throws JSONException {



        int i= heroes.getInt("cozme_orani");

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

    private void readKategoriler() {
        HashMap<String, String> params = new HashMap<>();
        System.out.println("kullanıcı id" + kidst);
        params.put("kullanici_id",kidst);


        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");

        if(dil.equals("tr")){
            PerformNetworkRequestk request = new PerformNetworkRequestk(Api.URL_READ_TOPLAM_ISTATISTIK+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequestk request = new PerformNetworkRequestk(Api.URL_READ_TOPLAM_ISTATISTIK+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequestk request = new PerformNetworkRequestk(Api.URL_READ_TOPLAM_ISTATISTIK+"&dil=en", params, CODE_POST_REQUEST);
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

    private void readPuanKategori() {


        HashMap<String, String> params = new HashMap<>();
        System.out.println("kullanıcı id" + kidst);
        params.put("kullanici_id",kidst);
        params.put("kullanici_id",kidst);


        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        if(dil.equals("tr")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_PUAN_KATEGORI+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_PUAN_KATEGORI+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_PUAN_KATEGORI+"&dil=en", params, CODE_POST_REQUEST);
            request.execute();

        }
    }



    class HeroAdapter extends ArrayAdapter<KategoriIstatistik> {
        List<KategoriIstatistik> heroList;

        public HeroAdapter(List<KategoriIstatistik> heroList) {
            super(İstatistik.this, R.layout.istatistik_kategori_bar_view, heroList);
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


            if (dil.equals("tr")){
                cozulen_soru_baslik.setText(R.string.cozulen_soru_tr);
                basari_oran_baslik.setText(R.string.basari_orani_tr);
                toplam_soru_baslik.setText(R.string.toplam_soru_tr);
                dogru_baslik.setText(R.string.dogru_tr);
                yanlis_baslik.setText(R.string.yanlis_tr);
                setTitle("İstatistikler");

            }else if(dil.equals("de")){
                cozulen_soru_baslik.setText(R.string.cozulen_soru_de);
                basari_oran_baslik.setText(R.string.basari_orani_de);
                toplam_soru_baslik.setText(R.string.toplam_soru_de);
                dogru_baslik.setText(R.string.dogru_de);
                yanlis_baslik.setText(R.string.yanlis_de);
                setTitle("Statistiken");
            }else if(dil.equals("en")){
                cozulen_soru_baslik.setText(R.string.cozulen_soru_en);
                basari_oran_baslik.setText(R.string.basari_orani_en);
                toplam_soru_baslik.setText(R.string.toplam_soru_en);
                dogru_baslik.setText(R.string.dogru_en);
                yanlis_baslik.setText(R.string.yanlis_en);
                setTitle("Statistics");

            }







            final KategoriIstatistik hero = heroList.get(position);

            kategoriName.setText(hero.getIsim());
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
