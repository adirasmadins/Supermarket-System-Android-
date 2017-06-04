package com.mahlet.supermarketsystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;


public class Item {
    private String name;
    private double price;
    private String type;
    private int shelfNumber;
    private String imageURL;
    public Item(String name,double price,String type,int shelfNumber,String imageURL){

    }
    public Item(){

    }
    private String getName(){
        return this.name;
    }
    private double getPrice(){
        return this.price;
    }
    private String getType(){
        return this.type;
    }
    private String getImageURL(){
        return this.imageURL;
    }
    private Bitmap getImage(final Context context){
        HashMap map=new HashMap();
        map.put("request","image");
        map.put("item",this.getName());
        AsyncResponse response=new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s==null || s.isEmpty())
                    Toast.makeText(context,"No connection",Toast.LENGTH_LONG).show();
                else if(s.contains("error"))
                    Toast.makeText(context,"An Error occured while retrieving image from server",Toast.LENGTH_LONG).show();
                else if(s.contains("false"))
                    Toast.makeText(context,"No Image found for this item!",Toast.LENGTH_LONG).show();
                else {

                   //Bitmap bitmap=Bitmap.
                    //Assing to global bitmap
                    //return that bitmap
                }

            }
        };
        PostResponseAsyncTask task=new PostResponseAsyncTask(context,map,response);
        task.execute(Config.SERVER+"/server.php");
        return null;
    }
}
