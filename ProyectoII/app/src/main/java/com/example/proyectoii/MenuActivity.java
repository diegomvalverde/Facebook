package com.example.proyectoii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.graphics.PorterDuff;
import android.os.Bundle;

import com.example.proyectoii.MenuFragments.FriendsFragment;
import com.example.proyectoii.MenuFragments.HomeFragment;
import com.example.proyectoii.MenuFragments.NotificationFragment;
import com.example.proyectoii.MenuFragments.OptionsFragment;
import com.example.proyectoii.MenuFragments.ProfileFragment;
import com.example.proyectoii.MenuFragments.SearchFragment;
import com.example.proyectoii.Objetos.Usuario;
import com.example.proyectoii.Utils.TabAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MenuActivity extends AppCompatActivity {
    public static Usuario usuario = new Usuario();
    private TabAdapter mTabAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTabAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager =  findViewById(R.id.contenido);


        configurarToolbar(mViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCurrentUser();
    }

    /**
     * Configura el toolbar
     */
    private void configurarToolbar(ViewPager viewPager){
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new ProfileFragment());
        adapter.addFragment(new FriendsFragment());
        adapter.addFragment(new NotificationFragment());
        adapter.addFragment(new SearchFragment());
        adapter.addFragment(new OptionsFragment());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.DarkCyan);
        tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconColor,PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_profile);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_friends);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_notification);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_search);
        tabLayout.getTabAt(5).setIcon(R.drawable.ic_options);

        tabLayout.addOnTabSelectedListener( new TabLayout.ViewPagerOnTabSelectedListener(viewPager){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.DarkCyan);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.ic_gris);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        }
        );


    }

    private void getCurrentUser(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference myRef = database.getReference("usuarios/"+firebaseAuth.getCurrentUser().getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuario =  dataSnapshot.getValue(Usuario.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
