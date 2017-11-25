package com.example.fragmenttest;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentActivity;

import android.util.Log;
import android.view.MenuItem;

import layout.HomeFragment;
import layout.BibleFragment;
import layout.TimeFragment;

//import static android.R.attr.id;
//import static com.example.fragmenttest.R.attr.layout;

public class MainActivity extends FragmentActivity {

    HomeFragment homeFragment;
    TimeFragment timeFragment;
    BibleFragment bibleFragment;

    String currentFragment;

    MyCountDownTimer myCountDownTimer;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean returnval = false;

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (currentFragment != "homeFragment") {

                        // In case this activity was started with special instructions from an
                        // Intent, pass the Intent's extras to the fragment as arguments
                        //homeFragment.setArguments(getIntent().getExtras());


                        // Add the fragment to the 'fragment_container' FrameLayout
                        //getSupportFragmentManager().beginTransaction()
                        //  .add(R.id.fragment_container, homeFragment).commit();


// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
                        transaction.replace(R.id.fragment_container, homeFragment);
                        currentFragment = "homeFragment";

// Commit the transaction

                        returnval = true;
                    }
                    break;

                case R.id.navigation_time:
                    if (currentFragment != "timeFragment") {

                        //timeFragment.setArguments(getIntent().getExtras());

                        transaction.replace(R.id.fragment_container, timeFragment);
                        currentFragment = "timeFragment";


                        returnval = true;
                    }
                    break;

                case R.id.navigation_bible:
                    if (currentFragment != "biblefragment") {

                        //settingFragment.setArguments(getIntent().getExtras());

                        transaction.replace(R.id.fragment_container, bibleFragment);
                        currentFragment = "bibleFragment";


                        returnval = true;
                    }
                    break;

            }
            //transaction.addToBackStack(null);
            transaction.commit();


            return returnval;

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (findViewById(R.id.fragment_container) != null) {


            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;

            }

            // Create a new Fragment to be placed in the activity layout
            //homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.home_fragment);
            homeFragment = new HomeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, homeFragment);
            currentFragment = "homeFragment";
            transaction.commit();

            timeFragment = new TimeFragment();
            bibleFragment = new BibleFragment();
        } else {
            Log.e("onCreate()/findViewbyId", "cannot find fragmentcontainer");

        }

        timeFragment.myCDTimerIsOn = false;


    }

    public class MyCountDownTimer extends CountDownTimer {


        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            timeFragment.cdTimerOnTick(millisUntilFinished);
        }

        @Override
        public void onFinish() {
            //timeTextView.setText("done!");
            timeFragment.cdTimerOnFinish();

            timeFragment.myCDTimerIsOn = false;
        }

    }

    public void startCDTimer (long countDownPeriod_mil) {
        if (myCountDownTimer == null) {
            myCountDownTimer = new MyCountDownTimer(countDownPeriod_mil, 1000);
            myCountDownTimer.start();
        }

        timeFragment.myCDTimerIsOn = true;
    }

    public void stopCDTimer() {
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
            myCountDownTimer = null;
        }

        timeFragment.myCDTimerIsOn = false;
    }

}
