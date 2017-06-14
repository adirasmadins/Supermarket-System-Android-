package com.mahlet.supermarketsystem;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.asynctask.*;



import java.util.HashMap;

import io.hypertrack.smart_scheduler.Job;
import io.hypertrack.smart_scheduler.SmartScheduler;

public class LoginActivity extends AppCompatActivity {

    static String type="none";
    static boolean loggedIn=false;
    private EditText etUsername;
    private EditText etPassword;
    private NotificationManager manager;
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
                Snackbar.make(view, "Copyright Arbaminch University, G4 IT students @2017", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        SmartScheduler.JobScheduledCallback callback = new SmartScheduler.JobScheduledCallback() {
            @Override
            public void onJobScheduled(Context context, Job job){
                HashMap map=new HashMap();
                map.put("request","get");
                AsyncResponse response=new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s==null || s.isEmpty())
                            Log.d("APP","Empty Notification");
                        else if(s.contains("error") || s.contains("Error")){
                            Log.d("APP","Notifcation Error: "+s);

                        }
                        else{
                            String message[]=s.split(";");
                            int counter=1;
                            for(String m:message){
                                String[] content=m.split(",");
                                String from=content[0];
                                String title=content[1];
                                String body=content[2];
                                String date=content[3];
                                String notification=body;
                                NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext());
                                builder.setSmallIcon(R.mipmap.ic_launcher);
                                builder.setContentTitle("New Notification from: "+from);
                                builder.setContentText(notification);
                                Uri alarm= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                builder.setSound(alarm);
                                manager.notify(counter+100,builder.build());
                                counter++;
                            }
                        }
                    }
                };
                PostResponseAsyncTask task=new PostResponseAsyncTask(getApplicationContext(),map,false,response);

                task.execute(Config.SERVER+"/notify.php");
            }
        };

        Job.Builder builder = new Job.Builder(101, callback,"type")
                .setRequiresCharging(false)
                .setIntervalMillis(30000);


            builder.setPeriodic(30000);


        Job job = builder.build();
        SmartScheduler jobScheduler = SmartScheduler.getInstance(getApplicationContext());
        boolean result = jobScheduler.addJob(job);

        if (result) {
            Toast.makeText(this,"Job Scheduled succesfully",Toast.LENGTH_LONG).show();
        }
        etUsername=(EditText)findViewById(R.id.txtUsername);
        etPassword=(EditText)findViewById(R.id.txtPassword);
        findViewById(R.id.btnContact).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("Contact Us");
                        builder.setMessage("Phone: 0934747474\nEmail: email@amu.supermaket.com\nWebsite: amu.supermaket.com");
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.show();
                    }
                }
        );
        findViewById(R.id.btnLogin).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username=etUsername.getText().toString();
                        String password=etPassword.getText().toString();
                        boolean valid=true;
                        if(username==null || username.isEmpty()){
                            valid=false;
                        }
                        if(password==null || password.isEmpty()){
                            valid=false;
                        }
                        if(valid)
                        login(username,password);
                        else{
                            Config.showDialog(LoginActivity.this,"Form Empty","Please fill all required fields","Ok");
                        }
                    }
                }
        );
        findViewById(R.id.btnRegister).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(LoginActivity.this,CustomerRegisterActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
        findViewById(R.id.btnFeedBack).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(LoginActivity.this,FeedbackActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
        autoLoad();
    }
    public static String username;
    public static String password;
    public void login(final String user, final String password){
        HashMap map=new HashMap();
        map.put("username",user);
        map.put("password",password);

        AsyncResponse response=new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.d("APP","Executed");
              if(s==null || s.isEmpty()){
                  Log.d("SUPERMAKRET","Connection Error");
                  Toast.makeText(LoginActivity.this,"Connection failed at the moment",Toast.LENGTH_LONG).show();
              }
              else if(s.contains("Error") || s.contains("error")){
                  Log.d("SUPERMARKET","Response: "+s);
                  Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_LONG).show();
              }
              else if(s.contains("false")){
                  Log.d("SUPERMARKET","Login Failed: Returned: "+s);
                  Toast.makeText(LoginActivity.this,"Wrong username or password",Toast.LENGTH_LONG).show();
                  Vibrator v = (Vibrator) LoginActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                  // Vibrate for 500 milliseconds
                  v.vibrate(500);
              }
              else if(s.contains("true")){
                  String[] result=s.split(":");
                  String type=result[1];
                  if(type.equalsIgnoreCase("customer")) {
                      username=user;
                      LoginActivity.password=password;
                      Intent intent = new Intent(LoginActivity.this, StoreActivity.class);
                      startActivity(intent);
                      loggedIn=true;
                  }
                  else if(s.contains("true") && s.contains("driver")){
                      Log.d("APP","Driver Login");
                      username=user;
                      Intent intent=new Intent(LoginActivity.this,ViewOrderActivity.class);
                      startActivity(intent);
                      finish();
                  }
                  else{
                      Toast.makeText(LoginActivity.this,"You can't Login as Admin using this app!",Toast.LENGTH_LONG).show();
                      Log.d("APP","Crednitals inccoreect");
                  }
              }
              else{
                  Log.d("APP","Unown Response");
                  Toast.makeText(LoginActivity.this,"Error: "+s,Toast.LENGTH_LONG).show();
              }
            }
        };
        PostResponseAsyncTask task=new PostResponseAsyncTask(this,map,response);
        task.execute(Config.SERVER+"/login.php");
    }
    public String[][] parse(String[] response){
        int i=0;
        String[][] items=new String[20][6];
        for(String s:response){
            if(s==null || s.isEmpty())
                continue;
            String[] data=s.split(",");
            for(int j=0;j<6;j++){
                items[i][j]=data[j];
                Log.d("APP","items["+i+"]["+j+"]="+items[i][j]);
            }
            i++;
        }
        return items;
    }
    public void autoLoad(){
        HashMap map=new HashMap();
        map.put("request","all");
        AsyncResponse response=new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s==null || s.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Failed to connect to internet, fix and try again",Toast.LENGTH_LONG).show();
                }
                else if(s.contains("error") || s.contains("Error")){
                    Toast.makeText(LoginActivity.this,"Error: we'll try to fix it soon. Sorry for the inconveniance",Toast.LENGTH_LONG).show();
                    Log.d("APP","Response: "+s);
                }
                else{
                    Log.d("APP","Response: "+s);
                    String[] response=s.split(";");
                    String[][] items=parse(response);
                    String[] item=new String[items.length];

                    int i=0;
                    for(i=0;i<item.length;i++){
                        if(items[i][0]==null)
                            break;
                        String id=items[i][0];
                        String label=items[i][1];
                        String type=items[i][2];
                        String image=items[i][3];
                        String price=items[i][4];
                        String shelf=items[i][5];


                        if(label!=null)
                            item[i]=label;
                        else item[i]="none";
                        Cart.items.put(label,items[i]);
                        Log.d("APP","ITEM["+i+"]= "+item[i]);
                        Log.d("APP","id: "+id+" label: "+label+" type: "+type+" image= "+image+" price= "+price+" shelf="+shelf);
                        //Adding Items to gridLayout

                    }
                }
            }
        };
        PostResponseAsyncTask task=new PostResponseAsyncTask(this,map,response);
        task.execute(Config.SERVER+"/server.php");
    }
}
