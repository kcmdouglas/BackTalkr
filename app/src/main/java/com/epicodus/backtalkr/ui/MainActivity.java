package com.epicodus.backtalkr.ui;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.epicodus.backtalkr.BackTalkrApplication;
import com.epicodus.backtalkr.R;
import com.epicodus.backtalkr.adapters.FirebaseCategoryAdapter;
import com.epicodus.backtalkr.models.Category;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Query mQuery;
    private Firebase mFirebaseRef;
    private FirebaseCategoryAdapter mAdapter;
    private String mCurrentUserId;

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.addCategoryButton) FloatingActionButton mAddCategoryButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFirebaseRef = BackTalkrApplication.getAppInstance().getFirebaseRef();

        checkForAuthenticatedUser();
        setupFirebaseQuery();
        setUpRecyclerView();

        mAddCategoryButton.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addCategoryButton:
                routeUnauthenticatedUserFromNewCategory();
                launchAddCategoryFragment();
                break;
        }
    }

    private void launchAddCategoryFragment() {
        FragmentManager fm = getSupportFragmentManager();
        AddCategoryFragment addCategory = AddCategoryFragment.newInstance();
        addCategory.show(fm, "fragment_add_category");
    }

    private void setupFirebaseQuery() {
        Firebase.setAndroidContext(this);
        String location = mFirebaseRef.child("categories").toString();
        mQuery = new Firebase(location);
    }

    private void setUpRecyclerView() {
        mAdapter = new FirebaseCategoryAdapter(mQuery, Category.class);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void checkForAuthenticatedUser() {
        AuthData authData = mFirebaseRef.getAuth();
        if (authData == null) {
            Toast.makeText(MainActivity.this, "You're not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    private void routeUnauthenticatedUserFromNewCategory() {
        AuthData authData = mFirebaseRef.getAuth();
        if (authData == null) {
            goToLoginActivity();
        } else {
            mCurrentUserId = mFirebaseRef.getAuth().getUid();
        }
    }

    private void goToLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                this.logout();
                return true;
            case R.id.action_login:
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mFirebaseRef.unauth();
    }
}
