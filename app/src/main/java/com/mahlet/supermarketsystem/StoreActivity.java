package com.mahlet.supermarketsystem;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.text.DateFormat;
import java.util.HashMap;
import java.io.File;


public class StoreActivity extends AppCompatActivity {

    private AutoCompleteTextView etSearch;
    private Spinner spinType;
    private LinearLayout layout;
    private ListView listView;
    private TableLayout tblResult;
    private GridLayout gridLayout;
    private TextView txtCart;
    private ImageView iconView;
    private Button btnBuy;

    public static HashMap hashMap=new HashMap();
    public static int itemsCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        spinType=(Spinner)findViewById(R.id.spinCatagory);
        etSearch=(AutoCompleteTextView)findViewById(R.id.etSearchTerm);
        gridLayout=(GridLayout)findViewById(R.id.gridLayout);
        gridLayout.setColumnCount(3);
        gridLayout.setRowCount(20);
        iconView=(ImageView)findViewById(R.id.iconView);
        txtCart=(TextView)findViewById(R.id.txtCart);
        btnBuy=(Button)findViewById(R.id.btnBuy);

        btnBuy.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buy();
                    }
                }
        );
        autoLoad();
    }
    public void buy(){

         String message="\n";
         int counter=1;
         for(String key:Cart.items.keySet()){
             String[] items=Cart.items.get(key);
             message+=counter+". "+items[1]+" Price: "+items[4];
             counter++;
         }
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
         builder.setTitle("Buy Items");
         builder.setMessage("You are going to order the following items"+message);
         builder.setPositiveButton("Continue",
                 new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         orderNow();
                     }
                 }
         );
         builder.setNegativeButton("Cancel",
                 new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.cancel();
                     }
                 }
         );
         builder.show();


    }
    public void orderNow(){
      HashMap map=new HashMap();
        map.put("request","order_now");
        map.put("user",LoginActivity.username);
        map.put("item",Cart.toString(Cart.items));
        map.put("address","not applied");
        map.put("date","2-2-2017");
        AsyncResponse response=new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s==null || s.isEmpty()){
                    Toast.makeText(StoreActivity.this,"Error: Connection failed, please try again later!",Toast.LENGTH_LONG).show();
                }
                else if(s.contains("error") || s.contains("Error")){
                    Toast.makeText(StoreActivity.this,"Error: "+s,Toast.LENGTH_LONG).show();
                    Log.d("APP",s);
                }
                else if(s.contains("true")){
                    Log.d("APP",s);
                    String message="You ordered the items you selected succussfully, do you want to give us feedback or comment?";
                    AlertDialog.Builder builder=new AlertDialog.Builder(StoreActivity.this);
                    builder.setTitle("Order Completed");
                    builder.setMessage(message);
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(StoreActivity.this,FeedbackActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                    );
                    builder.setNegativeButton("Not Now",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }
                    );
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.show();
                }
                Log.d("APP","Response: "+s);
            }
        };
        PostResponseAsyncTask task=new PostResponseAsyncTask(this,map,response);
        task.execute(Config.SERVER+"/server.php");
    }
    public void autoLoad(){
        HashMap map=new HashMap();
        map.put("request","all");
        AsyncResponse response=new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s==null || s.isEmpty()){
                    Toast.makeText(StoreActivity.this,"Failed to connect to internet, fix and try again",Toast.LENGTH_LONG).show();
                }
                else if(s.contains("error") || s.contains("Error")){
                    Toast.makeText(StoreActivity.this,"Error: we'll try to fix it soon. Sorry for the inconveniance",Toast.LENGTH_LONG).show();
                    Log.d("APP","Response: "+s);
                }
                else{
                    Log.d("APP","Response: "+s);
                    String[] response=s.split(";");
                    String[][] items=parse(response);
                    String[] item=new String[items.length];
                    TextView[] view=new TextView[item.length];
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
                        view[i]=new TextView(StoreActivity.this);
                        view[i].setText(label);
                        if(label!=null)
                        item[i]=label;
                        else item[i]="none";
                        hashMap.put(label,items[i]);
                        Log.d("APP","ITEM["+i+"]= "+item[i]);
                        Log.d("APP","id: "+id+" label: "+label+" type: "+type+" image= "+image+" price= "+price+" shelf="+shelf);
                        //Adding Items to gridLayout
                        ImageView iview=new ImageView(StoreActivity.this);
                        iview.setImageResource(R.mipmap.ic_launcher);
                        TextView tview=new TextView(StoreActivity.this);
                        tview.setText(label+"\tPrice: "+price);
                        Button button=new Button(StoreActivity.this);
                        button.setText("Add To Cart");
                        button.setHint(label);
                        button.setBackgroundResource(R.mipmap.add_cart);
                        button.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        addToCart(v);
                                    }
                                }
                        );
                        button.setPadding(5,5,5,5);
                        tview.setPadding(5,5,5,5);
                        iview.setPadding(5,5,5,5);
                        gridLayout.addView(iview);
                        gridLayout.addView(tview);
                        gridLayout.addView(button);
                    }
                    Log.d("APP","I= "+i);
                    String[] types = {"Lotion", "Cosmotic", "Food", "Fruit", "Drinks", "Cleaner"};
                    ArrayAdapter<String> adapt = new ArrayAdapter<String>(StoreActivity.this, R.layout.support_simple_spinner_dropdown_item, types);
                    spinType.setAdapter(adapt);
                    if(item[0]!=null) {

                        Log.d("APP","Checking Items");
                        String[] filtered=new String[i];
                        for(int j=0;j<i;j++) {
                            Log.d("APP", "Item[" + j + "]=" + item[j]);
                            filtered[j]=item[j];
                        }
                        Log.d("APP","Check Complete");


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(StoreActivity.this, R.layout.support_simple_spinner_dropdown_item, filtered);
                        etSearch.setAdapter(adapter);
                        etSearch.showDropDown();
                        etSearch.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        etSearch.setText("");
                                    }
                                }
                        );
                        etSearch.setOnItemSelectedListener(
                                new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                      Log.d("APP","Item Selected");
                                        showItem(etSearch.getText().toString(),hashMap);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        Config.showDialog(StoreActivity.this,"Oops","please select an Item?","Got It!");
                                    }
                                }
                        );
                        etSearch.setOnItemClickListener(

                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Log.d("APP","Item Clicked");
                                        showItem(etSearch.getText().toString(),hashMap);
                                    }
                                }
                        );
                    }
                }
            }
        };
        PostResponseAsyncTask task=new PostResponseAsyncTask(this,map,response);
        task.execute(Config.SERVER+"/server.php");
    }
    static String[][] items=new String[100][6];
    public void addToCart(View v){
        Button b=(Button)v;
        String label=b.getHint().toString();
        if(b.getText().toString().equals("Remove")){
                Cart.items.remove(label);
                Config.showDialog(this,"Removed Item",label+" is removed from cart","Got it");
            b.setText("Add To Cart");
            b.setBackgroundResource(R.mipmap.remove_cart);
        }
        else {
            String[] items = (String[]) hashMap.get(label);
            Cart.items.put(label, items);

            Config.showDialog(this,"Message",label+"  has been added to cart","Got it");
            b.setText("Remove");
            b.setBackgroundResource(R.mipmap.add_cart);
        }
        double cost=Cart.getTotalCost();
        int count=Cart.count;
        String title=count+" items, Total: "+cost+" ETB";
        txtCart.setText(title);
    }
    public String[][] parse(String[] response){
        int i=0;
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
    public void showItem(String item,HashMap map){
        String[] detail=(String[])map.get(item);
        Log.d("APP","Label: "+detail[1]);
        Log.d("APP","ID: "+detail[0]);
        Log.d("APP","Type: "+detail[2]);
        String title=detail[1]+" Details";
        String message="Catagory: "+detail[2]+"\nShelf: "+detail[5]+"\nPrice: "+detail[4]+" ETB";
        String image=detail[3];
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("ADD TO Cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 //add to cart
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
