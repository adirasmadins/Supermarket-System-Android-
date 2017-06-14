package com.mahlet.supermarketsystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class Config {
    public static final String SERVER="http://192.168.43.254/supermaket";
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
