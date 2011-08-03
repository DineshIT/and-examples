package com.totsp.example;

import android.content.Context;
import android.widget.Toast;

public class AndroidJSBridge {

   private Context context;

   AndroidJSBridge(Context c) {
       context = c;
   }

   public void showToast(String toast) {
       Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
   }
   
}
