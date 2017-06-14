package com.mahlet.supermarketsystem;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

public class FeedbackActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Visit our website: www.amusupermarket.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        findViewById(R.id.btnComment).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        comment();
                    }
                }
        );
        etEmail=(EditText)findViewById(R.id.txtEmail);
        etMessage=(EditText)findViewById(R.id.txtComment);

    }
    public void comment(){
        String email=etEmail.getText().toString();
        String message=etMessage.getText().toString();
        HashMap map=new HashMap();
        map.put("email",email);
        map.put("text",message);
        AsyncResponse response=new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s==null || s.isEmpty()){
                    Toast.makeText(FeedbackActivity.this," Connection to server failed at the momment ",Toast.LENGTH_LONG);
                }
                else if(s.contains("error")){
                    Toast.makeText(FeedbackActivity.this,"Error: "+s,Toast.LENGTH_LONG).show();
                }
                else if(s.contains("true")){
                    Toast.makeText(FeedbackActivity.this,"Your feedback is sent succesfully",Toast.LENGTH_LONG).show();
                }
            }
        };
        PostResponseAsyncTask task=new PostResponseAsyncTask(this,map,response);
        task.execute(Config.SERVER+"/feedback.php");

    }
}
