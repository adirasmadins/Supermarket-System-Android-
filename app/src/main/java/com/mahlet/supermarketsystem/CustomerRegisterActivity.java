package com.mahlet.supermarketsystem;

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

public class CustomerRegisterActivity extends AppCompatActivity {

    private EditText etID;
    private EditText etFullName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etAddress;
    static String id,fullName,address,phone,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);
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
        etID=(EditText)findViewById(R.id.etID);
        etFullName=(EditText)findViewById(R.id.etFullName);
        etAddress=(EditText)findViewById(R.id.etAddress);
        etPhone=(EditText)findViewById(R.id.etPhone);
        etEmail=(EditText)findViewById(R.id.etEmail);
        findViewById(R.id.btnNext).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        id=etID.getText().toString();
                        fullName=etFullName.getText().toString();
                        address=etAddress.getText().toString();
                        email=etEmail.getText().toString();
                        phone=etPhone.getText().toString();
                    }
                }
        );
    }
   public void register(String id,String name,String address,String email,String phone){
       HashMap map=new HashMap();
       map.put("id",id);
       map.put("name",name);
       map.put("address",address);
       map.put("email",email);
       map.put("phone",phone);
       AsyncResponse response=new AsyncResponse() {
           @Override
           public void processFinish(String s) {
               if(s==null || s.isEmpty()){
                   Log.d("Supermaket","Empty response");
                   Toast.makeText(CustomerRegisterActivity.this,"Connection to server failed, please try again later",Toast.LENGTH_LONG).show();
               }
           }
       };
       PostResponseAsyncTask task=new PostResponseAsyncTask(this,map,response);
       task.execute(Config.SERVER+"/server.php");

   }
}
