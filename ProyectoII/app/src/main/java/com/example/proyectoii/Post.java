package com.example.proyectoii;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.google.firebase.auth.FirebaseAuth;

public class Post extends AppCompatActivity {

    private static int IMAGE = 1;
    private static int VIDEO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Button back = findViewById(R.id.btn_post_back);
        ImageButton addImg = findViewById(R.id.addimagen);
        ImageButton addvid = findViewById(R.id.addvideourl);
        Button publishbtn = findViewById(R.id.publicar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open gallery to select image
                setLayout(IMAGE);
            }
        });

        addvid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add video url
                setLayout(VIDEO);
            }
        });

        publishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // publish post values to database.
                publish();
            }
        });
    }

    public void publish() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

    }

    public void setLayout(int media) {
        ImageView addIcon = findViewById(R.id.addimagen);
        ImageView addVidIcon = findViewById(R.id.addvideourl);
        EditText addLink = findViewById(R.id.ytlink);

        if (media == IMAGE) {
            addLink.setVisibility(View.INVISIBLE);
            addIcon.setImageResource(R.drawable.image);
            addVidIcon.setImageResource(R.drawable.videoplayerdisabled);
        } else if (media == VIDEO) {
            addLink.setVisibility(View.VISIBLE);
            addIcon.setImageResource(R.drawable.imagedisabled);
            addVidIcon.setImageResource(R.drawable.videoplayer);
        }
    }

}
