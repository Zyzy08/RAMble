package com.game.ramble;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    Button btn_start;
    MediaPlayer home_bgmusic, click_fxmusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        home_bgmusic = MediaPlayer.create(this, R.raw.home_bgmusic);
        home_bgmusic.setLooping(true);
        home_bgmusic.start();

        click_fxmusic = MediaPlayer.create(this, R.raw.click_fxmusic);

        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(view -> {
            click_fxmusic.start();
            home_bgmusic.stop();

            new Handler().postDelayed(() -> {
                Intent intent = new Intent(Home.this, MainActivity.class);
                intent.putExtra("isFirst", true);
                startActivity(intent);
            }, 1000);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (home_bgmusic != null) {
            home_bgmusic.stop();
            home_bgmusic.release();
        }

        if (click_fxmusic != null) {
            click_fxmusic.stop();
            click_fxmusic.release();
        }
    }
}