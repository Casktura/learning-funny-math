package com.app.nsc.learningfunnymath;

import android.media.session.MediaController;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.VideoView;


public class Addition extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addition);

        //ซ่อน Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //เล่นไฟล์วิดีโอ
        VideoView videoView = (VideoView)findViewById(R.id.video);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.addition;
        videoView.setVideoURI(Uri.parse(path));
        videoView.setMediaController(new android.widget.MediaController(this));
        videoView.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addition, menu);
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
}
