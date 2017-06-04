package com.mahlet.supermarketsystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.asynctask.*;



import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    static String type="none";
    static boolean loggedIn=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        findViewById(R.id.btnLogin).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username=((EditText)findViewById(R.id.txtUsername)).getText().toString();
                        String password=((EditText)findViewById(R.id.txtPassword)).getText().toString();
                        login(username,password);

                    }
                }
        );


    }
    public void login(String user,String password){
        HashMap map=new HashMap();
        map.put("username",user);
        map.put("password",password);
        map.put("type",type);
        AsyncResponse response=new AsyncResponse() {
            @Override
            public void processFinish(String s) {
              if(s==null || s.isEmpty()){
                  Log.d("SUPERMAKRET","Connection Error");
                  Toast.makeText(LoginActivity.this,"Connection failed at the moment",Toast.LENGTH_LONG).show();
              }
              else if(s.contains("Error") || s.contains("error")){
                  Log.d("SUPERMARKET","Response: "+s);
                  Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_LONG).show();
              }
              else if(s.contains("false")){
                  Log.d("SUPERMARKET","Login Failed");
                  Toast.makeText(LoginActivity.this,"Wrong username or password",Toast.LENGTH_LONG).show();
                  Vibrator v = (Vibrator) LoginActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                  // Vibrate for 500 milliseconds
                  v.vibrate(500);
              }
              else if(s.contains("true")){
                  Intent intent=new Intent(LoginActivity.this, StoreActivity.class);
                  startActivity(intent);
              }
            }
        };
        PostResponseAsyncTask task=new PostResponseAsyncTask(this,map,response);
        task.execute(Config.SERVER+"server.php");
    }

}
