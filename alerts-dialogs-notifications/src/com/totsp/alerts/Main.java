package com.totsp.alerts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener {

   // show alerts/notifications 
   // also demonstrates another way to handle OnClickListener (as opposed to anon inner, or explicit)
   // and Handler/Message/Thread

   private Button showToast;
   private Button showAlertDialog;
   private Button showProgressDialog;
   private Button showProgressBar;
   private Button showNotification;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      this.showToast = (Button) this.findViewById(R.id.toast);
      this.showAlertDialog = (Button) this.findViewById(R.id.alert);
      this.showProgressDialog = (Button) this.findViewById(R.id.progress_dialog);
      this.showProgressBar = (Button) this.findViewById(R.id.progress_bar);
      this.showNotification = (Button) this.findViewById(R.id.notification);

      this.showToast.setOnClickListener(this);
      this.showAlertDialog.setOnClickListener(this);
      this.showProgressDialog.setOnClickListener(this);
      this.showProgressBar.setOnClickListener(this);
      this.showNotification.setOnClickListener(this);
   }

   public void onClick(View v) {
      switch (v.getId()) {
      case R.id.toast:
         Toast.makeText(this, "A \"Toast\" is a \"quick little message\" for the user.", Toast.LENGTH_LONG).show();
         break;
      case R.id.alert:
         new AlertDialog.Builder(this).setMessage(
                  "AlertDialog is a \"Popup Notification\" (modal dialog) that can have buttons and choices").setIcon(
                  android.R.drawable.ic_menu_compass).setNegativeButton("Negative",
                  new DialogInterface.OnClickListener() {

                     @Override
                     public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub                        
                     }
                  }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
               // TODO Auto-generated method stub                        
            }
         }).setTitle("A Fancy Title").show();
         break;
      case R.id.progress_dialog:
         final ProgressDialog pd = ProgressDialog.show(this, "Simple ProgressDialog", "whistle while you work . . .");
         pd.show();

         // a Handler so that UI Thread can receive Messages from separate Thread
         final Handler pdHandler = new Handler() {
            public void handleMessage(Message m) {
               pd.hide();
            }
         };
         
         // a Thread to fake a long running task
         new Thread() {
            public void run() {
               for (int i = 1; i < 12; i++) {
                  SystemClock.sleep(200);
               }
               pdHandler.sendEmptyMessage(1);
            }
         }.start();
         break;
      case R.id.progress_bar:
         // progress range is 0..10000
         final ProgressDialog pb = new ProgressDialog(this);
         pb.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
         pb.setTitle("Progress Dialog");
         pb.setMessage("in progress  . . .");
         pb.show();

         // a Handler so that UI Thread can receive Messages from separate Thread
         final Handler pbHandler = new Handler() {
            public void handleMessage(Message m) {
               Log.d("TEST", "inc progress message - " + m.what);
               pb.setProgress(m.what * 10);
               if (m.what == 11) {
                  pb.hide();
               }
            }
         };

         // a Thread to fake a long running task
         new Thread() {
            public void run() {
               for (int i = 1; i < 12; i++) {
                  SystemClock.sleep(200);
                  pbHandler.sendEmptyMessage(i);
               }
            }
         }.start();
         break;
      case R.id.notification:
         NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
         Notification notification =
                  new Notification(android.R.drawable.ic_dialog_info, "Notification!",
                           System.currentTimeMillis() + 1000);
         notification.defaults |= Notification.DEFAULT_VIBRATE;
         notification.defaults |= Notification.DEFAULT_LIGHTS;
         Intent notificationIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:8675309"));
         PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
         notification.setLatestEventInfo(this, "NotificationTitle", "Notificiation Text", contentIntent);
         notificationManager.notify(1, notification);
         break;
      }
   }

}