package com.mahlet.supermarketsystem;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

import static com.mahlet.supermarketsystem.R.id.etPassword;
import static com.mahlet.supermarketsystem.R.id.etUsername;

public class AccountCreate extends AppCompatActivity {

    public static String id;
    static String name;
    static String address;
    static String email;
    static String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_create);
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
        final EditText etUsername=(EditText)findViewById(R.id.etUsername);
        final EditText etPass=(EditText)findViewById(etPassword);
        final EditText etPass2=(EditText)findViewById(R.id.etPassword2);
        final String user=etUsername.getText().toString();
        final String pass1=etPass.getText().toString();
        final String pass2=etPass2.getText().toString();
        findViewById(R.id.btnFinish).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etUsername.getText().toString().isEmpty() || etPass.getText().toString().isEmpty() || etPass2.getText().toString().isEmpty())
                            Toast.makeText(AccountCreate.this,"You need to fill all required fields!",Toast.LENGTH_LONG).show();
                        else if (!pass1.equals(pass2))
                            Toast.makeText(AccountCreate.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                        else {
                            register(id, name, address, email, phone, user, pass1);
                        }
                    }});
    }
    public void register(String id,String name,String address,String email,String phone,final String username,String password){
        HashMap map=new HashMap();
        map.put("id",id);
        map.put("name",name);
        map.put("address",address);
        map.put("email",email);
        map.put("phone",phone);
        map.put("username",username);
        map.put("password",password);
        AsyncResponse response=new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s==null || s.isEmpty()){
                    Log.d("Supermaket","Empty response");
                    Toast.makeText(AccountCreate.this,"Connection to server failed, please try again later",Toast.LENGTH_LONG).show();
                }
                else if(s.contains("error")){
                    Log.d("Response",s);
                    Toast.makeText(AccountCreate.this,"Error: "+s,Toast.LENGTH_LONG).show();
                }
                else if(s.contains("true")){
                    /*AlertDialog.Builder builder=new AlertDialog.Builder(AccountCreate.this);
                    builder.setTitle("Registeration Succesfull");
                    builder.setMessage("You have succesfully registered on our customers list, now you can login with your username: "+username);
                    builder.show();*/
                    Toast.makeText(AccountCreate.this,"Registration Succesfull",Toast.LENGTH_LONG).show();
                }
                else{
                    Log.d("response",s);
                    Toast.makeText(AccountCreate.this,"Error: Unknown Error!",Toast.LENGTH_LONG).show();
                }
            }
        };
        PostResponseAsyncTask task=new PostResponseAsyncTask(this,map,response);
        task.execute(Config.SERVER+"/register.php");
    }

}
