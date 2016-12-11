package com.app.nsc.learningfunnymath;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
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
import java.util.Random;
import android.os.Handler;


public class Bingo extends ActionBarActivity {

    Integer time = 120, timeMultiplier; //เวลาในหน่วยวินาที
    Integer tableWidth, IDPrefix = 1100, playingIndex, arraySize;
    Integer score = 0,correct = 0,wrong = 0, bingo = 0;
    String[] question, answer;
    Boolean[] played, checked;
    CountDownTimer waitTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingo);

        //ซ่อน Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //อ่านค่า Level
        Intent lastPage = getIntent();
        String level = lastPage.getStringExtra("level");

        //สร้างตาราง setup เพื่อการเล่นครั้งแรก
        setTable(level);
        newPlayingIndex();

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
        getMenuInflater().inflate(R.menu.menu_bingo, menu);
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
        //ตรวจสอบระดับความยาก
        if(level.equals("easy")){
            timeMultiplier = 1;
            tableWidth = 3;
        }else if(level.equals("medium")){
            timeMultiplier = 2;
            tableWidth = 4;
        }else if(level.equals("hard")){
            timeMultiplier = 3;
            tableWidth = 5;
        }
        arraySize = tableWidth * tableWidth *2;

        //เจนคำถามและคำตอบ
        question = new String[arraySize];
        answer = new String[arraySize];
        played = new Boolean[arraySize];
        checked = new Boolean[arraySize];
        QuestionGenerator.generate(question, answer, played, checked, arraySize);

        //สร้างตาราง
        Integer IDCounter = 0; //นับตำแหน่งเป็นอาร์เรย์แถวเดียว
        for(Integer K = 0;K < tableWidth;K++)
        {
            LinearLayout row;
            row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
            for(Integer L = 0;L < tableWidth;L++)
            {
                Button button = new Button(this);
                button.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
                button.setText(answer[IDCounter]);
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
    }

    //เมื่อปุ่มในตารางถูกกด
    public void buttonPressed(View view)
    {
        Integer tag = (Integer)view.getTag();
        Integer index = tag - IDPrefix;

        /**Delete
        //Debug
        TextView textView = (TextView)findViewById(R.id.test_text);
        textView.setText(question[index] + " - " + answer[playingIndex]);*/

        //ตรวจคำตอบ
        if(answer[index].equals(answer[playingIndex]))
        {
            //กรณีตอบถูก
            played[index] = Boolean.TRUE;
            checked[index] = Boolean.TRUE;
            correct++;

            //เปลี่ยนสีปุ่มเป็นสีเขียว
            Button button = (Button)findViewById(tag);
            button.setBackgroundColor(Color.GREEN);
            button.setEnabled(Boolean.FALSE);

            //นับคะแนน แล้วตรวจหาบิงโก
            score();
            checkForBingo();
        }
        else
        {
            //กรณีตอบผิด
            wrong++;
            //played[playingIndex] = Boolean.TRUE;

            //เปลี่ยนสีปุ่มเป็นสีแดงชั่วคราว
            final Button button = (Button)findViewById(tag);
            final Drawable backGround = button.getBackground();
            button.setBackgroundColor(Color.RED);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run(){
                    button.setBackground(backGround);
                }
            }, 150);

            //นับคะแนน
            score();
        }

        //สุ่มคำถามใหม่
        newPlayingIndex();
    }

    //สุ่มตัวเลือกที่จะมาเป็นคำถาม
    public void newPlayingIndex()
    {
        Random random = new Random();
        Integer K;

        //หาว่ายังมีคำถามอยู่ให้เล่นหรือเปล่า ถ้าไม่มีก็จะจบเกม
        for(K = 0;K < arraySize;K++)
        {
            if(played[K].equals(Boolean.FALSE))
                break;
        }
        if(K >= arraySize)
            endGame();

        playingIndex = random.nextInt(arraySize);
        while(played[playingIndex].equals(Boolean.TRUE))
        {
            playingIndex = random.nextInt(arraySize);
        }
        TextView textView = (TextView)findViewById(R.id.playing_number);
        textView.setText(question[playingIndex]);
    }

    //นับคะแนน
    public void score()
    {
        score = (3 * correct) - (2 * wrong);
        TextView textView = (TextView)findViewById(R.id.score_display);
        textView.setText(score.toString());
    }

    //ตรวจว่ามีบิงโกหรือไม่
    public void checkForBingo()
    {
        //แนวนอน
        for(Integer K = 0;K < tableWidth;K++){
            Boolean bingoTemp = Boolean.TRUE;
            for(Integer L = 0;L < tableWidth;L++){
                Integer index = (K * tableWidth) + L;
                if(checked[index] == Boolean.FALSE)
                    bingoTemp = Boolean.FALSE;
            }
            if(bingoTemp == Boolean.TRUE)
                bingo++;
        }

        //แนวตั้ง
        for(Integer K = 0;K < tableWidth;K++){
            Boolean bingoTemp = Boolean.TRUE;
            for(Integer L = 0;L < tableWidth;L++){
                Integer index = (L * tableWidth) + K;
                if(checked[index] == Boolean.FALSE)
                    bingoTemp = Boolean.FALSE;
            }
            if(bingoTemp == Boolean.TRUE)
                bingo++;
        }

        //แนวทแยงมุมทั้งสองข้าง
        Boolean bingoTemp = Boolean.TRUE;
        for(Integer K = 0;K < tableWidth;K++){
            Integer index = (K * tableWidth) + K;
            if(checked[index] == Boolean.FALSE)
                bingoTemp = Boolean.FALSE;
        }
        if(bingoTemp == Boolean.TRUE)
            bingo++;
        bingoTemp = Boolean.TRUE;
        for(Integer K = 0;K < tableWidth;K++){
            Integer L = tableWidth - K - 1;
            Integer index = (K * tableWidth) + L;
            if(checked[index] == Boolean.FALSE)
                bingoTemp = Boolean.FALSE;
        }
        if(bingoTemp == Boolean.TRUE)
            bingo++;

        //ถ้ามีบิงโกให้จบเกม
        if(bingo.equals(0) == Boolean.FALSE)
            endGame();
    }

    //จบเกม
    public void endGame()
    {
        if(waitTimer != null)
        {
            waitTimer.cancel();
            waitTimer = null;
        }
        Integer timeScore = time * timeMultiplier;
        Integer totalScore = score + timeScore + (bingo * 25);
        finish();
        Intent nextPage = new Intent(this, BingoScore.class);
        nextPage.putExtra("score", totalScore);
        startActivity(nextPage);
    }

    //เมื่อผู้เล่นกดปุ่มข้าม
    public void skipButton(View view)
    {
        newPlayingIndex();
    }

}
