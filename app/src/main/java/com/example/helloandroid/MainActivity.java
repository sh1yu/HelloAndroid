package com.example.helloandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user taps the showWebView button
     */
    public void showWebView(View view) {
        Intent intent = new Intent(this, ShowWebViewActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the user taps the showRunningProcess button
     */
    public void showRunningProcess(View view) {
        Intent intent = new Intent(this, ShowRunningProcessActivity.class);
        intent.putExtra("is_system", "true");
        startActivity(intent);
    }

    /**
     * Called when the user taps the showRunningProcess button
     */
    public void showRunningProcess2(View view) {
        Intent intent = new Intent(this, ShowRunningProcessActivity.class);
        intent.putExtra("is_system", "false");
        startActivity(intent);
    }
}