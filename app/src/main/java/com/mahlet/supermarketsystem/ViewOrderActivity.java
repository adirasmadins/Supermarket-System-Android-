package com.mahlet.supermarketsystem;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

public class ViewOrderActivity extends AppCompatActivity {

    GridLayout gridOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
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
        gridOrder=(GridLayout)findViewById(R.id.gridOrder);
        gridOrder.setColumnCount(4);
        gridOrder.setRowCount(15);
        loadOrder();
    }
    public void loadOrder(){
        HashMap map=new HashMap();
        map.put("request","order");
        AsyncResponse response=new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s==null)
                    Toast.makeText(ViewOrderActivity.this,"Connection Failed, Empty Response",Toast.LENGTH_LONG).show();
                else if(s.contains("error") || s.contains("Error"))
                    Toast.makeText(ViewOrderActivity.this,"Error "+s,Toast.LENGTH_LONG).show();
                else{
                    String[] result=s.split(";");
                    for(String r:result){
                        String[] row=r.split(",");
                        String user=row[0];
                        String item=row[1];
                        String date=row[2];
                        String address=row[3];
                        TextView userView=new TextView(ViewOrderActivity.this);
                        TextView itemView=new TextView(ViewOrderActivity.this);
                        TextView addressView=new TextView(ViewOrderActivity.this);
                        userView.setText(user);
                        itemView.setText(item);
                        addressView.setText(address);
                        Button btnRecieve=new Button(ViewOrderActivity.this);
                        btnRecieve.setText("Receive");
                        btnRecieve.setHint(user+":"+item);
                        btnRecieve.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        recieve((Button)v);
                                    }
                                }
                        );
                        gridOrder.addView(userView);
                        gridOrder.addView(itemView);
                        gridOrder.addView(addressView);
                        gridOrder.addView(btnRecieve);
                    }
                }
            }
        };
        PostResponseAsyncTask task=new PostResponseAsyncTask(this,map,response);
        task.execute(Config.SERVER+"/server.php");
    }
    public void recieve(final Button b){

        String[] content=b.getHint().toString().split(":");
        final String user=content[0];
        final String item=content[1];
        Log.d("APP","Item: "+item);
        Log.d("APP","User: "+user);

        String[] detail=Cart.items.get(item);
        Log.d("APP","Legth"+detail.length);
        Log.d("APP","detail[0]="+detail[0]+"detail[1]"+detail[1]+"d[2]: "+detail[2]);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Confirm Order Recieve");
        builder.setMessage("Item: "+item+" Catagory: "+detail[2]+" Shelf Number: "+detail[5]+"\nAre you sure to Recieve this item?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recieve(user,item,b);
                    }
                }
        );
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }
    public void recieve(String user,String item,final Button b){
        HashMap map=new HashMap();
        String driver=LoginActivity.username;
        map.put("item",item);
        map.put("request","receive");
        map.put("username",user);
        map.put("driver",driver);
        AsyncResponse response=new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s==null){
                    Toast.makeText(ViewOrderActivity.this,"Connection Error",Toast.LENGTH_LONG).show();
                }
                else if(s.contains("error"))
                    Toast.makeText(ViewOrderActivity.this,"Error: "+s,Toast.LENGTH_LONG).show();
                else{
                    b.setText("Received");
                    b.setEnabled(false);

                }
            }
        };
        PostResponseAsyncTask task=new PostResponseAsyncTask(this,map,response);
        task.execute(Config.SERVER+"/server.php");
    }
}
