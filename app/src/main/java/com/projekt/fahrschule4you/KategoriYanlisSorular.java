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
import android.widget.MediaController;
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

public class KategoriYanlisSorular extends AppCompatActivity {
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    String kategori,kidst,dogrucevapa,dogrucevapb,dogrucevapc,secim="O",resim;
    int position,cevap_kontrol=0,cevap_kontrola=0,cevap_kontrolb=0,cevap_kontrolc=0;
    ArrayList<String> idlist;
    TextView soru,asikki,bsikki,csikki,sorusayisi;
    Button kontrolet;
    ImageView ileriIv,imageView,resim_kapatma,imageView2;
    ProgressBar progressBar;
    RelativeLayout prgslyt;
    CheckBox checkBoxA,checkBoxB,checkBoxC;
    LinearLayout lina,linb,linc,lind;
    VideoView videoview;
    ImageButton zoom;
    EditText input_cevap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_yanlis_sorular);


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
        videoview=(VideoView)findViewById(R.id.videoView);
        zoom=(ImageButton)findViewById(R.id.zoom);
        resim_kapatma=(ImageButton)findViewById(R.id.imageButton);
        imageView2=(ImageView)findViewById(R.id.imageView5);
        input_cevap = (EditText)findViewById(R.id.input_answer);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        prgslyt = (RelativeLayout)findViewById(R.id.prgs);

        idlist = new ArrayList<>();

        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            kategori = extras.getString("kategori");
            kidst = extras.getString("kid");
            idlist = extras.getStringArrayList("idlist");
            position = extras.getInt("position");
        }

        setTitle(kategori);

        sorusayisi.setText(position+1+"/"+idlist.size());




        readHeroes();

        ileriIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(),KategoriSoru.class);
                intent.putStringArrayListExtra("idlist",idlist);
                intent.putExtra("kategori",kategori);
                intent.putExtra("position",position+1);
                intent.putExtra("kid",kidst);
                startActivity(intent);
            }
        });


        if (position+1>=idlist.size()){

            ileriIv.setVisibility(View.INVISIBLE);
        }

        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");

        if (dil.equals("tr")){
            kontrolet.setText(R.string.cevapla_tr);

        }else if(dil.equals("de")){
            kontrolet.setText(R.string.cevapla_de);
        }else if(dil.equals("en")){
            kontrolet.setText(R.string.cevapla_en);
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
    private void soruKaydet(String soru_id,
                            String verilen_cevapa,String verilen_cevapb,String verilen_cevapc){



        HashMap<String, String> params = new HashMap<>();
        params.put("soru_id", soru_id);
        params.put("a_sikki_verilen_cevap", verilen_cevapa);
        params.put("b_sikki_verilen_cevap", verilen_cevapb);
        params.put("c_sikki_verilen_cevap", verilen_cevapc);
        params.put("kullanici_id",kidst);




        //Calling the create hero API
        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String dil = settings.getString("dil", "defaultvalue");

        if(dil.equals("tr")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_UPDATE_YANLIS_SORU_KAYIT+"&dil=tr", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("de")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_UPDATE_YANLIS_SORU_KAYIT+"&dil=de", params, CODE_POST_REQUEST);
            request.execute();

        }else if(dil.equals("en")){
            PerformNetworkRequest2 request = new PerformNetworkRequest2(Api.URL_UPDATE_YANLIS_SORU_KAYIT+"&dil=en", params, CODE_POST_REQUEST);
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
                        zoom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(),FullScreenVideo.class);
                                intent.putExtra("video_url","http://panel.4you-fahrschule.de/"+resim);
                                startActivity(intent);


                            }
                        });
                        try {
                            // Start the MediaController
                            MediaController mediacontroller = new MediaController(this);
                            mediacontroller.setAnchorView(videoview);
                            // Get the URL from String videoUrl
                            Uri video = Uri.parse("http://panel.4you-fahrschule.de/"+resim);
                            videoview.setMediaController(mediacontroller);
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
                    kontrolet.setVisibility(View.INVISIBLE);

                    if (position+1<idlist.size()){
                        ileriIv.setVisibility(View.VISIBLE);
                    }
                    cevap_kontrol=1;
                    System.out.println("cevap kontorl cevapla" + cevap_kontrol);



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

                    }
                    checkBoxA.setEnabled(false);
                    checkBoxB.setEnabled(false);
                    checkBoxC.setEnabled(false);

                    if(dogrucevapa.toLowerCase().equals(input_cevap.getText().toString().toLowerCase())){
                        lind.setBackground(getDrawable(R.drawable.yesil_buton));
                        System.out.println("doğru çözüm");
                    }

                    String verilen_a,verilen_b,verilen_c;

                    if (lind.getVisibility()==View.VISIBLE){
                        verilen_a=input_cevap.getText().toString();
                        verilen_b="";
                        verilen_c="";
                    }else{
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

                    soruKaydet(String.valueOf(idlist.get(position)),verilen_a,verilen_b,verilen_c);




                }




            });


        }






    }
}
