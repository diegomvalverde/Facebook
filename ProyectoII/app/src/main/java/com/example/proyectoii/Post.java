package com.example.proyectoii;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.proyectoii.Objetos.PostObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Post extends AppCompatActivity {

    private static int TEXT = 0;
    private static int IMAGE = 1;
    private static int VIDEO = 2;
    private static final int GALLERY_REQUEST_CODE = 14;
    private ImageView preview;
    private boolean imgselected = false;
    private boolean vidselected = false;
    private Uri selectedImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Button back = findViewById(R.id.btn_post_back);
        ImageButton addImg = findViewById(R.id.addimagen);
        ImageButton addvid = findViewById(R.id.addvideourl);
        Button publishbtn = findViewById(R.id.publicar);
        preview = findViewById(R.id.imgpreview);

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
                pickFromGallery();
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
        EditText post = findViewById(R.id.postTxt);
        if (!post.getText().toString().equals("")) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            PostObject newPost = new PostObject(firebaseAuth.getUid(), post.getText().toString());
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            String key = mDatabase.child("posts").push().getKey();
            if (imgselected) {
                uploadImage(key);
                newPost.setTipo("IMAGE");
            } else if (vidselected) {
                EditText vidurl = findViewById(R.id.ytlink);
                newPost.setVideoUrl(vidurl.getText().toString());
                newPost.setTipo("VIDEO");
            } else {
                newPost.setTipo("TEXT");
            }
            mDatabase.child("posts").child(key).setValue(newPost)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Post.this, "Post publicado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Post.this, "No fue posible publicar el post", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void setLayout(int media) {
        ImageView addIcon = findViewById(R.id.addimagen);
        ImageView addVidIcon = findViewById(R.id.addvideourl);
        EditText addLink = findViewById(R.id.ytlink);

        if (media == IMAGE) {
            addLink.setVisibility(View.INVISIBLE);
            addIcon.setImageResource(R.drawable.image);
            addVidIcon.setImageResource(R.drawable.videoplayerdisabled);
            imgselected = true;
            vidselected = false;
        } else if (media == VIDEO) {
            addLink.setVisibility(View.VISIBLE);
            addIcon.setImageResource(R.drawable.imagedisabled);
            addVidIcon.setImageResource(R.drawable.videoplayer);
            preview.setVisibility(View.INVISIBLE);
            vidselected = true;
            imgselected = false;
        }
    }

    public void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String mimeTypes[] = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    public String uploadImage(final String key) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uri = "FotosPublicaciones/"+mAuth.getUid()+"/"+UUID.randomUUID().toString();
        StorageReference storage = FirebaseStorage.getInstance().getReference();

        final StorageReference ref = storage.child(uri);
        ref.putFile(selectedImg)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("posts").child(key).child("imageURI").setValue(uri.toString());
                            }
                        });
                        Toast.makeText(Post.this, "Foto publicada", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Post.this, "No se pudo publicar la imagen", Toast.LENGTH_SHORT).show();
                    }
                });
        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE :
                    selectedImg = data.getData();
                    preview.setImageURI(selectedImg);
                    break;
            }
    }

}
