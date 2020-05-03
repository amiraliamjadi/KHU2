package com.kharazmiuniversity.khu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (MyPreferenceManager.getInstance(MainActivity.this).getAccessToken() == null)
        {
            openLoginFragment();

        } else {

            openMainMenuActivity();
        }



    }


    private void openLoginFragment()
    {
        LoginFragment loginFragment = new LoginFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,loginFragment)
                .commit();
    }

    private void openMainMenuActivity()
    {
        Intent mainMenuIntent = new Intent(this, MainMenuActivity.class);

        this.startActivity(mainMenuIntent);
    }


    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(broadcastReceiver);

    }


    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(

                broadcastReceiver , new IntentFilter("login_finished")

        );

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // dar frame layout view E ke darim marboot be login va harchi hast ra pak kon
            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment_container);
            frameLayout.removeAllViews();


            // layout e main menu boodan ra namayesh bede
            openMainMenuActivity();
        }
    };

}
