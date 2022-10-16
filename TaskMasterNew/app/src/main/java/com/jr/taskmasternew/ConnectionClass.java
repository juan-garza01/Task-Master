package com.jr.taskmasternew;

import android.os.StrictMode;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    // Variables necessary for connection
    String classs = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://35.239.196.104/test";
    String un = "root";
    String pass = "va2se#Ez2ODfstG79!$";

    public Connection CONN(){
        // Set your connections policy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Start connection null so it can be used and exist after the try-catch
        Connection conn = null;
        String conURL = url;

        try{
            Class.forName(classs);
            conn = (Connection) DriverManager.getConnection(url, un, pass);


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // returns null if connection failed
        return conn;
    }

}

