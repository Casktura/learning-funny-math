package com.app.nsc.learningfunnymath;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

public class MatchingScore extends ActionBarActivity {
    private UiLifecycleHelper uiHelper;
    Integer score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_score);

        //ตั้งค่า Lifecycle ตอนกลับมาจากหน้า Facebook
        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);

        //ซ่อน Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //แสดงชื่อผู้เล่น
        TextView textView = (TextView)findViewById(R.id.name);
        textView.setText(Main.playerName);

        //อ่านค่าคะแนน
        Intent lastPage = getIntent();
        score = lastPage.getIntExtra("score", 0);

        //แสดงผลคะแนน
        textView = (TextView)findViewById(R.id.score_display);
        textView.setText("Score: " + score.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_matching_score, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Log.e("Activity", String.format("Error: %s", error.toString()));
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                Log.i("Activity", "Success!");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    //ปุ่มแชร์บนเฟซบุ๊ค
    public void postOnFacebook(View view){
        FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                .setLink("https://www.google.com")
                .setPicture("http://i1148.photobucket.com/albums/o567/Casktura/logo_zps07a1c9ca.png")
                .setName("ฉันเล่นเกมจับคู่")
                .setDescription("ได้คะแนน " + score.toString())
                .setCaption("Learning Funny Math")
                .build();
        uiHelper.trackPendingDialogCall(shareDialog.present());
    }
}
