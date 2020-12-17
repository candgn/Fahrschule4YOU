package com.projekt.fahrschule4you;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.projekt.fahrschule4you.Web.Api;
import com.projekt.fahrschule4you.Web.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SifremiDegistir extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    String kidst,eskist,yeni1st,yeni2st,sifre,eski_sifreniz,yeni_sifreniz,kaydet_string;
    TextView eski,yeni1,yeni2,eski_text,yeni_text,yeni2_text;
    Button kaydet;

    String sifreniz_basarili_degisti,lutfen_yeni_sifre_belirleyin,yeni_sifreler_uyusmuyor,lutfen_eski_sifrenizi_girin,lutfen_eski_sifrenizi_dogru_girin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifremi_degistir);

        eski = (TextView)findViewById(R.id.editText3) ;
        yeni1 = (TextView)findViewById(R.id.editText4) ;
        yeni2 = (TextView)findViewById(R.id.editText5) ;
        kaydet=(Button)findViewById(R.id.button2);
        eski_text = (TextView)findViewById(R.id.textView) ;
        yeni_text = (TextView)findViewById(R.id.textView6) ;
        yeni2_text = (TextView)findViewById(R.id.textView7) ;


        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            kidst = extras.getString("kid");
            sifre= extras.getString("sifre");
        }

        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");


        if (dil.equals("tr")){
            sifreniz_basarili_degisti= getString(R.string.sifreniz_basarili_degisti_tr);
            lutfen_yeni_sifre_belirleyin=getString(R.string.lutfen_yeni_sifre_belirleyin_tr);
            yeni_sifreler_uyusmuyor= getString(R.string.yeni_sifreler_uyusmuyor_tr);
            lutfen_eski_sifrenizi_girin=getString(R.string.lutfen_eski_sifrenizi_girin_tr);
            lutfen_eski_sifrenizi_dogru_girin=getString(R.string.lutfen_eski_sifrenizi_dogru_girin_tr);
            eski_sifreniz=getString(R.string.eski_sifreniz_tr);
            yeni_sifreniz=getString(R.string.yeni_sifreniz_tr);
            kaydet_string=getString(R.string.kaydet_tr);

        }else if(dil.equals("de")){
            sifreniz_basarili_degisti= getString(R.string.sifreniz_basarili_degist_de);
            lutfen_yeni_sifre_belirleyin=getString(R.string.lutfen_yeni_sifre_belirleyin_de);
            yeni_sifreler_uyusmuyor= getString(R.string.yeni_sifreler_uyusmuyor_de);
            lutfen_eski_sifrenizi_girin=getString(R.string.lutfen_eski_sifrenizi_girin_de);
            lutfen_eski_sifrenizi_dogru_girin=getString(R.string.lutfen_eski_sifrenizi_dogru_girin_de);
            eski_sifreniz=getString(R.string.eski_sifreniz_de);
            yeni_sifreniz=getString(R.string.yeni_sifreniz_de);
            kaydet_string=getString(R.string.kaydet_de);
        }else if(dil.equals("en")){
            sifreniz_basarili_degisti= getString(R.string.sifreniz_basarili_degisti_en);
            lutfen_yeni_sifre_belirleyin=getString(R.string.lutfen_yeni_sifre_belirleyin_en);
            yeni_sifreler_uyusmuyor= getString(R.string.yeni_sifreler_uyusmuyor_en);
            lutfen_eski_sifrenizi_girin=getString(R.string.lutfen_eski_sifrenizi_girin_en);
            lutfen_eski_sifrenizi_dogru_girin=getString(R.string.lutfen_eski_sifrenizi_dogru_girin_en);
            eski_sifreniz=getString(R.string.eski_sifreniz_en);
            yeni_sifreniz=getString(R.string.yeni_sifreniz_en);
            kaydet_string=getString(R.string.kaydet_en);
        }

        eski_text.setText(eski_sifreniz);
        yeni_text.setText(yeni_sifreniz);
        yeni2_text.setText(yeni_sifreniz);
        kaydet.setText(kaydet_string);




        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eskist=eski.getText().toString();
                yeni1st=yeni1.getText().toString();
                yeni2st=yeni2.getText().toString();
                System.out.println("eski şifre"+eskist);
                System.out.println("yeni şifre"+yeni1st);
                System.out.println("yeni şifre2"+yeni2st);
                System.out.println("yyyyyyyyyyyyyyy "+sifre+" "+eskist);

                if (sifre.equals(eskist)){
                    System.out.println("wwwwwwwwwwww"+yeni1st+" "+yeni2st);
                    if (yeni2st.equals(yeni1st)){

                        sifreDegistir(kidst,yeni1st);
                        Toast.makeText(getApplicationContext(),sifreniz_basarili_degisti, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),Profil.class);
                        intent.putExtra("kid",kidst);
                        startActivity(intent);
                    }else if (yeni2st==null){
                        Toast.makeText(getApplicationContext(),lutfen_yeni_sifre_belirleyin, Toast.LENGTH_LONG).show();

                    }else if (yeni1st==""){
                        Toast.makeText(getApplicationContext(),lutfen_yeni_sifre_belirleyin, Toast.LENGTH_LONG).show();
                    }else if (!yeni1st.equals(yeni2st)){
                        System.out.println("Şifreler: " + yeni2st + "2.; " + yeni1st);
                        Toast.makeText(getApplicationContext(),yeni_sifreler_uyusmuyor, Toast.LENGTH_LONG).show();

                    }


                } else if (eskist==null){
                    Toast.makeText(getApplicationContext(),lutfen_eski_sifrenizi_girin, Toast.LENGTH_LONG).show();

                }else if (!sifre.equals(eskist)){
                    Toast.makeText(getApplicationContext(),lutfen_eski_sifrenizi_dogru_girin, Toast.LENGTH_LONG).show();

                }

            }
        });



    }

    private void sifreDegistir(String kullanici_id,String yeni_sifre){



        HashMap<String, String> params = new HashMap<>();
        params.put("kullanici_id", kullanici_id);
        params.put("yeni_sifre", yeni_sifre);





        //Calling the create hero API
        PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_UPDATE_SIFRE, params, CODE_POST_REQUEST);
        request.execute();
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

            System.out.println("xxxxxxxxxxxx"+s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
//                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();

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
