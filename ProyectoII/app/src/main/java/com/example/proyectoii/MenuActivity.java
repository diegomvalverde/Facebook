package com.example.proyectoii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.PorterDuff;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

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
    public static Usuario usuario = null;
    private static TabAdapter mTabAdapter;
    private static ViewPager mViewPager;
    private static RelativeLayout relativeLayout;
    private static Context mContext;
    private static TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTabAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager =  findViewById(R.id.contenido);
        relativeLayout = findViewById(R.id.layout_Menu_Transparent);
        mContext = getApplicationContext();
        tabLayout = findViewById(R.id.tabs);




    }

    @Override
    protected void onResume() {
        super.onResume();
        relativeLayout.setVisibility(View.VISIBLE);
        getCurrentUser(false);
    }



    /**
     * Configura el toolbar
     */
    private static void configurarToolbar(ViewPager viewPager){

        mTabAdapter.addFragment(new HomeFragment());
        mTabAdapter.addFragment(new ProfileFragment());
        mTabAdapter.addFragment(new FriendsFragment());
        mTabAdapter.addFragment(new NotificationFragment());
        mTabAdapter.addFragment(new SearchFragment());
        mTabAdapter.addFragment(new OptionsFragment());
        viewPager.setAdapter(mTabAdapter);



        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        int tabIconColor = ContextCompat.getColor(mContext, R.color.DarkCyan);
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
                int tabIconColor = ContextCompat.getColor(mContext, R.color.DarkCyan);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                int tabIconColor = ContextCompat.getColor(mContext, R.color.ic_gris);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        }
        );


    }

    public static void getCurrentUser(boolean actualizar){
        if(usuario == null || actualizar) {
            Log.i("Resultados","Entre getCurrent");
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            DatabaseReference myRef = database.getReference("usuarios/" + firebaseAuth.getCurrentUser().getUid());

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    usuario = dataSnapshot.getValue(Usuario.class);
                    Log.i("Resultados","Ya tengo el usuario");

                    if (mTabAdapter.getCount() == 0) {
                        configurarToolbar(mViewPager);
                    }

                    relativeLayout.setVisibility(View.GONE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            Log.i("Resultados","El usuario ya estaba en la app");
            relativeLayout.setVisibility(View.GONE);

        }
    }


}
