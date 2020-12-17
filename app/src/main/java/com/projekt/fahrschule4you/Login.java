package com.projekt.fahrschule4you;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.projekt.fahrschule4you.Web.Api;
import com.projekt.fahrschule4you.Web.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    EditText email,sifre;
    Button giris;
    ProgressBar progressBar;
    RelativeLayout prgslyt;

    String sifreniz_uyusmuyor,hesabiniz_aktif_degil;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText)findViewById(R.id.editText);
        sifre = (EditText)findViewById(R.id.editText2);
        giris = (Button)findViewById(R.id.button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        prgslyt = (RelativeLayout)findViewById(R.id.prgs);

        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                readHeroes();

            }
        });

        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String myString = settings.getString("loggedin", "defaultvalue");

        if (!myString.equals("defaultvalue")){
            Intent intent = new Intent (getApplicationContext(),Profil.class);
            intent.putExtra("kid",myString);
            startActivity(intent);

        }



        String dil = settings.getString("dil", "defaultvalue");


        if (dil.equals("tr")){
            sifreniz_uyusmuyor= getString(R.string.sifreniz_uyusmuyor_tr);
            hesabiniz_aktif_degil=getString(R.string.hesabiniz_henuz_aktif_degil_tr);

        }else if(dil.equals("de")){
            sifreniz_uyusmuyor= getString(R.string.sifreniz_uyusmuyor_de);
            hesabiniz_aktif_degil=getString(R.string.hesabiniz_henuz_aktif_degil_de);
        }else if(dil.equals("en")){
            sifreniz_uyusmuyor= getString(R.string.sifreniz_uyusmuyor_en);
            hesabiniz_aktif_degil=getString(R.string.hesabiniz_henuz_aktif_degil_en);
        }





    }
    @Override
    public void onBackPressed() {

    }

    public void sifre_unuttum (View View){
        Intent intent = new Intent(getApplicationContext(),SifremiUnuttum.class);
        startActivity(intent);
    }

    private void readHeroes() {

        String emailst,sifrest;
        emailst=email.getText().toString();
        sifrest=sifre.getText().toString();
        System.out.println(emailst);
        HashMap<String, String> params = new HashMap<>();
        params.put("kullanici_email", emailst);
        params.put("kullanici_sifre", sifrest);
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_LOGIN, params, CODE_POST_REQUEST);
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
                if (!object.getBoolean("error")) {


                   int kontrol=object.getInt("kontrol");

                   if (kontrol ==1 ){



                       SharedPreferences settings = getSharedPreferences("mysettings",
                               Context.MODE_PRIVATE);

                       SharedPreferences.Editor editor = settings.edit();
                       editor.putString("loggedin", object.getString("id"));
                       editor.commit();


                       Intent intent = new Intent (getApplicationContext(),Profil.class);
                       intent.putExtra("kid",object.getString("id"));
                       startActivity(intent);
                       finish();
                   }else if (kontrol==0){
                    //eğer şifre yanlış yada boş fln bırakmışsa sadece kontrol = 0
                       Toast.makeText(getApplicationContext(),sifreniz_uyusmuyor, Toast.LENGTH_LONG).show();
                   }else if (kontrol == 2){
                    //eğer aktif değilse kontrol = 2
                       Toast.makeText(getApplicationContext(),hesabiniz_aktif_degil, Toast.LENGTH_LONG).show();
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
