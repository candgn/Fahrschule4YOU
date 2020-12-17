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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.projekt.fahrschule4you.Classes.EskiTest;
import com.projekt.fahrschule4you.Web.Api;
import com.projekt.fahrschule4you.Web.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EskiTestler extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    List<EskiTest> heroList;
    ListView listView;
    String kidst;
    ArrayList<String> test_idler;
    ArrayList<String> soru_id_list;

    String dogrus,yanliss,toplams,tid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eski_testler);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        test_idler = new ArrayList<>();
        heroList = new ArrayList<>();
        soru_id_list = new ArrayList<>();


        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            kidst = extras.getString("kid");
            test_idler = extras.getStringArrayList("test_idler_eski");

        }
System.out.println("eski test idleri: "+ test_idler);
        for (int i =0;i<test_idler.size();i++){
            readHeroes(test_idler.get(i));

        }

        listView = (ListView) findViewById(R.id.listView);






    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),Hazirlik.class);
        intent.putExtra("kid",kidst);
        startActivity(intent);
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

    private void readHeroes(String test_id) {
        HashMap<String, String> params = new HashMap<>();
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

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    //Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();


                    System.out.println("doğrular00" + object);

                    tid=object.get("test_id").toString();
                    dogrus=object.get("toplam_dogru_cevap").toString();
                    yanliss=object.get("toplam_yanlis_cevap").toString();
                    toplams=object.get("testte_bulunun_soru_sayisi").toString();

                    System.out.println("doğru" + dogrus);

                    heroList.add(new EskiTest(tid,dogrus,yanliss,toplams));

                    HeroAdapter adapter = new HeroAdapter(heroList);
                    listView.setAdapter(adapter);










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

    class HeroAdapter extends ArrayAdapter<EskiTest> {
        List<EskiTest> heroList;

        public HeroAdapter(List<EskiTest> heroList) {
            super(EskiTestler.this, R.layout.eski_test_view, heroList);
            this.heroList = heroList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.eski_test_view, null, true);

            TextView testid = listViewItem.findViewById(R.id.textView10);
            TextView dogru = listViewItem.findViewById(R.id.textView13);
            TextView yanlis = listViewItem.findViewById(R.id.textView14);
            TextView bos = listViewItem.findViewById(R.id.textView15);
            TextView toplam = listViewItem.findViewById(R.id.textView16);
            TextView testsayısı = listViewItem.findViewById(R.id.textView11);
            LinearLayout linearLayout = listViewItem.findViewById(R.id.itemLinearLayout);





            final EskiTest hero = heroList.get(position);
            System.out.println("herohero" + hero);


            int y=Integer.parseInt( hero.getDogru());
            int x=Integer.parseInt(hero.getYanlis());
            int z=Integer.parseInt(hero.getToplam());

            int boslar = z-(x+y);


            SharedPreferences settings = getSharedPreferences("mysettings",
                    Context.MODE_PRIVATE);
            String dil = settings.getString("dil", "defaultvalue");

            if (dil.equals("tr")){
                bos.setText(getString(R.string.bos_tr) + String.valueOf(boslar));
                dogru.setText(getString(R.string.dogru_tr) + hero.getDogru());
                yanlis.setText(getString(R.string.yanlis_tr )+ hero.getYanlis());
                toplam.setText(getString(R.string.toplam_soru_tr )+ hero.getToplam());

            }else if(dil.equals("de")){
                bos.setText(getString(R.string.bos_de) + String.valueOf(boslar));
                dogru.setText(getString(R.string.dogru_de )+ hero.getDogru());
                yanlis.setText(getString(R.string.yanlis_de )+ hero.getYanlis());
                toplam.setText(getString(R.string.toplam_soru_de )+ hero.getToplam());
            }else if(dil.equals("en")){
                bos.setText(getString(R.string.bos_en) + String.valueOf(boslar));
                dogru.setText(getString(R.string.dogru_en )+ hero.getDogru());
                yanlis.setText(getString(R.string.yanlis_en )+ hero.getYanlis());
                toplam.setText(getString(R.string.toplam_soru_en )+ hero.getToplam());
            }


            testid.setText("Test ID: " + hero.getId());

            testsayısı.setText(position+1+". Test");

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    readTestSorular(hero.getId());


                }
            });











            return listViewItem;
        }
    }

    private void readTestSorular(String x) {
        HashMap<String, String> params = new HashMap<>();
        params.put("test_id",x);
        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        if(dil.equals("tr")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_READ_TEST_ID_GORE_SORU_IDLERI+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_READ_TEST_ID_GORE_SORU_IDLERI+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_READ_TEST_ID_GORE_SORU_IDLERI+"&dil=en", params, CODE_POST_REQUEST);
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


                    JSONArray objectlist =  object.getJSONArray("test_soru_idler");
                    for (int i = 0;i<objectlist.length();i++){

                        soru_id_list.add(objectlist.getJSONArray(i).get(0).toString());

                    }

                    String test_id=String.valueOf(object.getInt("test_id"));
                    System.out.println("xxxxxxxxxx"+ test_id);








                    Intent intent =new Intent(getApplicationContext(),HazirlikIstatistik.class);
                    intent.putExtra("kid",kidst);
                    intent.putStringArrayListExtra("idlist",soru_id_list);
                    intent.putExtra("position",0);
                    intent.putExtra("test_id",test_id);
                    startActivity(intent);


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
