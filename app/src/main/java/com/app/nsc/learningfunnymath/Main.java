package com.app.nsc.learningfunnymath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main extends ActionBarActivity {
    public static String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //อ่านคำถามจากไฟล์
        generateQuestion();

        //ซ่อน Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        /**Delete
        //เปลี่ยนฟ้อนต์
        Typeface Mali = Typeface.createFromAsset(getAssets(), "TH Mali Grade6.ttf");
        TextView SetTypeface = (TextView)findViewById(R.id.header);
        SetTypeface.setTypeface(Mali);
        SetTypeface = (TextView)findViewById(R.id.button_learn);
        SetTypeface.setTypeface(Mali);
        SetTypeface = (TextView)findViewById(R.id.button_game);
        SetTypeface.setTypeface(Mali);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void goToLearn(View view){
        Intent nextPage = new Intent(this, LearnSelector.class);
        startActivity(nextPage);
    }

    public void goToGame(View view){
        /**Delete
        SharedPreferences prefs = getSharedPreferences("Naming", MODE_PRIVATE);
        String playerName = prefs.getString("playerName", null);
        if(playerName != null){
            Intent nextPage = new Intent(this, GameSelector.class);
            startActivity(nextPage);
        }else{
            Intent nextPage = new Intent(this, Naming.class);
            startActivity(nextPage);
        }*/
        if(playerName != null){
            Intent nextPage = new Intent(this, GameSelector.class);
            startActivity(nextPage);
        }else{
            Intent nextPage = new Intent(this, Naming.class);
            startActivity(nextPage);
        }
    }

    //อ่านคำถาม - คำตอบจากไฟล์
    public void generateQuestion() {
        QuestionGenerator.question = new String[500];
        QuestionGenerator.answer = new String[500];
        String temp;
        Integer K = 0;

        //อ่านไฟล์
        try{
            InputStream inputStream = getAssets().open("question");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while((temp = bufferedReader.readLine()) != null){
                QuestionGenerator.question[K] = temp;
                temp = bufferedReader.readLine();
                QuestionGenerator.answer[K] = temp;
                K++;
            }
            bufferedReader.close();
            inputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        //หาขนาดของอาร์เรย์คำถาม
        QuestionGenerator.size = K - 1;
    }
}
