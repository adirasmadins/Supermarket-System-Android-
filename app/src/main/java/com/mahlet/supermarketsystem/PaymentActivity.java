package com.mahlet.supermarketsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {

    EditText etFirst;
    EditText etLast;
    EditText etAccount;
    Button btnPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        etFirst=(EditText)findViewById(R.id.etFirst);
        etLast=(EditText)findViewById(R.id.etLast);
        etAccount=(EditText)findViewById(R.id.etAccount);
        btnPay=(Button)findViewById(R.id.btnPay);
        btnPay.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        payNow();
                    }
                }
        );
        etAccount.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        int number=Integer.parseInt(etAccount.getText().toString());
                        if(number==12345)
                            return true;
                        return false;
                    }
                }
        );
    }
    public void payNow(){
        Log.d("APP","Payment");
    }
}
