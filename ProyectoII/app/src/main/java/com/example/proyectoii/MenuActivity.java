package com.example.proyectoii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.graphics.PorterDuff;
import android.os.Bundle;

import com.example.proyectoii.MenuFragments.FriendsFragment;
import com.example.proyectoii.MenuFragments.HomeFragment;
import com.example.proyectoii.MenuFragments.NotificationFragment;
import com.example.proyectoii.MenuFragments.ProfileFragment;
import com.example.proyectoii.MenuFragments.SearchFragment;
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
        int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.ic_azul);
        tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconColor,PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_profile);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_friends);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_notification);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_search);

        tabLayout.addOnTabSelectedListener( new TabLayout.ViewPagerOnTabSelectedListener(viewPager){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.ic_azul);
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
}
