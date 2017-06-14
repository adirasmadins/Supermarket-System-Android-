package com.mahlet.supermarketsystem;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {

    EditText etFirst;
    EditText etLast;
    EditText etAccount;
    Button btnPay;
    static double payment=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        etFirst=(EditText)findViewById(R.id.etFirst);
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
        if(payment!=0){
            HashMap map=new HashMap();

            Log.d("APP","Username: "+LoginActivity.username);
            Log.d("APP","Cost: "+payment);
            map.put("request","bank");
            map.put("username",LoginActivity.username);
            map.put("cost",String.valueOf(payment));
            map.put("account",etAccount.getText().toString());
            map.put("name",etFirst.getText().toString());
            map.put("items",Cart.toString(Cart.items));
            map.put("address",((EditText)findViewById(R.id.etAddressLine)).getText().toString());
            AsyncResponse response=new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if(s==null){
                        Toast.makeText(PaymentActivity.this,"Connection Failed", Toast.LENGTH_LONG).show();
                    }
                    else if(s.contains("error")){
                        Toast.makeText(PaymentActivity.this,s,Toast.LENGTH_LONG).show();
                    }
                    else if(s.contains("true")){
                        String[] parse=s.split(":");
                        String balance=parse[1];
                        Config.showDialog(PaymentActivity.this,"Transaction Succesfull","You ordered the items you selected succesfully, Your current balance is: "+balance,"Fine");
                    }
                    else{
                        Toast.makeText(PaymentActivity.this,"Error: "+s,Toast.LENGTH_LONG).show();
                        Log.d("APP","Result: "+s);
                    }
                }
            };
            PostResponseAsyncTask task=new PostResponseAsyncTask(this,map,response);
            task.execute(Config.SERVER+"/server.php");
        }
    }
}
