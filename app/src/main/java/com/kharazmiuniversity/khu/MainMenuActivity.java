package com.kharazmiuniversity.khu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{


    private DrawerLayout drawer;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);



        navdrawerFunction();


        MainMenuDrawerBodyFragment mainMenuDrawerBodyFragment = new MainMenuDrawerBodyFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_menu,mainMenuDrawerBodyFragment)
                .commit();

    }




    private void navdrawerFunction()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {

        switch (item.getItemId()) {
            case R.id.nav_example1:
                Toast.makeText(getApplicationContext(),"example 1 clicked",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_example2:
                Toast.makeText(getApplicationContext(),"example 2 clicked",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_example3:
                Toast.makeText(getApplicationContext(),"example 3 clicked",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_example4:
                Toast.makeText(this, "example 4 clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_example5:
                Toast.makeText(this, "example 5 clicked", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            // exit
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }



}
