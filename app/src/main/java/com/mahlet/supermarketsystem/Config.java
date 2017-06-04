package com.mahlet.supermarketsystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Administrator on 5/23/2017.
 */

public class Config {
    public static final String SERVER="http://10.0.3.2:81/supermaket";
    public static final String PORT="81";
 public static void showDialog(Context context,String title,String message,String okText){
     AlertDialog.Builder builder=new AlertDialog.Builder(context);
     builder.setTitle(title);
     builder.setMessage(message);
     builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
             dialog.dismiss();
         }
     });
     builder.show();
 }
}
