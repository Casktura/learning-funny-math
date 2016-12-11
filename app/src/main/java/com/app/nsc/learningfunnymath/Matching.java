package com.app.nsc.learningfunnymath;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Matching extends ActionBarActivity {
    Integer time = 180, timeMultiplier;//เวลาหน่วยเป็นวินาที
    Integer width, height, wrong = 0, correct = 0, arraySize, IDPrefix = 1100, score, counting;
    String[] realValue;
    String[] question;
    Boolean selected = Boolean.FALSE;//มีการกดปุ่มหรือยัง
    Integer selectedIndex;//ปุ่มที่กดไปแล้ว
    Drawable backgroundTemp;
    CountDownTimer waitTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        //ซ่อน Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //อ่านค่า Level
        Intent lastPage = getIntent();
        String level = lastPage.getStringExtra("level");

        //สร้างตาราง setup เพื่อการเล่นครั้งแรก
        setTable(level);

        //ทำตัวนับเวลา
        waitTimer = new CountDownTimer(time * 1000, 1000){
            public void onTick(long timeLeft)
            {
                time--;
                TextView textView = (TextView)findViewById(R.id.time_display);
                Integer m = time / 60;
                Integer s = time % 60;
                textView.setText(m.toString() + ":" + s.toString());
            }

            public void onFinish()
            {
                time--;
                TextView textView = (TextView)findViewById(R.id.time_display);
                Integer m = time / 60;
                Integer s = time % 60;
                textView.setText(m.toString() + ":" + s.toString());
                score();
                endGame();
            }
        }.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_matching, menu);
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

    //วาดตาราง
    public void setTable(String level){
        //เซ็ตค่าความกว้าง ความยาวของตาราง
        if(level.equals("easy")){
            height = 4;
            width = 4;
            timeMultiplier = 1;
        }else if(level.equals("medium")){
            height = 5;
            width = 4;
            timeMultiplier = 2;
        }else if(level.equals("hard")){
            height = 6;
            width = 4;
            timeMultiplier = 3;
        }
        arraySize = width * height;
        counting = arraySize / 2;

        //เจ็นคำถามและคำตอบ
        question = new String[arraySize];
        realValue = new String[arraySize];
        QuestionGenerator.generate(question, realValue, arraySize);

        //วาดตาราง
        Integer IDCounter = 0;
        for(Integer K = 0;K < height;K++){
            LinearLayout row;
            row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
            for(Integer L = 0;L < width;L++){
                Button button = new Button(this);
                button.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
                button.setText(question[IDCounter]);
                button.setTag(IDPrefix + IDCounter);
                button.setId(IDPrefix + IDCounter);
                button.setTypeface(Typeface.DEFAULT_BOLD);
                button.setTextSize(40);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buttonPressed(view);
                    }
                });
                row.addView(button);
                IDCounter++;
            }
            LinearLayout tableDisplay = (LinearLayout)findViewById(R.id.table_display);
            tableDisplay.addView(row);
        }

        //อ่านสีปุ่มมาตรฐาน
        Button button = (Button)findViewById(IDPrefix + 0);
        backgroundTemp = button.getBackground();
    }

    //เมื่อปุ่มในตารางถูกกด
    public void buttonPressed(View view)
    {
        Integer tag = (Integer)view.getTag();
        Integer index = tag - IDPrefix;

        /**Delete
        //Debug
        TextView textView = (TextView)findViewById(R.id.test_text);
        textView.setText(question[index]);*/

        //ตรวจคำตอบ
        if(selected.equals(Boolean.FALSE)){
            selectedIndex = index;
            selected = Boolean.TRUE;

            //เปลี่ยนสีปุ่มเป็นสีเหลือง
            Button button = (Button)findViewById(tag);
            button.setBackgroundColor(Color.YELLOW);
        }else if(selected.equals(Boolean.TRUE) && index.equals(selectedIndex)){
            selected = Boolean.FALSE;

            //เปลี่ยนสีกลับ
            Button button = (Button)findViewById(tag);
            button.setBackground(backgroundTemp);
        }else{
            if(realValue[selectedIndex].equals(realValue[index]) && !question[selectedIndex].equals(question[index]) && (realValue[selectedIndex].equals(question[selectedIndex]) || realValue[index].equals(question[index]))){
                //กรณีกดถูก
                selected = Boolean.FALSE;
                correct++;
                counting--;
                score();

                //ตรวจสอบการจบเกม
                if(counting.equals(0))
                    endGame();

                //เปลี่ยนสีเป็นสีเขียว
                Button button = (Button)findViewById(tag);
                button.setBackgroundColor(Color.GREEN);
                button.setEnabled(Boolean.FALSE);
                button = (Button)findViewById(selectedIndex + IDPrefix);
                button.setBackgroundColor(Color.GREEN);
                button.setEnabled(Boolean.FALSE);
            }else{
                //กรณีกดผิด
                selected = Boolean.FALSE;
                wrong++;
                score();

                //เปลี่ยนสีเป็นสีแดง แล้วเปลี่ยนกลับ
                final Button button1 = (Button)findViewById(tag);
                button1.setBackgroundColor(Color.RED);
                final Button button2 = (Button)findViewById(selectedIndex + IDPrefix);
                button2.setBackgroundColor(Color.RED);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        button1.setBackground(backgroundTemp);
                        button2.setBackground(backgroundTemp);
                    }
                }, 250);
            }
        }
    }

    //นับคะแนน
    public void score(){
        score = (3 * correct) - (2 * wrong);
        TextView textView = (TextView)findViewById(R.id.score_display);
        textView.setText(score.toString());
    }

    //จบเกม
    public void endGame(){
        if(waitTimer !=null)
        {
            waitTimer.cancel();
            waitTimer = null;
        }
        Integer timeScore = time * timeMultiplier;
        Integer totalScore = score + timeScore;
        finish();
        Intent nextPage = new Intent(this, MatchingScore.class);
        nextPage.putExtra("score", totalScore);
        startActivity(nextPage);
    }
}
