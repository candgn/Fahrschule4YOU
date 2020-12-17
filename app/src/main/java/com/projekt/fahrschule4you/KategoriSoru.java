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

public class KategoriSoru extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    String kategori,kidst,dogrucevapa,dogrucevapb,dogrucevapc,dogrucevapd="",secim="O",resim,puan;
    int position,cevap_kontrol=0,cevap_kontrola=0,cevap_kontrolb=0,cevap_kontrolc=0;
    ArrayList<String> idlist,idListisaretli;
    TextView soru,asikki,bsikki,csikki,sorusayisi;
    Button kontrolet;
    ImageView ileriIv,imageView,resim_kapatma,imageView2,geri;
    ProgressBar progressBar;
    RelativeLayout prgslyt;
    CheckBox checkBoxA,checkBoxB,checkBoxC,checkMarkieren;
    LinearLayout lina,linb,linc,lind,ileri_geri;
    VideoView videoview;

    String ileri_geri_gelen="";

    TextView sonuc,isarettext;
    EditText input_cevap;
    ImageButton zoom,pause,replay;
    int secenek_sayisi;
    int play_kontrol=0;
    String tebrikler_soruyu_dogru_cozdunuz,maalesef_yanlis_cozdunuz;
    String verilen_a="",verilen_b="",verilen_c="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_soru);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        soru=(TextView)findViewById(R.id.textView9);
        asikki=(TextView)findViewById(R.id.textView14);
        bsikki=(TextView)findViewById(R.id.textView16);
        csikki=(TextView)findViewById(R.id.textView15);
        kontrolet= (Button) findViewById(R.id.button3);
        ileriIv=(ImageView)findViewById(R.id.ileri);
        imageView=(ImageView)findViewById(R.id.imageView4);
        sorusayisi=(TextView)findViewById(R.id.textView8);
        checkBoxA=(CheckBox)findViewById(R.id.checkBox2);
        checkBoxB=(CheckBox)findViewById(R.id.checkBox3);
        checkBoxC=(CheckBox)findViewById(R.id.checkBox4);
        lina=(LinearLayout)findViewById(R.id.lineara);
        linb=(LinearLayout)findViewById(R.id.linearb);
        linc=(LinearLayout)findViewById(R.id.linearc);
        lind=(LinearLayout)findViewById(R.id.lind);
        ileri_geri=(LinearLayout)findViewById(R.id.ileri_geri);
        sonuc=(TextView)findViewById(R.id.sonuc);
        checkMarkieren=(CheckBox)findViewById(R.id.markieren);
        isarettext=(TextView)findViewById(R.id.isarettext);
        videoview=(VideoView)findViewById(R.id.videoView);
        input_cevap = (EditText)findViewById(R.id.input_answer);
        zoom=(ImageButton)findViewById(R.id.zoom);
        pause=(ImageButton)findViewById(R.id.pause);
        replay=(ImageButton)findViewById(R.id.refresh);
        resim_kapatma=(ImageButton)findViewById(R.id.imageButton);
        imageView2=(ImageView)findViewById(R.id.imageView5);
        geri=(ImageView)findViewById(R.id.geri);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        prgslyt = (RelativeLayout)findViewById(R.id.prgs);

        idlist = new ArrayList<>();
        idListisaretli = new ArrayList<>();
        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            kategori = extras.getString("kategori");
            kidst = extras.getString("kid");
            idlist = extras.getStringArrayList("idlist");
            position = extras.getInt("position");
            if(extras.getString("ileri_geri")!=null){
                ileri_geri_gelen=extras.getString("ileri_geri");
            }



        }

        if (position==0){
            geri.setVisibility(View.INVISIBLE);
        }

        setTitle(kategori);

        sorusayisi.setText(position+1+"/"+idlist.size());

        /*if (ileri_geri_gelen.equals("geri")||ileri_geri_gelen.equals("ileri")){
            if (intent_sik_sayisi_gelen.equals("3")){
                if (intent_cevap_a_gelen.equals("1")){
                    checkBoxA.setChecked(true);
                }
                if (intent_cevap_b_gelen.equals("1")){
                    checkBoxB.setChecked(true);
                }
                if (intent_cevap_c_gelen.equals("1")){
                    checkBoxC.setChecked(true);
                }
            }else if (intent_sik_sayisi_gelen.equals("2")){
                if (intent_cevap_a_gelen.equals("1")){
                    checkBoxA.setChecked(true);
                }
                if (intent_cevap_b_gelen.equals("1")){
                    checkBoxB.setChecked(true);
                }
            }else if (intent_sik_sayisi_gelen.equals("1")){
                input_cevap.setText(intent_cevap_a_gelen);
            }
        }*/








        readHeroes();
        readIsaretliID();
        if (ileri_geri_gelen.equals("ileri_geri")){
            readVerilenCevap();
        }


        ileriIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if (position+1>=idlist.size()){
                    ileriIv.setVisibility(View.VISIBLE);
                    Intent intent2 = new Intent (getApplicationContext(),HazirlikIstatistik.class);
                    intent2.putExtra("geldigi_yer","kategori");
                    intent2.putExtra("kategori",kategori);
                    intent2.putExtra("kid",kidst);
                    intent2.putStringArrayListExtra("idlistkategori",idlist);
                    startActivity(intent2);
                }else if (ileri_geri_gelen.equals("ileri_geri")){
                    Intent intent = new Intent (getApplicationContext(),KategoriSoru.class);
                    intent.putStringArrayListExtra("idlist",idlist);
                    intent.putExtra("kategori",kategori);
                    intent.putExtra("position",position+1);
                    intent.putExtra("kid",kidst);
                    intent.putExtra("ileri_geri","ileri_geri");
                    startActivity(intent);
                }
                else if (position+1<idlist.size()){

                    Intent intent = new Intent (getApplicationContext(),KategoriSoru.class);
                    intent.putStringArrayListExtra("idlist",idlist);
                    intent.putExtra("kategori",kategori);
                    intent.putExtra("position",position+1);
                    intent.putExtra("kid",kidst);
                    startActivity(intent);
                }






            }
        });

        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(),KategoriSoru.class);
                intent.putStringArrayListExtra("idlist",idlist);
                intent.putExtra("kategori",kategori);
                intent.putExtra("position",position-1);
                intent.putExtra("kid",kidst);
                intent.putExtra("ileri_geri","ileri_geri");
                startActivity(intent);
            }
        });

        if (checkMarkieren.isChecked()){
            checkMarkieren.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateMarkierenDelete(idlist.get(position));
                }
            });

        }else{
            checkMarkieren.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateMarkieren(idlist.get(position));
                }
            });
        }






        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");

        if (dil.equals("tr")){
            kontrolet.setText(R.string.cevapla_tr);
            tebrikler_soruyu_dogru_cozdunuz=getString(R.string.tebrikler_soruyu_dogru_cozdunuz_tr);
            maalesef_yanlis_cozdunuz=getString(R.string.maalesef_yanlis_cozdunuz_tr);
            isarettext.setText(getText(R.string.isarettext_tr));

        }else if(dil.equals("de")){
            kontrolet.setText(R.string.cevapla_de);
            tebrikler_soruyu_dogru_cozdunuz=getString(R.string.tebrikler_soruyu_dogru_cozdunuz_de);
            maalesef_yanlis_cozdunuz=getString(R.string.maalesef_yanlis_cozdunuz_de);
            isarettext.setText(getText(R.string.isarettext_de));
        }else if(dil.equals("en")){
            kontrolet.setText(R.string.cevapla_en);
            tebrikler_soruyu_dogru_cozdunuz=getString(R.string.tebrikler_soruyu_dogru_cozdunuz_en);
            maalesef_yanlis_cozdunuz=getString(R.string.maalesef_yanlis_cozdunuz_en);
            isarettext.setText(getText(R.string.isarettext_en));
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(getApplicationContext(),KategoriDetay.class);
                intent.putExtra("kid",kidst);
                intent.putExtra("kategori",kategori);
                startActivity(intent);
                finish();

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
        params.put("test_id", "0");
        params.put("katagori_id", kategori);
        params.put("kullanici_id",kidst);
        params.put("puan",puan);
        System.out.println("puan kaydet"+puan);



        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");
        //Calling the create hero API
        if(dil.equals("tr")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_UPDATE_SORUKAYIT+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_UPDATE_SORUKAYIT+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_UPDATE_SORUKAYIT+"&dil=en", params, CODE_POST_REQUEST);
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

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);



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
            String id=String.valueOf(obj.getInt("id"));



           /* System.out.println("idlist size" + idlist.size());
            System.out.println("id: " + id);
            System.out.println("id.get position: " + idlist.get(position) );*/

                    if(idlist.size()>0) {
                        if (idlist.get(position).equals(id)) {
                            System.out.println(obj);



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
                                secenek_sayisi=2;
                                linc.setVisibility(View.GONE);
                            }
                            //1 şıklı
                            else if(sik_sayisi==1){
                                secenek_sayisi=1;
                                lina.setVisibility(View.GONE);
                                linb.setVisibility(View.GONE);
                                linc.setVisibility(View.GONE);
                                lind.setVisibility(View.VISIBLE);
                            }

                            soru.setText(Html.fromHtml(obj.getString("soru")));
                            asikki.setText(Html.fromHtml(obj.getString("a_sikki")));
                            bsikki.setText(Html.fromHtml(obj.getString("b_sikki")));
                            csikki.setText(Html.fromHtml(obj.getString("c_sikki")));
                            dogrucevapa = obj.getString("a_sikki_cevap");
                            dogrucevapb = obj.getString("b_sikki_cevap");
                            dogrucevapc = obj.getString("c_sikki_cevap");
                            kategori = obj.getString("katagori");
                            resim = obj.getString("resim_yolu");
                            puan=obj.getString("puan");
                            System.out.println("puan kontrol"+puan);
                            if (resim.contains("resimler")){
                                System.out.println("resim yolu boş");

                                imageView.setVisibility(View.VISIBLE);
                                Picasso.get().load("http://panel.4you-fahrschule.de/"+resim).into(imageView);
                                videoview.setVisibility(View.GONE);
                                zoom.setVisibility(View.VISIBLE);

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
                    }

            System.out.println("cevap kontrol baş" + cevap_kontrol);

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


                                }
                            }
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


                    /* Intent intent = new Intent (getApplicationContext(),HazirlikIstatistik.class);
                                    intent.putExtra("geldigi_yer","kategori");
                                    intent.putExtra("kategori",kategori);
                                    intent.putExtra("kid",kidst);
                                    startActivity(intent);*/

                        kontrolet.setVisibility(View.GONE);
                        //sonuc.setVisibility(View.VISIBLE);
                    if (position+1>=idlist.size()){
                        ileri_geri.setVisibility(View.VISIBLE);

                    }
                        int result=0;

                        if (position+1<idlist.size()){
                            ileri_geri.setVisibility(View.VISIBLE);

                        }

                        cevap_kontrol=1;
                        System.out.println("cevap kontorl cevapla" + cevap_kontrol);



                        if(secenek_sayisi==3){


                            if(dogrucevapa.equals("1")){
                                if (checkBoxA.isChecked()){

//                                    lina.setBackground(getDrawable(R.drawable.yesil_buton));
                                    checkBoxA.setChecked(true);
                                    result++;


                                }else {

//                                    lina.setBackground(getDrawable(R.drawable.kirmizi_buton));
                                    result--;



                                }
                            }else  if(dogrucevapa.equals("0")){
                                if (checkBoxA.isChecked()){

//                                    lina.setBackground(getDrawable(R.drawable.kirmizi_buton));
                                    checkBoxA.setChecked(true);
                                    result--;


                                }else {

//                                    lina.setBackground(getDrawable(R.drawable.yesil_buton));
                                    result++;



                                }
                            }
                            if(dogrucevapb.equals("1")){
                                if (checkBoxB.isChecked()){

//                                    linb.setBackground(getDrawable(R.drawable.yesil_buton));
                                    checkBoxB.setChecked(true);
                                    result++;


                                }else {

//                                    linb.setBackground(getDrawable(R.drawable.kirmizi_buton));
                                    result--;



                                }
                            } else if(dogrucevapb.equals("0")){
                                if (checkBoxB.isChecked()){

//                                    linb.setBackground(getDrawable(R.drawable.kirmizi_buton));
                                    checkBoxB.setChecked(true);
                                    result--;


                                }else {

//                                    linb.setBackground(getDrawable(R.drawable.yesil_buton));
                                    result++;



                                }
                            }
                            if(dogrucevapc.equals("1")){
                                if (checkBoxC.isChecked()){

//                                    linc.setBackground(getDrawable(R.drawable.yesil_buton));
                                    checkBoxC.setChecked(true);
                                    result++;


                                }else {

//                                    linc.setBackground(getDrawable(R.drawable.kirmizi_buton));
                                    result--;



                                }

                            }else if(dogrucevapc.equals("0")){
                                if (checkBoxC.isChecked()){

//                                    linc.setBackground(getDrawable(R.drawable.kirmizi_buton));
                                    checkBoxC.setChecked(true);
                                    result--;


                                }else {

//                                    linc.setBackground(getDrawable(R.drawable.yesil_buton));
                                    result++;



                                }
/*
                                if (result==3){
                                    sonuc.setText(tebrikler_soruyu_dogru_cozdunuz);
                                }
                                else{
                                    sonuc.setText(maalesef_yanlis_cozdunuz);
                                }*/


                                if (lind.getVisibility()==View.VISIBLE){
                                    verilen_a=input_cevap.getText().toString();
                                    verilen_b="";
                                    verilen_c="";
                                }else if (linc.getVisibility()==View.GONE){
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
                                        verilen_c="";
                                    }else{
                                        verilen_c="";

                                    }
                                }
                                else{
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
                                }


                            }
                        }else if(secenek_sayisi==2){/*
                            if(dogrucevapa.equals("1")){
                                if (checkBoxA.isChecked()){

                                    lina.setBackground(getDrawable(R.drawable.yesil_buton));
                                    checkBoxA.setChecked(true);
                                    result++;


                                }else {

                                    lina.setBackground(getDrawable(R.drawable.kirmizi_buton));
                                    result--;



                                }
                            }else  if(dogrucevapa.equals("0")){
                                if (checkBoxA.isChecked()){

                                    lina.setBackground(getDrawable(R.drawable.kirmizi_buton));
                                    checkBoxA.setChecked(true);
                                    result--;


                                }else {

                                    lina.setBackground(getDrawable(R.drawable.yesil_buton));
                                    result++;



                                }
                            }
                            if(dogrucevapb.equals("1")){
                                if (checkBoxB.isChecked()){

                                    linb.setBackground(getDrawable(R.drawable.yesil_buton));
                                    checkBoxB.setChecked(true);
                                    result++;


                                }else {

                                    linb.setBackground(getDrawable(R.drawable.kirmizi_buton));
                                    result--;



                                }
                            } else if(dogrucevapb.equals("0")){
                                if (checkBoxB.isChecked()){

                                    linb.setBackground(getDrawable(R.drawable.kirmizi_buton));
                                    checkBoxB.setChecked(true);
                                    result--;


                                }else {

                                    linb.setBackground(getDrawable(R.drawable.yesil_buton));
                                    result++;



                                }
                            }*/

                            dogrucevapc="";
/*
                            if (result==2){
                                sonuc.setText(tebrikler_soruyu_dogru_cozdunuz);
                            }
                            else{
                                sonuc.setText(maalesef_yanlis_cozdunuz);
                            }*/


                            if (lind.getVisibility()==View.VISIBLE){
                                verilen_a=input_cevap.getText().toString();
                                verilen_b="";
                                verilen_c="";
                            }else if (linc.getVisibility()==View.GONE){
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

                            }
                            else{
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
                            }



                        }else if (secenek_sayisi==1){
                            /*if(dogrucevapa.toLowerCase().equals(input_cevap.getText().toString().toLowerCase())){
                                sonuc.setText(tebrikler_soruyu_dogru_cozdunuz);
                            }else{
                                sonuc.setText(maalesef_yanlis_cozdunuz);
                            }*/
                            dogrucevapc="";
                            dogrucevapb="";
                        }




                    checkBoxA.setEnabled(false);
                    checkBoxB.setEnabled(false);
                    checkBoxC.setEnabled(false);





                        System.out.println("puan"+puan);
                        System.out.println("verilenkayıt a: " + verilen_a + " b: " + verilen_b + " c: " + verilen_c);
                        soruKaydet(String.valueOf(idlist.get(position)),dogrucevapa,dogrucevapb,dogrucevapc,verilen_a,verilen_b,verilen_c,puan);


                    if (position+1>=idlist.size()){
                        ileriIv.setVisibility(View.VISIBLE);
                        Intent intent2 = new Intent (getApplicationContext(),KategoriDetay.class);
                        intent2.putExtra("geldigi_yer","kategori");
                        intent2.putExtra("kategori",kategori);
                        intent2.putExtra("kid",kidst);
                        intent2.putStringArrayListExtra("idlistkategori",idlist);
                        startActivity(intent2);
                    }else if (ileri_geri_gelen.equals("ileri_geri")){
                        Intent intent = new Intent (getApplicationContext(),KategoriSoru.class);
                        intent.putStringArrayListExtra("idlist",idlist);
                        intent.putExtra("kategori",kategori);
                        intent.putExtra("position",position+1);
                        intent.putExtra("kid",kidst);
                        intent.putExtra("ileri_geri","ileri_geri");
                        startActivity(intent);
                    }
                    else if (position+1<idlist.size()){

                        Intent intent = new Intent (getApplicationContext(),KategoriSoru.class);
                        intent.putStringArrayListExtra("idlist",idlist);
                        intent.putExtra("kategori",kategori);
                        intent.putExtra("position",position+1);
                        intent.putExtra("kid",kidst);
                        startActivity(intent);
                    }






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
            PerformNetworkRequest4 request = new PerformNetworkRequest4(Api.URL_READ_MARKIEREN+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();
            System.out.println("kontrol kontrol" +kidst);

        }else if(dil.equals("de")){
            PerformNetworkRequest4 request = new PerformNetworkRequest4(Api.URL_READ_MARKIEREN+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest4 request = new PerformNetworkRequest4(Api.URL_READ_MARKIEREN+"&dil=en", params, CODE_POST_REQUEST);
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


            //idListisaretli.add(heroes.getString(i));
            if (heroes.getString(i).equals(idlist.get(position))){
                checkMarkieren.setChecked(true);
            }


        }










    }

    private void readVerilenCevap() {

        System.out.println("soru id" + idlist.get(position) + "kullanıcı id"  + kidst);

        HashMap<String, String> params = new HashMap<>();
        params.put("soru_id", idlist.get(position));
        params.put("test_id", "0");
        params.put("kullanici_id", kidst);

        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");

        if(dil.equals("tr")){
            KategoriSoru.PerformNetworkRequestv request = new KategoriSoru.PerformNetworkRequestv(Api.URL_READ_TOPLAM_VERILEN_CEVAP+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            KategoriSoru.PerformNetworkRequestv request = new KategoriSoru.PerformNetworkRequestv(Api.URL_READ_TOPLAM_VERILEN_CEVAP+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            KategoriSoru.PerformNetworkRequestv request = new KategoriSoru.PerformNetworkRequestv(Api.URL_READ_TOPLAM_VERILEN_CEVAP+"&dil=en", params, CODE_POST_REQUEST);
            request.execute();

        }
    }
    private class PerformNetworkRequestv extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequestv(String url, HashMap<String, String> params, int requestCode) {
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

                    JSONObject verilencevap = object.getJSONObject("verilen_cevap");
                    //JSONObject obj = verilencevap.getJSONObject(0);

                    System.out.println("verilencevaparray: " + verilencevap);



                    if (verilencevap.getString("b_sikki_verilen_cevap").equals("")&&verilencevap.getString("c_sikki_verilen_cevap").equals("")){

                        input_cevap.setText(verilencevap.getString("a_sikki_verilen_cevap"));

                    }else{
                        if (verilencevap.getString("a_sikki_verilen_cevap").equals("1")){
                            checkBoxA.setChecked(true);

                        }
                        if (verilencevap.getString("b_sikki_verilen_cevap").equals("1")){
                            checkBoxB.setChecked(true);

                        }
                        if (verilencevap.getString("c_sikki_verilen_cevap").equals("1")){
                            checkBoxC.setChecked(true);
                        }
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
}
