package com.mahlet.supermarketsystem;

import android.content.Intent;
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
                        if(etID.getText().toString().isEmpty() || etID.getText().toString().isEmpty() || etID.getText().toString().isEmpty())
                            Toast.makeText(CustomerRegisterActivity.this,"Please fill all required fields and try again!",Toast.LENGTH_LONG).show();
                        else if(!isValid(etEmail.getText().toString()))
                            Toast.makeText(etEmail.getContext(),"Invalid Email",Toast.LENGTH_LONG).show();
                        else {
                            id = etID.getText().toString();
                            fullName = etFullName.getText().toString();
                            address = etAddress.getText().toString();
                            email = etEmail.getText().toString();
                            phone = etPhone.getText().toString();
                            AccountCreate.address = address;
                            AccountCreate.name = fullName;
                            AccountCreate.email = email;
                            AccountCreate.phone = phone;
                            AccountCreate.id=id;
                            Intent intent = new Intent(CustomerRegisterActivity.this, AccountCreate.class);
                            startActivity(intent);

                            finish();
                        }
                    }
                }
        );

        findViewById(R.id.btnBack).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(CustomerRegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }
  @Override
    public void onBackPressed(){
      Intent intent=new Intent(this,LoginActivity.class);
      startActivity(intent);
      finish();
  }
  public boolean isValid(String email){
      boolean valid=false;
      if(email.contains("@") && email.contains("."))
          valid=true;
      else valid=false;
      return valid;
  }
}
