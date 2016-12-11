package com.app.nsc.learningfunnymath;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class LearnSelector extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_selector);

        //ซ่อน Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_learn_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToAddition(View vIew){
        Intent nextPage = new Intent(this, Addition.class);
        startActivity(nextPage);
    }

    public void goToSubtraction(View vIew){
        Intent nextPage = new Intent(this, Subtraction.class);
        startActivity(nextPage);
    }

    public void goToMultiplication(View vIew){
        Intent nextPage = new Intent(this, Multiplication.class);
        startActivity(nextPage);
    }

    public void goToDivision(View vIew){
        Intent nextPage = new Intent(this, Division.class);
        startActivity(nextPage);
    }
}
