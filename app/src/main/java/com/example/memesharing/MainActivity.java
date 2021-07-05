package com.example.memesharing;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.service.chooser.ChooserTargetService;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    Button next,share;
    ImageView im;
    ProgressBar p;
    String currentmemeurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        next=findViewById(R.id.BtnNext);
        share=findViewById(R.id.btnshare);
        im=findViewById(R.id.imageView);
        p=findViewById(R.id.progressBar);

        load();
    }
    private void load(){
         //next.setEnabled(false);
        //share.setEnabled(false);
        p.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
       currentmemeurl ="https://meme-api.herokuapp.com/gimme";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, currentmemeurl, null, new Response.Listener<JSONObject>() {
                    ImageView imageView=(ImageView)findViewById(R.id.imageView);
                    @Override
                    public void onResponse(JSONObject response) {
                        String url= null;
                        try {
                            url = response.getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Glide.with(MainActivity.this).load(url).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                p.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                p.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(imageView);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Something wrong!!!", Toast.LENGTH_SHORT).show();
                    }
                });;

        queue.add(jsonObjectRequest);
    }
    public void showNextMeme(View view) {
        load();
    }

    public void shareMeme(View view) {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,"Hey Checkout!"+currentmemeurl);
        startActivity(Intent.createChooser(i,"share this!!"));
    }
}
