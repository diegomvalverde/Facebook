package com.example.proyectoii.MenuFragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoii.Objetos.UserPreview;
import com.example.proyectoii.Objetos.Usuario;
import com.example.proyectoii.R;
import com.example.proyectoii.Utils.RecyclerViewUserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private CheckBox users;
    private CheckBox posts;
    private EditText search;
    private ArrayList<UserPreview> usersList;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search,container,false);

        users = view.findViewById(R.id.userson);
        users.setChecked(true);
        posts = view.findViewById(R.id.postson);
        search = view.findViewById(R.id.criteriobusqueda);
        usersList = new ArrayList<UserPreview>();

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleSelection(0);
            }
        });

        posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleSelection(1);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    search(s.toString());
                } else {
                    search("");
                }
                setAdapter();
            }
        });

        return view;
    }



    public void singleSelection(int chkbox) {
        if (chkbox==0) {
            users.setChecked(true);
            posts.setChecked(false);
        } else {
            users.setChecked(false);
            posts.setChecked(true);
        }
    }

    public void search(String s) {
        usersList.clear();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("usuarios").orderByChild("nombre")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                        String nombre = dss.child("nombre").getValue().toString() + " " + dss.child("apellido").getValue().toString();
                        final UserPreview userPreview = new UserPreview(nombre, dss.child("id").getValue().toString());
                        userPreview.setProfilepic(dss.child("linkImgPerfil").getValue().toString());
                        usersList.add(userPreview);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setAdapter() {
        RecyclerView recyclerView = view.findViewById(R.id.resultados);
        RecyclerViewUserAdapter userAdapter = new RecyclerViewUserAdapter(getContext(), usersList);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }
}

