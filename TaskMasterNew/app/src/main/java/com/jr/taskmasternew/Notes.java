package com.jr.taskmasternew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class Notes extends AppCompatActivity {

    ConnectionClass connection;
    ListView listView;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String EXTRA_NAME= "com.example.myfirstapp.MESSAGE2";
    public static final String EXTRA_SAVE= "com.example.myfirstapp.MESSAGE3";
    public static final String EXTRA_NOTE= "com.example.myfirstapp.MESSAGE4";
    public static final String EXTRA_BUTTON = "com.example.myfirstapp.MESSAGEButton";
    public static final String EXTRA_COLUMN= "com.example.myfirstapp.MESSAGEcolumn";
    public static String messageButton;
    public static String message;
    public static String messageName;
    public static String savedstateNum;
    public static String columnName;
    public String[] data;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        message = intent.getStringExtra(savesProfile.EXTRA_MESSAGE);
        messageName = intent.getStringExtra(savesProfile.EXTRA_NAME);
        savedstateNum = intent.getStringExtra(savesProfile.EXTRA_SAVE);
        messageButton = intent.getStringExtra(savesProfile.EXTRA_BUTTON);
        columnName = intent.getStringExtra(savesProfile.EXTRA_COLUMN);

        //add message to text textView8
        TextView textView = findViewById(R.id.textView8);
        textView.setText(messageButton + "s");

        connection = new ConnectionClass();
        ProgressDialog progressDialog = new ProgressDialog(this);

        Connection con = connection.CONN();


        listView=(ListView) findViewById(R.id.noteList);

        ArrayList<String> arrayList = new ArrayList<>();

        Statement stmt = null;
        try {
            stmt = con.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("select "+ columnName + " from games Where (email = '" + message + "')*(gameTitle = '" + messageName + "')*(savedstateNum = '" + savedstateNum + "');");
            data = new String[100];
            int i;
            String temp;
            for(i = 0; rs.next();)
            {
                temp = rs.getString(""+columnName);
                if(temp != null) {
                    data[i] = temp;
                    i++;
                }
            }
            String data2[] = new String[i];
            for(int j = 0; j < i; j++)
            {
                data2[j] = data[j];
            }
            data = data2;

            for(int j = 0; j < data.length; j++)
            {
                arrayList.add(messageButton + "s " + (j+1) +": \n\n" + data[j]);
            }
            //insert ... email...gametitle...data[data.length - 1] + 1
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for(int i = 0; i < data.length; i++ )
                    if(position == i)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Notes.this);

                        builder.setCancelable(true);
                        builder.setTitle(messageButton + " #" +(i+1));
                        builder.setMessage(data[i]);

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.show();
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
    public void goBack(View view){
        Intent intent = new Intent(Notes.this, savesProfile.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_NAME, messageName);
        intent.putExtra(EXTRA_SAVE, savedstateNum);
        startActivity(intent);
    }
    public void goEdit(View view){
        Intent intent = new Intent(Notes.this, EditNotes.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_NAME, messageName);
        intent.putExtra(EXTRA_SAVE, savedstateNum);
        intent.putExtra(EXTRA_BUTTON, messageButton);
        intent.putExtra(EXTRA_COLUMN, columnName);
        intent.putExtra(EXTRA_NOTE, data);
        startActivity(intent);
    }
}