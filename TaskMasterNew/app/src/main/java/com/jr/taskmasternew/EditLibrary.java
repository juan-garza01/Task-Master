package com.jr.taskmasternew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class EditLibrary extends AppCompatActivity {

    ConnectionClass connection; // Class to save connection
    EditText game;
    Button add, delete;
    ProgressDialog progressDialog;

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static String emailData;
    public static String keyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_library);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        emailData = intent.getStringExtra(GameLibraryActivity.EXTRA_MESSAGE);

        game = (EditText) findViewById(R.id.editGame);
        connection = new ConnectionClass();
        progressDialog = new ProgressDialog(this);

        add = (Button) findViewById(R.id.addBtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditLibrary.addingBtn doadd = new EditLibrary.addingBtn();
                doadd.execute("");
            }
        });

        delete = (Button) findViewById(R.id.deleteBtn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditLibrary.deletingBtn dodelete = new EditLibrary.deletingBtn();
                dodelete.execute("");
            }
        });
    }

    public class addingBtn extends AsyncTask<String, String, String> {

        String gameStr = game.getText().toString();
        //String emailStr = emailData;

        // These 2 will be used to define the connection state (success/failure)
        String msg = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            // Show a loading message so the user knows something is happening
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            // Check if all fields were used
            if (gameStr.trim().equals("")) {
                msg = "Please add a game!";
            } else {
                try {
                    Connection con = connection.CONN();

                    // if connection is null, then something went wrong in ConnectionClass
                    if (con == null) {
                        msg = "Please check your internet connection";
                    }
                    // You can add more if statements to check for correct email or anything else you'll require.
                    else {
                        // Define query to send to server
                        String query = "insert into games (email, gameTitle) Values ('" + emailData + "', '" + gameStr + "');";

                        System.out.println(query); // For testing purposes
                        // Create statement and send query
                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);

                        // Success
                        msg = "Adding Successful";
                        isSuccess = true;
                    }
                } catch (Exception e) {
                    isSuccess = false;
                    msg = "Exceptions " + e;
                    e.printStackTrace();
                }
            }
            return msg;
        }

        protected void onPostExecute(String s) {
            if (isSuccess) {
                // Show user error or success message (depends on the doInBackground function)
                Toast.makeText(getBaseContext(), "" + msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(EditLibrary.this, GameLibraryActivity.class);
                intent.putExtra(EXTRA_MESSAGE, emailData);
                startActivity(intent);

            } else {
                // Show user error message
                Toast.makeText(getBaseContext(), "" + msg, Toast.LENGTH_LONG).show();
            }

            progressDialog.hide();
        }
    }

    public class deletingBtn extends AsyncTask<String, String, String> {


        String gameStr = game.getText().toString();


        // These 2 will be used to define the connection state (success/failure)
        String msg = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            // Show a loading message so the user knows something is happening
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            // Check if all fields were used
            if (gameStr.trim().equals("")) {
                msg = "Please add a game to delete!";
            } else {
                try {
                    Connection con = connection.CONN();

                    // if connection is null, then something went wrong in ConnectionClass
                    if (con == null) {
                        msg = "Please check your internet connection";
                    }
                    // You can add more if statements to check for correct email or anything else you'll require.
                    else {
                        // Define query to send to server
                        String query = "SELECT * FROM test.games WHERE (email = '" + emailData + "')*(gameTitle = '" + gameStr + "');";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);


                        // Success
                        while(rs.next()) {
                            if(rs.getString("gameTitle").equals(gameStr)) {
                                 int keyNum = rs.getInt("key");


                                String query2 = "DELETE FROM `test`.`games` WHERE (`key` = '" + keyNum + "');";

                                Statement stmt2 = con.createStatement();
                                stmt2.executeUpdate(query2);


                                isSuccess = true;
                            }
                        }
                        if(!isSuccess) {
                            msg = "could not delete";
                        }
                        else{
                            msg = "Deleting Successful";
                        }
                    }
                } catch (Exception e) {
                    isSuccess = false;
                    msg = "Exceptions "+e;
                    e.printStackTrace();
                }
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s){
            if(isSuccess){
                // Show user success message
                Toast.makeText(getBaseContext(), ""+msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(EditLibrary.this, GameLibraryActivity.class);
                intent.putExtra(EXTRA_MESSAGE, emailData);
                startActivity(intent);

            }
            else{
                // Show user error message
                Toast.makeText(getBaseContext(), ""+msg, Toast.LENGTH_LONG).show();
            }

            progressDialog.hide();
        }
    }

    public void goBack(View view){
        Intent intent = new Intent(EditLibrary.this, GameLibraryActivity.class);
        intent.putExtra(EXTRA_MESSAGE, emailData);
        startActivity(intent);
    }

}