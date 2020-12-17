package com.projekt.fahrschule4you;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.projekt.fahrschule4you.Web.Api;
import com.projekt.fahrschule4you.Web.RequestHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HazirlikSoru extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    String kidst,dogrucevapa,dogrucevapb,dogrucevapc,secim="O",kategori,testid,resim,puan;
    ArrayList<Integer> idlist,idListisaretli;
    int position,cevap_kontrol=0,cevap_kontrola=0,cevap_kontrolb=0,cevap_kontrolc=0,secenek_sayisi;
    TextView soru,asikki,bsikki,csikki,sorusayisi,isarettext;
    Button kontrolet,sonuclar;
    ImageView ileriIv,imageView,resim_kapatma,imageView2;
    VideoView videoview;
    ImageButton zoom,pause,replay;

    ProgressBar progressBar;
    RelativeLayout prgslyt;

    CheckBox checkBoxA,checkBoxB,checkBoxC,checkMarkieren;
    LinearLayout lina,linb,linc,lind;
    EditText input_cevap;

    int play_kontrol=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazirlik_soru);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        soru=(TextView)findViewById(R.id.textView9);
        asikki=(TextView)findViewById(R.id.textView14);
        bsikki=(TextView)findViewById(R.id.textView16);
        csikki=(TextView)findViewById(R.id.textView15);
        kontrolet= (Button) findViewById(R.id.button3);
        sonuclar= (Button) findViewById(R.id.button4);
        ileriIv=(ImageView)findViewById(R.id.ileri);
        sorusayisi=(TextView)findViewById(R.id.textView8);
        checkBoxA=(CheckBox)findViewById(R.id.checkBox2);
        checkBoxB=(CheckBox)findViewById(R.id.checkBox3);
        checkBoxC=(CheckBox)findViewById(R.id.checkBox4);
        lina=(LinearLayout)findViewById(R.id.lineara);
        linb=(LinearLayout)findViewById(R.id.linearb);
        linc=(LinearLayout)findViewById(R.id.linearc);
        lind=(LinearLayout)findViewById(R.id.lind);
        checkMarkieren=(CheckBox)findViewById(R.id.markieren);
        isarettext=(TextView)findViewById(R.id.isarettext);
        imageView=(ImageView)findViewById(R.id.imageView4);
        videoview=(VideoView)findViewById(R.id.videoView);
        zoom=(ImageButton)findViewById(R.id.zoom);
        pause=(ImageButton)findViewById(R.id.pause);
        replay=(ImageButton)findViewById(R.id.refresh);
        resim_kapatma=(ImageButton)findViewById(R.id.imageButton);
        imageView2=(ImageView)findViewById(R.id.imageView5);
        input_cevap = (EditText)findViewById(R.id.input_answer);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        prgslyt = (RelativeLayout)findViewById(R.id.prgs);

        idlist = new ArrayList<>();
        idListisaretli = new ArrayList<>();

        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            kidst = extras.getString("kid");
            idlist = extras.getIntegerArrayList("idlist");
            position = extras.getInt("position");
            testid= extras.getString("test_id");
        }
        System.out.println("çökme"+idlist);

        sorusayisi.setText(position+1+"/"+idlist.size());

        readHeroes();
        readIsaretliID();

        ileriIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(),HazirlikSoru.class);
                intent.putIntegerArrayListExtra("idlist",idlist);
                intent.putExtra("position",position+1);
                intent.putExtra("kid",kidst);
                intent.putExtra("test_id",testid);
                startActivity(intent);
            }
        });

        sonuclar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> idlistString;
                idlistString = new ArrayList<>();

                for (int i = 0;i<idlist.size();i++){
                    idlistString.add(String.valueOf(idlist.get(i)));

                }

                Intent intent = new Intent (getApplicationContext(),HazirlikIstatistik.class);
                intent.putStringArrayListExtra("idlist",idlistString);
                intent.putExtra("position",0);
                intent.putExtra("kid",kidst);
                intent.putExtra("test_id",testid);
                intent.putExtra("geldigi_yer","test");
                startActivity(intent);
            }
        });

        if (position+1>=idlist.size()){
            ileriIv.setVisibility(View.GONE);



        }

        if (checkMarkieren.isChecked()){
            checkMarkieren.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateMarkierenDelete(String.valueOf(idlist.get(position)));

                }
            });

        }else{
            checkMarkieren.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateMarkieren(String.valueOf(idlist.get(position)));

                }
            });
        }

        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");

        if (dil.equals("tr")){
            kontrolet.setText(R.string.cevapla_tr);
            sonuclar.setText(getString(R.string.sonuclar_tr));
            isarettext.setText(getText(R.string.isarettext_tr));

        }else if(dil.equals("de")){
            kontrolet.setText(R.string.cevapla_de);
            sonuclar.setText(getString(R.string.sonuclar_de));
        }else if(dil.equals("en")){
            kontrolet.setText(R.string.cevapla_en);
            sonuclar.setText(getString(R.string.sonuclar_en));
        }
    }
    @Override
    public void onBackPressed() {

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



    private void readHeroes() {
        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        if(dil.equals("tr")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_SORU+"&dil=tr", null, CODE_GET_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_SORU+"&dil=de", null, CODE_GET_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_SORU+"&dil=en", null, CODE_GET_REQUEST);
            request.execute();

        }
    }
    private void soruKaydet(String soru_id,String dogru_cevap_a,String dogru_cevap_b,String dogru_cevap_c,
                            String verilen_cevapa,String verilen_cevapb,String verilen_cevapc,String puan){



        HashMap<String, String> params = new HashMap<>();
        params.put("soru_id", soru_id);
        params.put("a_sikki_cevap", dogru_cevap_a);
        params.put("b_sikki_cevap", dogru_cevap_b);
        params.put("c_sikki_cevap", dogru_cevap_c);
        params.put("a_sikki_verilen_cevap", verilen_cevapa);
        params.put("b_sikki_verilen_cevap", verilen_cevapb);
        params.put("c_sikki_verilen_cevap", verilen_cevapc);
        params.put("test_id", testid);
        params.put("puan",puan);

       // System.out.println("aaaaaa: " + soru_id + "bbbbbbbb" + verilen_cevap + "tttttt"+ testid);




        //Calling the create hero API
        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");

        if(dil.equals("tr")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_UPDATE_TESTKAYIT+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_UPDATE_TESTKAYIT+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_UPDATE_TESTKAYIT+"&dil=en", params, CODE_POST_REQUEST);
            request.execute();

        }
    }

    private void updateMarkieren(String soru_id){



        HashMap<String, String> params = new HashMap<>();
        params.put("soru_id", soru_id);
        params.put("kullanici_id",kidst);



        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        //Calling the create hero API
        if(dil.equals("tr")){
            PerformNetworkRequest3 request = new PerformNetworkRequest3(Api.URL_UPDATE_MARKIEREN+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest3 request = new PerformNetworkRequest3(Api.URL_UPDATE_MARKIEREN+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest3 request = new PerformNetworkRequest3(Api.URL_UPDATE_MARKIEREN+"&dil=en", params, CODE_POST_REQUEST);
            request.execute();

        }


    }
    private void updateMarkierenDelete(String soru_id){



        HashMap<String, String> params = new HashMap<>();
        params.put("soru_id", soru_id);
        params.put("kullanici_id",kidst);



        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        //Calling the create hero API
        if(dil.equals("tr")){
            PerformNetworkRequest3 request = new PerformNetworkRequest3(Api.URL_UPDATE_MARKIEREN_DELETE+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest3 request = new PerformNetworkRequest3(Api.URL_UPDATE_MARKIEREN_DELETE+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest3 request = new PerformNetworkRequest3(Api.URL_UPDATE_MARKIEREN_DELETE+"&dil=en", params, CODE_POST_REQUEST);
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

            System.out.println("xxxxxxxxxxxx"+s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
//                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void refreshHeroList(JSONArray heroes) throws JSONException {

        for (int i = 0; i < heroes.length(); i++) {
            //getting each hero object
            JSONObject obj = heroes.getJSONObject(i);
            System.out.println("son hata" +idlist+" gelen " + obj );
            if (idlist.get(position).equals(obj.getInt("id"))){

                int sik_sayisi=0;
                if(!obj.getString("a_sikki").equals("")){
                    sik_sayisi++;
                }
                if(!obj.getString("b_sikki").equals("")){
                    sik_sayisi++;
                }
                if(!obj.getString("c_sikki").equals("")){
                    sik_sayisi++;
                }
                //3 şıklı
                if(sik_sayisi==3){
                    secenek_sayisi=3;

                }
                //2 şıklı
                else if(sik_sayisi==2){
                    linc.setVisibility(View.GONE);
                    secenek_sayisi=2;
                }
                //1 şıklı
                else if(sik_sayisi==1){
                    lina.setVisibility(View.GONE);
                    linb.setVisibility(View.GONE);
                    linc.setVisibility(View.GONE);
                    lind.setVisibility(View.VISIBLE);
                    secenek_sayisi=1;
                }

                soru.setText(Html.fromHtml(obj.getString("soru")));
                asikki.setText(Html.fromHtml(obj.getString("a_sikki")));
                bsikki.setText(Html.fromHtml(obj.getString("b_sikki")));
                csikki.setText(Html.fromHtml(obj.getString("c_sikki")));
                dogrucevapa = obj.getString("a_sikki_cevap");
                dogrucevapb = obj.getString("b_sikki_cevap");
                dogrucevapc = obj.getString("c_sikki_cevap");
                kategori=obj.getString("katagori");
                resim = obj.getString("resim_yolu");
                puan=obj.getString("puan");
                System.out.println("resim yolu"+resim);
                if (resim.contains("resimler")){
                    System.out.println("resim yolu boş");

                    imageView.setVisibility(View.VISIBLE);
                    Picasso.get().load("http://panel.4you-fahrschule.de/"+resim).into(imageView);
                    videoview.setVisibility(View.GONE);zoom.setVisibility(View.VISIBLE);

                    zoom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            prgslyt.setVisibility(View.VISIBLE);
                            imageView2.setVisibility(View.VISIBLE);
                            resim_kapatma.setVisibility(View.VISIBLE);
                            Picasso.get().load("http://panel.4you-fahrschule.de/"+resim).into(imageView2);
                        }
                    });
                    resim_kapatma.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            prgslyt.setVisibility(View.GONE);
                            imageView2.setVisibility(View.GONE);
                            resim_kapatma.setVisibility(View.GONE);
                        }
                    });

                }else if(resim.contains("video")){
                    videoview.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    zoom.setVisibility(View.VISIBLE);
                    replay.setVisibility(View.VISIBLE);
                    pause.setVisibility(View.VISIBLE);
                    zoom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(),FullScreenVideo.class);
                            intent.putExtra("video_url","http://panel.4you-fahrschule.de/"+resim);
                            startActivity(intent);


                        }
                    });
                    try {

                        // Get the URL from String videoUrl
                        Uri video = Uri.parse("http://panel.4you-fahrschule.de/"+resim);
                        videoview.setMediaController(null);
                        videoview.setVideoURI(video);

                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }


                    videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {

                            videoview.start();
// do something when video is ready to play, you want to start playing video here

                        }
                    });

                    pause.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (play_kontrol==0){
                                if(null!=videoview){
                                    videoview.pause();
                                    play_kontrol=1;
                                    pause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                }
                            }else if (play_kontrol==1){
                                videoview.start();
                                play_kontrol=0;
                                pause.setImageResource(R.drawable.ic_pause_black_24dp);

                            }

                        }
                    });
                    replay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            videoview.resume();
                        }
                    });
                }
                else{
                    videoview.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);

                }


            }

            lina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("cevap kontrol" + cevap_kontrol);
                    if (cevap_kontrol==0) {
                        if (cevap_kontrola==0){
                            checkBoxA.setChecked(true);
                            lina.setBackground(getDrawable(R.drawable.turuncu_buton));
                            cevap_kontrola=1;
                        }else {
                            checkBoxA.setChecked(false);
                            lina.setBackground(getDrawable(R.drawable.siyah_transp_buton));
                            cevap_kontrola=0;
                        }


                    }
                }
            });

            linb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cevap_kontrol == 0) {
                        if (cevap_kontrolb==0){
                            checkBoxB.setChecked(true);
                            linb.setBackground(getDrawable(R.drawable.turuncu_buton));
                            cevap_kontrolb=1;
                        }else {
                            checkBoxB.setChecked(false);
                            linb.setBackground(getDrawable(R.drawable.siyah_transp_buton));
                            cevap_kontrolb=0;
                        }


                    }
                }
            });

            linc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cevap_kontrol == 0) {
                        if (cevap_kontrolc==0){
                            checkBoxC.setChecked(true);
                            linc.setBackground(getDrawable(R.drawable.turuncu_buton));
                            cevap_kontrolc=1;
                        }else {
                            checkBoxC.setChecked(false);
                            linc.setBackground(getDrawable(R.drawable.siyah_transp_buton));
                            cevap_kontrolc=0;
                        }


                    }    }
            });

            checkBoxA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("cevap kontrol" + cevap_kontrol);
                    if (cevap_kontrol==0) {
                        if (cevap_kontrola==0){
                            checkBoxA.setChecked(true);
                            lina.setBackground(getDrawable(R.drawable.turuncu_buton));
                            cevap_kontrola=1;
                        }else {
                            checkBoxA.setChecked(false);
                            lina.setBackground(getDrawable(R.drawable.siyah_transp_buton));
                            cevap_kontrola=0;
                        }


                    }
                }
            });

            checkBoxB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cevap_kontrol == 0) {
                        if (cevap_kontrolb==0){
                            checkBoxB.setChecked(true);
                            linb.setBackground(getDrawable(R.drawable.turuncu_buton));
                            cevap_kontrolb=1;
                        }else {
                            checkBoxB.setChecked(false);
                            linb.setBackground(getDrawable(R.drawable.siyah_transp_buton));
                            cevap_kontrolb=0;
                        }


                    }
                }
            });

            checkBoxC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cevap_kontrol == 0) {
                        if (cevap_kontrolc==0){
                            checkBoxC.setChecked(true);
                            linc.setBackground(getDrawable(R.drawable.turuncu_buton));
                            cevap_kontrolc=1;
                        }else {
                            checkBoxC.setChecked(false);
                            linc.setBackground(getDrawable(R.drawable.siyah_transp_buton));
                            cevap_kontrolc=0;
                        }


                    }
                }
            });
            kontrolet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kontrolet.setVisibility(View.GONE);

                    if (position+1<idlist.size()){
                        ileriIv.setVisibility(View.VISIBLE);
                    }
                    if (position+1>=idlist.size()){
                        sonuclar.setVisibility(View.VISIBLE);



                    }
                    cevap_kontrol=1;
                    System.out.println("cevap kontorl cevapla" + cevap_kontrol);


