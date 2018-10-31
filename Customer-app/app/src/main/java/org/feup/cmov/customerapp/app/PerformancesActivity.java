package org.feup.cmov.customerapp.app;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.feup.cmov.customerapp.R;
import org.feup.cmov.customerapp.adapter.PerformanceAdapter;
import org.feup.cmov.customerapp.model.Performance;

public class PerformancesActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performances);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Next Performances");

        setupRecyclerView();
        setupDrawer();
    }

    private void setupDrawer() {
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.nav_drawer_frag);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerFragment.setupDrawer(R.id.nav_drawer_frag, drawerLayout, toolbar);
    }


    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.next_performances);
        PerformanceAdapter performanceAdapter = new PerformanceAdapter(this, Performance.getData());
        recyclerView.setAdapter(performanceAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO: update the items
        return true;
    }
}
