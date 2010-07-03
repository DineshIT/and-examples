package com.totsp.networking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity {

   private Button getButton;
   private Button apacheButton;
   private Button apacheViaHelperButton;
   private Button helperFormButton;
   private Button deliciousButton;
   private Button gClientLoginButton;

   private TextView status;

   @Override
   public void onCreate(final Bundle icicle) {
      super.onCreate(icicle);
      setContentView(R.layout.main);

      getButton = (Button) findViewById(R.id.main_get_button);
      getButton.setOnClickListener(new OnClickListener() {

         public void onClick(final View v) {
            startActivity(new Intent(Main.this, JavaNetHttpGet.class));
         }
      });

      apacheButton = (Button) findViewById(R.id.main_apache_button);
      apacheButton.setOnClickListener(new OnClickListener() {

         public void onClick(final View v) {
            startActivity(new Intent(Main.this, ApacheHttpSimple.class));
         }
      });

      apacheViaHelperButton = (Button) findViewById(R.id.main_apacheviahelper_button);
      apacheViaHelperButton.setOnClickListener(new OnClickListener() {

         public void onClick(final View v) {
            startActivity(new Intent(Main.this, ApacheHttpViaHelper.class));
         }
      });

      helperFormButton = (Button) findViewById(R.id.main_helper_button);
      helperFormButton.setOnClickListener(new OnClickListener() {

         public void onClick(final View v) {
            startActivity(new Intent(Main.this, HttpHelperForm.class));
         }
      });

      deliciousButton = (Button) findViewById(R.id.main_delicious_button);
      deliciousButton.setOnClickListener(new OnClickListener() {

         public void onClick(final View v) {
            startActivity(new Intent(Main.this, DeliciousRecentPosts.class));
         }
      });

      gClientLoginButton = (Button) findViewById(R.id.main_gclientlogin_button);
      gClientLoginButton.setOnClickListener(new OnClickListener() {

         public void onClick(final View v) {
            startActivity(new Intent(Main.this, GoogleClientLogin.class));
         }
      });

      status = (TextView) findViewById(R.id.main_status);

      getStatus();
   }

   private void getStatus() {
      ConnectivityManager cMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
      status.setText(netInfo.toString());
   }
}
