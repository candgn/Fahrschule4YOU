package com.projekt.fahrschule4you;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projekt.fahrschule4you.Web.Api;
import com.projekt.fahrschule4you.Web.RequestHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Profil extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    int kid;
    String ad,soyad,email,tel,baslangic,bitis,kidst,sifre,profil_resim_yolu;
    String evet,hayir,cikmak_istediginize_emin_misiniz;
    TextView adtv,emailtv,teltv,baslangictv,bitistv,sifremidegistir;
    ImageView profilimage;
    ProgressBar progressBar;
    RelativeLayout prgslyt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_language_black_24dp);
        toolbar.setOverflowIcon(drawable);


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
            evet= getString(R.string.evet_tr);
            hayir =getString(R.string.hayir_tr);
            cikmak_istediginize_emin_misiniz=getString(R.string.cikmak_istediginize_emin_misiniz_tr);



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
            evet= getString(R.string.evet_de);
            hayir =getString(R.string.hayir_de);
            cikmak_istediginize_emin_misiniz=getString(R.string.cikmak_istediginize_emin_misiniz_de);

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
            evet= getString(R.string.evet_en);
            hayir =getString(R.string.hayir_en);
            cikmak_istediginize_emin_misiniz=getString(R.string.cikmak_istediginize_emin_misiniz_en);
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        prgslyt = (RelativeLayout)findViewById(R.id.prgs);


        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            kidst = extras.getString("kid");
        }



        adtv = (TextView) findViewById(R.id.isim);
        teltv = (TextView) findViewById(R.id.gsm);
        emailtv = (TextView) findViewById(R.id.eposta);
        baslangictv = (TextView) findViewById(R.id.start_date);
        bitistv = (TextView) findViewById(R.id.end_date);
        profilimage =(ImageView)findViewById(R.id.profile_image);
        sifremidegistir = (TextView)findViewById(R.id.sifremidegistir);



        readHeroes();



        if (dil.equals("tr")){
            sifremidegistir.setText(R.string.ifremi_de_i_tirmek_istiyorum_tr);

        }else if(dil.equals("de")){
            sifremidegistir.setText(R.string.ifremi_de_i_tirmek_istiyorum_de);
        }else if(dil.equals("en")){
            sifremidegistir.setText(R.string.ifremi_de_i_tirmek_istiyorum_en);
        }

    }



    @Override
    public void onBackPressed() {



        AlertDialog.Builder builder = new AlertDialog.Builder(Profil.this);
        builder.setTitle("4YOU Fahrschule");
        builder.setMessage(cikmak_istediginize_emin_misiniz);
        builder.setNegativeButton(hayir, null);
        builder.setPositiveButton(evet, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                SharedPreferences settings = getSharedPreferences("mysettings",
                        Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = settings.edit();
                editor.putString("loggedin", "defaultvalue");
                editor.putString("dil", "defaultvalue");
                editor.commit();
                finish();
                System.exit(0);

            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.tr){

            SharedPreferences settings = getSharedPreferences("mysettings",
                    Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("dil", "tr");
            editor.commit();
            Toast.makeText(getApplicationContext(),"Dili Türkçe olarak seçtiniz!", Toast.LENGTH_LONG).show();
            Intent intent= new Intent (getApplicationContext(),Profil.class);
            intent.putExtra("kid",kidst);
            startActivity(intent);

        }else if (id==R.id.de){
            SharedPreferences settings = getSharedPreferences("mysettings",
                    Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("dil", "de");
            editor.commit();
            Toast.makeText(getApplicationContext(),"Sie haben die Sprache in Deutsch gewählt!", Toast.LENGTH_LONG).show();
            Intent intent= new Intent (getApplicationContext(),Profil.class);
            intent.putExtra("kid",kidst);
            startActivity(intent);

        }else if (id==R.id.en){
            SharedPreferences settings = getSharedPreferences("mysettings",
                    Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("dil", "en");
            editor.commit();
            Toast.makeText(getApplicationContext(),"You have chosen the language in English!", Toast.LENGTH_LONG).show();
            Intent intent= new Intent (getApplicationContext(),Profil.class);
            intent.putExtra("kid",kidst);
            startActivity(intent);
        }



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.test1) {
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
        }else if (id == R.id.istatistikler) {
            Intent intent = new Intent(getApplicationContext(),İstatistik.class);
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

    public void sifremidegistir(View view){
        Intent intent = new Intent(getApplicationContext(),SifremiDegistir.class);
        intent.putExtra("kid",kidst);
        intent.putExtra("sifre",sifre);
        startActivity(intent);

    }

    private void readHeroes() {

        HashMap<String, String> params = new HashMap<>();
        params.put("kullanici_id",kidst);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_KULLANICI, params, CODE_POST_REQUEST);
        request.execute();
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        //when the task started displaying a progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            prgslyt.setVisibility(View.VISIBLE);

        }




        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            prgslyt.setVisibility(View.GONE);

            try {
                JSONObject object = new JSONObject(s);
                System.out.println("aaaaaaaaaaa"+s);
                if (!object.getBoolean("error")) {






                    ad =  object.getString("ad");
                    soyad =  object.getString("soyad");
                    email =  object.getString("email");
                    tel=  object.getString("telefon");
                    baslangic=  object.getString("baslangic_tarihi");
                    bitis=  object.getString("bitis_tarihi");
                    sifre=object.getString("sifre");
                    profil_resim_yolu=object.getString("profil_foto");
                    System.out.println("profil resim yolu: " + profil_resim_yolu);

                    adtv.setText(ad + " " +soyad);
                    emailtv.setText(email);
                    teltv.setText(tel);

                    Picasso.get().load("http://panel.4you-fahrschule.de"+profil_resim_yolu).into(profilimage);


                    SharedPreferences settings = getSharedPreferences("mysettings",
                            Context.MODE_PRIVATE);
                    String dil = settings.getString("dil", "defaultvalue");


                    if (dil.equals("tr")){
                        baslangictv.setText(getString(R.string.baslangic_tarihi_tr)+baslangic);
                        bitistv.setText(getString(R.string.bitis_tarihi_tr)+bitis);

                    }else if(dil.equals("de")){
                        baslangictv.setText(getString(R.string.baslangic_tarihi_de)+baslangic);
                        bitistv.setText(getString(R.string.bitis_tarihi_de)+bitis);
                    }else if(dil.equals("en")){
                        baslangictv.setText(getString(R.string.baslangic_tarihi_en)+baslangic);
                        bitistv.setText(getString(R.string.bitis_tarihi_en)+bitis);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //the network operation will be performed in background
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
