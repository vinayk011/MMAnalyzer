package com.hug.mma.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.hug.mma.M2AApplication;
import com.hug.mma.R;
import com.hug.mma.databinding.ActivityHomeBinding;
import com.hug.mma.db.room.entity.Ec;
import com.hug.mma.listener.DrawerLockListener;
import com.hug.mma.preference.AppPrefConstants;
import com.hug.mma.preference.AppPreference;
import com.hug.mma.util.MyContextWrapper;
import com.hug.mma.util.Trace;

import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.NavUtils;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> implements DrawerLockListener {
    private DrawerLayout drawerLayout;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, AppPreference.getInstance().getString(AppPrefConstants.PREF_LANGUAGE, "en")));
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        //loadLocale();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        navController = Navigation.findNavController(this, R.id.home_nav_fragment);
        drawerLayout = binding.drawerLayout;
        setDestination();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setDrawerLayout(drawerLayout).build();

        // Set up ActionBar
        setSupportActionBar(binding.toolbar);
        refreshActionBar();
        //actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Set up navigation menu
        NavigationUI.setupWithNavController(binding.navigationView, navController);

        updateNavHeader();

    }

    private void refreshActionBar() {
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(AppPreference.getInstance().getBoolean(AppPrefConstants.SIGN_IN));
    }

    private void updateNavHeader() {
        try {
            View headerView = binding.navigationView.getHeaderView(0);
            TextView navUsername = (TextView) headerView.findViewById(R.id.user_no);
            navUsername.setText(AppPreference.getInstance().getString(AppPrefConstants.USER_PHONE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDestination() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.home_nav_fragment);  // Hostfragment
        NavInflater inflater = navHostFragment.getNavController().getNavInflater();
        NavGraph graph = inflater.inflate(R.navigation.navigation_graph);
        graph.setDefaultArguments(getIntent().getExtras());
        if (!AppPreference.getInstance().getBoolean(AppPrefConstants.SIGN_IN)) {
            Trace.i("");
            graph.setStartDestination(R.id.signin_fragment);
        } else {
            graph.setStartDestination(R.id.devices_fragment);
        }
        navHostFragment.getNavController().setGraph(graph);
        navHostFragment.getNavController().getGraph().setDefaultArguments(getIntent().getExtras());
        NavigationView navigationView = findViewById(R.id.navigation_view);
        NavigationUI.setupWithNavController(navigationView, navHostFragment.getNavController());
    }


    @Override
    public boolean onSupportNavigateUp() {
        super.onSupportNavigateUp();
        return navController.navigateUp();
    }

    private void loadLocale() {
        setLocale(AppPreference.getInstance().getString(AppPrefConstants.PREF_LANGUAGE, "en"));
    }

    public void setLocale(String localeStr) {
        MyContextWrapper.wrap(getBaseContext(), localeStr);
        AppPreference.getInstance().putString(AppPrefConstants.PREF_LANGUAGE, localeStr);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (R.id.devices_fragment == getNavController().getCurrentDestination().getId()) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    onBackPressed();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setDrawerLocked(boolean shouldLock) {
        if (shouldLock) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        refreshActionBar();
    }

    public NavController getNavController() {
        if (navController == null)
            navController = Navigation.findNavController(this, R.id.home_nav_fragment);
        return navController;
    }


    public void destroy() {
        AppPreference.getInstance().remove(AppPrefConstants.PREF_SUMMARY_GRAPH);
    }
}
