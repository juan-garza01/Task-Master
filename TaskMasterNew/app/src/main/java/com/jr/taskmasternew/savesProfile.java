package com.jr.taskmasternew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class savesProfile extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String EXTRA_NAME= "com.example.myfirstapp.MESSAGE2";
    public static final String EXTRA_SAVE= "com.example.myfirstapp.MESSAGE3";
    public static final String EXTRA_BUTTON= "com.example.myfirstapp.MESSAGEButton";
    public static final String EXTRA_COLUMN= "com.example.myfirstapp.MESSAGEcolumn";
    public static String message;
    public static String messageName;
    public static String savestateNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saves_profile);

        Intent intent = getIntent();
        message = intent.getStringExtra(SavedGamesActivity.EXTRA_MESSAGE);
        messageName = intent.getStringExtra(SavedGamesActivity.EXTRA_NAME);
        savestateNum = intent.getStringExtra(SavedGamesActivity.EXTRA_SAVE);
    }
    public void goBack(View view){
        Intent intent = new Intent(savesProfile.this, SavedGamesActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_NAME, messageName);
        startActivity(intent);
    }

    public void goNotes(View view){
        Intent intent = new Intent(savesProfile.this, Notes.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_NAME, messageName);
        intent.putExtra(EXTRA_SAVE, savestateNum);
        intent.putExtra(EXTRA_BUTTON, "Note");
        intent.putExtra(EXTRA_COLUMN, "notes");
        startActivity(intent);
    }

    public void goTask(View view){
        Intent intent = new Intent(savesProfile.this, Notes.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_NAME, messageName);
        intent.putExtra(EXTRA_SAVE, savestateNum);
        intent.putExtra(EXTRA_BUTTON, "Task");
        intent.putExtra(EXTRA_COLUMN, "task");
        startActivity(intent);
    }

    public void goChallenge(View view){
        Intent intent = new Intent(savesProfile.this, Notes.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_NAME, messageName);
        intent.putExtra(EXTRA_SAVE, savestateNum);
        intent.putExtra(EXTRA_BUTTON, "Challenge");
        intent.putExtra(EXTRA_COLUMN, "challenges");

        startActivity(intent);
    }
}