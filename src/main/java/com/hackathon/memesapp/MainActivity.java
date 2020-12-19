package com.hackathon.memesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.DataSource;
//import com.bumptech.glide.load.engine.GlideException;
//import com.bumptech.glide.request.RequestListener;
//import com.bumptech.glide.request.target.Target;
//import kotlinx.android.synthetic.main.activity_main.*;

public class MainActivity extends AppCompatActivity {


    Button share;
    public String link;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private Button mLogoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();



        loadMeme();






//        mLogoutBtn = findViewById(R.id.logout_btn);
//
//        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mAuth.signOut();
//                sendUserToLogin();
//
//            }
//        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mCurrentUser == null){
            sendUserToLogin();
        }
    }

    private void sendUserToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    private void loadMeme(){
        final ProgressBar simpleProgressBar=(ProgressBar) findViewById(R.id.progressBar); // initiate the progress bar
        simpleProgressBar.setVisibility(View.VISIBLE);



        // Instantiate the RequestQueue.

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://meme-api.herokuapp.com/gimme";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        textView.setText("Response: " + response.toString());
                        String url= null;
                        try {
                            url = response.getString("url");
                            link = url;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        share = (Button) findViewById(R.id.shareButton);
                        share.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v){
                                Intent myIntent = new Intent(Intent.ACTION_SEND);
                                myIntent.setType("text/plain");
//                                String body = "Your body here";
//                                String sub = "Your Subject";
//                                myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
                                myIntent.putExtra(Intent.EXTRA_TEXT, link);
                                startActivity(Intent.createChooser(myIntent, "Share Using"));

                            }
                        });
                        ImageView imageView = (ImageView) findViewById(R.id.memeImageView);
                        Glide.with(getApplicationContext())
                                .load(url)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e,
                                                                Object model,
                                                                Target<Drawable> target,
                                                                boolean isFirstResource) {

                                        simpleProgressBar.setVisibility(View.GONE);
                                        return false;


                                    }


                                    @Override
                                    public boolean onResourceReady(Drawable resource,
                                                                   Object model,
                                                                   Target<Drawable> target,
                                                                   DataSource dataSource,
                                                                   boolean isFirstResource) {

                                        simpleProgressBar.setVisibility(View.GONE);
                                        return false;


                                    }
                                }).into(imageView);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }


    public void showNextMeme(View view) {
        loadMeme();
    }

    public void shareMeme(View view) {
    }
}