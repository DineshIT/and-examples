package com.totsp.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.totsp.networking.data.HttpHelper;

/**
 * Android HTTP example demonstrating basic auth over Apache HttpClient 4 (using del.icio.us API) -
 * AND using custom HttpRequestHelper.
 * 
 * 
 * @author charliecollins
 * 
 */
public class ApacheHttpViaHelper extends Activity {

   private static final String URL1 = "http://www.comedycentral.com/rss/jokes/index.jhtml";
   private static final String URL2 = "http://feeds.theonion.com/theonion/daily";

   private Spinner urlChooser;
   private Button button;
   private TextView output;

   @Override
   public void onCreate(final Bundle icicle) {
      super.onCreate(icicle);
      // inflate the SAME view XML layout file as ApacheHTTPSimple Activity (re-use it)
      setContentView(R.layout.apache_http_simple);

      urlChooser = (Spinner) findViewById(R.id.apache_url);
      ArrayAdapter<String> urls =
               new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] {
                        ApacheHttpViaHelper.URL1, ApacheHttpViaHelper.URL2 });
      urls.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      urlChooser.setAdapter(urls);

      button = (Button) findViewById(R.id.apachego_button);
      output = (TextView) findViewById(R.id.apache_output);

      button.setOnClickListener(new OnClickListener() {

         public void onClick(final View v) {
            output.setText("");
            new HttpRequestTask().execute(urlChooser.getSelectedItem().toString());
         }
      });
   };

   // AsyncTask
   private class HttpRequestTask extends AsyncTask<String, Void, String> {
      private final ProgressDialog dialog = new ProgressDialog(ApacheHttpViaHelper.this);

      private HttpHelper httpHelper = new HttpHelper();

      // can use UI thread here
      @Override
      protected void onPreExecute() {
         dialog.setMessage("Performing HTTP request...");
         dialog.show();
      }

      // automatically done on worker thread (separate from UI thread)
      @Override
      protected String doInBackground(final String... args) {
         return httpHelper.performGet(args[0], null, null, null);
      }

      // can use UI thread here
      @Override
      protected void onPostExecute(final String result) {
         if (dialog.isShowing()) {
            dialog.dismiss();
         }

         ApacheHttpViaHelper.this.output.setText(result);
      }
   }
}
