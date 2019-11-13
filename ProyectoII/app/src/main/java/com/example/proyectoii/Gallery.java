package com.example.proyectoii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.proyectoii.Objetos.PostObject;
import com.example.proyectoii.Utils.RecyclerViewImgAdapter;
import com.example.proyectoii.Utils.RecyclerViewPostAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Gallery extends AppCompatActivity {

    private String userid;
    private ArrayList<String> userImgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        userid = getIntent().getStringExtra("USERID");
        userImgs = new ArrayList<String>();
        getUserImgs();

        Button back = findViewById(R.id.btn_post_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getUserImgs() {
        userImgs.clear();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.orderByChild("posts");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dss: dataSnapshot.child("posts").getChildren()) {
                    PostObject post = dss.getValue(PostObject.class);
                    if (post.getAuthorId().equals(userid)) {
                        if (post.getTipo().equals("IMAGE")){
                            userImgs.add(post.getImageURI());
                            setRecyclerView();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.gallery_recycler);
        RecyclerViewImgAdapter adapter = new RecyclerViewImgAdapter(getApplicationContext() ,userImgs);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
