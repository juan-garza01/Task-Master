package com.jr.taskmasternew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;



public class GameLibraryActivity extends AppCompatActivity {

    ConnectionClass connection;
    ListView listView;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String EXTRA_NAME= "com.example.myfirstapp.MESSAGE2";
    public static String message;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_library2);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        message = intent.getStringExtra(GameActivity.EXTRA_MESSAGE);
        if(message == null)
        {
            message = intent.getStringExtra(EditLibrary.EXTRA_MESSAGE);
        }

        connection = new ConnectionClass();
        ProgressDialog progressDialog = new ProgressDialog(this);

        Connection con = connection.CONN();


        listView=(ListView) findViewById(R.id.gameList);

        ArrayList<String> arrayList = new ArrayList<>();

        Statement stmt = null;
        try {
            stmt = con.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("select gameTitle from games Where email = '" + message + "';");

            while(rs.next()){
                if(!arrayList.contains(rs.getString("gameTitle"))){
                    arrayList.add(rs.getString("gameTitle"));
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

                for(int i = 0; i < arrayList.size(); i++ )
                if(position == i)
                {
                    Intent intent = new Intent(view.getContext(), SavedGamesActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, message);
                    intent.putExtra(EXTRA_NAME, arrayList.get(i));
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
        Intent intent = new Intent(GameLibraryActivity.this, EditLibrary.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void goBack(View view){
        Intent intent = new Intent(GameLibraryActivity.this, GameActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}