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

public class HazirlikEskiSoru extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    String kategori,kidst,dogrucevapa,dogrucevapb,dogrucevapc,secim_a="x",secim_b="x",secim_c="x",verilen_cevap="x",resim,test_id;
    int position;
    ArrayList<String> idlist;
    TextView soru,asikki,bsikki,csikki,sorusayisi,dogrucevaptext,input_cevap;

    ImageView ileriIv,imageView,geriIv,resim_kapatma,imageView2;

    ProgressBar progressBar;
    RelativeLayout prgslyt;

    CheckBox checkBoxA,checkBoxB,checkBoxC;
    LinearLayout lina,linb,linc,lind;
    VideoView videoview;
    ImageButton zoom,pause,replay;
    int play_kontrol=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hazirlik_eski_soru);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        soru=(TextView)findViewById(R.id.textView9);
        asikki=(TextView)findViewById(R.id.textView14);
        bsikki=(TextView)findViewById(R.id.textView16);
        csikki=(TextView)findViewById(R.id.textView15);

        ileriIv=(ImageView)findViewById(R.id.ileri);
        geriIv=(ImageView)findViewById(R.id.geri);
        imageView=(ImageView)findViewById(R.id.imageView4);
        sorusayisi=(TextView)findViewById(R.id.textView8);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        prgslyt = (RelativeLayout)findViewById(R.id.prgs);

        checkBoxA=(CheckBox)findViewById(R.id.checkBox2);
        checkBoxB=(CheckBox)findViewById(R.id.checkBox3);
        checkBoxC=(CheckBox)findViewById(R.id.checkBox4);
        lina=(LinearLayout)findViewById(R.id.lineara);
        linb=(LinearLayout)findViewById(R.id.linearb);
        linc=(LinearLayout)findViewById(R.id.linearc);
        lind=(LinearLayout)findViewById(R.id.lind);
        videoview=(VideoView)findViewById(R.id.videoView);
        zoom=(ImageButton)findViewById(R.id.zoom);
        pause=(ImageButton)findViewById(R.id.pause);
        replay=(ImageButton)findViewById(R.id.refresh);
        resim_kapatma=(ImageButton)findViewById(R.id.imageButton);
        imageView2=(ImageView)findViewById(R.id.imageView5);
        dogrucevaptext=(TextView)findViewById(R.id.correct_answer);
        input_cevap=(TextView)findViewById(R.id.input_answer);

        idlist = new ArrayList<>();

        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            kidst = extras.getString("kid");
            idlist = extras.getStringArrayList("idlist");
            position = extras.getInt("position");
            test_id=extras.getString("test_id");
        }
        System.out.println("test_idxxx"+test_id);


        sorusayisi.setText(position+1+"/"+idlist.size());




        readVerilenCevap();

        readHeroes();

        ileriIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), HazirlikEskiSoru.class);
                intent.putStringArrayListExtra("idlist",idlist);
                intent.putExtra("position",position+1);
                intent.putExtra("kid",kidst);
                intent.putExtra("test_id",test_id);
                startActivity(intent);
            }
        });

        geriIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), HazirlikEskiSoru.class);
                intent.putStringArrayListExtra("idlist",idlist);
                intent.putExtra("position",position-1);
                intent.putExtra("kid",kidst);
                startActivity(intent);

            }
        });

        if (position+1>=idlist.size()){

            ileriIv.setVisibility(View.INVISIBLE);
        }
        if (position==0){
            geriIv.setVisibility(View.INVISIBLE);
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


    } private void readHeroes() {
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

                    System.out.println("doğru cevap" + obj.getString("soru"));



                    if(dogrucevapa.equals("1")){
                        if (secim_a=="1"){

                            lina.setBackground(getDrawable(R.drawable.yesil_buton));
                            checkBoxA.setChecked(true);


                        }else {

                            lina.setBackground(getDrawable(R.drawable.kirmizi_buton));



                        }
                    }else  if(dogrucevapa.equals("0")){
                        if (secim_a=="1"){

                            lina.setBackground(getDrawable(R.drawable.kirmizi_buton));
                            checkBoxA.setChecked(true);


                        }else {

                            lina.setBackground(getDrawable(R.drawable.yesil_buton));



                        }
                    }
                    if(dogrucevapb.equals("1")){
                        if (secim_b=="1"){

                            linb.setBackground(getDrawable(R.drawable.yesil_buton));
                            checkBoxB.setChecked(true);


                        }else {

                            linb.setBackground(getDrawable(R.drawable.kirmizi_buton));



                        }
                    } else if(dogrucevapb.equals("0")){
                        if (secim_b=="1"){

                            linb.setBackground(getDrawable(R.drawable.kirmizi_buton));
                            checkBoxB.setChecked(true);


                        }else {

                            linb.setBackground(getDrawable(R.drawable.yesil_buton));



                        }
                    }
                    if(dogrucevapc.equals("1")){
                        if (secim_c=="1"){

                            linc.setBackground(getDrawable(R.drawable.yesil_buton));
                            checkBoxC.setChecked(true);


                        }else {

                            linc.setBackground(getDrawable(R.drawable.kirmizi_buton));



                        }

                    }else if(dogrucevapc.equals("0")){
                        if (secim_c=="1"){

                            linc.setBackground(getDrawable(R.drawable.kirmizi_buton));
                            checkBoxC.setChecked(true);


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
        params.put("test_id", test_id);
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

                    System.out.println("verilencevaparray: " + verilencevap.getString("a_sikki_verilen_cevap"));

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
}
