package com.jr.taskmasternew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    private ConnectionClass connection; // Class to save connection
    // Elements of the layout
    private EditText pass, email;
    private Button registerGo, login;
    private ProgressDialog progressDialog;

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide support bar
        getSupportActionBar().hide();

        // set app to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // initialize layout components
        pass = (EditText) findViewById(R.id.loginPass);
        email = (EditText) findViewById(R.id.loginEmail);
        connection = new ConnectionClass();
        progressDialog = new ProgressDialog(this);

        registerGo = (Button) findViewById(R.id.registerBtn);
        login = (Button) findViewById(R.id.loginBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginBtn dologin = new LoginBtn();
                dologin.execute("");
            }
        });


    }

    public void goRegister(View v){
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public class LoginBtn extends AsyncTask<String, String, String>
    {
        // Because we need strings to use the text from the editText fields
        String emailStr = email.getText().toString();
        String passStr = pass.getText().toString();
        // These 2 will be used to define the connection state (success/failure)
        String msg = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute(){
            // Show a loading message so the user knows something is happening
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            // Check if all fields were used
            if(emailStr.trim().equals("") || passStr.trim().equals("")){
                msg = "Please enter all fields";
            }
            else{
                try {
                    Connection con = connection.CONN();

                    // if connection is null, then something went wrong in ConnectionClass
                    if (con == null){
                        msg = "Please check your internet connection";
                    }
                    else{
                        // Define query to send to server
                        String query = "Select * from users where `email`='"+emailStr+"';";
                        System.out.println(query); // For testing purposes
                        // Create statement and send query
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        // Success
                        if(rs.next()) {
                            if(rs.getString("password").equals(passStr)) {
                                msg = "Login Successful";
                                isSuccess = true;
                            }
                        }
                        if(!isSuccess) {
                            msg = "Incorrect username or password";
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
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                String message = email.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);


            }
            else{
                // Show user error message
                Toast.makeText(getBaseContext(), ""+msg, Toast.LENGTH_LONG).show();
            }

            progressDialog.hide();
        }
    }

}