/*
                    if(dogrucevapa.equals("1")){
                        if (checkBoxA.isChecked()){

                            lina.setBackground(getDrawable(R.drawable.yesil_buton));
                            checkBoxA.setChecked(true);


                        }else {

                            lina.setBackground(getDrawable(R.drawable.kirmizi_buton));



                        }
                    }else  if(dogrucevapa.equals("0")){
                        if (checkBoxA.isChecked()){

                            lina.setBackground(getDrawable(R.drawable.kirmizi_buton));
                            checkBoxA.setChecked(true);


                        }else {

                            lina.setBackground(getDrawable(R.drawable.yesil_buton));



                        }
                    }
                    if(dogrucevapb.equals("1")){
                        if (checkBoxB.isChecked()){

                            linb.setBackground(getDrawable(R.drawable.yesil_buton));
                            checkBoxB.setChecked(true);


                        }else {

                            linb.setBackground(getDrawable(R.drawable.kirmizi_buton));



                        }
                    } else if(dogrucevapb.equals("0")){
                        if (checkBoxB.isChecked()){

                            linb.setBackground(getDrawable(R.drawable.kirmizi_buton));
                            checkBoxB.setChecked(true);


                        }else {

                            linb.setBackground(getDrawable(R.drawable.yesil_buton));



                        }
                    }
                    if(dogrucevapc.equals("1")){
                        if (checkBoxC.isChecked()){

                            linc.setBackground(getDrawable(R.drawable.yesil_buton));
                            checkBoxC.setChecked(true);


                        }else {

                            linc.setBackground(getDrawable(R.drawable.kirmizi_buton));



                        }

                    }else if(dogrucevapc.equals("0")){
                        if (checkBoxC.isChecked()){

                            linc.setBackground(getDrawable(R.drawable.kirmizi_buton));
                            checkBoxC.setChecked(true);


                        }else {

                            linc.setBackground(getDrawable(R.drawable.yesil_buton));



                        }

                    }*/
                    checkBoxA.setEnabled(false);
                    checkBoxB.setEnabled(false);
                    checkBoxC.setEnabled(false);


                    String verilen_a="",verilen_b="",verilen_c="";

                    if (secenek_sayisi==3){
                        if (checkBoxA.isChecked()){
                            verilen_a="1";
                        }else{
                            verilen_a="0";
                        }

                        if (checkBoxB.isChecked()){
                            verilen_b="1";

                        }else{
                            verilen_b="0";

                        }
                        if (checkBoxC.isChecked()){
                            verilen_c="1";
                        }else{
                            verilen_c="0";

                        }
                    }else if(secenek_sayisi==2){
                        if (checkBoxA.isChecked()){
                            verilen_a="1";
                        }else{
                            verilen_a="0";
                        }

                        if (checkBoxB.isChecked()){
                            verilen_b="1";

                        }else{
                            verilen_b="0";

                        }
                        verilen_c="";
                        dogrucevapc="";

                    }else if(secenek_sayisi==1){
                        verilen_a=input_cevap.getText().toString();
                        verilen_b="";
                        verilen_c="";
                        dogrucevapb="";
                        dogrucevapc="";
                    }




                    soruKaydet(String.valueOf(idlist.get(position)),dogrucevapa,dogrucevapb,dogrucevapc,verilen_a,verilen_b,verilen_c,puan);




                }




            });


        }






    }

    private void readIsaretliID() {
        HashMap<String, String> params = new HashMap<>();
        params.put("kullanici_id",kidst);


        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        if(dil.equals("tr")){
            HazirlikSoru.PerformNetworkRequest4 request = new HazirlikSoru.PerformNetworkRequest4(Api.URL_READ_MARKIEREN+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();
            System.out.println("kontrol kontrol" +kidst);

        }else if(dil.equals("de")){
            HazirlikSoru.PerformNetworkRequest4 request = new HazirlikSoru.PerformNetworkRequest4(Api.URL_READ_MARKIEREN+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            HazirlikSoru.PerformNetworkRequest4 request = new HazirlikSoru.PerformNetworkRequest4(Api.URL_READ_MARKIEREN+"&dil=en", params, CODE_POST_REQUEST);
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
            progressBar.setVisibility(View.VISIBLE);
            prgslyt.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            prgslyt.setVisibility(View.GONE);

            System.out.println("isaretlisorular id sss: " + s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {


                    refreshHeroList4(object.getJSONArray("isaretli_sorular"));



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
        idListisaretli.clear();

        //traversing through all the items in the json array
        //the json we got from the response
        for (int i = 0; i < heroes.length(); i++) {
            //getting each hero objectobj


            //adding the hero to the list

            System.out.println("işaretli soru id kontrol: " + heroes.getString(i) + "soru id: " + idlist.get(position));
            //idListisaretli.add(heroes.getString(i));
            String x =heroes.getString(i);
            String y = idlist.get(position).toString();
            if (x.equals(y)){
                checkMarkieren.setChecked(true);
                System.out.println("soru işaretli");
            }


        }










    }
}
