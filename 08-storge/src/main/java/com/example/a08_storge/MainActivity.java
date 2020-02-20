package com.example.a08_storge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.button:
                intent = new Intent(MainActivity.this, SharedActivity.class);
                break;
            default:
                intent = new Intent(MainActivity.this, MainActivity.class);
                break;
        }

        startActivity(intent);
    }
}
