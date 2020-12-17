package com.projekt.fahrschule4you;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

public class Hazirlik extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    ArrayList<Integer> heroList;
    ArrayList<String>test_id_eski;
    Button basla,gozdengecir;
    String kidst,testid;

    ProgressBar progressBar;
    RelativeLayout prgslyt;

    String gozden_gecirilecek_soru_bulunmamakta;
    TextView sinav;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazirlik);
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
        TextView text1= (TextView)findViewById(R.id.textView3);
        TextView text2= (TextView)findViewById(R.id.textView24);
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
            gozden_gecirilecek_soru_bulunmamakta=getString(R.string.gozden_gecirilecek_soru_bulunmamakta_tr);
            setTitle("Sınav");
            text1.setText(getString(R.string.bu_b_l_mde_kursumuz_taraf_ndan_haz_rlanan_test_ile_kendinizi_geli_tirebilirsiniz_tr));
            text2.setText(getString(R.string.e_itime_ba_la_butonuna_t_klayarak_sizin_i_in_haz_rlad_m_z_testin_detaylar_n_g_rebilir_ve_soru_zmeye_ba_layabilirsiniz_tr));
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
            gozden_gecirilecek_soru_bulunmamakta=getString(R.string.gozden_gecirilecek_soru_bulunmamakta_de);
            setTitle("Prüfung");
            text1.setText(getString(R.string.bu_b_l_mde_kursumuz_taraf_ndan_haz_rlanan_test_ile_kendinizi_geli_tirebilirsiniz_de));
            text2.setText(getString(R.string.e_itime_ba_la_butonuna_t_klayarak_sizin_i_in_haz_rlad_m_z_testin_detaylar_n_g_rebilir_ve_soru_zmeye_ba_layabilirsiniz_de));
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
            gozden_gecirilecek_soru_bulunmamakta=getString(R.string.gozden_gecirilecek_soru_bulunmamakta_en);
            setTitle("Exam");
            text1.setText(getString(R.string.bu_b_l_mde_kursumuz_taraf_ndan_haz_rlanan_test_ile_kendinizi_geli_tirebilirsiniz_en));
            text2.setText(getString(R.string.e_itime_ba_la_butonuna_t_klayarak_sizin_i_in_haz_rlad_m_z_testin_detaylar_n_g_rebilir_ve_soru_zmeye_ba_layabilirsiniz_en));
            isaretlisorular.setTitle(R.string.title_isaretli_sorular_en);
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        prgslyt = (RelativeLayout)findViewById(R.id.prgs);


        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            kidst = extras.getString("kid");
        }

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        basla=(Button)findViewById(R.id.button2);
        gozdengecir=(Button)findViewById(R.id.button3);
        sinav=(TextView)findViewById(R.id.textView2);






        heroList = new ArrayList<>();
        test_id_eski = new ArrayList<>();

        readEskiTestID();


        basla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),HazirTestDetay.class);
                intent.putExtra("kid",kidst);
                startActivity(intent);

            }
        });

        gozdengecir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (test_id_eski.size()==0){
                    Toast.makeText(getApplicationContext(),gozden_gecirilecek_soru_bulunmamakta, Toast.LENGTH_LONG).show();
                }else{

                    Intent intent = new Intent(getApplicationContext(),EskiTestler.class);
                    intent.putStringArrayListExtra("test_idler_eski",test_id_eski);
                    intent.putExtra("position",0);
                    intent.putExtra("kid",kidst);
                    startActivity(intent);
                }


            }
        });




        if (dil.equals("tr")){
            basla.setText(R.string.egitime_basla_tr);
            gozdengecir.setText(R.string.eski_cozdugun_testleri_gozden_gecir_tr);
            sinav.setText(R.string.sinav_tr);

        }else if(dil.equals("de")){
            basla.setText(R.string.egitime_basla_de);
            gozdengecir.setText(R.string.eski_cozdugun_testleri_gozden_gecir_de);
            sinav.setText(R.string.sinav_de);
        }else if(dil.equals("en")){
            basla.setText(R.string.egitime_basla_en);
            gozdengecir.setText(R.string.eski_cozdugun_testleri_gozden_gecir_en);
            sinav.setText(R.string.sinav_en);
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hazirlik, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.bilgilerim) {
            Intent intent = new Intent(getApplicationContext(),Profil.class);
            intent.putExtra("kid",kidst);
            startActivity(intent);
        }else if (id == R.id.istatistikler) {
            Intent intent = new Intent(getApplicationContext(),İstatistik.class);
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
   /* @Override
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


    }*/





    private void readEskiTestID() {
        HashMap<String, String> params = new HashMap<>();
        params.put("kullanici_id",kidst);


        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        if(dil.equals("tr")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_READ_TEST_ID+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();
            System.out.println("kontrol kontrol" +kidst);

        }else if(dil.equals("de")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_READ_TEST_ID+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_READ_TEST_ID+"&dil=en", params, CODE_POST_REQUEST);
            request.execute();

        }
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
            progressBar.setVisibility(View.VISIBLE);
            prgslyt.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            prgslyt.setVisibility(View.GONE);

            System.out.println("Eski test id sss: " + s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {


                    refreshHeroList2(object.getJSONArray("test_id"));



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

    private void refreshHeroList2(JSONArray heroes) throws JSONException {
        //clearing previous heroes
        test_id_eski.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < heroes.length(); i++) {
            //getting each hero objectobj


            //adding the hero to the list


            test_id_eski.add(heroes.getString(i));


        }









    }
}
