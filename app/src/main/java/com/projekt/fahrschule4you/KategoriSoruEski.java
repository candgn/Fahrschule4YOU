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
import android.widget.CheckBox;
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

public class KategoriSoruEski extends AppCompatActivity {
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    String kategori,kidst,dogrucevapa,dogrucevapb,dogrucevapc,secim_a="x",secim_b="x",secim_c="x",resim,verilen_cevap="x";
    int position;
    int play_kontrol=0;
    ArrayList<String> idlist,idListisaretli;
    TextView soru,asikki,bsikki,csikki,sorusayisi,isarettext,dogrucevaptext,input_cevap,soru_bilgi,sorudogrumu;

    ImageView ileriIv,imageView,geriIv,resim_kapatma,imageView2;
    ProgressBar progressBar;
    RelativeLayout prgslyt;

    CheckBox checkBoxA,checkBoxB,checkBoxC,checkMarkieren,dogrucevap1,dogrucevap2,dogrucevap3;
    LinearLayout lina,linb,linc,lind;
    VideoView videoview;
    ImageButton zoom,pause,replay;

    String geldigi_yer,isaretli_sorular_titel,puan,puan_text,sorudogrumust,sorudogrumust2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_soru_eski);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        soru=(TextView)findViewById(R.id.textView9);
        asikki=(TextView)findViewById(R.id.textView14);
        bsikki=(TextView)findViewById(R.id.textView16);
        csikki=(TextView)findViewById(R.id.textView15);
        soru_bilgi=(TextView)findViewById(R.id.soru_bilgi);
        sorudogrumu=(TextView)findViewById(R.id.sorudogrumu);

        ileriIv=(ImageView)findViewById(R.id.ileri);
        geriIv=(ImageView)findViewById(R.id.geri);
        imageView=(ImageView)findViewById(R.id.imageView4);
        sorusayisi=(TextView)findViewById(R.id.textView8);

        checkBoxA=(CheckBox)findViewById(R.id.checkBox2);
        checkBoxB=(CheckBox)findViewById(R.id.checkBox3);
        checkBoxC=(CheckBox)findViewById(R.id.checkBox4);

        dogrucevap1=(CheckBox)findViewById(R.id.checkBoxcevap1);
        dogrucevap2=(CheckBox)findViewById(R.id.checkBoxcevap2);
        dogrucevap3=(CheckBox)findViewById(R.id.checkBoxcevap3);

        lina=(LinearLayout)findViewById(R.id.lineara);
        linb=(LinearLayout)findViewById(R.id.linearb);
        linc=(LinearLayout)findViewById(R.id.linearc);
        lind=(LinearLayout)findViewById(R.id.lind);

        checkMarkieren=(CheckBox)findViewById(R.id.markieren);
        isarettext=(TextView)findViewById(R.id.isarettext);

        videoview=(VideoView)findViewById(R.id.videoView);
        pause=(ImageButton)findViewById(R.id.pause);
        replay=(ImageButton)findViewById(R.id.refresh);



        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        prgslyt = (RelativeLayout)findViewById(R.id.prgs);

        zoom=(ImageButton)findViewById(R.id.zoom);
        resim_kapatma=(ImageButton)findViewById(R.id.imageButton);
        imageView2=(ImageView)findViewById(R.id.imageView5);
        input_cevap=(TextView)findViewById(R.id.input_answer);
        dogrucevaptext=(TextView)findViewById(R.id.correct_answer);



        idlist = new ArrayList<>();
        idListisaretli = new ArrayList<>();

        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            kategori = extras.getString("kategori");
            kidst = extras.getString("kid");
            idlist = extras.getStringArrayList("idlist");
            position = extras.getInt("position");
            geldigi_yer = extras.getString("geldigi_yer");
        }
        System.out.println("KAtegorisorueski islist gelen" + kategori);




        sorusayisi.setText(position+1+"/"+idlist.size());

        readIsaretliID();

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




        readVerilenCevap();

        readHeroes();

        ileriIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), KategoriSoruEski.class);
                intent.putExtra("kategori",kategori);
                intent.putStringArrayListExtra("idlist",idlist);
                intent.putExtra("position",position+1);
                intent.putExtra("kid",kidst);
                intent.putExtra("geldigi_yer","gozden_gecir");
                if (geldigi_yer.equals("isaretli")){
                intent.putExtra("geldigi_yer","isaretli");}
                startActivity(intent);
            }
        });

        geriIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), KategoriSoruEski.class);
                intent.putExtra("kategori",kategori);
                intent.putStringArrayListExtra("idlist",idlist);
                intent.putExtra("position",position-1);
                intent.putExtra("kid",kidst);
                intent.putExtra("geldigi_yer","gozden_gecir");
                if (geldigi_yer.equals("isaretli")){intent.putExtra("geldigi_yer","isaretli");}
                startActivity(intent);

            }
        });

        if (position+1>=idlist.size()){

            ileriIv.setVisibility(View.INVISIBLE);
        }
        if (position==0){
            geriIv.setVisibility(View.INVISIBLE);
        }

        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");

        if (dil.equals("tr")){

            isarettext.setText(getText(R.string.isarettext_tr));
            isaretli_sorular_titel=getResources().getString(R.string.title_isaretli_sorular_tr);
            puan_text=getResources().getString(R.string.puanliksoru_tr);
            sorudogrumust=getResources().getString(R.string.tebrikler_soruyu_dogru_cozdunuz_tr);
            sorudogrumust2=getResources().getString(R.string.maalesef_yanlis_cozdunuz_tr);


        }else if(dil.equals("de")){
            isarettext.setText(getText(R.string.isarettext_de));
            isaretli_sorular_titel=getResources().getString(R.string.title_isaretli_sorular_de);
            puan_text=getResources().getString(R.string.puanliksoru_de);
            sorudogrumust=getResources().getString(R.string.tebrikler_soruyu_dogru_cozdunuz_de);
            sorudogrumust2=getResources().getString(R.string.maalesef_yanlis_cozdunuz_de);
        }else if(dil.equals("en")){
            isarettext.setText(getText(R.string.isarettext_en));
            isaretli_sorular_titel=getResources().getString(R.string.title_isaretli_sorular_en);
            puan_text=getResources().getString(R.string.puanliksoru_en);
            sorudogrumust=getResources().getString(R.string.tebrikler_soruyu_dogru_cozdunuz_en);
            sorudogrumust2=getResources().getString(R.string.maalesef_yanlis_cozdunuz_en);
        }


        if (geldigi_yer.equals("isaretli")){
            setTitle(isaretli_sorular_titel);
            System.out.println("titel"+isaretli_sorular_titel);
            setTitle(Html.fromHtml("<font color='#ffffff'>"+isaretli_sorular_titel+" </font>"));
        }else{
            setTitle(kategori );
        }


    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                if (geldigi_yer.equals("isaretli")){
                    Intent intent = new Intent(getApplicationContext(),IsaretliSorular.class);
                    intent.putExtra("kid",kidst);
                    startActivity(intent);

                }else{
                    Intent intent = new Intent(getApplicationContext(),KategoriDetay.class);
                    intent.putExtra("kid",kidst);
                    intent.putExtra("kategori",kategori);
                    startActivity(intent);
                }



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



    private void refreshHeroList(JSONArray heroes) throws JSONException {

        for (int i = 0; i < heroes.length(); i++) {
            //getting each hero object
            JSONObject obj = heroes.getJSONObject(i);
            String id=String.valueOf(obj.getInt("id"));




           /* System.out.println("idlist size" + idlist.size());

            System.out.println("id.get position: " + idlist.get(position) );*/

            System.out.println("Kategori soru eski refresh hero list idlist" +  idlist + "position: " + position +"id.get position: " + idlist.get(position));
            System.out.println("id: " + id);
            if(idlist.size()>0) {
                if (idlist.get(position).equals(id)) {





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
                    System.out.println("a şıkkı cevap: "+ asikki.toString());
                    soru_bilgi.setText(kategori+" - "+puan+" "+puan_text);


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

                    }
                    //2 şıklı
                    else if(sik_sayisi==2){
                        linc.setVisibility(View.GONE);
                    }
                    //1 şıklı
                    else if(sik_sayisi==1){
                        lina.setVisibility(View.GONE);
                        linb.setVisibility(View.GONE);
                        linc.setVisibility(View.GONE);
                        lind.setVisibility(View.VISIBLE);
                        input_cevap.setText(verilen_cevap);
                        dogrucevaptext.setText(dogrucevapa);
                    }

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
                    System.out.println("verilen cevap" + secim_a);



                    int soru_dogruluk=1;

                    if(dogrucevapa.equals("1")){
                        dogrucevap1.setChecked(true);
                        if (secim_a=="1"){

                            lina.setBackground(getDrawable(R.drawable.yesil_buton));
                            checkBoxA.setChecked(true);


                        }else {
                            lina.setBackground(getDrawable(R.drawable.kirmizi_buton));
                            soru_dogruluk=soru_dogruluk*0;



                        }
                    }else  if(dogrucevapa.equals("0")){
                        if (secim_a=="1"){
                            lina.setBackground(getDrawable(R.drawable.kirmizi_buton));
                            soru_dogruluk=soru_dogruluk*0;
                            checkBoxA.setChecked(true);


                        }else {
                            lina.setBackground(getDrawable(R.drawable.yesil_buton));




                        }
                    }
                    if(dogrucevapb.equals("1")){
                        dogrucevap2.setChecked(true);
                        if (secim_b=="1"){
                            linb.setBackground(getDrawable(R.drawable.yesil_buton));
                            checkBoxB.setChecked(true);


                        }else {

                            soru_dogruluk=soru_dogruluk*0;
                            linb.setBackground(getDrawable(R.drawable.kirmizi_buton));



                        }
                    } else if(dogrucevapb.equals("0")){
                        if (secim_b=="1"){

                            soru_dogruluk=soru_dogruluk*0;
                            checkBoxB.setChecked(true);
                            linb.setBackground(getDrawable(R.drawable.kirmizi_buton));


                        }else {
                            linb.setBackground(getDrawable(R.drawable.yesil_buton));





                        }
                    }
                    if(dogrucevapc.equals("1")){
                        dogrucevap3.setChecked(true);
                        if (secim_c=="1"){

                            linc.setBackground(getDrawable(R.drawable.yesil_buton));

                            checkBoxC.setChecked(true);


                        }else {

                            soru_dogruluk=soru_dogruluk*0;
                            linc.setBackground(getDrawable(R.drawable.kirmizi_buton));



                        }

                    }else if(dogrucevapc.equals("0")){
                        if (secim_c=="1"){

                            soru_dogruluk=soru_dogruluk*0;
                            checkBoxC.setChecked(true);
                            linc.setBackground(getDrawable(R.drawable.kirmizi_buton));


                        }else {

                            linc.setBackground(getDrawable(R.drawable.yesil_buton));




                        }

                    }
                    dogrucevaptext.setBackground(getDrawable(R.drawable.yesil_buton));
                    if(dogrucevapa.toLowerCase().equals(input_cevap.getText().toString().toLowerCase())){
                        input_cevap.setBackground(getDrawable(R.drawable.yesil_buton));
                    }else{
                        input_cevap.setBackground(getDrawable(R.drawable.kirmizi_buton));
                    }
                    checkBoxA.setEnabled(false);
                    checkBoxB.setEnabled(false);
                    checkBoxC.setEnabled(false);
                    dogrucevap1.setEnabled(false);
                    dogrucevap2.setEnabled(false);
                    dogrucevap3.setEnabled(false);
                    if (soru_dogruluk==0){

                        sorudogrumu.setText(sorudogrumust2);
                        sorudogrumu.setTextColor(0xAAFF0000);
                    }else{
                        sorudogrumu.setText(sorudogrumust);
                        sorudogrumu.setTextColor(0xAA008000);

                    }

                    String verilen_a,verilen_b,verilen_c;
                    System.out.println("seçim_a; " + secim_a);
                    System.out.println("seçim_b; " + secim_b);
                    System.out.println("seçim_c; " + secim_c);
                    if (secim_a=="1"){
                        checkBoxA.setChecked(true);
                    }else{
                        checkBoxA.setChecked(false);
                    }

                    if (secim_b=="1"){
                        checkBoxB.setChecked(true);

                    }else{
                        checkBoxB.setChecked(false);

                    }
                    if (secim_c=="1"){
                        checkBoxC.setChecked(true);
                    }else{
                        checkBoxC.setChecked(false);

                    }

                }
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
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_READ_TOPLAM_VERILEN_CEVAP+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_READ_TOPLAM_VERILEN_CEVAP+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_READ_TOPLAM_VERILEN_CEVAP+"&dil=en", params, CODE_POST_REQUEST);
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

                    JSONObject verilencevap = object.getJSONObject("verilen_cevap");
                    //JSONObject obj = verilencevap.getJSONObject(0);

                    System.out.println("verilencevaparray: " + verilencevap);



                    if (verilencevap.getString("b_sikki_verilen_cevap").equals("")&&verilencevap.getString("c_sikki_verilen_cevap").equals("")){
                        verilen_cevap=verilencevap.getString("a_sikki_verilen_cevap");

                    }else{
                        if (verilencevap.getString("a_sikki_verilen_cevap").equals("1")){
                            secim_a="1";

                        }else{
                            secim_a="0";
                        }
                        if (verilencevap.getString("b_sikki_verilen_cevap").equals("1")){
                            secim_b="1";

                        }else{
                            secim_b="0";
                        }
                        if (verilencevap.getString("c_sikki_verilen_cevap").equals("1")){
                            secim_c="1";
                        }else{
                            secim_c="0";
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


}
