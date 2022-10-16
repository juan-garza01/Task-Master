package com.jr.taskmasternew;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
    }

    //goes from game activity page to saved games activity page
    public void goLibrary(View view){
       Intent intent = new Intent(GameActivity.this, GameLibraryActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
       startActivity(intent);
    }

    public void goBack(View view){
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
    }
}