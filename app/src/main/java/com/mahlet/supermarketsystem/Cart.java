package com.mahlet.supermarketsystem;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 6/4/2017.
 */

public class Cart {
    public static HashMap<String,String[]> items=new HashMap<>();
    public static int count=0;
    public static double getTotalCost(){
        double cost=0;
        count=0;
        for(String label:items.keySet()){
            String[] item=items.get(label);
           double c=Double.parseDouble(item[4]);
            cost+=c;
            count++;
        }
        return cost;
    }
    public static String toString(HashMap map){
        String data="";
        for(String label:items.keySet()){
            data+=";"+items.get(label);
        }
        return data;
    }
}
