package com.merw_okuw_merkezi.bellikler;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.merw_okuw_merkezi.bellikler.fragments.FragmentAddNote;
import com.merw_okuw_merkezi.bellikler.fragments.FragmentHome;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        setNav();
    }

    private void setNav() {
        setFragment(FragmentHome.newInstance(), FragmentHome.class.getSimpleName());

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                setFragment(FragmentHome.newInstance(), FragmentHome.class.getSimpleName());
                return true;
            }
            setFragment(FragmentAddNote.newInstance(-1), FragmentAddNote.class.getSimpleName());
            return true;
        });
    }

    private void setFragment(Fragment fragment, String tagFragmentName) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            if (fragmentTemp == null) return;
            fragmentTransaction.add(R.id.fragment_container, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitAllowingStateLoss();

    }

    private void initComponents() {
        bottomNavigationView = findViewById(R.id.bottom_nav);
    }

    @Override
    public void onBackPressed() {
        Fragment home = getSupportFragmentManager().findFragmentByTag(FragmentHome.class.getSimpleName());
        Fragment add = getSupportFragmentManager().findFragmentByTag(FragmentAddNote.class.getSimpleName());
        Fragment edit = getSupportFragmentManager().findFragmentByTag(FragmentAddNote.class.getName());

        //bular bizin fragment manager de bar bolan fragmentlarimiz yokardakylar
        //add note duran wagty yza bassak home geler yaly etdik shuwagt

        if (edit != null && edit.isVisible()) {
            super.onBackPressed();
            return;
        }

        if (home != null && home.isVisible()) {
            finish();
        } else if (add != null && add.isVisible()) {
            setFragment(home, home.getClass().getSimpleName());
        } else super.onBackPressed();
    }
}