package com.jr.taskmasternew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SavedGamesActivity extends AppCompatActivity {
    ConnectionClass connection;
    ListView listView;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String EXTRA_NAME= "com.example.myfirstapp.MESSAGE2";
    public static final String EXTRA_SAVE= "com.example.myfirstapp.MESSAGE3";
    public static String message;
    public static String messageName;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_games);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        message = intent.getStringExtra(GameLibraryActivity.EXTRA_MESSAGE);
        messageName = intent.getStringExtra(GameLibraryActivity.EXTRA_NAME);


        connection = new ConnectionClass();
        ProgressDialog progressDialog = new ProgressDialog(this);

        Connection con = connection.CONN();


        listView=(ListView) findViewById(R.id.savesList);

        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> theDatabaseData = new ArrayList<>();



        Statement stmt = null;
        try {
            stmt = con.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("select savedstateNum from games Where (email = '" + message + "')*(gameTitle = '" + messageName + "');");
            int i = 0;
            while(rs.next()){
                if(!theDatabaseData.contains(rs.getString("savedStateNum"))){
                    theDatabaseData.add(rs.getString("savedStateNum"));
                    arrayList.add("Saved State: " + theDatabaseData.get(i));
                    i++;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for(int i = 0; i < theDatabaseData.size(); i++ )
                    if(position == i)
                    {
                        Intent intent = new Intent(view.getContext(), savesProfile.class);
                        intent.putExtra(EXTRA_MESSAGE, message);
                        intent.putExtra(EXTRA_NAME, messageName);
                        intent.putExtra(EXTRA_SAVE, theDatabaseData.get(i));
                        startActivity(intent);
                    }
            }


        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), "To delete please go to edit", Toast.LENGTH_LONG).show();
                return true;
            }
        });


    }

    public void goEdit(View view){
        Intent intent = new Intent(SavedGamesActivity.this, editSaves.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_NAME, messageName);
        startActivity(intent);
    }

    public void goBack(View view){
        Intent intent = new Intent(SavedGamesActivity.this, GameLibraryActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}