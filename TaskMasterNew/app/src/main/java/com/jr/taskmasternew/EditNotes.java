package com.jr.taskmasternew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class EditNotes extends AppCompatActivity {

    ConnectionClass connection; // Class to save connection
    EditText notesState, notesState2;
    Button add, delete;
    ProgressDialog progressDialog;

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String EXTRA_NAME= "com.example.myfirstapp.MESSAGE2";
    public static final String EXTRA_SAVE= "com.example.myfirstapp.MESSAGE3";
    public static final String EXTRA_NOTE= "com.example.myfirstapp.MESSAGE4";
    public static final String EXTRA_BUTTON = "com.example.myfirstapp.MESSAGEButton";
    public static final String EXTRA_COLUMN= "com.example.myfirstapp.MESSAGEcolumn";
    public static String messageButton;
    public static String columnName;
    public static String emailData;
    public static String messageName;
    public static String savedstateNum;
    public String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        emailData = intent.getStringExtra(Notes.EXTRA_MESSAGE);
        messageName = intent.getStringExtra(Notes.EXTRA_NAME);
        savedstateNum = intent.getStringExtra(Notes.EXTRA_SAVE);
        messageButton = intent.getStringExtra(Notes.EXTRA_BUTTON);
        columnName = intent.getStringExtra(Notes.EXTRA_COLUMN);

        data = intent.getStringArrayExtra(Notes.EXTRA_NOTE);

        TextView textView = findViewById(R.id.textView8);
        textView.setText("Edit " + messageButton + "s");

        notesState = (EditText) findViewById(R.id.notesTV);
        notesState2 = (EditText) findViewById(R.id.noteNum);
        connection = new ConnectionClass();
        progressDialog = new ProgressDialog(this);

        add = (Button) findViewById(R.id.addBtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditNotes.addingBtn doadd = new EditNotes.addingBtn();
                doadd.execute("");
            }
        });

        delete = (Button) findViewById(R.id.deleteBtn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditNotes.deletingBtn dodelete = new EditNotes.deletingBtn();
                dodelete.execute("");
            }
        });
    }

    public class addingBtn extends AsyncTask<String, String, String> {

        String notes = notesState.getText().toString();
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
            if (notes.trim().equals("")) {
                msg = "Please add a "+ messageButton + "!";
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
                        String query = "insert into games (email, gameTitle, savedstateNum, "+ columnName +") Values ('" + emailData + "', '" + messageName + "', '" + savedstateNum + "', '" + notes + "');";

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
                Intent intent = new Intent(EditNotes.this, Notes.class);
                intent.putExtra(EXTRA_MESSAGE, emailData);
                intent.putExtra(EXTRA_NAME, messageName);
                intent.putExtra(EXTRA_SAVE, savedstateNum);
                intent.putExtra(EXTRA_BUTTON, messageButton);
                intent.putExtra(EXTRA_COLUMN, columnName);
                startActivity(intent);

            } else {
                // Show user error message
                Toast.makeText(getBaseContext(), "" + msg, Toast.LENGTH_LONG).show();
            }

            progressDialog.hide();
        }
    }

    public class deletingBtn extends AsyncTask<String, String, String> {


        String notesString = notesState2.getText().toString();
        int notes=Integer.parseInt(notesString) - 1;


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
            if (notesString.trim().equals("")) {
                msg = "Please add a " + messageButton + " number to delete!";
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
                        String query = "SELECT * FROM test.games WHERE (email = '" + emailData + "')*(gameTitle = '" + messageName + "')*(savedstateNum = '" + savedstateNum + "')*("+ columnName +" = '" + data[notes] + "');";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);


                        // Success
                        if(rs.next()) {
                            if(rs.getString(""+columnName).equals(data[notes])) {
                                int keyNum = rs.getInt("key");

                                String query2 = "DELETE FROM `test`.`games` WHERE (`key` = '" + keyNum + "');";

                                Statement stmt2 = con.createStatement();
                                stmt2.executeUpdate(query2);



                                msg = "Deleting Successful";
                                isSuccess = true;
                            }
                        }
                        if(!isSuccess) {
                            msg = "could not delete";
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
        protected void onPostExecute(String s) {
            if (isSuccess) {
                // Show user error or success message (depends on the doInBackground function)
                Toast.makeText(getBaseContext(), "" + msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(EditNotes.this, Notes.class);
                intent.putExtra(EXTRA_MESSAGE, emailData);
                intent.putExtra(EXTRA_NAME, messageName);
                intent.putExtra(EXTRA_SAVE, savedstateNum);
                intent.putExtra(EXTRA_BUTTON, messageButton);
                intent.putExtra(EXTRA_COLUMN, columnName);
                startActivity(intent);

            } else {
                // Show user error message
                Toast.makeText(getBaseContext(), "" + msg, Toast.LENGTH_LONG).show();
            }

            progressDialog.hide();
        }
    }

    public void goBack(View view){
        Intent intent = new Intent(EditNotes.this, Notes.class);
        intent.putExtra(EXTRA_MESSAGE, emailData);
        intent.putExtra(EXTRA_NAME, messageName);
        intent.putExtra(EXTRA_SAVE, savedstateNum);
        intent.putExtra(EXTRA_BUTTON, messageButton);
        intent.putExtra(EXTRA_COLUMN, columnName);
        startActivity(intent);
    }

}