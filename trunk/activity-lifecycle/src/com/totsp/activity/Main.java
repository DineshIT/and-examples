package com.totsp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

// view logs with
// adb -e logcat "*:s ActivityLifecycle:v"

// view Activity manager state with "adb shell dumpsys activity"

public class Main extends Activity {

   private static final String LOG_TAG = "ActivityLifecycle";

   Button b;

   //
   // ENTIRE activity lifetime START
   //
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      Log.d(Main.LOG_TAG, "onCreate -- ENTIRE lifecycle START");

      b = new Button(this);
      b.setText("call finish");
      b.setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            Main.this.finish();
         }
      });

      LinearLayout l = (LinearLayout) findViewById(R.id.mainlayout);
      LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(200, 50);
      p.setMargins(0, 20, 0, 0);
      l.addView(b, p);
   }

   // onPostCreate too, but not documented

   //
   // VISIBLE activity lifetime START
   //    
   @Override
   protected void onRestart() {
      Log.d(Main.LOG_TAG, "   onRestart -- VISIBLE lifecycle RE-START");
      super.onRestart();
   }

   @Override
   protected void onStart() {
      Log.d(Main.LOG_TAG, "   onStart -- VISIBLE lifecycle START");
      super.onStart();
   }

   // 
   // FOREGROUND activity lifetime START
   //  
   @Override
   protected void onResume() {
      Log.d(Main.LOG_TAG, "      onResume -- FOREGROUND lifecycle START");
      super.onResume();
   }

   // onPostResume too but not documented

   //
   // FOREGROUND activity lifetime STOP
   // 
   @Override
   protected void onPause() {
      Log.d(Main.LOG_TAG, "      onPause -- FOREGROUND lifecycle STOP");
      // onPause and EVERYTHING AFTER IT is "killable"
      // system may decide to kill and subsequent methods are not guaranteed to run
      super.onPause();
   }

   //
   // VISIBLE activity lifetime STOP
   //  
   @Override
   protected void onStop() {
      Log.d(Main.LOG_TAG, "   onStop -- VISIBLE lifecycle STOP");
      super.onStop();
   }

   //
   // ENTIRE activity lifetime STOP
   // 
   @Override
   protected void onDestroy() {
      Log.d(Main.LOG_TAG, "onDestroy -- ENTIRE lifecycle STOP");
      super.onDestroy();
   }

   //
   // Other interesting stuff
   //   
   //onSaveInstanceState
   //onRestoreInstanceState
   //onRetainNonConfigurationInstance
   // use "isFinishing" to tell if finish called (versus system onDestroy)
   // use "onLowMemory" to react before killed  
}