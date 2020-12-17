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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.projekt.fahrschule4you.Classes.Kategori;
import com.projekt.fahrschule4you.Web.Api;
import com.projekt.fahrschule4you.Web.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Kategoriler extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    List<Kategori> heroList;
    ListView listView;
    String kidst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategoriler);
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
            setTitle("Kategoriler");
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
            setTitle("Kategorien");
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
            setTitle("Categories");
            isaretlisorular.setTitle(R.string.title_isaretli_sorular_en);

        }


        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            kidst = extras.getString("kid");
        }

        listView = (ListView) findViewById(R.id.listView);

        heroList = new ArrayList<>();

        readHeroes();

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
        getMenuInflater().inflate(R.menu.kategoriler, menu);
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

        if (id == R.id.bilgilerim) {
            Intent intent = new Intent(getApplicationContext(),Profil.class);
            intent.putExtra("kid",kidst);
            startActivity(intent);
        }else if (id == R.id.istatistikler) {
            Intent intent = new Intent(getApplicationContext(),İstatistik.class);
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

        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        System.out.println("dil"+dil);
        if(dil.equals("tr")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_KATEGORILER+"&dil=tr", null, CODE_GET_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_KATEGORILER+"&dil=de", null, CODE_GET_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_KATEGORILER+"&dil=en", null, CODE_GET_REQUEST);
            request.execute();

        }



    }
    private void refreshHeroList(JSONArray heroes) throws JSONException {
        //clearing previous heroes
        heroList.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < heroes.length(); i++) {
            //getting each hero object
            JSONObject obj = heroes.getJSONObject(i);

            //adding the hero to the list
            heroList.add(new Kategori(
                    obj.getInt("id"),
                    obj.getString("katagori"),
                    obj.getString("sayi"),
                    obj.getString("durum")

            ));
        }

        //creating the adapter and setting it to the listview
        HeroAdapter adapter = new HeroAdapter(heroList);
        listView.setAdapter(adapter);
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

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
           //         Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshHeroList(object.getJSONArray("kategoriler"));
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

    class HeroAdapter extends ArrayAdapter<Kategori> {
        List<Kategori> heroList;

        public HeroAdapter(List<Kategori> heroList) {
            super(Kategoriler.this, R.layout.kategori_view, heroList);
            this.heroList = heroList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.kategori_view, null, true);

            TextView kategoriName = listViewItem.findViewById(R.id.textView10);



            final Kategori hero = heroList.get(position);

            kategoriName.setText(hero.getKategori());

            kategoriName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),KategoriDetay.class);
                    intent.putExtra("kategori",hero.getKategori());
                    intent.putExtra("kid",kidst);
                    startActivity(intent);
                }

            });






            return listViewItem;
        }
    }
}
