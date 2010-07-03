package com.totsp.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.totsp.networking.data.DeliciousHandler;
import com.totsp.networking.data.DeliciousPost;
import com.totsp.networking.data.HttpHelper;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Android HTTP example demonstrating basic auth over Apache HttpClient (via HttpHelper)
 * against del.icio.us API (and XML parsing -- HTTP and Plain XML - POX).
 * 
 * @author ccollins
 * 
 */
public class DeliciousRecentPosts extends Activity {

   private static final String URL_GET_POSTS_RECENT = "https://api.del.icio.us/v1/posts/recent?";

   private EditText user;
   private EditText pass;
   private TextView output;
   private Button button;

   @Override
   public void onCreate(final Bundle icicle) {
      super.onCreate(icicle);
      setContentView(R.layout.delicious_posts);

      user = (EditText) findViewById(R.id.del_user);
      pass = (EditText) findViewById(R.id.del_pass);
      button = (Button) findViewById(R.id.delgo_button);
      output = (TextView) findViewById(R.id.del_output);

      button.setOnClickListener(new OnClickListener() {

         public void onClick(final View v) {
            output.setText("");
            new HttpRequestTask().execute(user.getText().toString(), pass.getText().toString());
         }
      });
   };

   private String parseXmlResult(String xmlString) {
      StringBuilder result = new StringBuilder();
      DeliciousHandler handler = new DeliciousHandler();

      try {
         Xml.parse(new ByteArrayInputStream(xmlString.getBytes("UTF-8")), Xml.Encoding.UTF_8, handler);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }

      List<DeliciousPost> posts = handler.getPosts();
      for (DeliciousPost p : posts) {
         Log.d(Constants.LOG_TAG, "DeliciousPost - " + p.getHref());
         result.append("\n" + p.getHref());
      }

      return result.toString();
   }

   // AsyncTask
   private class HttpRequestTask extends AsyncTask<String, Void, String> {
      private final ProgressDialog dialog = new ProgressDialog(DeliciousRecentPosts.this);

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
         String user = args[0];
         String pass = args[1];
         return httpHelper.performPost(DeliciousRecentPosts.URL_GET_POSTS_RECENT, user, pass, null, null);
      }

      // can use UI thread here
      @Override
      protected void onPostExecute(final String result) {
         if (dialog.isShowing()) {
            dialog.dismiss();
         }

         String resultParsed = parseXmlResult(result);
         DeliciousRecentPosts.this.output.setText(resultParsed);
      }
   }

}
