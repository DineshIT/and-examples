package com.totsp.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.totsp.networking.data.HttpHelper;

import java.util.HashMap;

/**
 * Simple form to exercise the HttpHelper class (which wraps HttpClient).
 * 
 * GET: http://www.yahoo.com 
 * GET: http://www.google.com/search?&q=android 
 * POST: http://www.snee.com/xml/crud/posttest.cgi (fname and lname params)
 * 
 * @author ccollins
 * 
 */
public class HttpHelperForm extends Activity {

   private EditText url;
   private Spinner method;
   private EditText param1Name;
   private EditText param1Value;
   private EditText param2Name;
   private EditText param2Value;
   private EditText param3Name;
   private EditText param3Value;
   private EditText user;
   private EditText pass;
   private Button go;
   private TextView output;

   @Override
   public void onCreate(final Bundle icicle) {
      super.onCreate(icicle);
      // inflate the SAME view XML layout file as ApacheHttpWithAuth Activity (re-use it)
      setContentView(R.layout.http_helper_form);

      url = (EditText) findViewById(R.id.htth_url);
      method = (Spinner) findViewById(R.id.htth_method);
      param1Name = (EditText) findViewById(R.id.htth_param1_name);
      param1Value = (EditText) findViewById(R.id.htth_param1_value);
      param2Name = (EditText) findViewById(R.id.htth_param2_name);
      param2Value = (EditText) findViewById(R.id.htth_param2_value);
      param3Name = (EditText) findViewById(R.id.htth_param3_name);
      param3Value = (EditText) findViewById(R.id.htth_param3_value);
      user = (EditText) findViewById(R.id.htth_user);
      pass = (EditText) findViewById(R.id.htth_pass);
      go = (Button) findViewById(R.id.htth_go_button);
      output = (TextView) findViewById(R.id.htth_output);

      ArrayAdapter<String> methods =
               new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] { "GET", "POST" });
      methods.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      method.setAdapter(methods);

      go.setOnClickListener(new OnClickListener() {

         public void onClick(final View v) {
            HttpHelperForm.this.output.setText("");

            HttpHelperForm.this.performRequest(HttpHelperForm.this.url.getText().toString(), HttpHelperForm.this.method
                     .getSelectedItem().toString(), HttpHelperForm.this.param1Name.getText().toString(),
                     HttpHelperForm.this.param1Value.getText().toString(), HttpHelperForm.this.param2Name.getText()
                              .toString(), HttpHelperForm.this.param2Value.getText().toString(),
                     HttpHelperForm.this.param3Name.getText().toString(), HttpHelperForm.this.param3Value.getText()
                              .toString(), HttpHelperForm.this.user.getText().toString(), HttpHelperForm.this.pass
                              .getText().toString());
         }
      });
   };

   private void performRequest(final String url, final String method, final String param1Name,
            final String param1Value, final String param2Name, final String param2Value, final String param3Name,
            final String param3Value, final String user, final String pass) {

      Log.d(Constants.LOG_TAG, " request url - " + url);
      Log.d(Constants.LOG_TAG, " request method - " + method);
      Log.d(Constants.LOG_TAG, " param1Name - " + param1Name);
      Log.d(Constants.LOG_TAG, " param1Value - " + param1Value);
      Log.d(Constants.LOG_TAG, " param2Name - " + param2Name);
      Log.d(Constants.LOG_TAG, " param2Value - " + param2Value);
      Log.d(Constants.LOG_TAG, " param3Name - " + param3Name);
      Log.d(Constants.LOG_TAG, " param3Value - " + param3Value);
      Log.d(Constants.LOG_TAG, " user - " + user);
      if (pass != null) {
         Log.d(Constants.LOG_TAG, " pass length - " + pass.length());
      }

      final HashMap<String, String> params = new HashMap<String, String>();
      if ((param1Name != null) && (param1Value != null)) {
         params.put(param1Name, param1Value);
      }
      if ((param2Name != null) && (param2Value != null)) {
         params.put(param2Name, param2Value);
      }
      if ((param3Name != null) && (param3Value != null)) {
         params.put(param3Name, param3Value);
      }

      RequestBean requestBean = new RequestBean();
      requestBean.user = user;
      requestBean.pass = pass;
      requestBean.method = method;
      requestBean.url = url;
      requestBean.params = params;

      new HttpRequestTask().execute(requestBean);
   }

   // simple bean to hold heterogeneous params (vararg "String" won't cut it)
   private class RequestBean {
      String user;
      String pass;
      String method;
      String url;
      HashMap<String, String> params;
   }

   // AsyncTask
   private class HttpRequestTask extends AsyncTask<RequestBean, Void, String> {
      private final ProgressDialog dialog = new ProgressDialog(HttpHelperForm.this);

      private final HttpHelper httpHelper = new HttpHelper();

      // can use UI thread here
      @Override
      protected void onPreExecute() {
         dialog.setMessage("Performing HTTP request...");
         dialog.show();
      }

      // automatically done on worker thread (separate from UI thread)
      @Override
      protected String doInBackground(final RequestBean... requestBeans) {
         RequestBean requestBean = requestBeans[0];
         String result = null;
         if (requestBean.method.equals("GET")) {
            result = httpHelper.performGet(requestBean.url, requestBean.user, requestBean.pass, null);
         } else if (requestBean.method.equals("POST")) {
            result =
                     httpHelper.performPost(HttpHelper.MIME_FORM_ENCODED, requestBean.url, requestBean.user,
                              requestBean.pass, null, requestBean.params);
         } else {
            Log.e(Constants.LOG_TAG, "Unknown request method - " + requestBean.method);
         }
         return result;
      }

      // can use UI thread here
      @Override
      protected void onPostExecute(final String result) {
         if (dialog.isShowing()) {
            dialog.dismiss();
         }

         HttpHelperForm.this.output.setText(result);
      }
   }
}
