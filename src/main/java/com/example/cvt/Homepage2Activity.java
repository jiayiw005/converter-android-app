package com.example.cvt;

import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cvt.databinding.ActivityHomepage2Binding;

public class Homepage2Activity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomepage2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate GUI
        binding = ActivityHomepage2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarHomepage2.toolbar);

        //inflate drawer
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_profile, R.id.nav_contact, R.id.nav_collection, R.id.nav_contribution, R.id.nav_settings
                )
                .setOpenableLayout(drawer)
                .build();

        //manage navigation host view
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_homepage2);
        NavController navController = navHostFragment.getNavController();

        //manage navigation controller
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_homepage2);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}