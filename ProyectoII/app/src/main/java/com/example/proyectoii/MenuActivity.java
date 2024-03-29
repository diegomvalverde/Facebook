package com.example.proyectoii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
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
import com.example.proyectoii.ProfileUtils.ProfileEditor;
import com.example.proyectoii.Objetos.Usuario;
import com.example.proyectoii.Utils.TabAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MenuActivity extends AppCompatActivity {
    public static Usuario usuario = null;
    private static TabAdapter mTabAdapter;
    private static ViewPager mViewPager;
    private static RelativeLayout cargandoLayout,errorLayout;
    private static Context mContext;
    private static TabLayout tabLayout;
    private static Boolean isGettingUser = false;
    private static ConnectivityManager connMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ConnectivityManager connectivityManager;
        mTabAdapter = new TabAdapter(getSupportFragmentManager());
        mViewPager =  findViewById(R.id.contenido);
        cargandoLayout = findViewById(R.id.layout_Menu_Transparent);
        errorLayout = findViewById(R.id.layout_Menu_Error);
        mContext = getApplicationContext();
        tabLayout = findViewById(R.id.tabs);
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (checkNetworkConnectionStatus()) {
            if (!isGettingUser) {
                cargandoLayout.setVisibility(View.VISIBLE);
                isGettingUser = true;
                getCurrentUser(false);
            }
        }
        else{
            cargandoLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.GONE);
        }



    }


    @Override
    protected void onResume() {
        super.onResume();
        if (checkNetworkConnectionStatus()) {
            if (!isGettingUser) {
                cargandoLayout.setVisibility(View.VISIBLE);
                isGettingUser = true;
                getCurrentUser(false);
            }
        }
        else{
            cargandoLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkNetworkConnectionStatus()) {
            if (!isGettingUser) {
                cargandoLayout.setVisibility(View.VISIBLE);
                isGettingUser = true;
                getCurrentUser(false);
            }
        }
        else{
            cargandoLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        isGettingUser = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isGettingUser = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isGettingUser = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (checkNetworkConnectionStatus()) {
            if (!isGettingUser) {
                cargandoLayout.setVisibility(View.VISIBLE);
                isGettingUser = true;
                getCurrentUser(false);
            }
        }
        else{
            cargandoLayout.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Configura el toolbar
     */
    private static void configurarToolbar(ViewPager viewPager){

        mTabAdapter.addFragment(new HomeFragment());
        mTabAdapter.addFragment(new ProfileFragment());
        mTabAdapter.addFragment(new FriendsFragment());
//        mTabAdapter.addFragment(new NotificationFragment());
        mTabAdapter.addFragment(new SearchFragment());
        mTabAdapter.addFragment(new OptionsFragment());
        viewPager.setAdapter(mTabAdapter);



        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        int tabIconColor = ContextCompat.getColor(mContext, R.color.DarkCyan);
        tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconColor,PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_profile);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_friends);
//        tabLayout.getTabAt(3).setIcon(R.drawable.ic_notification);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_search);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_options);

        tabLayout.addOnTabSelectedListener( new TabLayout.ViewPagerOnTabSelectedListener(viewPager){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                if(!checkNetworkConnectionStatus()){
                    errorLayout.setVisibility(View.VISIBLE);
                }
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

    public static void setTabSelected(int i){

        TabLayout.Tab tab = tabLayout.getTabAt(i);
        tab.select();



    }

    public static void getCurrentUser(boolean actualizar){
        if(usuario == null || actualizar) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            DatabaseReference myRef = database.getReference("usuarios/" + firebaseAuth.getCurrentUser().getUid());
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            firebaseUser.updateEmail("Nuevo email");
            firebaseAuth.updateCurrentUser(firebaseUser);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    usuario = dataSnapshot.getValue(Usuario.class);

                    if (mTabAdapter.getCount() == 0) {
                        configurarToolbar(mViewPager);
                    }

                    cargandoLayout.setVisibility(View.GONE);
                    isGettingUser = false;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });
        }
        else{
            cargandoLayout.setVisibility(View.GONE);

        }
    }


    public void OnClickReconnect(View view){
        if (checkNetworkConnectionStatus()) {
            errorLayout.setVisibility(View.GONE);
            if (!isGettingUser) {
                cargandoLayout.setVisibility(View.VISIBLE);
                isGettingUser = true;
                getCurrentUser(false);
            }
        }
        else{
            cargandoLayout.setVisibility(View.INVISIBLE);
            errorLayout.setVisibility(View.VISIBLE);
        }
    }


    private static boolean checkNetworkConnectionStatus() {
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()){ //connected with either mobile or wifi
            return  true;
        }
        else { //no internet connection
            return false;
        }
    }


    //    For Profile Fragment
    public void editProfile(View view)
    {
        Intent intent = new Intent(this, ProfileEditor.class);
        startActivity(intent);
    }

}
