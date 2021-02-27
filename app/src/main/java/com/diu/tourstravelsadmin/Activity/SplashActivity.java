package com.diu.tourstravelsadmin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.diu.tourstravelsadmin.R;

import java.util.Objects;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {

    static SplashActivity splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalash);
        Objects.requireNonNull(getSupportActionBar()).hide();
        splash=this;
        Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                    Intent i = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static SplashActivity getInstance(){
        return splash;
    }
}