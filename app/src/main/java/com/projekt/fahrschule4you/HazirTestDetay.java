package com.projekt.fahrschule4you;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

public class HazirTestDetay extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    ArrayList<Integer> heroList;
    ArrayList<String>test_id_eski;
    Button basla,gozdengecir;
    String kidst,testid;
    List<Kategori> dagilim;
    ListView listView;

    ProgressBar progressBar;
    RelativeLayout prgslyt;

    TextView sinav_icerigi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazir_test_detay);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        basla=(Button)findViewById(R.id.button2);
        //gozdengecir=(Button)findViewById(R.id.button3);
        listView = (ListView) findViewById(R.id.listView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        prgslyt = (RelativeLayout)findViewById(R.id.prgs);

        sinav_icerigi=(TextView)findViewById(R.id.textView2);


        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            kidst = extras.getString("kid");
        }

        heroList = new ArrayList<>();
        test_id_eski = new ArrayList<>();
        dagilim = new ArrayList<>();
        readHeroes();
        readKategoriDagilim();



        basla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),HazirlikSoru.class);
                intent.putIntegerArrayListExtra("idlist",heroList);
                intent.putExtra("position",0);
                intent.putExtra("kid",kidst);
                intent.putExtra("test_id",testid);
                startActivity(intent);

            }
        });

        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");

        if (dil.equals("tr")){
            sinav_icerigi.setText(R.string.sinav_icerigi_tr);
            basla.setText(R.string.hemen_coz_tr);

        }else if(dil.equals("de")){
            sinav_icerigi.setText(R.string.sinav_icerigi_de);
            basla.setText(R.string.hemen_coz_de);
        }else if(dil.equals("en")){
            sinav_icerigi.setText(R.string.sinav_icerigi_en);
            basla.setText(R.string.hemen_coz_en);
        }
/*
        gozdengecir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EskiTestler.class);
                intent.putStringArrayListExtra("test_idler_eski",test_id_eski);
                intent.putExtra("position",0);
                intent.putExtra("kid",kidst);
                startActivity(intent);

            }
        });
*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(getApplicationContext(),Hazirlik.class);
                intent.putExtra("kid",kidst);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
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
            heroList.add(obj.getInt("id"));
            testid=obj.getString("test_id");


        }
        System.out.println("testtesttest"+testid);





    }

    private void readHeroes() {
        HashMap<String, String> params = new HashMap<>();
        params.put("kullanici_id",kidst);
        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");

        if(dil.equals("tr")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_HAZIRTEST+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_HAZIRTEST+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_HAZIRTEST+"&dil=en", params, CODE_POST_REQUEST);
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
                    //Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshHeroList(object.getJSONArray("heroes"));


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


    private void readKategoriDagilim() {

        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        if(dil.equals("tr")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_READ_KATEGORI_AYAR+"&dil=tr", null, CODE_GET_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_READ_KATEGORI_AYAR+"&dil=de", null, CODE_GET_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_READ_KATEGORI_AYAR+"&dil=en", null, CODE_GET_REQUEST);
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

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {
                JSONObject object = new JSONObject(s);

                if (!object.getBoolean("error")) {
                    //Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshHeroList2(object.getJSONArray("katagori_istatistik"));



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
        dagilim.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < heroes.length(); i++) {
            //getting each hero object
          JSONArray obj = heroes.getJSONArray(i);

            //adding the hero to the list
           // heroList.add(obj.getInt("id"));

            dagilim.add(new Kategori(
                    Integer.parseInt(obj.get(0).toString()),
                    obj.get(1).toString(),
                    obj.get(2).toString(),
                    obj.get(3).toString()

            ));
            System.out.println("aaaaaaaaaaaa"+obj.get(0));


        }

        //creating the adapter and setting it to the listview
        HeroAdapter adapter = new HeroAdapter(dagilim);
        listView.setAdapter(adapter);






    }

    class HeroAdapter extends ArrayAdapter<Kategori> {
        List<Kategori> dagilim;

        public HeroAdapter(List<Kategori> heroList) {
            super(HazirTestDetay.this, R.layout.dagilim_view, heroList);
            this.dagilim = heroList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.dagilim_view, null, true);

            TextView kategoriName = listViewItem.findViewById(R.id.textView22);



            final Kategori hero = dagilim.get(position);

            kategoriName.setText(hero.getKategori()+":"+hero.getSayi());







            return listViewItem;
        }
    }
}
