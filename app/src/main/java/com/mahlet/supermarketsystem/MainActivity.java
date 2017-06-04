package com.mahlet.supermarketsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnCustomer;
    Button btnDriver;
    Button btnAbout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCustomer=(Button)findViewById(R.id.btnCustomer);
        btnDriver=(Button)findViewById(R.id.btnDriver);
        btnAbout=(Button)findViewById(R.id.btnAbout);
        btnCustomer.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this,StoreActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }
}
