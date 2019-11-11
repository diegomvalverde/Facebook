package com.example.proyectoii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

<<<<<<< Updated upstream
import android.graphics.PorterDuff;
import android.os.Bundle;
=======
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
>>>>>>> Stashed changes

import com.example.proyectoii.MenuFragments.FriendsFragment;
import com.example.proyectoii.MenuFragments.HomeFragment;
import com.example.proyectoii.MenuFragments.NotificationFragment;
import com.example.proyectoii.MenuFragments.ProfileFragment;
import com.example.proyectoii.MenuFragments.SearchFragment;
<<<<<<< Updated upstream
=======
import com.example.proyectoii.Objetos.Usuario;
import com.example.proyectoii.ProfileUtils.ProfileEditor;
>>>>>>> Stashed changes
import com.example.proyectoii.Utils.TabAdapter;
import com.google.android.material.tabs.TabLayout;


public class MenuActivity extends AppCompatActivity {

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
<<<<<<< Updated upstream
=======

    public static void setTabSelected(int i){

        TabLayout.Tab tab = tabLayout.getTabAt(i);
        tab.select();



    }

    public static void getCurrentUser(boolean actualizar){
        if(usuario == null || actualizar) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            DatabaseReference myRef = database.getReference("usuarios/" + firebaseAuth.getCurrentUser().getUid());

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


    private boolean checkNetworkConnectionStatus() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
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


>>>>>>> Stashed changes
}
