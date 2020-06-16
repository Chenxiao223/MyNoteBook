package com.example.mynotebook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mynotebook.R;

/**
 * 注册页
 */
public class StartActivity extends BaseActivity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                finish();
            }
        }, 3000);
    }
}
